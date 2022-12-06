import {FC, useState} from "react";
import {Button, Form, Input, Modal, Select} from "antd";
import {Content, VisibilityLevel} from "./types/types";
import TextArea from "antd/es/input/TextArea";
import {Response} from "./types/response"
import {enum2Options} from 'antd-utils'
import axiosInstance from "./customAxios";

interface EditContentModalProps {
	showEditContentModal: boolean;
	setShowEditContentModal: () => void;
	content: Content | null;
}

const options = enum2Options(VisibilityLevel)

const EditContentModal: FC<EditContentModalProps> = (props: EditContentModalProps): JSX.Element => {
	const [loading, setLoading] = useState(false);

	const editContent = (content: Content) => {
		console.log(content)
		setLoading(true);
		axiosInstance.put("/contents", content, {withCredentials: true}).then(res => {
			const response: Response = res.data;
			if (!response.successful) {
				alert(response.errorDetail);
			} else {
				props.setShowEditContentModal();
			}
		}).catch(res => {
			console.log(res);
			const data: Response = res.response.data;
			alert(
				data.violations.map(violation => violation && violation.field ? violation.field + " " + violation.cause : ""));
		}).finally(
			() => setLoading(false));
	}

	return <Modal open={props.showEditContentModal} onCancel={props.setShowEditContentModal} footer={null}>
		<Form labelCol={{span: 6}}
		      labelAlign={"left"}
		      autoComplete="off"
		      style={{padding: '5%'}}
		      onFinish={editContent}
		      disabled={loading}>
			<Form.Item hidden={true} name="id" initialValue={props.content?.id}/>
			<Form.Item label={"Title"} name="title" initialValue={props.content?.title}
			           rules={[{required: true, message: "Please enter title."}]}>
				<Input/>
			</Form.Item>
			<Form.Item label={"Url"} name="url" initialValue={props.content?.url}
			           rules={[{required: true, message: "Please enter url."}]}>
				<Input/>
			</Form.Item>
			<Form.Item label={"Description"} name="description" initialValue={props.content?.description}>
				<TextArea/>
			</Form.Item>
			<Form.Item label={"Image Url"} name="imageUrl" initialValue={props.content?.imageUrl}>
				<Input/>
			</Form.Item>
			<Form.Item label={"Privacy Level"} name="visibilityLevel"
			           rules={[{required: true, message: "Please select privacy level."}]}
			           valuePropName="option"
			initialValue={props.content?.visibilityLevel}>
				<Select allowClear showSearch defaultValue={props.content?.visibilityLevel} options={options}/>
			</Form.Item>
			<Form.Item wrapperCol={{offset: 11}}>
				<Button type="primary" htmlType="submit" loading={loading}>
					Submit
				</Button>
			</Form.Item>
		</Form>
	</Modal>
}

export default EditContentModal;