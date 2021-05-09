<template>
    <div>
        <div class="my-8 px-8">
            <h1 class="mt-10 mb-10 text-6xl">
                {{ $t('game.games') }}
            </h1>

            <div class="absolute top-20 right-8 mt-6">
                <button
                    class="bg-green-600 hover:bg-green-900 text-white font-bold py-3 px-4 border order-gray-900 rounded"
                    @click="showModal = true"
                >
                    {{ $t('game.createGame') }}
                </button>
            </div>

            <RoomCreateForm
                v-show="showModal"
                @close="showModal = false"
            />
            <section aria-labelledby="games-table">
                <div class="flex flex-col">
                    <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
                        <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
                            <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
                                <table class="min-w-full divide-y divide-gray-200">
                                    <thead class="bg-gray-50">
                                        <tr>
                                            <th
                                                scope="col"
                                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                            >
                                                {{ $t('game.room') }}
                                            </th>
                                            <th
                                                scope="col"
                                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                            >
                                                {{ $t('game.numOfPlayers') }}
                                            </th>
                                            <th
                                                scope="col"
                                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                            >
                                                {{ $t('game.numOfTeams') }}
                                            </th>
                                            <th
                                                scope="col"
                                                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                            >
                                                {{ $t('game.topic') }}
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr
                                            v-for="(room, index) in roomsList"
                                            :key="room.id"
                                            class="cursor-pointer"
                                            :class="{ 'bg-gray-50': index % 2 !== 0 }"
                                        >
                                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                {{ room.room_name }}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                {{ room.amountOfPlayers }}/24
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                {{ Object.keys(room.teams).length }}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                {{ room.topic_id ? topicList.find(topic => room.topic_id === topic.id).name : '' }}
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
</template>
<script>
import {
    mapGetters,
    mapActions,
    mapMutations,
} from 'vuex';
import RoomCreateForm from '@/components/page/RoomCreateForm';
import { subChannel, unsubChannel, isConnected } from '@/services/websocket.service';

export default {
    name: 'RoomSelection',
    components: {
        RoomCreateForm,
    },
    data() {
        return {
            showModal: false,
        };
    },
    computed: { ...mapGetters(['roomsList', 'topicList']) },
    unmounted() {
        unsubChannel('/rooms');
    },
    mounted() {
        this.fetchRooms();
        if (!this.topicList) this.fetchTopics();
        while (!isConnected);
        subChannel('/rooms', (message) => {
            switch (message.type) {
            case 'ROOM_CREATED':
                this.addRoom(message.data);
                break;
            case 'ROOM_DELETED':
                this.removeRoom(message.id);
                break;
            case 'ROOM_CHANGED':
                this.updateRoom(message.data);
                break;
            default:
                break;
            }
        });
    },
    methods: {
        ...mapActions(['fetchRooms', 'fetchTopics']),
        ...mapMutations(['removeRoom', 'addRoom', 'updateRoom']),
    },
};
</script>
