import * as TopicsService from '@/services/topic.service';

export const topicModule = {
    state: {
        topics: [],
    },
    actions: {
        async fetchTopics({ commit }) {
            const response = await TopicsService.fetchTopics();
            commit('setTopics', response.data);
        },
        // async createGame({ commit }, game) {
        //     const response = await GameService.createGame(game);
        //     commit('CREATE_NEW_TOPIC', response.data);
        // },
        // async updateTopic({ commit }, gameId) {
        //   const response = await GameService.removeGame(gameId);
        //   commit('REMOVE_TOPIC', response.data);
        // },
        // async removeGame({ commit }, gameId) {
        //     const response = await GameService.removeGame(gameId);
        //     commit('REMOVE_TOPIC', response.data);
        // },
    },
    mutations: {
        setTopics: (state, fetchedTopics) => {
            state.topics = fetchedTopics;
        },
        // CREATE_NEW_TOPIC: (state, topic) => {
        //     state.topics.unshift(topic);
        // },
        // REMOVE_TOPIC: (state, id) => {
        //     state.topic.filter((topic) => topic.id !== id);
        //     state.topic.splice((topic) => topic.id, 1);
        // },
    },
    getters: {
        topicList: (state) => state.topics,
    },
};
