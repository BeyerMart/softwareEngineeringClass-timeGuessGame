import App from '@/pages/App.vue';
import Error404 from '@/pages/errors/404';
import Error500 from '@/pages/errors/500';
import Signup from '@/pages/auth/Signup';
import Login from '@/pages/auth/Login';
import Auth from '@/pages/auth/Auth';
import Profile from '@/pages/Profile';
import WSDebug from '@/pages/WSDebug';

const routes = [
    {
        path: '/',
        component: App,
    },
    {
        path: '/auth/signup',
        name: 'signup',
        component: Signup,
    },
    {
        path: '/auth/login',
        name: 'login',
        component: Login,
    },
    {
        path: '/auth',
        name: 'auth',
        component: Auth,
    },
    {
        path: '/profile',
        name: 'userProfile',
        component: Profile,
        meta: {
            auth: true,
        },
    },
    {
        path: '/profile/:id',
        name: 'profile',
        component: Profile,
        meta: {
            auth: true,
        },
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
        path: '/wsdebug',
        name: 'wsdebug',
        component: WSDebug,
    },
    {
        path: '*',
        redirect: {
            name: 'error404',
        },
    },
];
export default routes;
