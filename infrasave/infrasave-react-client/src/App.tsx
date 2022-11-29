import React, {useEffect} from 'react';
import './App.css';
import AppRouter from "./AppRouter";
import 'antd/dist/antd.min.css'
import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import {loadUser, logout} from "./redux/authenticationReducer";
import {RootState} from "./redux/store";

function App(): JSX.Element {
	const dispatch = useDispatch();
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);

	useEffect(() => {
		axios.get("http://localhost:8080/users/current", {withCredentials: true}).then((response) => {
			if (response.status === 200) {
				dispatch(loadUser(response.data));
			}
		}).catch((error) => {
			if (error.response.status === 403) {
				localStorage.removeItem("authenticated");
				dispatch(logout());
			}
		})
	}, [authenticated])
	return <div><AppRouter/></div>;
}

export default App;
