<template>
    <div>
        <table class="min-w-full divide-y divide-gray-200 bg-white w-full mb-6 shadow rounded">
            <thead class="bg-gray-50">
                <tr>
                    <th
                        v-for="column in columns"
                        :key="column"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                        {{ column }}
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr
                    v-for="(game, index) in games"
                    :key="game.id"
                    class="cursor-default"
                    :class="{ 'bg-gray-50': index % 2 !== 0}"
                >
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ game.id }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ game.name }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ getTeamName(game.team_id) }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ getTopicName(game.topic_id) }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ creationDate(game.created_at) }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        <font-awesome-icon
                            icon="trash"
                            class="text-l cursor-pointer"
                            @click="deleteGame(game)"
                        />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import { getTeams } from '@/services/team.service';
import { deleteGame } from '@/services/game.service';

export default {
    name: 'GameDashboard',
    props: {
        games: {
            type: Array,
            default: () => {},
        },
        topics: {
            type: Array,
            default: () => {},
        },
        users: {
            type: Array,
            default: () => {},
        },
    },
    data() {
        return {
            columns: [
                'ID',
                this.$t('generic.name'),
                this.$t('game.teamName'),
                this.$t('game.topic'),
                this.$t('game.createdAt'),
                this.$t('dashboard.options'),
            ],
            teams: [],
            fetched: false,
        };
    },
    watch: {
        games() {},
    },
    mounted() {
        this.getTeams();
    },
    methods: {
        getTeams() {
            if (!this.fetched) {
                getTeams().then((res) => {
                    this.teams = res.data;
                }).catch((err) => {
                    console.error(err.response);
                });
            }
        },

        deleteGame(game) {
            this.$confirm(
                {
                    title: this.$t('generic.confirmTitle'),
                    message: this.$t('generic.confirmMessage'),
                    button: {

                        yes: this.$t('generic.yes'),
                        no: this.$t('generic.no'),
                    },
                    callback: (confirm) => {
                        if (confirm) {
                            deleteGame(game.id).then(() => {
                                this.$notify({
                                    title: this.$t('dashboard.messages.gameDeleteSuccess'),
                                    type: 'success',
                                });
                                this.$emit('fetchGames');
                            }).catch((err) => {
                                this.$notify({
                                    title: this.$t('generic.error'),
                                    text: err.response.data.error,
                                    type: 'error',
                                });
                            });
                        }
                    },
                },
            );
        },
        creationDate(timestamp) {
            return new Date(timestamp).toLocaleDateString();
        },

        getTopicName(topicId) {
            const res = this.topics.find((topic) => topic.id === topicId);
            return res ? res.name : topicId;
        },

        getTeamName(teamId) {
            const res = this.teams.find((team) => team.id === teamId);
            return res ? res.name : teamId;
        },
    },

};
</script>
