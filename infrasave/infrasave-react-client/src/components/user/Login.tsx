import {Button, Card, Form, Input, Layout, Space, Tooltip, Typography} from 'antd';
import React, {useEffect, useState} from 'react';
import {LoginRequest} from "../../types/requests";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import {authenticateFail, authenticateSuccess} from "../../redux/authenticationReducer";
import {Link, useNavigate} from "react-router-dom";
import {Content} from "antd/es/layout/layout";
import axiosInstance from "../../config/customAxios";
import {Response} from "../../types/response";

const Login: React.FC = () => {

	const [loading, setLoading] = useState(false);
	const onFinish = (values: any) => {
		const loginRequest: LoginRequest = {
			email: values.email,
			password: values.password
		}
		setLoading(true);
		axiosInstance.post("/login", loginRequest, {withCredentials: true}).then(
			response => {
				const res: Response = response.data;
				if (res.successful) {
					dispatch(authenticateSuccess());
					localStorage.setItem("authenticated", "true");
				} else {
					dispatch(authenticateFail());
					alert("Error title: " + res.errorTitle + " Error description: " + res.errorDetail);
				}
			}).catch(response => {
			alert("Couldn't login.")
			dispatch(authenticateFail());
		}).finally(() => setLoading(false));
	};

	const dispatch = useDispatch();
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const navigate = useNavigate();

	useEffect(() => {
		const authenticatedLocal = localStorage.getItem("authenticated");
		if (authenticatedLocal || authenticated) {
			navigate("/");
		}
	}, [])

	useEffect(() => {
		if (authenticated) {
			navigate("/");
		}
	}, [authenticated])
	return (
		<div>
			<Layout style={{
				padding: '50px',
				background: '#ececec'
			}}>
				<Content style={{margin: 'auto', width: '50%', height: 'auto'}}>
					<Card>
						<Form
							layout="horizontal"
							name="basic"
							labelCol={{span: 7}}
							wrapperCol={{span: 12}}
							onFinish={onFinish}
							autoComplete="off"
							disabled={loading}
						>
							<Form.Item
								label="E-mail"
								name="email"
								rules={[{required: true, message: 'Please input your email!'}]}
							>
								<Input/>
							</Form.Item>

							<Form.Item
								label="Password"
								name="password"
								rules={[{required: true, message: 'Please input your password!'}]}
							>
								<Input.Password/>
							</Form.Item>

							<Form.Item wrapperCol={{offset: 11, span: 16}}>
								<Button type="primary" htmlType="submit" loading={loading}>
									Submit
								</Button>
							</Form.Item>
							<Space>
								<Tooltip title="Useful information">
									<Link to="/forgot-password">Forgot password?</Link>
								</Tooltip>
							</Space>
						</Form>
					</Card>
				</Content>
			</Layout>
		</div>
	);
};

export default Login;