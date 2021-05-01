import Vue from 'vue';
import VueRouter from 'vue-router';
import store from '@/store';
import routes from './routes';

Vue.use(VueRouter);
const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

router.beforeEach((to, from, next) => {
    if (to.meta.auth) {
        // Check if user is already authenticated
        if (store.getters['user/isLoggedIn']) {
            // The user is logged in
            const { role } = store.getters['user/getUser'];
            if (!to.meta.role || (to.meta.role.includes(role))) {
                next();
            } else {
                // Simulate page is not found
                next({ name: 'error404' });
            }
        } else {
            localStorage.setItem('entry_url', to.path);

            next({ name: 'auth' });
        }
    } else {
        // No auth is required
        next();
    }
});

export default router;
