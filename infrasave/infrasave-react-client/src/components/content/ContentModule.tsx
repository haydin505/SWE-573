import React, {FC, useEffect, useState} from "react";
import {Content} from "../../types/types";
import {Anchor, Button, Card, Col, Descriptions, Empty, Image, Layout, Popover, Row, Space, Spin, Tag} from "antd";
import {DeleteOutlined, EditOutlined, HeartFilled, HeartOutlined} from "@ant-design/icons";
import {Link} from "react-router-dom";
import AddContentModal from "./AddContentModal";
import EditContentModal from "./EditContentModal";
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import axiosInstance from "../../config/customAxios";
import {Response} from "../../types/response";
import {User} from "../../redux/authenticationReducer";
import moment from "moment-timezone";

interface ContentModuleProps {
	contents: Content[];
	user?: User;
	enableAddContent: boolean;

	reloadContent(): any;

	loading: boolean;
}

const ContentModule: FC<ContentModuleProps> = (props): JSX.Element => {
	const user = useSelector((state: RootState) => state.authentication.user);
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const [contents, setContents] = useState<Content[]>([]);
	const [showAddContentModal, setShowAddContentModal] = useState(false);
	const [showEditContentModal, setShowEditContentModal] = useState(false);
	const [currentContent, setCurrentContent] = useState<Content | null>(null);
	const {loading} = props;

	useEffect(() => {
		props.reloadContent();
	}, [])

	useEffect(() => {
		setContents(props.contents);
	}, [props.contents])

	const addContent = () => {
		showAddContentModal ? setShowAddContentModal(false) : setShowAddContentModal(true);
	}

	const showEditContent = () => {
		showEditContentModal ? setShowEditContentModal(false) : setShowEditContentModal(true);
	}

	const onClickEditContent = (content: Content) => {
		setCurrentContent(content);
		setShowEditContentModal(true);
	}

	const onEditContentComplete = () => {
		props.reloadContent();
	}

	const onAddContentComplete = () => {
		props.reloadContent();
	}

	const onClickDeleteContent = (content: Content) => {
		axiosInstance.delete("/contents/" + content.id, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
				return;
			}
			const filteredContents = contents.filter(c => c.id !== content.id);
			setContents(filteredContents);
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		})
	}

	const onClickAddToMyContent = (content: Content) => {
		console.log(content.id);
		axiosInstance.post("/my-content", {contentId: content.id}, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
				return;
			}
			const filteredContents = contents.map((c: Content) => {
				if (c.id === content.id) {
					c.myContent = true;
				}
				return c;
			});
			setContents(filteredContents);
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		});
	}

	const onClickDeleteMyContent = (content: Content) => {
		axiosInstance.delete(`/my-content/${content.id}`, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
				return;
			}
			const filteredContents = contents.map((c: Content) => {
				if (c.id === content.id) {
					c.myContent = false;
				}
				return c;
			});
			setContents(filteredContents);
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		});
	}

	return <div className="site-card-border-less-wrapper">
		<Layout style={{
			padding: '50px',
			background: '#ececec'
		}}>
			{authenticated && <Space>
				<Button size='middle' type="primary" onClick={addContent}>
					Add Content
				</Button>
			</Space>}
			<AddContentModal showAddContentModal={showAddContentModal} setShowAddContentModal={addContent}
			                 onAddContentComplete={onAddContentComplete}/>
			<EditContentModal content={currentContent} showEditContentModal={showEditContentModal}
			                  setShowEditContentModal={showEditContent} onEditContentComplete={onEditContentComplete}/>
			{loading ? (<Spin tip="Loading" size="large">
				<div className="content"/>
			</Spin>) : (user && contents.length > 0 ? contents.map((content: Content) => {
				return (<Card className="content" style={{maxWidth: '900px', padding: 0}}>
					< Row style={{marginBottom: 5}}>
						{user && content.creatorUser.userId === user.userId ?
							<>
								<Popover content={<p>Add to my content</p>}>
									<Col span={2} offset={18}>
										<Card>{content.myContent ? <HeartFilled onClick={() => onClickDeleteMyContent(content)}/> :
											<HeartOutlined onClick={() => onClickAddToMyContent(content)}/>}</Card>
									</Col>
								</Popover>
								<Popover content={<p>Edit content</p>}>
									<Col span={2} offset={0}>
										<Card><EditOutlined onClick={() => onClickEditContent(content)}/></Card>
									</Col>
								</Popover>
								<Popover content={<p>Delete content</p>}>
									<Col span={2} offset={0}>
										<Card><DeleteOutlined onClick={() => onClickDeleteContent(content)}/></Card>
									</Col>
								</Popover>
							</>
							:
							<Popover content={<p>Add to my content</p>}>
								<Col span={2} offset={22}>
									<Card>{content.myContent ? <HeartFilled onClick={() => onClickDeleteMyContent(content)}/> :
										<HeartOutlined onClick={() => onClickAddToMyContent(content)}/>}</Card>
								</Col>
							</Popover>
						}
					</Row>
					<Descriptions bordered>
						<Descriptions.Item span={3} label="Creator username">
							<Link to={`/users/${content.creatorUser.userId}`}>{content.creatorUser.username}</Link>
						</Descriptions.Item>
						<Descriptions.Item span={3} label="Title">
							<h2>{content.title}</h2>
						</Descriptions.Item>
						<Descriptions.Item span={3} label="Description">
							<p>{content.description}</p>
						</Descriptions.Item>
						{content.imageUrl && <Descriptions.Item span={3}>
							<Image style={{width: '100%'}} src={content.imageUrl}/>
						</Descriptions.Item>}
						{content.tags && content.tags.length > 0 && <Descriptions.Item span={3} label={"Tags"}>
							{content.tags.map(tag => {
								return <Popover content={tag.description}><Tag color={tag.color}>{tag.name}</Tag></Popover>
							})}
						</Descriptions.Item>}
						<Descriptions.Item span={0} label="Created At">
							<p>{moment.utc(content.createdAt).local().format('YYYY-MM-DD HH:mm')}</p>
						</Descriptions.Item>
						<Descriptions.Item span={0} label="Last Updated At">
							<p>{moment.utc(content.lastUpdatedAt).local().format('YYYY-MM-DD HH:mm')}</p>
						</Descriptions.Item>
					</Descriptions>
					<Row>
						<Anchor>
							<Anchor.Link target="_blank" href={content.url} title="Link"/>
						</Anchor>
					</Row>
				</Card>)
			}) : <Empty/>)}
		</Layout>
	</div>
}

export default ContentModule;