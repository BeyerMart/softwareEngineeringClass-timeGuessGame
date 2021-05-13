<template>
    <div
        class="ml-3 relative hidden sm:block"
    >
        <div>
            <button
                id="menu-button"
                class="inline-flex justify-center w-full rounded-md border border-gray-700 shadow-sm px-4 py-2 bg-gray-700 text-sm font-medium text-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-700 focus:ring-white"
                aria-expanded="true"
                aria-haspopup="true"
                @click="showSelectorDropdown = !showSelectorDropdown"
            >
                {{ $i18n.locale }}
                <svg
                    class="-mr-1 ml-2 h-5 w-5"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 20 20"
                    fill="currentColor"
                    aria-hidden="true"
                >
                    <path
                        fill-rule="evenodd"
                        d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                        clip-rule="evenodd"
                    />
                </svg>
            </button>
        </div>
        <div
            v-show="showSelectorDropdown"
            class="absolute right-0 origin-top-right mt-2 w-40 rounded shadow-lg py-2 bg-white ring-1 ring-black ring-opacity-5 focus:outline-none"
        >
            <a
                v-for="lang in languages"
                :key="lang"
                href="#"
                class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-200"
                @click="changeLanguage(lang)"
            >{{ $t('languages.' + lang) }} ({{ lang }})</a>
        </div>
    </div>
</template>
<script>
import { messages } from '@/i18n/index';

export default {
    data() {
        return {
            showSelectorDropdown: false,
        };
    },
    computed: {
        languages() {
            return Object.keys(messages);
        },
    },
    methods: {
        changeLanguage(lang) {
            this.$i18n.locale = lang;
            localStorage.setItem('lang', this.$i18n.locale);
        },
    },
};
</script>
