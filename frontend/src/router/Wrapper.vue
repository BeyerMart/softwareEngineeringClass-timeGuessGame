<template>
    <div>
        <navbar v-show="showNavbar" />
        <router-view />
    </div>
</template>
<script>
import Navbar from '@/components/page/Navbar.vue';

export default {
    name: 'Wrapper',
    components: {
        Navbar,
    },
    computed: {
        showNavbar() {
            return this.$route.meta.navbar == null ? true : this.$route.meta.navbar;
        },
    },
};
</script>
