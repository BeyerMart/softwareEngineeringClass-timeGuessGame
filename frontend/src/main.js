import Vue from 'vue';
import Wrapper from './router/Wrapper.vue';
import router from './router';
import store from './store';

const app = new Vue({
  el: '#app',
  router,
  store,
  render: (h) => h(Wrapper),
});

export default app;