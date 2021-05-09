import axios from 'axios';

const API_URL = '/api/rooms';

export function fetchRooms() {
    return axios.get(`${API_URL}/`);
}

export function createRoom() {
    return axios.post(`${API_URL}/`);
}

export function removeRoom(roomId) {
    return axios.delete(`${API_URL}/${roomId}`);
}

export function updateRoom(roomData) {
    return axios.patch(`${API_URL}/${roomData}`)
}

export function fetchRoomById(roomId) {
    return axios.get(`${API_URL}/${roomId}`)
}

export function joinRoom(roomId, virtualUser) {
    return axios.post(`${API_URL}/${roomId}/users`, virtualUser)
}

export function leaveRoom(roomId, virtualUser, userId) {
    if (userId || userId === 0)
        return axios.delete(`${API_URL}/${roomId}/users/${roomId}`, virtualUser)
    return axios.delete(`${API_URL}/${roomId}/users`, virtualUser)
}

export function joinTeam(roomId, virtualTeam) {
    return axios.post(`${API_URL}/${roomId}/teams`, virtualTeam)
}

export function leaveTeam(roomId, virtualTeam) {
    return axios.delete(`${API_URL}/${roomId}/teams`, virtualTeam)
}