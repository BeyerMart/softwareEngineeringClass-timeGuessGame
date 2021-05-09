import axios from 'axios';

const API_URL = '/api/cubes';

export function fetchCubes() {
    return axios.get(`${API_URL}/`);
}