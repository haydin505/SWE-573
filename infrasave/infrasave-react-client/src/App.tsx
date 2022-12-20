import React, {useEffect} from 'react';
import './App.css';
import AppRouter from "./AppRouter";
import 'antd/dist/antd.min.css'
import {useDispatch, useSelector} from "react-redux";
import {authenticateSuccess, loadUser, logout} from "./redux/authenticationReducer";
import {RootState} from "./redux/store";
import axiosInstance from "./config/customAxios";
import {redirect} from "react-router-dom";

function App(): JSX.Element {
	const dispatch = useDispatch();
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	console.log(authenticated);
	useEffect(() => {
		const authenticatedLocal = localStorage.getItem("authenticated");
		if (authenticatedLocal) {
			dispatch(authenticateSuccess());
		}
		axiosInstance.get("/users/current", {withCredentials: true}).then((response) => {
			if (response.status === 200) {
				dispatch(loadUser(response.data));
			}
		}).catch((error) => {
			if (error.response.status === 403) {
				localStorage.removeItem("authenticated");
				dispatch(logout());
				redirect("/login");

			}
		})
	}, [authenticated])
	return <div><AppRouter/></div>;
}

export default App;
