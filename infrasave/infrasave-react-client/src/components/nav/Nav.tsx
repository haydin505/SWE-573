import {Badge, Button, Menu, Modal, Table} from "antd";
import {Link} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {
	HeartOutlined,
	HomeOutlined,
	LoginOutlined,
	NotificationOutlined,
	SearchOutlined,
	UserOutlined
} from '@ant-design/icons';
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import axiosInstance from "../../config/customAxios";
import {Response} from "../../types/response";
import {FriendDTO, UserDTO} from "../../types/types";

const Nav: React.FC = (): JSX.Element => {
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const [friendRequests, setFriendRequests] = useState<FriendDTO[]>([]);
	const [showNotificationModal, setShowNotificationModal] = useState(false);

	const toggleNotificationModal = () => setShowNotificationModal(!showNotificationModal);

	useEffect(() => {
		getPendingFriendRequest();

	}, []);

	useEffect(() => {
		getPendingFriendRequest();

	}, [authenticated]);

	const getPendingFriendRequest = async () => {
		const res = await axiosInstance.get("friends/requests/requestee/pending", {withCredentials: true});
		const response: Response = res.data;
		if (response.successful) {
			setFriendRequests(response.data);
		}
	}

	const friendRequestTableColumns = [{
		title: 'Username',
		dataIndex: 'requesterDetail',
		render: (data: UserDTO, record: any) => {
			return <Link target="_blank" to={`/users/${data.userId}`}>{data.username}</Link>
		}
	},
		{
			title: 'Name',
			dataIndex: ["requesterDetail", "name"],
		},
		{
			title: 'Surname',
			dataIndex: ['requesterDetail', 'surname'],
		},
		{
			title: '',
			dataIndex: 'id',
			render: (data: number, record: any) => <Button onClick={() => onUpdateFriend(data, 'APPROVED')}
			                                               type="primary">Approve</Button>,
		}
	]

	const onUpdateFriend = async (id: number, friendRequestStatus: string) => {
		const request = {
			id: id,
			friendRequestStatus: friendRequestStatus
		}
		try {
			const res = await axiosInstance.put("/friends", request, {withCredentials: true});
			const response: Response = res.data;
			if (!response.successful) {
				alert("Couldn't approve friend request");
				return;
			}
			const updatedFriendRequests = friendRequests.filter(friendRequest => friendRequest.id !== id);
			setFriendRequests(updatedFriendRequests);
		} catch (err) {
			alert("Couldn't complete friend request.");
		}
	}

	console.log(authenticated)
	return (
		<>
			<Modal footer={false} open={showNotificationModal} onCancel={toggleNotificationModal}>{<Table
				dataSource={friendRequests} columns={friendRequestTableColumns}/>}</Modal>
			<Menu mode="horizontal" inlineIndent={1} theme={"dark"}>
				<Menu.Item title="Home" icon={<HomeOutlined/>}>Home<Link to={"/"}/></Menu.Item>
				{authenticated ? null : <Menu.Item title="Login" icon={<LoginOutlined/>}>Login<Link to={"/login"}/></Menu.Item>}
				{authenticated ? null : <Menu.Item title="Register">Register<Link to={"/register"}/></Menu.Item>}
				{authenticated ? <Menu.Item title="Search" icon={<SearchOutlined/>}>Search<Link
					to={"/search"}/></Menu.Item> : null}
				{authenticated ? <Menu.Item title="Liked Contents" icon={<HeartOutlined/>}>Liked Contents<Link
					to={"/liked-content"}/></Menu.Item> : null}
				{authenticated ? <Menu.Item title="Profile" icon={<UserOutlined/>}>Profile<Link
					to={"/profile"}/></Menu.Item> : null}
				{authenticated ? <Menu.Item onClick={toggleNotificationModal} title="Friend Requests"
				                            icon={<NotificationOutlined/>}>Friend
					Requests <Badge
						style={{marginLeft: 5}} count={friendRequests.length}/></Menu.Item> : null}
				{authenticated ? <Menu.Item title="Logout">Logout<Link to={"/logout"}/></Menu.Item> : null}
			</Menu>
		</>);
}

export default Nav;