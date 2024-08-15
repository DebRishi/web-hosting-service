import { Container, Grid } from "@mui/material";
import Project from "./Project";

const ProjectDetails = ({ projects, deleteProject, redeployProject }) => {

    return (
        <Container maxWidth="xl">
            <Grid container spacing={1} style={{ marginTop: "20px" }} >
                {projects.map((project, index) => (
                    <Grid item xs={12} sm={6} ms={4} lg={3} key={index}>
                        <Project
                            project={project}
                            deleteProject={deleteProject}
                            redeployProject={redeployProject}
                        />
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
}

export default ProjectDetails;