import {Button, Card, DatePicker, Form, Input, Layout, Result} from 'antd';
import React, {useEffect, useState} from 'react';
import {RegisterRequest} from "../../types/requests";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import {Link, useNavigate} from "react-router-dom";
import {Content} from "antd/es/layout/layout";
import axiosInstance from "../../config/customAxios";

const Register: React.FC = () => {
	const onFinish = (values: any) => {
		const registerRequest: RegisterRequest = {
			name: values.name,
			surname: values.surname,
			email: values.email,
			username: values.username,
			password: values.password,
			phoneNumber: values.phoneNumber,
			birthDate: values.birthDate
		}
		console.log(registerRequest);
		axiosInstance.post("/register", registerRequest).then(
			response => {
				if (response.status === 200) {
					const data = response.data;
					console.log(data);
					if (!data.successful) {
						alert(data.errorDetail);
					} else {
						setSuccessful(true)
						alert("Register Successful!")
					}
				}
			}).catch(error => {
			alert(error.response.data.violations[0].field + error.response.data.violations[0].cause);
		});
	};

	const dispatch = useDispatch();
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const navigate = useNavigate();
	const [successful, setSuccessful] = useState(false);

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
			}}> {successful ? <Result status="success" title="Successfully registered!" extra={
					<Button type="primary" key="login">
						<Link to="/login">Login</Link>
					</Button>
				}/> :
				<Content style={{margin: 'auto', width: '50%', height: 'auto'}}>
					<Card>
						<Form
							layout="horizontal"
							name="basic"
							labelCol={{span: 8}}
							wrapperCol={{span: 16}}
							onFinish={onFinish}
							autoComplete="off"
						>
							<Form.Item
								label="Name"
								name="name"
								rules={[{required: true, message: 'Please enter your name!'}]}
							>
								<Input/>
							</Form.Item>
							<Form.Item
								label="Surname"
								name="surname"
								rules={[{required: true, message: 'Please enter your surname!'}]}
							>
								<Input/>
							</Form.Item>
							<Form.Item
								label="Username"
								name="username"
								rules={[{required: true, message: 'Please enter your username!'}]}
							>
								<Input/>
							</Form.Item>
							<Form.Item
								label="E-mail"
								name="email"
								rules={[{required: true, message: 'Please enter your email!'}]}
							>
								<Input/>
							</Form.Item>

							<Form.Item
								label="Password"
								name="password"
								rules={[{required: true, message: 'Please input your password!'}]}
								hasFeedback
							>
								<Input.Password/>
							</Form.Item>
							<Form.Item
								name="confirm"
								label="Confirm Password"
								dependencies={['password']}
								hasFeedback
								rules={[
									{
										required: true,
										message: 'Please confirm your password!',
									},
									({getFieldValue}) => ({
										validator(_, value) {
											if (!value || getFieldValue('password') === value) {
												return Promise.resolve();
											}
											return Promise.reject(new Error('The two passwords that you entered do not match!'));
										},
									}),
								]}
							>
								<Input.Password/>
							</Form.Item>
							<Form.Item
								label="Phone Number"
								name="phoneNumber"
								rules={[{required: true, message: 'Please enter your phone number!'}]}
							>
								<Input/>
							</Form.Item>
							<Form.Item
								label="Birth Date"
								name="birthDate"
								rules={[{required: true, message: 'Please enter your birth date!'}]}
							>
								<DatePicker></DatePicker>
							</Form.Item>

							<Form.Item wrapperCol={{offset: 8, span: 16}}>
								<Button type="primary" htmlType="submit">
									Submit
								</Button>
							</Form.Item>
						</Form>
					</Card>
				</Content>
			}
			</Layout>
		</div>
	);
};

export default Register;