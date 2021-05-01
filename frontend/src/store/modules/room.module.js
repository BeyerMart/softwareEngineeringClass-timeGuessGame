import * as RoomService from '@/services/room.service';

export const roomModule = {
    state: {
        rooms: [],
    },
    actions: {
        async fetchRooms({ commit }) {
            const response = await RoomService.fetchRooms();
            commit('setRooms', response.data);
        },
        async createRoom({ commit }, room) {
            const response = await RoomService.createRoom(room);
            commit('createNewRoom', response.data);
            return response;
        },
        async removeRoom({ commit }, roomId) {
            const response = await RoomService.removeRoom(roomId);
            commit('removeRoom', response.data);
        },
    },
    mutations: {
        setRooms: (state, fetchedRooms) => {
            state.rooms = fetchedRooms;
        },
        createRoom: (state, room) => {
            state.users.unshift(room);
        },
        removeRoom: (state, id) => {
            state.rooms.filter((room) => room.id !== id);
            state.rooms.splice((room) => room.id, 1);
        },
    },

    getters: {
        roomsList: (state) => state.rooms,
    },
};
