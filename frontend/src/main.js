import Vue from 'vue';
import Wrapper from '@/router/Wrapper.vue';
import router from '@/router';
import store from '@/store';
import '@/assets/app.css';
import i18n from '@/i18n';

Vue.config.productionTip = true;
Vue.config.devtools = true;

const app = new Vue({
    el: '#app',
    router,
    store,
    i18n,
    render: (h) => h(Wrapper),
});

export default app;
