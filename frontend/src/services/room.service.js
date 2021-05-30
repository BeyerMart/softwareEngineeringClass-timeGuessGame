import axios from 'axios';

const API_URL = '/api/rooms';

export function fetchRooms() {
    return axios.get(`${API_URL}/`);
}

export function createRoom(form) {
    if (form) {
        const room = {};
        if (form.roomName) room.name = form.roomName;
        if (form.topic) room.topic_id = form.topic;
        if (form.maxPoints) room.max_points = form.maxPoints;
        return axios.post(`${API_URL}/`, room);
    }
    return axios.post(`${API_URL}/`);
}

export function removeRoom(roomId) {
    return axios.delete(`${API_URL}/${roomId}`);
}

export function updateRoom(roomData) {
    return axios.patch(`${API_URL}/${roomData}`);
}

export function fetchRoomById(roomId) {
    return axios.get(`${API_URL}/${roomId}`);
}

export function joinRoom(roomId, virtualUser) {
    return axios.post(`${API_URL}/${roomId}/users`, virtualUser);
}

export function leaveRoom(roomId, virtualUser, userId) {
    if (userId || userId === 0) return axios.delete(`${API_URL}/${roomId}/users/${userId}`, virtualUser);
    return axios.delete(`${API_URL}/${roomId}/users`, virtualUser);
}

export function joinTeam(roomId, virtualTeam) {
    return axios.post(`${API_URL}/${roomId}/teams`, virtualTeam);
}

export function leaveTeam(roomId, virtualTeam) {
    return axios.delete(`${API_URL}/${roomId}/teams`, virtualTeam);
}

export function connectPi(piName, roomId) {
    return axios.post(`${API_URL}/${roomId}/connect_pi`, { piName });
}

export function disconnectPi(piName, roomId) {
    return axios.post(`${API_URL}/${roomId}/disconnect_pi`, piName);
}

export function getPlayers(roomId) {
    return axios.get(`${API_URL}/${roomId}/users`);
}

export function getTeams(roomId) {
    return axios.get(`${API_URL}/${roomId}/teams`);
}
