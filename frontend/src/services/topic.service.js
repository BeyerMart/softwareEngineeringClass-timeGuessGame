import axios from 'axios';

const API_URL = '/api/topics';

export function fetchTopics() {
    return axios.get(`${API_URL}/`);
}
