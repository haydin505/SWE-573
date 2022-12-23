import {Button, Card, Form, Input, Layout, Result} from 'antd';
import React, {useEffect, useState} from 'react';
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import {Link, useNavigate} from "react-router-dom";
import {Content} from "antd/es/layout/layout";
import axiosInstance from "../../config/customAxios";
import {Response} from "../../types/response";

const ForgotPassword: React.FC = () => {
	const [successful, setSuccessful] = useState(false);
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const navigate = useNavigate();

	useEffect(() => {
		if (authenticated) {
			navigate("/");
		}
	}, [authenticated])

	const [loading, setLoading] = useState(false);
	const onFinish = async (values: any) => {
		const request = {
			email: values.email,
		}
		setLoading(true);
		try {
			const res = await axiosInstance.post("/reset-password", request);
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
		successful ? <Result status="success" title="Success! Please check your e-mail inbox to reset your password!"
		                     extra={
			                     <Button type="primary" key="login">
				                     <Link to="/">Home</Link>
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
								label="E-mail"
								name="email"
								rules={[{required: true, message: 'Please input your email!'}]}
							>
								<Input/>
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

export default ForgotPassword;