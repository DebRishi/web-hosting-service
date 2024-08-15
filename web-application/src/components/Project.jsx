import { AutoDeleteTwoTone, OpenInNew, Replay } from "@mui/icons-material";
import { Button, Card, CardActions, CardContent, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import RetryDailog from "./dailog/RetryDailog";
import DeleteDialog from "./dailog/DeleteDailog";
import LogsDialog from "./dailog/LogsDailog";
import axios from "axios";

const Project = ({ project, deleteProject, redeployProject }) => {
    
    const [openLogs, setOpenLogs] = useState(false);
    const [openRetry, setOpenRetry] = useState(false);
    const [openDelete, setOpenDelete] = useState(false);
    const [logs, setLogs] = useState([]);
    
    useEffect(() => {
        if (openLogs) {
            axios.get(`${import.meta.env.VITE_BASE_URL}/logs/` + "test-deployment")
            .then((response) => {
                setLogs(response.data);
            }).catch((error) => {
                console.log(error);
            })
        }
    }, [openLogs]);
    
    const logsToggle = () => {
        console.log("View Logs");
        setOpenLogs((prev) => !prev);
    }
    
    const retryToggle = () => {
        setOpenRetry((prev) => !prev);
    }
    
    const deleteToggle = () => {
        setOpenDelete((prev) => !prev);
    }

    return (
        <Card variant="outlined" sx={{ maxWidth: 345 }} >
            <CardContent onClick={logsToggle}>
                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                    {project.status}
                </Typography>
                <Typography variant="h5" component="div">
                    {project.projectName}
                </Typography>
                <Typography sx={{ mt: 1.5 }} color="text.secondary">
                    {project.gitUrl}
                </Typography>
            </CardContent>
            <CardActions>
                <Button variant="outlined" size="small" startIcon={<OpenInNew/>} disabled={project.status !== "SUCCESS"} href={project.projectUrl} target="__blank">
                    Open
                </Button>
                <Button variant="outlined" size="small" startIcon={<Replay/>} color="secondary" onClick={retryToggle}>
                    Rebuild
                </Button>
                <Button variant="outlined" size="small" color="error" startIcon={<AutoDeleteTwoTone/>} onClick={deleteToggle}>
                    Delete
                </Button>
            </CardActions>
            <LogsDialog
                open={openLogs}
                handleClose={logsToggle}
                logs={logs}
            />
            <RetryDailog
                open={openRetry}
                handleClose={retryToggle}
                handleRetry={() => {
                    redeployProject(project.projectId);
                    retryToggle();
                }}
            />
            <DeleteDialog
                open={openDelete}
                handleClose={deleteToggle}
                handleDelete={() => {
                    deleteProject(project.projectId);
                    deleteToggle();
                }}
            />
        </Card>
    );
}

export default Project;