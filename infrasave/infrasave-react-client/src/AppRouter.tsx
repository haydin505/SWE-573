import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./user/Login";
import React from "react";
import Home from "./Home";
import Nav from "./Nav";
import Logout from "./user/Logout";
import Register from "./user/Register";
import Search from "./SearchPage";

const AppRouter: React.FC = () => {
	return <BrowserRouter>
		<Nav/>
		<Routes>
			<Route  path="login" element={<Login/>}/>
			<Route  path="register" element={<Register/>}/>
			<Route  path="logout" element={<Logout/>}/>
			<Route  path="search" element={<Search/>}/>
			<Route path="/" element={<Home/>}/>
		</Routes>
	</BrowserRouter>
}

export default AppRouter;

