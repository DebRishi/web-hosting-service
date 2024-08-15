require('dotenv').config();

const { exec } = require('child_process');
const { readdirSync, createReadStream, lstatSync, statSync } = require('fs');
const path = require('path');
const { S3Client, PutObjectCommand } = require('@aws-sdk/client-s3');
const mime = require('mime-types');
const { Kafka } = require('kafkajs');

const client = new S3Client({
    region: process.env.AWS_S3_REGION,
    credentials: {
        accessKeyId: process.env.AWS_S3_ACCESS,
        secretAccessKey: process.env.AWS_S3_SECRET
    }
});

const BUCKET_KEY = process.env.AWS_S3_BUCKET;
const PROJECT_ID = process.env.PROJECT_ID;
const DEPLOYMENT_ID = process.env.DEPLOYMENT_ID;

const kafka = new Kafka({
    clientId: `builder-service-${DEPLOYMENT_ID}`,
    brokers: [`${process.env.KAFKA_ADDRESS}:9092`]
});

const producer = kafka.producer();

const publishLog = async (log, status) => {
    await producer.send({
        topic: `container-logs`,
        messages: [
            { key: 'log', value: JSON.stringify({
                projectId: PROJECT_ID,
                deploymentId: DEPLOYMENT_ID,
                status,
                log
            }) }
        ]
    });
};

const logAndPublish = async (log, status) => {
    console.log(`${status}: ${log})`);
    await publishLog(log, status);
};

const STATUS = {
    PENDING: "PENDING",
    RUNNING: "RUNNING",
    FAILURE: "FAILURE",
    SUCCESS: "SUCCESS"
}

const initialization = async () => {
    
    await producer.connect();
    
    try {
        
        await logAndPublish("Build creation is initiated", STATUS.RUNNING);
    
        // Create project build
        const workspace = path.join(__dirname, 'workspace');
        const process = exec(`cd ${workspace} && npm ci && npm run build`);
        
        process.stdout.on('data', async (data) => {
            await logAndPublish(data, STATUS.RUNNING);
        });
        
        process.stderr.on('data', async (data) => {
            await logAndPublish(data, STATUS.RUNNING);
        });
        
        process.on('close', async () => {
            try {
                await logAndPublish("Build completed successfully", STATUS.RUNNING);
                const distFoldPath = path.join(workspace, 'build');
                
                // Checking if build folder was created or not
                try {
                    const distFoldStat = statSync(distFoldPath);
                    if (!distFoldStat.isDirectory()) {
                        await logAndPublish("Build folder couldn't be found", STATUS.FAILURE);
                        await producer.disconnect();
                        return;
                    }
                } catch (error) {
                    await logAndPublish("Build folder couldn't be found", STATUS.FAILURE);
                    await producer.disconnect();
                    return;
                }
                
                // Uploading all file within build folder to S3 bucket
                const distFoldFiles = readdirSync(distFoldPath, { recursive: true });
                for (const file of distFoldFiles) {
                    const filePath = path.join(distFoldPath, file);
                    if (lstatSync(filePath).isDirectory()) {
                        continue;
                    } else {
                        await logAndPublish(`Uploading File: ${file}`, STATUS.RUNNING);
                        const command = new PutObjectCommand({
                            Bucket: BUCKET_KEY,
                            Key: `public/${PROJECT_ID}/${file}`,
                            Body: createReadStream(filePath),
                            ContentType: mime.lookup(filePath)
                        });
                        await client.send(command);
                        await logAndPublish(`Upload complete: ${file}`, STATUS.RUNNING);
                        console.log("Upload complete:", file);
                    }
                }
                
                await logAndPublish("Build creation is complete", STATUS.SUCCESS);
                await producer.disconnect();
            } catch (error) {
                await logAndPublish("Error occurred while uploading file from build folder", STATUS.FAILURE);
                await producer.disconnect();
            }
        })
    } catch (error) {
        await logAndPublish("Error occurred while create build or while uploading it", STATUS.FAILURE);
        await producer.disconnect();
    }
}

initialization();