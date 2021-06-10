<template>
    <div>
        <div class="my-8">
            <div
                class="max-w-3xl mx-auto px-4 sm:px-6 lg:max-w-7xl lg:px-8"
            >
                <h1 class="mb-8 text-4xl md:text-5xl">
                    {{ $t('generic.profile') }}
                </h1>
                <div class="grid grid-cols-1 gap-4 items-start lg:grid-cols-3 lg:gap-8">
                    <div class="grid grid-cols-1 gap-4 lg:col-span-2">
                        <!-- USER CARD -->
                        <section
                            aria-labelledby="user-card"
                            class="mb-5"
                        >
                            <UserCard
                                :user="user"
                                :is-admin="isAdmin"
                                :is-self="isSelf"
                                @update-user="getUser"
                            />
                        </section>
                        <section aria-labelledby="games-table">
                            <i
                                v-show="matchHistory.length == 0"
                            >{{ $t('profile.noGamesPlayed') }}</i>
                            <div
                                v-show="matchHistory.length > 0"
                                class="flex flex-col"
                            >
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
                                                            {{ $t('game.teamName') }}
                                                        </th>
                                                        <th
                                                            scope="col"
                                                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                        >
                                                            {{ $t('game.winnerTeam') }}
                                                        </th>
                                                        <th
                                                            scope="col"
                                                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                        >
                                                            {{ $t('game.topic') }}
                                                        </th>
                                                        <th
                                                            scope="col"
                                                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                        >
                                                            {{ $t('generic.date') }}
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr
                                                        v-for="match in matchHistory"
                                                        :key="match.id"
                                                        class="cursor-pointer"
                                                    >
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                            {{ match.name }}
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            {{
                                                                match.winner_id ? teams.find(team => match.winner_id === team.id).name : ''
                                                            }}
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            {{
                                                                match.topic_id ? topicList.find(topic => match.topic_id === topic.id).name : ''
                                                            }}
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            {{ gameDate(user.created_at) }}
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
                    <div class="grid grid-cols-1 gap-4">
                        <section>
                            <div class="rounded-lg bg-gray-100 overflow-hidden shadow">
                                <div class="p-6">
                                    <h2
                                        class="text-base font-medium text-gray-900"
                                    >
                                        {{ $t('profile.lastPlayedWith') }}
                                    </h2>
                                    <div class="flow-root mt-6">
                                        <ul class="-my-5 divide-y divide-gray-200">
                                            <li
                                                v-for="lpuser in lastPlayedWithUsers"
                                                :key="lpuser.id"
                                                class="py-5"
                                            >
                                                <div class="relative focus-within:ring-2 focus-within:ring-cyan-500">
                                                    <h3 class="text-sm font-semibold text-gray-800">
                                                        <router-link
                                                            :to="{ name: 'profile', params: { id: lpuser.id }}"
                                                            class="hover:underline focus:outline-none"
                                                        >
                                                            {{ lpuser.username }}
                                                        </router-link>
                                                    </h3>
                                                </div>
                                            </li>
                                            <li
                                                v-show="lastPlayedWithUsers.length == 0"
                                                class="py-5"
                                            >
                                                <i>{{ $t('profile.nothingYet') }}</i>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </section>
                        <section>
                            <div class="rounded-lg bg-gray-100 overflow-hidden shadow">
                                <div class="p-6">
                                    <h2
                                        class="text-base font-medium text-gray-900"
                                    >
                                        {{ $t('profile.stats') }}
                                    </h2>
                                    <div class="flow-root mt-6">
                                        <PieChart
                                            v-show="pieData[0].value != 0"
                                            :pie-data="pieData"
                                        />
                                        <i v-show="pieData[0].value == 0">
                                            {{ $t('profile.nothingYet') }}
                                        </i>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import UserCard from '@/components/page/UserCard.vue';
import {
    getUserById, getUserMatchHistory, getUserWinRatio, getUserTotalGames, getLastPlayedWithUsers,
} from '@/services/user.service';
import { getTeams } from '@/services/team.service';
import { mapGetters, mapActions } from 'vuex';
import PieChart from '@/components/generic/PieChart.vue';

export default {
    name: 'Profile',
    components: {
        UserCard,
        PieChart,
    },
    data() {
        return {
            user: {},
            isSelf: false,
            matchHistory: [],
            teams: [],
            pieData: [
                { color: '#00A37A', value: 0 },
                { color: '#365164', value: 0 },
            ],
            totalGames: 0,
            lastPlayedWithUsers: [],
            viewKey: 1,
        };
    },
    computed: {
        ...mapGetters(['topicList']),
        isAdmin() {
            return this.$store.getters['user/isAdmin'];
        },
    },
    mounted() {
        this.fetchTopics();
        this.getUser();
    },
    methods: {
        ...mapActions(['fetchTopics']),
        gameDate(timestamp) {
            return new Date(timestamp).toLocaleDateString();
        },
        async getUser() {
            if (this.$route.params.id) {
                await getUserById(this.$route.params.id).then((res) => {
                    this.user = res.data;
                }).catch((err) => {
                    this.$notify({
                        title: this.$t('generic.error'),
                        text: err.response.data.error,
                        type: 'error',
                    });
                    if (err.response.data.status === 404) {
                        this.$router.push({ name: 'error404' });
                    } else {
                        this.$router.push({ name: 'error500' });
                    }
                });
            } else {
                this.isSelf = true;
                this.user = this.$store.getters['user/getUser'];
            }

            this.getUserStatics();
        },
        getUserStatics() {
            this.getMatchHistory();
            getTeams().then((teamsRes) => {
                this.teams = teamsRes.data;
            });
            this.getWinLossRatio();
            this.getLastPlayedWithUsers();
        },
        getMatchHistory() {
            getUserMatchHistory(this.user.id).then((res) => {
                this.matchHistory = res.data;
            }).catch((err) => {
                console.log(err);
            });
        },
        getWinLossRatio() {
            getUserWinRatio(this.user.id).then((res) => {
                getUserTotalGames(this.user.id).then((totalGamesRes) => {
                    this.totalGames = totalGamesRes.data;
                    this.pieData = [
                        { color: '#00A37A', value: (this.totalGames * res.data) * 100 },
                        { color: '#365164', value: (this.totalGames - res.data) * 100 },
                    ];
                });
            }).catch((err) => {
                console.error(err);
            });
        },
        getLastPlayedWithUsers() {
            getLastPlayedWithUsers(this.user.id).then((res) => {
                this.lastPlayedWithUsers = res.data;
                this.lastPlayedWithUsers = this.lastPlayedWithUsers.filter((lpUsr) => lpUsr.id !== this.user.id);
            }).catch((err) => {
                console.error(err);
            });
        },
    },

};
</script>
