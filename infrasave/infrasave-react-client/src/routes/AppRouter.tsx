import {BrowserRouter, Route, Routes} from "react-router-dom";
import Login from "../components/user/Login";
import React from "react";
import Home from "../Home";
import Nav from "../components/nav/Nav";
import Logout from "../components/user/Logout";
import Register from "../components/user/Register";
import Search from "../components/search/SearchPage";
import LikedContent from "../components/likedContent/LikedContent";
import Profile from "../components/user/Profile";
import PrivateRoute from "./PrivateRoute";
import UserPage from "../components/user/UserPage";

const AppRouter: React.FC = () => {
	return <BrowserRouter>
		<Nav/>
		<Routes>
			<Route path="login" element={<Login/>}/>
			<Route path="register" element={<Register/>}/>
			<Route path="logout" element={<Logout/>}/>
			<Route path="liked-content" element={<PrivateRoute><LikedContent/></PrivateRoute>}/>
			<Route path="profile" element={<PrivateRoute><Profile/></PrivateRoute>}/>
			<Route path="search" element={<PrivateRoute><Search/></PrivateRoute>}/>
			<Route path="users/:id" element={<PrivateRoute><UserPage/></PrivateRoute>}/>
			<Route path="/" element={<Home/>}/>
		</Routes>
	</BrowserRouter>
}

export default AppRouter;

