<template>
    <div class="h-screen flex flex-col justify-between">
        <Navbar v-show="showNavbar" />
        <router-view class="mb-auto" />
        <notifications
            class="vue-notf mr-2"
            position="top right"
        />
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
</style>
