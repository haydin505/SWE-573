import {FC, useState} from "react";
import {Button, Form, Input, Modal, Select} from "antd";
import {Content, VisibilityLevel} from "../../types/types";
import TextArea from "antd/es/input/TextArea";
import {Response} from "../../types/response"
import {enum2Options} from "antd-utils";
import axiosInstance from "../../config/customAxios";
import TagSelect from "../tag/TagSelect";

interface AddContentModalProps {
	showAddContentModal: boolean;
	setShowAddContentModal: () => void;
	onAddContentComplete:() => void;
}

const options = enum2Options(VisibilityLevel)

const AddContentModal: FC<AddContentModalProps> = (props: AddContentModalProps): JSX.Element => {
	const [loading, setLoading] = useState(false);

	const addContentModal = (content: Content) => {
		console.log(content);
		setLoading(true);
		axiosInstance.post("/contents", content, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
			}
			props.setShowAddContentModal()
			props.onAddContentComplete();
		}).catch(err => {
			alert("Could not create content.")
		}).finally(() => setLoading(false));
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
				<Select options={options}/>
			</Form.Item>
			<Form.Item label={"Tags"} name="tags">
				{/* @ts-ignore */}
				<TagSelect/>
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