import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./user/Login";
import React from "react";
import Home from "./Home";
import Nav from "./Nav";
import Logout from "./user/Logout";
import Register from "./user/Register";

const AppRouter: React.FC = () => {
	return <BrowserRouter>
		<Nav/>
		<Routes>
			<Route path="/" element={<Home/>}/>
			<Route path="/login" element={<Login/>}/>
			<Route path="/register" element={<Register/>}/>
			<Route path="/logout" element={<Logout/>}/>
		</Routes>
	</BrowserRouter>
}

export default AppRouter;

