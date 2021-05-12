<template>
    <div class="w-full max-w-screen-xl mx-auto p-6">
        <div class="relative rounded overflow-hidden border border-grey-light mb-8 bg-white">
            <div class="border-b border-grey-light p-4 bg-grey-lighter py-8">
                <div class="bg-white mx-auto max-w-sm shadow-lg rounded-lg overflow-hidden">
                    <div class="sm:flex sm:items-center px-2 py-4">
                        <div class="flex-grow">
                            <h3 class="font-normal px-2 py-3 leading-tight">
                                Contacts
                            </h3>
                            <div class="w-full">
                                <div class="flex cursor-pointer my-1 hover:bg-blue-lightest rounded">
                                    <div
                                        v-for="(player, index) in players"
                                        :key="index"
                                    >
                                        <Player
                                            :player="player"
                                            :badges="hostId && player.id === hostId ? [{text: 'host', colour: 'green'}] : null"
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="sm:flex bg-grey-light sm:items-center px-2 py-4">
                        <div class="flex-grow text-right">
                            <button
                                class="bg-blue hover:bg-blue-dark text-white py-2 px-4 rounded"
                                @click="$emit('joinTeam')"
                            >
                                Join
                            </button>
                        </div>
                    </div>
                </div>
            </div>
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
