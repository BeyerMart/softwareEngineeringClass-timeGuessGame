<template>
    <div class="h-screen flex flex-col justify-between">
        <Navbar v-show="showNavbar" />
        <router-view class="mb-auto" />
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
