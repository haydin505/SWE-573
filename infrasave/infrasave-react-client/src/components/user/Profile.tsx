import React, {FC, useEffect, useState} from "react";
import {Button, Col, DatePicker, Form, Input, Layout, Row} from "antd";
import axiosInstance from "../../config/customAxios";
import {redirect} from "react-router-dom";
import {useDispatch} from "react-redux";
import {Content, UserDTO} from "../../types/types";
import moment from "moment";
import {Response} from "../../types/response";
import {logout} from "../../redux/authenticationReducer";
import ContentModule from "../content/Content";

interface ProfileProps {
}

const Profile: FC<ProfileProps> = (props: ProfileProps): JSX.Element => {
	const dispatch = useDispatch();
	const [user, setUser] = useState<UserDTO | undefined>(undefined);
	const [currentContent, setCurrentContent] = useState<Content | null>(null);
	const [showEditContentModal, setShowEditContentModal] = useState(false);
	const [contents, setContents] = useState<Content[]>([]);

	useEffect(() => {
		getCurrentUser();
	}, [])

	const getCurrentUser = () => {
		axiosInstance.get("/users/current", {withCredentials: true}).then((res) => {
			const userDTO: UserDTO = res.data;
			setUser(userDTO);
			setContents(userDTO.createdContents);
		}).catch((error) => {
			if (error.response.status === 403) {
				localStorage.removeItem("authenticated");
				dispatch(logout());
				redirect("/login");
			}
		})
	}

	const updateCurrentUser = (user: UserDTO) => {
		const request = {
			username: user.username,
			name: user.name,
			surname: user.surname,
			email: user.email,
			birthDate: user.birthDate
		}
		axiosInstance.put("/users", request, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert("Couldn't update profile");
			}
		}).catch(err => {
			alert("Couldn't update profile");
		})
	}

	return (
		<Layout style={{
			padding: '50px',
			background: '#ececec'
		}}>
			{user &&
				<Form onFinish={updateCurrentUser}>
					<Row>
						<Col>
							<Form.Item label="Name" name="name">
								<Input defaultValue={user?.name}/>
							</Form.Item>
						</Col>
						<Col>
							<Form.Item label="Surname" name="surname">
								<Input defaultValue={user?.surname}/>
							</Form.Item>
						</Col>
					</Row>
					<Row>
						<Col>
							<Form.Item label="Username">
								{user.username}
							</Form.Item>
						</Col>
						<Col>
							<Form.Item label="E-mail">
								{user.email}
							</Form.Item>
						</Col>
					</Row>
					<Row>
						<Col>
							<Form.Item label="Number of Friends">
								{user.friendCount}
							</Form.Item>
						</Col>
					</Row>
					<Row>
						<Col>
							<Form.Item label="Birth Date" name="birthDate">
								<DatePicker format="YYYY-MM-DD" defaultValue={moment(user.birthDate)}/>
							</Form.Item>
						</Col>
					</Row>
					<Form.Item wrapperCol={{offset: 11}}>
						<Button type="primary" htmlType="submit">
							Submit
						</Button>
					</Form.Item>
				</Form>
			}
			<ContentModule contents={contents} reloadContent={getCurrentUser} enableAddContent={true}/>
		</Layout>)

}

export default Profile;