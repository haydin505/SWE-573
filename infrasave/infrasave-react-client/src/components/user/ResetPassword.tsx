import {Button, Card, Form, Input, Layout, Result} from 'antd';
import React, {useEffect, useState} from 'react';
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import {Link, useNavigate, useSearchParams} from "react-router-dom";
import {Content} from "antd/es/layout/layout";
import axiosInstance from "../../config/customAxios";
import {Response} from "../../types/response";

const ResetPassword: React.FC = () => {
	const [successful, setSuccessful] = useState(false);
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const [searchParams, setSearchParams] = useSearchParams();
	const [token, setToken] = useState<string | undefined>(undefined);
	const navigate = useNavigate();

	useEffect(() => {
		if (authenticated) {
			navigate("/");
		}
	}, [authenticated])

	useEffect(() => {
		const urlParsedToken = searchParams.get("token");
		if (urlParsedToken) {
			setToken(urlParsedToken)
		}
	}, [searchParams]);

	const [loading, setLoading] = useState(false);
	const onFinish = async (values: any) => {
		if (!token) {
			alert("Cannot find token!");
			navigate("/");
			return;
		}
		const request = {
			token: token,
			password: values.password,
		}
		setLoading(true);
		try {
			const res = await axiosInstance.post("/reset-password/token", request);
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
				return
			}
			setSuccessful(true);
		} catch (ex) {
			alert("Couldn't send reset password request.");
		} finally {
			setLoading(false)
		}

	};

	return (
		successful ? <Result status="success" title="Your password changed successfully!" extra={
				<Button type="primary" key="login">
					<Link to="/login">Login</Link>
				</Button>
			}/> :
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
							<Form.Item wrapperCol={{offset: 11, span: 16}}>
								<Button type="primary" htmlType="submit" loading={loading}>
									Submit
								</Button>
							</Form.Item>
						</Form>
					</Card>
				</Content>
			</Layout>
	);
};

export default ResetPassword;