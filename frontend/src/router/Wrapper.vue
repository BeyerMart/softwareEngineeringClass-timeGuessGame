<template>
    <div class="h-screen flex flex-col justify-between">
        <Navbar v-show="showNavbar" />
        <router-view
            :key="$route.fullPath"
            class="mb-auto"
        />
        <notifications
            class="vue-notf mr-2"
            position="top right"
        />
        <vue-confirm-dialog class="confirm-dialog" />
        <Footer v-show="showFooter" />
    </div>
</template>
<script>
import Navbar from '@/components/page/Navbar.vue';
import Footer from '@/components/page/Footer.vue';

export default {
    name: 'Wrapper',
    components: {
        Navbar,
        Footer,
    },
    computed: {
        showNavbar() {
            return this.$route.meta.navbar == null ? true : this.$route.meta.navbar;
        },
        showFooter() {
            return this.$route.meta.footer == null ? true : this.$route.meta.footer;
        },
    },
};
</script>
<style>
    .vue-notf {
        margin-top: 75px;
        z-index: 10000 !important;
    }
    .vue-notification {
        margin-right: 75px;
    }
    @media (max-width: 450px) {
        .vue-notf {
            width: 100% !important;
            right: 0 !important;
        }
        .vue-notification {
            margin-right: 0;
        }
        .notification-wrapper {
            width: 80%;
            margin: auto;
        }
    }
    .confirm-dialog .vc-container {
        @apply bg-white rounded-lg p-6 shadow-xl;
    }
    .confirm-dialog .vc-container .vc-title {
        @apply text-lg my-2 font-bold uppercase tracking-normal;
    }
    .confirm-dialog .vc-container .vc-text {
        @apply text-base my-2;
    }
    .confirm-dialog .vc-container .vc-btn-grid {
        @apply flex justify-evenly rounded-none gap-2;
    }
    .confirm-dialog .vc-container .vc-btn-grid .vc-btn {
        @apply rounded border-none w-full outline-none bg-gray-900 hover:bg-gray-600 text-white ;
    }
    .confirm-dialog .vc-container .vc-btn-grid .vc-btn.left {
        @apply bg-gray-500 hover:bg-gray-300;
    }

</style>
