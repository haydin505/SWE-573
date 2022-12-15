import {createSlice} from '@reduxjs/toolkit'

export interface User{
	userId:  number;
	name: String;
	surname:String;
	email: String;
	roles: String[];
}

export interface AuthenticationState {
	authenticated: boolean;
	user?: User;
}

const initialState: AuthenticationState = {
	authenticated: false,
	user: undefined
}

export const authenticationSlice = createSlice({
	name: 'authentication',
	initialState,
	reducers: {
		authenticate: (state) => {
			state.authenticated = false;
			state.user = undefined;
		},
		authenticateSuccess: (state) => {
			state.authenticated = true;
		},
		authenticateFail: (state) => {
			console.log("here")
			state.authenticated = false;
			state.user = undefined;
		},
		logout: (state) => {
			state.authenticated = false;
			state.user = undefined;
		},
		loadUser: (state, payload) => {
			state.user = payload.payload;
		}
	},
})

export const {authenticate, authenticateSuccess, authenticateFail, logout, loadUser} = authenticationSlice.actions

export default authenticationSlice.reducer