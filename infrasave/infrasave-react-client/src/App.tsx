import React, {useEffect} from 'react';
import './App.css';
import AppRouter from "./routes/AppRouter";
import 'antd/dist/antd.min.css'
import {useDispatch, useSelector} from "react-redux";
import {authenticateSuccess, loadUser, logout} from "./redux/authenticationReducer";
import {RootState} from "./redux/store";
import axiosInstance from "./config/customAxios";

function App(): JSX.Element {
	const dispatch = useDispatch();
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);

	useEffect(() => getCurrentUser(), [])

	useEffect(() => getCurrentUser(), [authenticated])

	const getCurrentUser = () => {
		const authenticatedLocal = localStorage.getItem("authenticated");
		if (authenticatedLocal) {
			dispatch(authenticateSuccess());
		}
		axiosInstance.get("/users/current", {withCredentials: true}).then((response) => {
			if (response.status === 200) {
				dispatch(authenticateSuccess());
				dispatch(loadUser(response.data));
			}
		}).catch((error) => {
			if (error.response.status === 403) {
				localStorage.removeItem("authenticated");
				dispatch(logout());
			}
		})
	}

	return <AppRouter/>
}

export default App;
