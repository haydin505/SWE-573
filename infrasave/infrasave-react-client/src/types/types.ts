export interface Content {
	id: number;
	createdAt: Date;
	lastUpdatedAt: Date;
	visibilityLevel: VisibilityLevel;
	title: string;
	url: string;
	imageUrl?: string;
	description?: string;
	myContent: boolean;
	creatorUser: UserDTO;
	tags: TagDTO[];
}

export interface TagDTO {
	id: number;
	name: string;
	description: string;
	color: string;
	createdAt: Date;
	lastUpdatedAt: Date;
}

export enum VisibilityLevel {
	Everyone = "EVERYONE", Private = "PRIVATE", "Only Friends" = "FRIENDS"
}

export interface Search {
	users?: UserDTO[];
	contents?: Content[];
	contentsByTags?: Content[],
}

export interface UserDTO {
	username: string;
	userId: number;
	name: string;
	surname: string;
	email: string;
	birthDate: Date;
	createdContents: Content[];
	friendCount: number;
	friendRequestStatus: string;
}

export enum FriendRequestStatus {
	PENDING,
	APPROVED,
	REJECTED,
	NONE
}

