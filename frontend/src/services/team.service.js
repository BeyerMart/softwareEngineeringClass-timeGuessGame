import axios from 'axios';

const API_URL = '/api/teams';

/**
 * Fetch teams
 * @returns {Promise} Teams
 */
export function getTeams() {
    return axios.get(`${API_URL}`);
}

export function createTeam(newTeam) {
    return axios.post(`${API_URL}`, newTeam);
}

export function updateTeam(updatedTeam) {
    return axios.patch(`${API_URL}`, updatedTeam);
}

export function getTeam(teamId) {
    return axios.get(`${API_URL}/${teamId}`);
}

export function getTeamUsers(teamId) {
    return axios.get(`${API_URL}/${teamId}/users`);
}

export function joinTeam(teamId, virtualUser) {
    if (virtualUser) return axios.post(`${API_URL}/${teamId}/users`, virtualUser);
    return axios.post(`${API_URL}/${teamId}/users`);
}

export function leaveRoom(teamId, virtualUser) {
    if (virtualUser) return axios.delete(`${API_URL}/${teamId}/users/${virtualUser.virtual_id}`);
    return axios.delete(`${API_URL}/${teamId}/users`);
}

export function deleteTeam(teamId) {
    return axios.delete(`${API_URL}/${teamId}`);
}
