import React, {FC, useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {RootState} from "../../redux/store";
import axiosInstance from "../../config/customAxios";
import {Response} from "../../types/response";
import ContentModule from "../content/Content";
import {Content} from "../../types/types";

interface MyContentProps {

}

const MyContent: FC<MyContentProps> = (props: MyContentProps): JSX.Element => {
	const user = useSelector((state: RootState) => state.authentication.user);
	const [myContents, setMyContents] = useState<Content[]>([]);

	useEffect(() => {
		getMyContents();
	}, [])

	const getMyContents = () => {
		axiosInstance.get("/my-content", {withCredentials: true}).then((res) => {
			const response: Response = res.data;
			if (!response.successful) {
				alert("Couldn't fetch my contents.");
				return;
			}
			const myContentDTOs: Content[] = response.data;
			setMyContents(myContentDTOs);
		})
	}

	return <ContentModule contents={myContents} enableAddContent={false} reloadContent={getMyContents}/>

}

export default MyContent;