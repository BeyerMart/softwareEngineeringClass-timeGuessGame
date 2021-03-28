import Vue from 'vue';
import VueI18n from 'vue-i18n';

import german from './german';
import english from './english';

Vue.use(VueI18n);

export const messages = {
    en: english,
    de: german,
};

export default new VueI18n({
    locale: localStorage.getItem('lang') || 'de', // set locale, defaults to german
    messages,
});
