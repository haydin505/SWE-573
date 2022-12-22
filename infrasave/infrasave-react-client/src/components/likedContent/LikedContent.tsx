import React, {FC, useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import axiosInstance from "../../config/customAxios";
import {Response} from "../../types/response";
import ContentModule from "../content/ContentModule";
import {Content} from "../../types/types";

interface LikedContent {

}

const LikedContent: FC<LikedContent> = (props: LikedContent): JSX.Element => {
	const user = useSelector((state: RootState) => state.authentication.user);
	const [myContents, setMyContents] = useState<Content[]>([]);
	const [loading, setLoading] = useState(false);

	useEffect(() => {
		getMyContents();
	}, [])

	const getMyContents = () => {
		setLoading(true);
		axiosInstance.get("/my-content", {withCredentials: true}).then((res) => {
			const response: Response = res.data;
			if (!response.successful) {
				alert("Couldn't fetch my contents.");
				return;
			}
			const myContentDTOs: Content[] = response.data;
			setMyContents(myContentDTOs);
		}).catch(err => {
			alert("Couldn't fetch my contents.");
		}).finally(() => setLoading(false))
	}

	return <ContentModule contents={myContents} enableAddContent={false} reloadContent={getMyContents} loading={loading}/>

}

export default LikedContent;