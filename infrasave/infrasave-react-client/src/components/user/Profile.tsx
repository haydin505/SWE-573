import React, {FC, useEffect, useState} from "react";
import {Avatar, Button, Card, Col, DatePicker, Form, Input, Layout, List, Row, Skeleton} from "antd";
import axiosInstance from "../../config/customAxios";
import {Link} from "react-router-dom";
import {Content, UserDTO} from "../../types/types";
import {Response} from "../../types/response";
import ContentModule from "../content/ContentModule";
import moment from "moment-timezone";

interface ProfileProps {

}

const Profile: FC<ProfileProps> = (props: ProfileProps): JSX.Element => {
	const [user, setUser] = useState<UserDTO | undefined>(undefined);
	const [contents, setContents] = useState<Content[]>([]);
	const [loading, setLoading] = useState(false);

	useEffect(() => {
		getCurrentUser();
	}, [])

	const getCurrentUser = () => {
		setLoading(true)
		axiosInstance.get("/users/current", {withCredentials: true}).then((res) => {
			const userDTO: UserDTO = res.data;
			setUser(userDTO);
			setContents(userDTO.createdContents);
		}).catch((error) => {
			alert("Couldn't get profile details.")
		}).finally(() => setLoading(false))
	}

	const updateCurrentUser = (user: UserDTO) => {
		setLoading(true)
		const request = {
			username: user.username,
			name: user.name,
			surname: user.surname,
			email: user.email,
			birthDate: user.birthDate,
			phoneNumber: user.phoneNumber
		}
		axiosInstance.put("/users", request, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert("Couldn't update profile");
			}
			getCurrentUser();
		}).catch(err => {
			alert("Couldn't update profile");
			setLoading(false);
		});
	}

	return (
		<>
			<Layout style={{
				padding: '50px',
				background: '#ececec',
				alignItems: 'center'
			}}>
				<Card style={{width: '50%'}} loading={loading} title="User Details">
					<Form labelCol={{span: 8}}
					      layout="horizontal"
					      onFinish={updateCurrentUser}
					      fields={[
						      {name: ["name"], value: user?.name},
						      {name: ["surname"], value: user?.surname},
						      {name: ["username"], value: user?.username},
						      {name: ["phoneNumber"], value: user?.phoneNumber},
						      {name: ["birthDate"], value: moment.utc(user?.birthDate).local()}
					      ]}
					>
						<Row>
							<Col span={12}>
								<Form.Item label="Name" name="name">
									<Input/>
								</Form.Item>
							</Col>
							<Col span={12}>
								<Form.Item label="Surname" name="surname">
									<Input/>
								</Form.Item>
							</Col>
						</Row>
						<Row>
							<Col span={12}>
								<Form.Item label="Birth Date" name="birthDate">
									<DatePicker format="YYYY-MM-DD"/>
								</Form.Item>
							</Col>
						</Row>
						<Row>
							<Col span={12}>
								<Form.Item label="Username" name="username">
									<Input/>
								</Form.Item>
							</Col>
							<Col span={12}>
								<Form.Item label="E-mail">
									{user?.email}
								</Form.Item>
							</Col>
						</Row>
						<Row>
							<Col span={12}>
								<Form.Item label="Phone Number" name="phoneNumber">
									<Input/>
								</Form.Item>
							</Col>
						</Row>
						<Row>
							<Col span={12}>
								<Form.Item label="Number of Friends">
									{user?.friendCount}
								</Form.Item>
							</Col>
						</Row>
						<Form.Item wrapperCol={{offset: 11}}>
							<Button type="primary" htmlType="submit" loading={loading}>
								Update
							</Button>
						</Form.Item>
					</Form>
					<Card title="Friend List">
						<div
							id="scrollableDiv"
							style={{
								height: 200,
								overflow: 'auto',
								padding: '0 16px',
								border: '1px solid rgba(140, 140, 140, 0.35)',
							}}
						>
							<List
								className="demo-loadmore-list"
								loading={loading}
								itemLayout="horizontal"
								dataSource={user?.friends}
								renderItem={(item: UserDTO) => (
									<List.Item
										actions={[]}
									>
										<Skeleton avatar title={false} loading={loading} active>
											<List.Item.Meta
												avatar={<Avatar src={null}/>}
												title={<Link to={`/users/${item?.userId}`}>{item.name + " " + item.surname}</Link>}
												description={item.username}
											/>
										</Skeleton>
									</List.Item>
								)}
							/>
						</div>
					</Card>
				</Card>
			</Layout>
			<Layout style={{
				padding: 0,
				background: '#ececec',
				alignItems: 'center'
			}}>
				<h1>Created Contents</h1>
			</Layout>
			<ContentModule contents={contents} reloadContent={getCurrentUser} enableAddContent={true} loading={loading}/>
		</>)

}

export default Profile;