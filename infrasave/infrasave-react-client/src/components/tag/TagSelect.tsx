import * as React from "react";
import {FC, useEffect, useState} from "react";
import {Button, Select, Tag} from "antd";
import {TagDTO} from "../../types/types";
import customAxios from "../../config/customAxios";
import {Response} from "../../types/response";
import CreateTagModal from "./CreateTagModal";

interface TagSelectProps {

}

interface OnChangeHandler {
	(e: any): void;
}

interface TagSelectProps {
	value: number;
	onChange: OnChangeHandler;
	defVal?: TagDTO[];
}

const TagSelect: FC<TagSelectProps> = (props: TagSelectProps): JSX.Element => {
	const [tags, setTags] = useState<TagDTO[] | undefined>(undefined);
	const [selectedTags, setSelectedTags] = useState<TagDTO[] | undefined>(props.defVal);
	const [showCreateTagModal, setShowCreateTagModal] = useState<boolean>(false);

	useEffect(() => {
		fetchTags();
	}, [])

	const onClickCreateTag = () => setShowCreateTagModal(!showCreateTagModal);

	const tagRender = (currentTag: any): React.ReactElement => {
		const onPreventMouseDown = (event: React.MouseEvent<HTMLSpanElement>) => {
			event.preventDefault();
			event.stopPropagation();
		};
		const targetTag: TagDTO | undefined = tags?.filter(tag => tag.id === currentTag.value)[0];
		if (!targetTag) {
			return <></>;
		}
		return (
			<Tag
				color={targetTag.color}
				onMouseDown={onPreventMouseDown}
				closable={true}
				onClose={(data: React.MouseEvent<HTMLElement>) => internalOnClose(data, targetTag.id)}
				style={{marginRight: 3}}
			>
				{targetTag.name}
			</Tag>
		)
	}

	console.log(selectedTags);

	const internalOnClose = (data: React.MouseEvent<HTMLElement>, targetTagId: number) => {
		console.log(data);
		console.log(data.currentTarget);
		const filteredTags = selectedTags?.filter(tag => targetTagId !== tag.id);
		setSelectedTags(filteredTags);
		props.onChange(filteredTags?.map(tag => tag.id));
	}

	const internalOnChange = (data: number[]) => {
		const filteredTags = tags?.filter(tag => {
			return data.includes(tag.id);
		});
		setSelectedTags(filteredTags);
		props.onChange(data);
	}

	const fetchTags = () => {
		customAxios.get("/tags", {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert("Alert");
				return;
			}
			const data: TagDTO[] = response.data;
			setTags(data);
		})
	}

	return (<><Select
		mode="multiple"
		showArrow
		tagRender={tagRender}
		style={{width: '100%'}}
		options={tags?.map((tag: TagDTO) => {
			return {label: tag.name, value: tag.id}
		})}
		onChange={internalOnChange}
		value={selectedTags?.map((tag: TagDTO) => {
			return tag.id
		})}
		// defaultValue={tags?.map((tag: TagDTO) => {
		// 	return {label: tag.name, value: tag.id}
		// })}
	/>
		<Button onClick={onClickCreateTag} type="dashed" size="small">Create Tag</Button>
		<CreateTagModal showCreateTagModal={showCreateTagModal} setShowCreateTagModal={onClickCreateTag}
		                fetchTags={fetchTags}/></>)
}

export default TagSelect;