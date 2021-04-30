export const user = {
    namespaced: true,
    state: {
        user: null,
    },
    getters: {
        /*
         * Returns stored user
         * @returns {object} user
         */
        getUser(state) {
            return state.user;
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
        },
        /**
         * Forgets user information
         */
        forgetUser({ commit }) {
            commit('setUser', null);
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
    },
};
