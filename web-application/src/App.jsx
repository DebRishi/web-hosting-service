import { useEffect, useState } from 'react';
import './App.css';
import Navbar from './components/Navbar'
import ProjectDetails from './components/ProjectDetails';
import axios from 'axios';

function App() {
	
	const [projects, setProjects] = useState([]);
	
	useEffect(() => {
		axios.get(`${import.meta.env.VITE_BASE_URL}/project`)
		.then((response) => {
			setProjects(response.data);
		})
		.catch((error) => {
			console.log(error);
		})
	}, []);
	
	const addNewProject = (projectName, gitUrl) => {
		axios.post(`${import.meta.env.VITE_BASE_URL}/project`, {
			projectName,
			gitUrl
		}).then((projectResponse) => {
			const project = projectResponse.data;
			axios.post(`${import.meta.env.VITE_BASE_URL}/deploy`, {
				projectId: project.projectId
			}).then((deploymentResponse) => {
				const deployment = deploymentResponse.data;
				setProjects((prev) => ([
					...prev,
					{
						projectId: project.projectId,
						projectName: project.projectName,
						gitUrl: project.gitUrl,
						deploymentId: deployment.deploymentId,
						projectUrl: deployment.projectUrl,
						status: deployment.status
					}
				]))
			}).catch((error) => {
				console.log(error);
			})
		}).catch((error) => {
			console.log(error);
		})
	}
	
	const deleteProject = (projectId) => {
		axios.delete(`${import.meta.env.VITE_BASE_URL}/project/` + projectId)
		.then((response) => {
			setProjects((prev) => {
				return prev.filter(project => project.projectId !== projectId);
			});
			console.log("DELETED: ", response.data);
		}).catch((error) => {
			console.log(error);
		})
	}
	
	const redeployProject = (projectId) => {
		axios.post(`${import.meta.env.VITE_BASE_URL}/deploy`, {
			projectId
		}).then((response) => {
			console.log(projects);
			const deploymentData = response.data;
			const updatedProjects = projects.map(project => {
				if (project.projectId === projectId) {
					return {
						...project,
						deploymentId: deploymentData.deploymentId,
						status: deploymentData.status
					}
				} else {
					return project;
				}
			});
			setProjects(updatedProjects);
			console.log(updatedProjects);
		}).catch((error) => {
			console.log(error);
		})
	}
	
	return (
		<>
			<Navbar addNewProject={addNewProject}/>
			<ProjectDetails 
				projects={projects} 
				deleteProject={deleteProject}
				redeployProject={redeployProject}
			/>
		</>
	)
}

export default App
