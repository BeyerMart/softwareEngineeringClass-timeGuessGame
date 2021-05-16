import axios from 'axios';

const API_URL = '/api/cubes';

export function getCubes() {
    return axios.get(`${API_URL}/`);
}
