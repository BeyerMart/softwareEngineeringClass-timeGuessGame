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
        role: userData.role,
    });
}
/**
 * Fetch user match history
 * @param {number} userId
 * @returns {Promise} Match history
 */
export function getUserMatchHistory(userId) {
    return axios.get(`${API_URL}/${userId}/statistics/history`);
}

/**
 * Get user win/loss ratio
 * @param {number} userId
 * @returns {Promise} User win/loss ratio
 */
export function getUserWinRatio(userId) {
    return axios.get(`${API_URL}/${userId}/statistics/winratio`);
}

/**
 * Get user total played games
 * @param {number} userId
 * @returns {Promise} Total games played by user
 */
export function getUserTotalGames(userId) {
    return axios.get(`${API_URL}/${userId}/statistics/totalgames`);
}

/**
 * Get last played with users
 * @param {number} userId
 * @returns {Promise} Last played with users
 */
export function getLastPlayedWithUsers(userId) {
    return axios.get(`${API_URL}/${userId}/statistics/lastplayedwith`);
}

/**
 * Delete user
 * @param {number} userId
 * @returns {Promise} response
 */
export function deleteUser(userId) {
    return axios.delete(`${API_URL}/${userId}/`);
}
