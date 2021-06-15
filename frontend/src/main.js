import Vue from 'vue';
import Wrapper from '@/router/Wrapper.vue';
import axios from 'axios';
import router from '@/router';
import store from '@/store';
import '@/assets/app.css';
import i18n from '@/i18n';
import Vuelidate from 'vuelidate';
import Notifications from 'vue-notification';
import Multiselect from 'vue-multiselect';
import VueConfirmDialog from 'vue-confirm-dialog';
import { initSocket } from '@/services/websocket.service';
import * as AuthService from '@/services/auth.service';

// Icon font
import { library } from '@fortawesome/fontawesome-svg-core';
import {
    faEllipsisV, faExclamation, faUpload, faChevronLeft,
    faPlus, faTimesCircle, faPen, faTrash, faFileImport,
    faSignOutAlt, faSync, faPlayCircle, faCheck,
    faSkullCrossbones, faDiceD6, faBolt, faShare,
} from '@fortawesome/free-solid-svg-icons';

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

library.add(faEllipsisV);
library.add(faExclamation);
library.add(faUpload);
library.add(faChevronLeft);
library.add(faPlus);
library.add(faTimesCircle);
library.add(faPen);
library.add(faTrash);
library.add(faFileImport);
library.add(faSignOutAlt);
library.add(faSync);
library.add(faPlayCircle);
library.add(faCheck);
library.add(faSkullCrossbones);
library.add(faDiceD6);
library.add(faBolt);
library.add(faShare);

Vue.component('FontAwesomeIcon', FontAwesomeIcon);
// End of icon font

Vue.component('Multiselect', Multiselect);
Vue.use(Vuelidate);
Vue.use(Notifications);
Vue.use(VueConfirmDialog);
Vue.component('VueConfirmDialog', VueConfirmDialog.default);

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

if (token) {
    AuthService.getCurrentUser().then((response) => {
        store.dispatch('user/setUser', response.data);
    }).catch(() => {
        localStorage.removeItem('token');
    });
}

initSocket();

const app = new Vue({
    el: '#app',
    router,
    store,
    i18n,
    render: (h) => h(Wrapper),
});

export default app;
