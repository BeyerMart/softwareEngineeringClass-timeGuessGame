<template>
    <div>
        <div class="my-8 px-8">
            <h1 class="mt-10 mb-10 text-6xl">
                {{ $t('game.games') }}
            </h1>

            <div class="absolute top-20 right-8 mt-6">
                <button class="bg-green-600 hover:bg-green-900 text-white font-bold py-3 px-4 border order-gray-900 rounded">
                    {{ $t('game.createGame') }}
                </button>
            </div>

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
                                            v-for="(game, index) in gamesList"
                                            :key="game.id"
                                            class="cursor-pointer"
                                            :class="{ 'bg-gray-50': index % 2 !== 0 }"
                                        >
                                            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                {{ game.name }}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                {{ game.numOfPlayers }}/24
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                {{ game.numOfTeams }}
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                {{ game.topic }}
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
} from 'vuex';

export default {
    name: 'GameSelection',
    computed: { ...mapGetters(['gamesList', 'gamesCount']) },
    created() {
        this.fetchGames();
    },
    methods: {
        ...mapActions(['fetchGames', 'createGame']),
        // https://css-tricks.com/snippets/javascript/random-hex-color/
        setBg() {
            return `background-color: #${Math.floor(Math.random() * 16777215).toString(16)}`;
        },
    },

};
</script>
