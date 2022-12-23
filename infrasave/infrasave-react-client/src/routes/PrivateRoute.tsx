import React from 'react'
import {Navigate} from 'react-router-dom'

interface PrivateRouteProps {
	children: JSX.Element;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({children}): JSX.Element => {

	if (localStorage.getItem("authenticated")) {
		return children;
	} else {
		return <Navigate to="/login" replace/>
	}
}

export default PrivateRoute;