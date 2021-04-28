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
            console.log('HERRRO', response.data);
            commit('setGames', response.data);
        },
        async createGame({ commit }, game) {
            const response = await GameService.createGame(game);
            commit('createNewGame', response.data);
        },
        async removeGame({ commit }, gameId) {
            const response = await GameService.removeGame(gameId);
            commit('removeGame', response.data);
        },
    },
    mutations: {
        setGames: (state, fetchedGames) => {
            state.games = fetchedGames;
            console.log('HERRO', fetchedGames, state.games);
        },
        createNewGame: (state, game) => {
            state.users.unshift(game);
        },
        removeGame: (state, id) => {
            state.games.filter((game) => game.id !== id);
            state.games.splice((game) => game.id, 1);
        },
    },
    getters: {
        gamesList: (state) => state.games,
        gamesCount: (state, getters) => getters.gamesList.length,
    },
};
