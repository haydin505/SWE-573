import React from 'react'
import {Navigate} from 'react-router-dom'
import {useSelector} from "react-redux";
import {RootState} from "../redux/store";

interface PrivateRouteProps {
	children: JSX.Element;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({children}): JSX.Element => {

	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	if (localStorage.getItem("authenticated")) {
		return children;
	} else {
		return <Navigate to="/login" replace/>
	}
}

export default PrivateRoute;