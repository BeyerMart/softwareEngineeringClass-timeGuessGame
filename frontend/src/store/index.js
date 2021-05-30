import Vue from 'vue';
import Vuex from 'vuex';

import { roomModule } from './modules/room.module';
import { topicModule } from './modules/topic.module';
import { user } from './modules/user.module';

Vue.use(Vuex);
export default new Vuex.Store({
    state: {},
    mutations: {},
    actions: {},
    modules: {
        roomModule,
        topicModule,
        user,
    },
});
