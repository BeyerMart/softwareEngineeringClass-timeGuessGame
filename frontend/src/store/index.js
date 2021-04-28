import Vue from 'vue';
import Vuex from 'vuex';
import { GamesModule } from './modules/games.module';

Vue.use(Vuex);
export default new Vuex.Store({
    state: {},
    mutations: {},
    actions: {},
    modules: {
        GamesModule,
    },
});
