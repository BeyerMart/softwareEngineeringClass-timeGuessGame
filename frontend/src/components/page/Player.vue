<template>
    <router-link
        :to="!player.virtual_id ? '/profile/' + player.id: ''"
    >
        <div class="w-full flex items-center justify-between p-3 space-x-6 bg-white rounded mb-2">
            <div class="flex-1 truncate">
                <div class="flex items-center space-x-3">
                    <h3 class="text-gray-900 text-base font-medium truncate">
                        {{ player.username }}
                    </h3>
                    <span
                        v-if="player.virtual_id"
                        class="flex-shrink-0 inline-block px-2 py-0.5 text-green-800 text-xs font-medium bg-green-100 rounded-full"
                    >Virtual User</span>
                    <span
                        v-for="(badge, index) in badges"
                        :key="index"
                        class="flex-shrink-0 inline-block px-2 py-0.5 text-xs font-medium rounded-full"
                        :class="getClass(badge.colour)"
                    >{{ badge.text }}</span>
                </div>
            </div>
        </div>
    </router-link>
</template>

<script>
export default {
    name: 'Player',
    props: {
        player: {
            required: true,
            type: Object,
        },
        badges: {
            required: false,
            type: Array,
            default() {
                return [];
            },
        },
    },
    methods: {
        getClass(colour) {
            return `text-${colour}-100 bg-${colour}-600`;
        },
    },
};
</script>

<style scoped>

</style>
