import Vue from 'vue';
import Vuex from 'vuex';

import { room } from './modules/room.module';
import { topic } from './modules/topic.module';
import { user } from './modules/user.module';

Vue.use(Vuex);
export default new Vuex.Store({
    state: {},
    mutations: {},
    actions: {},
    modules: {
        room,
        topic,
        user,
    },
});
