import moment from "moment";

export const formatDate = (date: Date, format = 'YYYY-MM-DD') => (date ? moment(date).format(format) : '');