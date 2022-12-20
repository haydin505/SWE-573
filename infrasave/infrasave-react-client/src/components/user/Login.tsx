import {Button, Card, Form, Input, Layout} from 'antd';
import React, {useEffect, useState} from 'react';
import {LoginRequest} from "../../types/requests";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import {authenticateFail, authenticateSuccess} from "../../redux/authenticationReducer";
import {useNavigate} from "react-router-dom";
import {Content} from "antd/es/layout/layout";
import axiosInstance from "../../config/customAxios";

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
				if (response.status === 200) {
					dispatch(authenticateSuccess());
					localStorage.setItem("authenticated", "true");
				}
			}).catch(response => {
			dispatch(authenticateFail());
		}).finally(() => setLoading(false));
	};

	const onFinishFailed = (errorInfo: any) => {
		console.log('Failed:', errorInfo);
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
							labelCol={{span: 8}}
							wrapperCol={{span: 16}}
							onFinish={onFinish}
							onFinishFailed={onFinishFailed}
							autoComplete="off"
							disabled={loading}
						>
							<Form.Item
								label="E-mail"
								name="email"
								rules={[{required: true, message: 'Please input your username!'}]}
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

							<Form.Item wrapperCol={{offset: 8, span: 16}}>
								<Button type="primary" htmlType="submit" loading={loading}>
									Submit
								</Button>
							</Form.Item>
						</Form>
					</Card>
				</Content>
			</Layout>
		</div>
	);
};

export default Login;