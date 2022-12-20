import {FC, useState} from "react";
import {Button, Form, Input, Modal} from "antd";
import {ColorResult, SketchPicker} from "react-color";
import TextArea from "antd/es/input/TextArea";
import customAxios from "../../config/customAxios";
import {Response} from "../../types/response";

interface CreateTagModalProps {
	showCreateTagModal: boolean;
	setShowCreateTagModal: () => void;
	fetchTags: () => void;
}

interface CreateTagDTO {
	name: string;
	description: string;
	color: ColorResult;
}

const CreateTagModal: FC<CreateTagModalProps> = (props: CreateTagModalProps) => {
	const [loading, setLoading] = useState(false);
	const [color, setColor] = useState<string | undefined>(undefined);

	const createTag = (formTag: CreateTagDTO) => {
		const tag: any = {
			name: formTag.name,
			description: formTag.description,
			color: formTag.color.hex
		}
		customAxios.post("/tags", tag, {withCredentials: true}).then((res) => {
			setLoading(true);
			const response: Response = res.data;
			if (!response.successful) {
				alert("Tag couldn't created");
				return;
			}
			props.setShowCreateTagModal();
			props.fetchTags();
		}).catch(err => {
			alert("Could not create tag.");
		}).finally(() => setLoading(false));
	}

	return <Modal title="Create Tag" open={props.showCreateTagModal} onCancel={props.setShowCreateTagModal} footer={null}>
		<Form labelCol={{span: 6}}
		      labelAlign={"left"}
		      autoComplete="off"
		      style={{padding: '5%'}}
		      onFinish={createTag}
		      disabled={loading}>
			<Form.Item label="Tag Name" name="name">
				<Input/>
			</Form.Item>
			<Form.Item label="Description" name="description">
				<TextArea/>
			</Form.Item>
			<Form.Item label="Pick Color" name="color">
				<SketchPicker color={color} onChangeComplete={(color) => setColor(color.hex)}/>
			</Form.Item>
			<Form.Item wrapperCol={{offset: 11}}>
				<Button type="primary" htmlType="submit" loading={loading}>
					Submit
				</Button>
			</Form.Item>
		</Form>
	</Modal>

}

export default CreateTagModal;