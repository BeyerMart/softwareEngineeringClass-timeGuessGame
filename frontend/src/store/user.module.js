export const user = {
    namespaced: true,
    state: {
        user: null,
    },
    getters: {
        /*
         * Returns stored user
         * @param {object} state
         * @returns {object} user
         */
        getUser(state) {
            return state.user;
        },
        /**
         * Returns if user is logged in
         * @param {object} state
         * @returns {boolean} isLoggedIn
         */
        isLoggedIn(state) {
            return state.user !== null;
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
