import {FC, useState} from "react";
import {Button, Form, Input, Modal, Select} from "antd";
import {Content, VisibilityLevel} from "./types";
import TextArea from "antd/es/input/TextArea";
import axios from "axios";
import {Response} from "./response"

interface AddContentModalProps {
	showAddContentModal: boolean;
	setShowAddContentModal: () => void;
}

const AddContentModal: FC<AddContentModalProps> = (props: AddContentModalProps): JSX.Element => {
	const [loading, setLoading] = useState(false);

	const addContentModal = (content: Content) => {
		console.log(content);
		setLoading(true);
		axios.post("http://localhost:8080/contents", content, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
			}
		}).finally(
			() => setLoading(false));
	}

	return <Modal open={props.showAddContentModal} onCancel={props.setShowAddContentModal} footer={null}>
		<Form labelCol={{span: 6}}
		      labelAlign={"left"}
		      autoComplete="off"
		      style={{padding: '5%'}}
		      onFinish={addContentModal}
		      disabled={loading}>
			<Form.Item label={"Title"} name="title" rules={[{required: true, message: "Please enter title."}]}>
				<Input/>
			</Form.Item>
			<Form.Item label={"Url"} name="url" rules={[{required: true, message: "Please enter url."}]}>
				<Input/>
			</Form.Item>
			<Form.Item label={"Description"} name="description">
				<TextArea/>
			</Form.Item>
			<Form.Item label={"Image Url"} name="imageUrl">
				<Input/>
			</Form.Item>
			<Form.Item label={"Privacy Level"} name="visibilityLevel"
			           rules={[{required: true, message: "Please select privacy level."}]}>
				<Select options={
					[
						{
							value: VisibilityLevel.PRIVATE,
							label: 'Private'
						},
						{
							value: VisibilityLevel.FRIENDS,
							label: 'Only Friends'
						},
						{
							value: VisibilityLevel.EVERYONE,
							label: 'Everyone'
						}
					]}/>
			</Form.Item>
			<Form.Item wrapperCol={{offset: 11}}>
				<Button type="primary" htmlType="submit" loading={loading}>
					Submit
				</Button>
			</Form.Item>
		</Form>
	</Modal>
}

export default AddContentModal;