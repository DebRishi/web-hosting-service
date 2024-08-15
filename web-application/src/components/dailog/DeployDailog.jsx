import { Dialog, DialogTitle, DialogContent, DialogContentText, TextField, DialogActions, Button } from "@mui/material";

const DeployDailog = ({
    open,
    handleClose,
    addNewProject
}) => {
    
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            PaperProps={{
                component: 'form',
                onSubmit: (event) => {
                    event.preventDefault();
                    const formData = new FormData(event.currentTarget);
                    const { projectName, gitUrl } = Object.fromEntries(formData.entries());
                    addNewProject(projectName, gitUrl);
                    handleClose();
                },
            }}
        >
            <DialogTitle>Deploy Project</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Please provide an appropiate project name and git URL for deploying your static website
                </DialogContentText>
                <TextField
                    required
                    id="projectName"
                    name="projectName"
                    label="Project Name"
                    fullWidth
                    sx={{ marginTop: 2 }}
                />
                <TextField
                    required
                    id="gitUrl"
                    name="gitUrl"
                    label="Git URL"
                    fullWidth
                    sx={{ marginTop: 2 }}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button type="submit">Deploy</Button>
            </DialogActions>
        </Dialog>
    );
}

export default DeployDailog;