import axios from 'axios';

const API_URL = '/api/auth';

export function login(loginData) {
    return axios.post(`${API_URL}/signin`, {
        username: loginData.username,
        password: loginData.password,
    }).then((response) => {
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
            axios.defaults.headers.common.Authorization = `Bearer ${response.data.token}`;
        }
        return response;
    });
}
export function getCurrentUser() {
    return axios.get(`${API_URL}/me`);
}

export function logout() {
    localStorage.removeItem('token');
}
