import React, {useEffect, useState} from "react";
import {Anchor, Button, Checkbox, Input, Layout, Popover, Spin, Table, Tabs, Tag} from "antd";
import customAxios from "../../config/customAxios";
import axiosInstance from "../../config/customAxios";
import {Response} from "../../types/response";
import {CheckboxValueType} from "antd/es/checkbox/Group";
import {Content, Search, UserDTO} from "../../types/types";
import {Link, useSearchParams} from "react-router-dom";
import {formatDate} from "../../utils/util";
import {CheckOutlined, ClockCircleOutlined, HeartFilled, HeartOutlined} from "@ant-design/icons";
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import {User} from "../../redux/authenticationReducer";

const SearchPage: React.FC = (): JSX.Element => {
	const options = [{label: "User", value: "user"}, {label: "Content", value: "content"}, {label: "Tag", value: "tag"}];
	const [checkedValues, setCheckedValues] = useState<CheckboxValueType[]>(['user', 'content', 'tag']);
	const [search, setSearch] = useState<Search | undefined>(undefined);
	const [loading, setLoading] = useState(false);
	const [query, setQuery] = useState<string | undefined>(undefined);
	const [searchParams, setSearchParams] = useSearchParams();
	const currentUser: User | undefined = useSelector((state: RootState) => state.authentication.user);

	useEffect(() => {
		const urlParsedQuery = searchParams.get("query");
		if (urlParsedQuery) {
			setQuery(urlParsedQuery)
			onSearch(urlParsedQuery);
		}
	}, [searchParams]);

	const onSearch = (data: string | undefined = query) => {
		window.history.replaceState(query, "Search", `/search?query=${data}`)
		setLoading(true);
		const obj: any = {
			query: data
		};
		checkedValues.forEach((c: any) => {
			obj[c] = true;
		});
		customAxios.get("/search", {params: obj, withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert("Search couldn't complete successfully");
				return;
			}
			const search: Search = response.data;
			setSearch(search);
		}).catch(err => {
			alert("Search couldn't complete successfully");
		}).finally(() => setLoading(false));
	}

	const changeMyContentStatus = (content: Content) => {
		const contents: Content[] | undefined = search?.contents?.map((c: Content) => {
			if (c.id === content.id) {
				c.myContent = !c.myContent;
			}
			return c;
		});
		const contentsByTags: Content[] | undefined = search?.contentsByTags?.map((c: Content) => {
			if (c.id === content.id) {
				c.myContent = !c.myContent;
			}
			return c;
		});
		const s: Search = {...search, contents: contents, contentsByTags: contentsByTags};
		setSearch(s);
	}

	const onCheckBoxChange = (checkedValues: CheckboxValueType[]) => {
		console.log(checkedValues)
		setCheckedValues(checkedValues);
	};

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
			onSearch();
		} catch (err) {
			alert("Couldn't add friend request.");
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
			onSearch();
		} catch (err) {
			alert("Couldn't remove friend.");
		}
	}

	const userColumns = [{
		title: 'Username',
		dataIndex: 'username',
		render: (data: string, record: UserDTO) => {
			return <Link to={`/users/${record.userId}`}>{data}</Link>
		}
	},
		{
			title: 'Name',
			dataIndex: 'name',
			key: 'name',
		},
		{
			title: 'Surname',
			dataIndex: 'surname',
			key: 'surname',
		},
		{
			title: 'Friend Status',
			dataIndex: ['friendDTO', 'friendRequestStatus'],
			render: (data: string, record: UserDTO) => {
				const id = record.friendDTO?.id;
				const friendRequestStatus = data;
				if (currentUser?.userId === record.userId) {
					return <p>This is your profile</p>
				}
				const friendButtons = () => {
					if (friendRequestStatus === 'APPROVED') {
						return (<>
							<CheckOutlined/><Button onClick={() => onRemoveFriend(id)} style={{marginLeft: 10}}>Remove
							Friend</Button>
						</>)
					}
					if (friendRequestStatus === 'PENDING') {
						return <><ClockCircleOutlined style={{marginRight: 10}}/>Pending</>
					}
					if (!friendRequestStatus
						|| friendRequestStatus
						=== 'NONE'
						|| friendRequestStatus
						=== 'REJECTED') {
						return <Button onClick={() => onAddFriend(record.userId)}>Add Friend</Button>
					}
					return null;
				}
				return friendButtons();
			},
		}
	]

	const onClickAddToMyContent = (content: Content) => {
		console.log(content.id);
		axiosInstance.post("/my-content", {contentId: content.id}, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
				return;
			}
			changeMyContentStatus(content);
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		})
	}

	const onClickDeleteMyContent = (content: Content) => {
		axiosInstance.delete(`/my-content/${content.id}`, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
				return;
			}
			console.log(content)
			changeMyContentStatus(content);
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		})
	}

	const contentColumns = [
		{
			title: 'Title',
			dataIndex: 'title',
			key: 'title',
		},
		{
			title: 'Url',
			dataIndex: 'url',
			render: (data: string, record: Content) => <Anchor><Anchor.Link target="_blank" href={data}
			                                                                title="Link"/></Anchor>
		},
		{
			title: 'Created At',
			dataIndex: 'createdAt',
			render: (data: Date) => formatDate(data, 'YYYY-MM-DD HH:mm')
		},
		{
			title: 'Last Updated At',
			dataIndex: 'lastUpdatedAt',
			render: (data: Date) => formatDate(data, 'YYYY-MM-DD HH:mm')
		},
		{
			title: 'Description',
			dataIndex: 'description',
			key: 'description',
		},
		{
			title: 'My Content',
			dataIndex: 'myContent',
			render: (data: boolean, record: Content) => {
				return <>{record.myContent ? <HeartFilled onClick={() => onClickDeleteMyContent(record)}/> :
					<HeartOutlined onClick={() => onClickAddToMyContent(record)}/>
				}</>
			}
		},
		{
			title: 'Creator User',
			dataIndex: 'creatorUser',
			render: (data: boolean, record: Content) => {
				return <Link target="_blank" to={`/users/${record.creatorUser.userId}`}>{record.creatorUser.username}</Link>
			}
		},
		{
			title: 'Tags',
			dataIndex: 'tags',
			render: (data: boolean, record: Content) => {
				return <>{record.tags.map(tag => {
					return <Popover content={tag.description}><Tag color={tag.color}>{tag.name}</Tag></Popover>
				})
				}</>
			}
		}
	]

	return (<Layout style={{
		textAlign: "center", display: "flex", padding: '50px', background: '#ececec'
	}}>
		<Input.Search style={{margin: 'auto', width: '50%'}} size={"middle"} bordered
		              value={query} defaultValue={query} onChange={(data) => setQuery(data.target.value)}
		              onSearch={onSearch}/>
		<Checkbox.Group options={options} defaultValue={['user', 'content', 'tag']} onChange={onCheckBoxChange}/>
		<Tabs
			defaultActiveKey="1"
			onChange={() => {
			}}
			items={[
				{
					label: `User (${search?.users?.length || 0})`,
					key: '1',
					children: loading ? <Spin/> : <Table dataSource={search?.users} columns={userColumns}/>,
				},
				{
					label: `Content (${search?.contents?.length || 0})`,
					key: '2',
					children: loading ? <Spin/> : <Table dataSource={search?.contents} columns={contentColumns}/>,
				},
				{
					label: `Content By Tag  (${search?.contentsByTags?.length || 0})`,
					key: '3',
					children: loading ? <Spin/> : <Table dataSource={search?.contentsByTags} columns={contentColumns}/>,
				},
			]}
		/>
	</Layout>);
}

export default SearchPage;