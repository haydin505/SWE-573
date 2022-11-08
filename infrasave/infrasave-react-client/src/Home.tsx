import React from "react";
import {Anchor, Card, Image, Layout} from "antd";
import "./Home.css";

const Home: React.FC = () => {
	const { Link } = Anchor;
	return <div className="site-card-border-less-wrapper">
		<h1>Home</h1>
		<Layout style={{
			padding: '50px',
			background: '#ececec'
		}}>
			<Card title={<Image title={"hey"} width={50} src="https://img.icons8.com/color/512/twitter.png"/>} className="content">
				<h2>Analysis: Nagging U.S. Treasury liquidity problems raise Fed balance sheet predicament</h2>
				<Anchor>
					<Link target="_blank" href="https://twitter.com/Reuters/status/1590027914367713280" title="Link" />
				</Anchor>
				<Image width={450} src="https://pbs.twimg.com/media/FhDp9jIXEAIllKD?format=jpg&name=medium"/>
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