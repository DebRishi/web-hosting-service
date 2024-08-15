import { Button, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions } from "@mui/material";

const RetryDailog = ({
    open,
    handleClose,
    handleRetry
}) => {
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogTitle id="alert-dialog-title">
                Rebuild Project
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    Are you sure you want to rebuild this project ?
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleRetry}>Yes</Button>
                <Button onClick={handleClose} autoFocus>
                    No
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default RetryDailog;