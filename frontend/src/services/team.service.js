import axios from 'axios';

const API_URL = '/api/teams';

/**
 * Fetch teams
 * @returns {Promise} Teams
 */
export function getTeams() {
    return axios.get(`${API_URL}`);
}
