import axios from "axios";

console.log(process.env.REACT_APP_BASE_URL);
axios.defaults.baseURL = process.env.REACT_APP_BASE_URL;
axios.defaults.withCredentials = true;

const axiosInstance = axios.create();

export default axiosInstance;