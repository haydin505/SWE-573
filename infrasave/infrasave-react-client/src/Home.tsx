import React, {useEffect, useState} from "react";
import "./Home.css";
import {Response} from "./types/response";
import {Content} from "./types/types";
import {useSelector} from "react-redux";
import {RootState} from "./redux/store";
import axiosInstance from "./config/customAxios";
import ContentModule from "./components/content/ContentModule";

const Home: React.FC = () => {
	const [loading, setLoading] = useState(false);
	const [contents, setContents] = useState<Content[]>([]);
	const user = useSelector((state: RootState) => state.authentication.user);
	const authenticated = useSelector((state: RootState) => state.authentication.authenticated);

	const getMyFeed = () => {
		setLoading(true);
		axiosInstance.get("/contents/my-feed", {withCredentials: true}).then(res => {
			const response: Response = res.data;
			setContents(response.data);
		}).finally(() => setLoading(false));
	}

	return <ContentModule contents={contents} user={user} reloadContent={getMyFeed} enableAddContent={true}
	                      loading={loading}/>
}

export default Home;