export interface Response {
	data: [];
	errorCode: string;
	errorDetail: string;
	errorTitle: string;
	successful: boolean;
	violations: [violation?];
}

export interface violation {
	cause?: string;
	errorDetail?: string;
	errorTitle?: string;
	field?:  string;
}