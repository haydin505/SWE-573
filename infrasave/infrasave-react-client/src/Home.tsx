import React, {useEffect, useState} from "react";
import {Anchor, Button, Card, Col, Descriptions, Empty, Image, Layout, Row, Space, Spin} from "antd";
import "./Home.css";
import {Response} from "./types/response";
import {Content} from "./types/types";
import AddContentModal from "./AddContentModal";
import {useSelector} from "react-redux";
import {RootState} from "./redux/store";
import {DeleteOutlined, EditOutlined} from "@ant-design/icons";
import EditContentModal from "./EditContentModal";
import axiosInstance from "./customAxios";

const Home: React.FC = () => {
	const [loading, setLoading] = useState(false);
	const [contents, setContents] = useState([]);
	const [showAddContentModal, setShowAddContentModal] = useState(false);
	const [showEditContentModal, setShowEditContentModal] = useState(false);
	const [currentContent, setCurrentContent] = useState<Content | null>(null);
	const {Link} = Anchor;

	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);

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

	return <div className="site-card-border-less-wrapper">
		<h1>Home</h1>
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
				return (<Card className="content" style={{maxWidth: '900px'}}>
					<Row style={{marginBottom: 5}}>
						<Col span={2} offset={20}><EditOutlined onClick={() => onClickEditContent(content)}/></Col>
						<Col span={2} offset={0}><DeleteOutlined onClick={() => onClickDeleteContent(content)}/></Col>
					</Row>
					<Descriptions bordered>
						<Descriptions.Item span={3} label="Title">
							<h2>{content.title}</h2>
						</Descriptions.Item>
						<Descriptions.Item span={3} label="Description">
							<p>{content.description}</p>
						</Descriptions.Item>
						<Descriptions.Item span={3}>
							<Image width={450} src={content.imageUrl}/>
						</Descriptions.Item>
					</Descriptions>
					<Row>
						<Anchor>
							<Link target="_blank" href={content.url} title="Link"/>
						</Anchor>
					</Row>
				</Card>)
			}) : <Empty/>)}
		</Layout>
	</div>
}

export default Home;