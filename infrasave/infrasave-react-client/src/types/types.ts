export interface Content {
	id: number;
	createdAt: Date;
	lastUpdatedAt: Date;
	creatorId: number;
	description?: string;
	imageUrl?: string;
	url: string;
	title: string;
	visibilityLevel: VisibilityLevel;
}

export enum VisibilityLevel {
	Everyone = "EVERYONE", Private = "PRIVATE", "Only Friends" = "FRIENDS"
}