import axios from 'axios';

const API_URL = '/api/rooms';

export function fetchRooms() {
    return axios.get(`${API_URL}/`);
}

export function createRoom(room) {
    return axios.post(`${API_URL}/`, room);
}

export function removeRoom(roomId) {
    return axios.delete(`${API_URL}/${roomId}`);
}
