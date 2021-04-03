import App from '@/pages/App.vue';
import Error404 from '@/pages/errors/404';

const routes = [
    {
        path: '/',
        component: App,
    },

    /* Errors */
    {
        path: '/errors/404',
        name: 'error404',
        component: Error404,
    },
    {
        path: '*',
        redirect: {
            name: 'error404',
        },
    },
];
export default routes;
