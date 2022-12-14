export interface LoginRequest {
	email: string;
	password: string;
}

export interface RegisterRequest {
	name:String;
	surname: String;
	username: String;
	email: String;
	password: String;
	phoneNumber: String;
	birthDate: Date;
}