<template>
    <div class="w-4/5 h-10 py-3 px-1">
        <p class="hover:text-blue-dark">
            {{ player.username }}
        </p>
        <p class="text-sm text-grey-dark">
            <span
                v-if="player.virtual_id"
                class="inline-flex items-center justify-center px-2 py-1 mr-2 text-xs font-bold leading-none text-red-100 bg-red-600 rounded-full"
            >Virtual User</span>
            <span
                v-for="(badge, index) in badges"
                :key="index"
                :class="getClass(badge.colour)"
            >badge.text</span>
        </p>
    </div>
</template>

<script>
export default {
    name: 'Player',
    props: {
        player: {
            required: true,
        },
        badges: {
            required: false,
            type: Array,
            default: [],
        },
    },
    methods: {
        getClass(colour) {
            return `inline-flex items-center justify-center px-2 py-1 mr-2 text-xs font-bold leading-none text-${colour}-100 bg-${colour}-600 rounded-full`
        }
    }
};
</script>

<style scoped>

</style>
