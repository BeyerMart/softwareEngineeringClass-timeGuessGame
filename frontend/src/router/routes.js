import App from '@/pages/App.vue';
import Error404 from '@/pages/errors/404';
import Error500 from '@/pages/errors/500';

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
        path: '/errors/500',
        name: 'error500',
        component: Error500,
    },
    {
        path: '*',
        redirect: {
            name: 'error404',
        },
    },
];
export default routes;
