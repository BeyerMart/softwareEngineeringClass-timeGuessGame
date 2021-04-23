import axios from 'axios';

const API_URL = '/api/users';

export function updateUser(userData) {
    const userId = userData.id;
    return axios.patch(`${API_URL}/${userId}`, {
        username: userData.username,
        email: userData.email,
    });
}
