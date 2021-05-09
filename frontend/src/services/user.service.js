import axios from 'axios';

const API_URL = '/api/users';

/**
 * Fetch users
 * @returns {Promise} Users
 */
export function getUsers() {
    return axios.get(`${API_URL}`);
}

export function getUserById(userId) {
    return axios.get(`${API_URL}/${userId}`);
}

export function updateUser(userData) {
    const userId = userData.id;
    return axios.patch(`${API_URL}/${userId}`, {
        username: userData.username,
        email: userData.email,
    });
}
