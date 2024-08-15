require('dotenv').config();

const express = require('express');
const httpProxy = require('http-proxy');
const cors = require('cors')

const app = express();
const PORT = process.env.SERVICE_PORT_NO || 8000;

const proxy = httpProxy.createProxy();

app.use(cors());

app.use((req, res) => {
    
    const hostname = req.hostname;
    const subdomain = hostname.split('.')[0];
    const resolvesTo = `${process.env.AWS_S3_BASE_URL}/${subdomain}`;
    
    console.log(`HOSTNAME: [${hostname}], SUBDOMAIN: [${subdomain}], RESOLVES_TO: [${resolvesTo}]`);
    
    proxy.web(req, res, {
        target: resolvesTo,
        changeOrigin: true
    });
});

proxy.on('proxyReq', (proxyReq, req, res) => {
    
    const url = req.url;
    
    console.log(`URL: [${url}]`);
    
    if (url === '/') {
        proxyReq.path += "index.html";
    }
});

app.listen(PORT, () => {
    console.log(`Gateway service is listening on port: ${PORT}`);
});