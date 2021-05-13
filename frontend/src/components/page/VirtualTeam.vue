<template>
    <div>
        <div class="mb-5">
            <h1 class="text-white text-3xl font-medium ">
                {{ team.name }}
            </h1>
        </div>
        <div>
            <div
                v-for="(player, index) in players"
                :key="index"
                class="space-y-6 xl:space-y-10"
            >
                <div class="space-y-2">
                    <div class="font-medium text-lg leading-6 space-y-1">
                        <h3 class="text-white">
                            <Player
                                :player="player"
                                :badges="hostId && player.id === hostId ? [{text: 'host', colour: 'green'}] : null"
                            />
                        </h3>
                    </div>
                </div>
            </div>
        </div>

        <div
            v-show="!isGameplay"
            class="leading-6 space-y-1 w-full"
        >
            <p>
                <button
                    class="bg-gray-400 text-white py-2 px-4 outline-none rounded w-full mt-5"
                    @click="$emit('joinTeam', team.name)"
                >
                    Join
                </button>
            </p>
        </div>
    </div>
</template>

<script>
import Player from '@/components/page/Player';

export default {
    name: 'VirtualTeam',
    components: { Player },
    props: {
        team: {
            required: true,
            type: Object,
        },
        hostId: {
            default: null,
            required: false,
            type: Number,
        },
        isGameplay: {
            default: false,
            type: Boolean,
        },
    },
    computed: {
        players() {
            return this.team.players;
        },
    },
};
</script>

<style scoped>

</style>
