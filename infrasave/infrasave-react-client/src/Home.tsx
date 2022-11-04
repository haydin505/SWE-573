import React from "react";
import {Card, Layout} from "antd";
import "./Home.css";

const Home: React.FC = () => {
	return <div className="site-card-border-less-wrapper">
		<h1>Home</h1>
		<Layout style={{
			padding: '50px',
			background: '#ececec'
		}}>
			<Card title={"1"} className="content">
				<p>1</p>
				<p>1</p>
				<p>1</p>
				<p>1</p>
			</Card>
			<Card title={"2"} className="content">
				<p>2</p>
				<p>2</p>
				<p>2</p>
				<p>2</p>
			</Card>
			<Card title={"3"} className="content">
				<p>3</p>
				<p>3</p>
				<p>3</p>
				<p>3</p>
			</Card>
		</Layout>
	</div>
}

export default Home;