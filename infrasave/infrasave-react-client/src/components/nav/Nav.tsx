import {Menu} from "antd";
import {Link} from "react-router-dom";
import React from "react";
import {
	AppstoreOutlined,
	HeartOutlined,
	HomeOutlined,
	LoginOutlined,
	SearchOutlined,
	UserOutlined
} from '@ant-design/icons';
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";

const Nav: React.FC = (): JSX.Element => {
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	return (
		<div>
			<Menu mode="horizontal" inlineIndent={1} theme={"dark"}>
				<Menu.Item title="Home" icon={<HomeOutlined/>}>Home<Link to={"/"}/></Menu.Item>
				{authenticated ? null : <Menu.Item title="Login" icon={<LoginOutlined/>}>Login<Link to={"/login"}/></Menu.Item>}
				{authenticated ? null : <Menu.Item title="Register">Register<Link to={"/register"}/></Menu.Item>}
				{authenticated ? <Menu.Item title="Search" icon={<SearchOutlined/>}>Search<Link
					to={"/search"}/></Menu.Item> : null}
				{authenticated ? <Menu.Item title="My Contents" icon={<HeartOutlined/>}>My Content<Link
					to={"/my-content"}/></Menu.Item> : null}
				{authenticated ? <Menu.Item title="Profile" icon={<UserOutlined/>}>Profile<Link
					to={"/profile"}/></Menu.Item> : null}
				{authenticated ? <Menu.Item title="Logout">Logout<Link to={"/logout"}/></Menu.Item> : null}
			</Menu>
		</div>);
}

export default Nav;