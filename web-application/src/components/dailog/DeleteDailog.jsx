import { Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from "@mui/material";

const DeleteDialog = ({
    open,
    handleClose,
    handleDelete
}) => {
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogTitle id="alert-dialog-title">
                Delete Project
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    Are you sure you want to delete this project ?
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleDelete}>Yes</Button>
                <Button onClick={handleClose} autoFocus>
                    No
                </Button>
            </DialogActions>
        </Dialog>
    );
}
 
export default DeleteDialog;