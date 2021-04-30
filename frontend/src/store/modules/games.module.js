import * as GameService from '@/services/game.service';
import axios from 'axios';

export const GamesModule = {
    state: {
        games: [],
    },
    actions: {
        async fetchGames({ commit }) {
            // const response = await GameService.fetchGames();
            const response = await axios.get('http://localhost:3000/games');
            commit('SET_GAMES', response.data);
        },
        async createGame({ commit }, game) {
            const response = await GameService.createGame(game);
            commit('CREATE_NEW_GAME', response.data);
            return response;
        },
        async removeGame({ commit }, gameId) {
            const response = await GameService.removeGame(gameId);
            commit('REMOVE_GAME', response.data);
        },
    },
    mutations: {
        SET_GAMES: (state, fetchedGames) => {
            state.games = fetchedGames;
        },
        CREATE_NEW_GAME: (state, game) => {
            state.users.unshift(game);
        },
        REMOVE_GAME: (state, id) => {
            state.games.filter((game) => game.id !== id);
            state.games.splice((game) => game.id, 1);
        },
    },
    getters: {
        gamesList: (state) => state.games,
        gamesCount: (state, getters) => getters.gamesList.length,
    },
};
