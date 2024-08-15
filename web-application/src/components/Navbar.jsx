import { CreateNewFolder } from "@mui/icons-material";
import { AppBar, Box, Button, Toolbar, Typography } from "@mui/material";
import { useState } from "react";
import DeployDailog from "./dailog/DeployDailog";

const Navbar = ({
    addNewProject
}) => {
    
    const [openForm, setOpenForm] = useState(false);
    
    const formHandler = () => {
        setOpenForm(prev => !prev);
    }
    
    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar>
                    <Typography
                        variant="h4"
                        noWrap
                        component="div"
                        sx={{ flexGrow: 1, display: { xs: 'none', sm: 'flex' } }}
                        
                    >
                        <img src="/vite.svg" style={{ marginRight: "10px" }}/>
                        <span>StatixWeb</span>
                    </Typography>
                    <Button variant="contained" onClick={formHandler} color="info" startIcon={<CreateNewFolder/>}>Deploy Project</Button>
                </Toolbar>
            </AppBar>
            <DeployDailog
                open={openForm}
                handleClose={formHandler}
                handleSubmit={formHandler}
                addNewProject={addNewProject}
            />
        </Box>
    );
    
    // return (
    //     <Box sx={{ flexGrow: 1 }}>
    //         <AppBar position="static">
    //             <Toolbar>
    //                 <Typography
    //                     variant="h4"
    //                     noWrap
    //                     component="div"
    //                     sx={{ flexGrow: 1, display: { xs: 'none', sm: 'block' } }}
    //                 >
    //                     WebStatix
    //                 </Typography>
    //                 <Button variant="contained" onClick={formHandler} color="info" startIcon={<CreateNewFolder/>}>Deploy Project</Button>
    //             </Toolbar>
    //         </AppBar>
    //         <DeployDailog
    //             open={openForm}
    //             handleClose={formHandler}
    //             handleSubmit={formHandler}
    //             addNewProject={addNewProject}
    //         />
    //     </Box>
    // );
}

export default Navbar;