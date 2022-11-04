import React, {useEffect} from 'react';
import axios from 'axios';
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../redux/store";
import {logout} from "../redux/authenticationReducer";
import {useNavigate} from "react-router-dom";

const Logout = () => {
	const dispatch = useDispatch();
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const navigate = useNavigate();

	useEffect(() => {
		if (!authenticated) {
			navigate("/");
		}
		axios.post("http://localhost:8080/exit", {}, {withCredentials: true}).then((response) => {
			if (response.status === 200) {
				dispatch(logout());
				localStorage.removeItem("authenticated");
				navigate("/");
			}
		})
	}, [])

	return (<h1>Logout</h1>);
};

export default Logout;