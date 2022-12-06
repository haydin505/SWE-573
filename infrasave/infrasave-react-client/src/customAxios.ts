import axios from "axios";

// eslint-disable-next-line no-restricted-globals
axios.defaults.baseURL = location.protocol + '//' + location.hostname + ':' + "8080";
const axiosInstance = axios.create();

export default axiosInstance;