import {Menu} from "antd";
import {Link, useNavigate} from "react-router-dom";
import React, {useEffect} from "react";
import {AppstoreOutlined, HomeOutlined, LoginOutlined, UserOutlined} from '@ant-design/icons';
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "./redux/store";
import {authenticateSuccess} from "./redux/authenticationReducer";

const Nav: React.FC = (): JSX.Element => {
	const dispatch = useDispatch();
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const navigate = useNavigate();
	useEffect(() => {
		const authenticatedLocal = localStorage.getItem("authenticated");
		if (authenticatedLocal && !authenticated) {
			dispatch(authenticateSuccess());
		}
		if (authenticatedLocal || authenticated) {
			navigate("/");
		}
	}, [])
	return (
		<div>
			<Menu mode="horizontal" inlineIndent={1} theme={"dark"}>
				<Menu.Item title="Home" icon={<HomeOutlined/>}>Home<Link to={"/"}/></Menu.Item>
				{authenticated ? null : <Menu.Item title="Login" icon={<LoginOutlined/>}>Login<Link to={"/login"}/></Menu.Item>}
				{authenticated ? null : <Menu.Item title="Register">Register<Link to={"/register"}/></Menu.Item>}
				{authenticated ? <Menu.SubMenu key="user" title="User" icon={<UserOutlined/>}>
					<Menu.Item key="two" icon={<AppstoreOutlined/>}>
						Navigation Two
					</Menu.Item>
					<Menu.Item key="three" icon={<AppstoreOutlined/>}>
						Navigation Three
					</Menu.Item>
					<Menu.ItemGroup title="Item Group">
						<Menu.Item key="four" icon={<AppstoreOutlined/>}>
							Navigation Four
						</Menu.Item>
						<Menu.Item key="five" icon={<AppstoreOutlined/>}>
							Navigation Five
						</Menu.Item>
					</Menu.ItemGroup>
				</Menu.SubMenu> : null}
				{authenticated ? <Menu.Item title="Logout">Logout<Link to={"/logout"}/></Menu.Item> : null}
			</Menu>
		</div>);
}

export default Nav;