import * as UserService from '@/services/user.service';

export const user = {
    namespaced: true,
    state: {
        user: null,
        users: [],
    },
    getters: {
        /*
         * Returns stored user
         * @returns {object} user
         */
        getUser(state) {
            return state.user;
        },
        getUsers(state) {
            return state.users;
        },
        /**
         * Returns if user is logged in
         * @returns {boolean} isLoggedIn
         */
        isLoggedIn(state) {
            return state.user !== null;
        },
        /**
         * Returns true if user is an admin
         * @returns {boolean} isAdmin
         */
        isAdmin(state) {
            return state.user.role === 'ROLE_ADMIN';
        },
        /**
         * Returns true if user is a manager
         * @returns {boolean} isManager
         */
        isManager(state) {
            return state.user.role === 'ROLE_MANAGER';
        },
        /**
         * Returns true if user is a user
         * @returns {boolean} isUser
         */
        isUser(state) {
            return state.user.role === 'ROLE_USER';
        },
    },
    actions: {
        /**
         * Sets user information
         * @param {object} userData
         */
        setUser({ commit }, userData) {
            commit('setUser', userData);
            commit('updateUser', userData);
        },
        /**
         * Forgets user information
         */
        forgetUser({ commit }) {
            commit('setUser', null);
        },
        async fetchUser({ commit }, userId) {
            const response = await UserService.getUserById(userId);
            commit('updateUser', response.data);
        },
    },
    mutations: {
        /**
         * Sets user information
         * @param {object} userData
         */
        setUser(state, userData) {
            state.user = userData;
        },
        updateUser(state, userData) {
            if (state.users.some((storageUser) => userData.id === storageUser.id)) state.users.map((storageUser) => (userData.id === storageUser.id ? userData : storageUser));
            else state.users.unshift(userData);
        },
    },
};
