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
        async fetchRoom({ commit }, roomId) {
            const response = await RoomService.fetchRoomById(roomId);
            commit('updateRoom', response.data);
        },
        // eslint-disable-next-line no-unused-vars
        async createRoom({ commit }, room) {
            return RoomService.createRoom(room);
        },
    },
    mutations: {
        setRooms: (state, fetchedRooms) => {
            state.rooms = Object.values(fetchedRooms);
        },
        addRoom: (state, room) => {
            state.rooms.unshift(room);
        },
        removeRoom: (state, id) => {
            state.rooms.filter((room) => room.room_id !== id);
            state.rooms.splice((room) => room.id, 1);
        },
        updateRoom: (state, room) => {
            if (state.rooms.some((oldRoom) => (oldRoom.room_id === room.roomn_id ? room : oldRoom))) state.rooms.map((oldRoom) => (oldRoom.room_id === room.roomn_id ? room : oldRoom));
            else state.rooms.unshift(room);
        },
    },

    getters: {
        roomsList: (state) => state.rooms,
        roomById: (state) => (id) => state.rooms.find((roomElement) => roomElement.room_id === id),
    },
};
