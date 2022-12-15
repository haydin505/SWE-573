import React, {useEffect, useState} from "react";
import {Anchor, Button, Card, Col, Descriptions, Empty, Image, Layout, Popover, Row, Space, Spin, Tag} from "antd";
import "./Home.css";
import {Response} from "./types/response";
import {Content} from "./types/types";
import AddContentModal from "./AddContentModal";
import {useSelector} from "react-redux";
import {RootState} from "./redux/store";
import EditContentModal from "./EditContentModal";
import axiosInstance from "./customAxios";
import {DeleteOutlined, EditOutlined, HeartFilled, HeartOutlined} from "@ant-design/icons";
import {Link} from "react-router-dom";

const Home: React.FC = () => {
	const [loading, setLoading] = useState(false);
	const [contents, setContents] = useState([]);
	const [showAddContentModal, setShowAddContentModal] = useState(false);
	const [showEditContentModal, setShowEditContentModal] = useState(false);
	const [currentContent, setCurrentContent] = useState<Content | null>(null);

	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);
	const user = useSelector((state: RootState) => state.authentication.user);
	const addContent = () => {
		showAddContentModal ? setShowAddContentModal(false) : setShowAddContentModal(true);
		getMyFeed();
	}

	const showEditContent = () => {
		showEditContentModal ? setShowEditContentModal(false) : setShowEditContentModal(true);
		getMyFeed();
	}

	useEffect(() => {
		getMyFeed();
	}, [])

	const getMyFeed = () => {
		setLoading(true);
		axiosInstance.get("/contents/my-feed", {withCredentials: true}).then(res => {
			const response: Response = res.data;
			setContents(response.data);
		}).finally(() => setLoading(false));
	}

	const onClickEditContent = (content: Content) => {
		setCurrentContent(content);
		setShowEditContentModal(true);
	}

	const onClickDeleteContent = (content: Content) => {
		axiosInstance.delete("/contents/" + content.id, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
			}
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		}).finally(() => getMyFeed());
	}

	const onClickAddToMyContent = (content: Content) => {
		console.log(content.id);
		axiosInstance.post("/my-content", {contentId: content.id}, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
			}
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		}).finally(() => getMyFeed());
	}

	const onClickDeleteMyContent = (content: Content) => {
		axiosInstance.delete(`/my-content/${content.id}`, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
			}
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		}).finally(() => getMyFeed());
	}

	return <div className="site-card-border-less-wrapper">
		<Layout style={{
			padding: '50px',
			background: '#ececec'
		}}>
			{authenticated && <Space>
        <Button size='middle' type="primary" htmlType="submit" onClick={addContent}>
          Add Content
        </Button>
      </Space>}
			<AddContentModal showAddContentModal={showAddContentModal} setShowAddContentModal={addContent}/>
			<EditContentModal content={currentContent} showEditContentModal={showEditContentModal}
			                  setShowEditContentModal={showEditContent}/>
			{loading ? (<Spin tip="Loading" size="large">
				<div className="content"/>
			</Spin>) : (contents.length > 0 ? contents.map((content: Content) => {
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
              <Image width={450} src={content.imageUrl}/>
            </Descriptions.Item>}
						{content.tags && content.tags.length > 0 && <Descriptions.Item span={3} label={"Tags"}>
							{content.tags.map(tag => {
								return <Popover content={tag.description}><Tag color={tag.color}>{tag.name}</Tag></Popover>
							})}
            </Descriptions.Item>}
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

export default Home;