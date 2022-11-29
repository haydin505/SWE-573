import React, {useEffect, useState} from "react";
import {Anchor, Button, Card, Image, Layout, Space} from "antd";
import "./Home.css";
import axios from "axios";
import {Response} from "./response";
import {Content} from "./types";
import AddContentModal from "./AddContentModal";
import {useSelector} from "react-redux";
import {RootState} from "./redux/store";

const Home: React.FC = () => {
	const [contents, setContents] = useState([]);
	const [showAddContentModal, setShowAddContentModal] = useState(false);
	const {Link} = Anchor;

	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);

	const addContent = () => {
		showAddContentModal ? setShowAddContentModal(false) : setShowAddContentModal(true);
	}

	useEffect(() => {
		axios.get("http://localhost:8080/contents/my-feed", {withCredentials: true}).then(res => {
			const response: Response = res.data;
			setContents(response.data);
		})
	}, [])

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
			{contents.length > 0 ? contents.map((content: Content) => {
				return (<Card className="content">
					<h2>{content.title}</h2>
					<Anchor>
						<Link target="_blank" href={content.url} title="Link"/>
					</Anchor>
					<Image width={450} src={content.imageUrl}/>
				</Card>)
			}) : <h2>No content 😢</h2>}
		</Layout>
	</div>
}

export default Home;