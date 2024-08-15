import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import { createTheme, CssBaseline, ThemeProvider } from '@mui/material'

const darkTheme = createTheme({
	palette: {
		mode: 'dark',
		primary: {
			main: '#90caf9', // Light blue
		},
		secondary: {
			main: '#f48fb1', // Pink
		},
		background: {
			default: '#222222', // Dark background
			paper: '#1e1e1e', // Slightly lighter dark background
		},
		text: {
			primary: '#ffffff', // White text
			secondary: '#b0bec5', // Light gray text
		},
		divider: '#37474f', // Divider color similar to the site
	},
});

ReactDOM.createRoot(document.getElementById('root')).render(
	<React.StrictMode>
		<ThemeProvider theme={darkTheme}>
			<CssBaseline />
			<App />
		</ThemeProvider>
	</React.StrictMode>,
)
