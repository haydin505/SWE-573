import moment from "moment";

export const formatDate = (date: Date | undefined, format = 'YYYY-MM-DD') => (date ? moment(date).format(format) : '');