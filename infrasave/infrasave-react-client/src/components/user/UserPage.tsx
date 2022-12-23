import React, {FC, useEffect, useState} from "react";
import {Avatar, Button, Card, Descriptions, Layout, List, Skeleton} from "antd";
import {Link, useNavigate, useParams} from "react-router-dom";
import axiosInstance from "../../config/customAxios";
import {UserDTO} from "../../types/types";
import {Response} from "../../types/response";
import {formatDate} from "../../utils/util";
import ContentModule from "../content/ContentModule";
import {CheckOutlined, ClockCircleOutlined} from "@ant-design/icons";
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";

interface UserPageProps {

}

const UserPage: FC<UserPageProps> = (props): JSX.Element => {

	const [user, setUser] = useState<UserDTO | undefined>(undefined);
	const [loading, setLoading] = useState(false);
	const navigate = useNavigate();
	const currentUser = useSelector((state: RootState) => state.authentication.user);

	const {id} = useParams();

	useEffect(() => {
		if (id && currentUser) {
			if (currentUser.userId === +id) {
				navigate("/profile");
				return;
			}
			getUser();
		}
	}, [id, currentUser]);

	useEffect(() => {

	}, [id, user]);

	const onAddFriend = async (requesteeId: number | undefined) => {
		const request = {
			requesteeId: requesteeId,
		}
		try {
			const res = await axiosInstance.post("/friends", request, {withCredentials: true});
			const response: Response = res.data;
			console.log(response);
			if (!response.successful) {
				alert("Couldn't add friend.");
				return;
			}
			getUser();
		} catch (err) {
			alert("Couldn't add friend request.");
		}
	}

	const onUpdateFriend = async (id: number, friendRequestStatus: string) => {
		const request = {
			id: id,
			friendRequestStatus: friendRequestStatus
		}
		try {
			const res = await axiosInstance.put("/friends", request, {withCredentials: true});
			const response: Response = res.data;
			console.log(response);
		} catch (err) {
			alert("Couldn't complete friend request.");
		}
	}

	const onRemoveFriend = async (id: number | undefined) => {
		if (!id) {
			return;
		}
		try {
			const res = await axiosInstance.delete(`/friends/${id}`, {withCredentials: true});
			const response: Response = res.data;
			console.log(response)
			if (!response.successful) {
				alert("Couldn't remove friend.");
			}
			getUser();
		} catch (err) {
			alert("Couldn't remove friend.");
		}
	}

	const getUser = () => {
		setLoading(true);
		axiosInstance.get(`/users/${id}`, {withCredentials: true}).then((res) => {
			const response: Response = res.data;
			if (!response.successful) {
				alert("Couldn't get user details.");
				return;
			}
			const userDTO: UserDTO = response.data;
			setUser(userDTO)
		}).catch(err => {
			alert("Couldn't get user details.");
		}).finally(() => setLoading(false));
	}

	const friendButtons = () => {
		if (user?.friendDTO?.friendRequestStatus === 'APPROVED') {
			return (<>
				<CheckOutlined/><Button onClick={() => onRemoveFriend(user?.friendDTO?.id)} style={{marginLeft: 10}}>Remove
				Friend</Button>
			</>)
		}
		if (user?.friendDTO?.friendRequestStatus === 'PENDING') {
			return <><ClockCircleOutlined style={{marginRight: 10}}/><p>Pending</p></>
		}
		if (!user?.friendDTO?.friendRequestStatus
			|| user?.friendDTO?.friendRequestStatus
			=== 'NONE'
			|| user?.friendDTO?.friendRequestStatus
			=== 'REJECTED') {
			return <Button onClick={() => onAddFriend(user?.userId)}>Add Friend</Button>
		}
		return null;
	}

	console.log(user);
	return (
		<>
			<Layout style={{
				padding: '50px',
				background: '#ececec',
				alignItems: 'center'
			}}>
				<Card style={{width: '50%'}} loading={loading}>
					<Descriptions title="User Details">
						<Descriptions.Item label="Name">{user?.name}</Descriptions.Item>
						<Descriptions.Item label="Surname">{user?.surname}</Descriptions.Item>
						<Descriptions.Item label="Username">{user?.username}</Descriptions.Item>
						<Descriptions.Item label="Birth Date">{formatDate(user?.birthDate)}</Descriptions.Item>
						<Descriptions.Item label="Number of Friends">{user?.friendCount}</Descriptions.Item>
						<Descriptions.Item label="Friend Status">{friendButtons()}</Descriptions.Item>
					</Descriptions>
					{user?.friends && user?.friends.length > 0 &&
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
						</Card>}
				</Card>
			</Layout>
			<Layout style={{
				padding: 0,
				background: '#ececec',
				alignItems: 'center'
			}}>
				<h1>Created Contents</h1>
			</Layout>
			{user && user.createdContents && <ContentModule contents={user.createdContents} enableAddContent={false}
																											reloadContent={getUser} loading={loading}/>}
		</>
	)
}

export default UserPage;