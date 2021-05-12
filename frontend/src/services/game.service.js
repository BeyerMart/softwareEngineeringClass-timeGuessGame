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
