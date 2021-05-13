import axios from 'axios';

const API_URL = '/api/games';

/**
 * Fetch games
 * @returns {Promise} Games
 */
export function getGames() {
    return axios.get(`${API_URL}`);
}
/**
 * Delete a game
 * @param {number} gameId
 * @returns {Promise} response
 */
export function deleteGame(topicId) {
    return axios.delete(`${API_URL}/${topicId}/`);
}

export function createGame(roomId) {
    return axios.post(`${API_URL}`, { room_id: roomId });
}

export function getGame(gameId) {
    return axios.get(`${API_URL}/${gameId}/`);
}

export function confirmPoints(gameId) {
    return axios.post(`${API_URL}/${gameId}/points`);
}

export function rejectPoints(gameId) {
    return axios.put(`${API_URL}/${gameId}/points`);
}

export function getAllTeams(gameId) {
    return axios.get(`${API_URL}/${gameId}/teams`);
}
