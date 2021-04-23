import Vue from 'vue';
import Wrapper from '@/router/Wrapper.vue';
import axios from 'axios';
import router from '@/router';
import store from '@/store';
import '@/assets/app.css';
import i18n from '@/i18n';
import Vuelidate from 'vuelidate';
import Notifications from 'vue-notification';
import { initSocket } from '@/services/websocket.service';

// Icon font
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEllipsisV } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faEllipsisV);
Vue.component('FontAwesomeIcon', FontAwesomeIcon);
// End of icon font

Vue.use(Vuelidate);
Vue.use(Notifications);

Vue.config.productionTip = true;
Vue.config.devtools = true;

// Set auth token
const token = localStorage.getItem('token');
axios.defaults.headers.common.Authorization = token ? `Bearer ${token}` : '';

// Skip currently unused data
axios.interceptors.response.use((response) => {
    /* eslint-disable-next-line no-param-reassign */
    response.data = response.data.data;
    return response;
}, (error) => Promise.reject(error));

initSocket();

const app = new Vue({
    el: '#app',
    router,
    store,
    i18n,
    render: (h) => h(Wrapper),
});

export default app;
