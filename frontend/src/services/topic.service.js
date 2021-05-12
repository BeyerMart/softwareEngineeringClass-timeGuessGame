import axios from 'axios';

const API_URL = '/api/topics';

export function fetchTopics() {
    return axios.get(`${API_URL}/`);
}
export function fetchTopic(id) {
    return axios.get(`${API_URL}/${id}`);
}
