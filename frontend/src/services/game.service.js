import axios from 'axios';

const API_URL = '/api/games';

export function fetchGames() {
    return axios.get(`${API_URL}/`);
}

export function createGame(game) {
    return axios.post(`${API_URL}/`, game);
}

export function removeGame(gameId) {
    return axios.delete(`${API_URL}/${gameId}`);
}
