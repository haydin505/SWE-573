import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "./components/user/Login";
import React from "react";
import Home from "./Home";
import Nav from "./components/nav/Nav";
import Logout from "./components/user/Logout";
import Register from "./components/user/Register";
import Search from "./components/search/SearchPage";
import MyContent from "./components/mycontent/MyContent";
import Profile from "./components/user/Profile";

const AppRouter: React.FC = () => {
	return <BrowserRouter>
		<Nav/>
		<Routes>
			<Route path="login" element={<Login/>}/>
			<Route path="register" element={<Register/>}/>
			<Route path="logout" element={<Logout/>}/>
			<Route path="my-content" element={<MyContent/>}/>
			<Route path="profile" element={<Profile/>}/>
			<Route path="search" element={<Search/>}/>
			<Route path="/" element={<Home/>}/>
		</Routes>
	</BrowserRouter>
}

export default AppRouter;

