<template>
    <div>
        <div class="my-8">
            <div
                class="max-w-3xl mx-auto px-4 sm:px-6 lg:max-w-7xl lg:px-8"
            >
                <h1 class="mb-8 text-4xl md:text-5xl">
                    {{ $t('dashboard.dashboard') }}
                </h1>
                <!-- TABS https://www.creative-tim.com/learning-lab/tailwind-starter-kit/documentation/vue/tabs/text-->
                <ul class="flex mb-0 list-none flex-wrap pt-3 pb-4 flex-row">
                    <li class="-mb-px mr-2 flex-auto text-center cursor-pointer">
                        <a
                            class="text-xs font-bold uppercase px-5 py-3 shadow rounded block leading-normal"
                            :class="{'text-gray-900 bg-white': openTab !== 1, 'bg-gray-100': openTab === 1}"
                            @click="toggleTabs(1);"
                        >
                            {{ $t('dashboard.players') }}
                        </a>
                    </li>
                    <li class="-mb-px mr-2 flex-auto text-center cursor-pointer">
                        <a
                            class="text-xs font-bold uppercase px-5 py-3 shadow rounded block leading-normal"
                            :class="{'text-gray-900 bg-white': openTab !== 2, 'bg-gray-100': openTab === 2}"
                            @click="toggleTabs(2); fetchTopics(); fetchGames();"
                        >
                            {{ $t('dashboard.games') }}
                        </a>
                    </li>
                    <li class="-mb-px flex-auto text-center cursor-pointer">
                        <a
                            class="text-xs font-bold uppercase px-5 py-3 shadow rounded block leading-normal"
                            :class="{'text-gray-900 bg-white': openTab !== 3, 'bg-gray-100': openTab === 3}"
                            @click="toggleTabs(3); fetchTopics();"
                        >
                            {{ $t('dashboard.topics') }}
                        </a>
                    </li>
                </ul>
                <div class="">
                    <div class="flex-auto">
                        <div class="tab-content tab-space">
                            <div :class="{'hidden': openTab !== 1, 'block': openTab === 1}">
                                <p>
                                    <UserDashboard
                                        :users="users"
                                        @fetchUsers="fetchUsers(true)"
                                    />
                                </p>
                            </div>
                            <div :class="{'hidden': openTab !== 2, 'block': openTab === 2}">
                                <p>
                                    <GameDashboard
                                        :topics="topics"
                                        :games="games"
                                        :users="users"
                                        @fetchGames="fetchGames(true)"
                                    />
                                </p>
                            </div>

                            <div :class="{'hidden': openTab !== 3, 'block': openTab === 3}">
                                <p>
                                    <TopicDashboard
                                        :users="users"
                                        :topics="topics"
                                        @fetchTopics="fetchTopics(true)"
                                    />
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { getTopics } from '@/services/topic.service';
import { getUsers } from '@/services/user.service';
import { getGames } from '@/services/game.service';
import TopicDashboard from '@/components/page/TopicDashboard.vue';
import UserDashboard from '@/components/page/UserDashboard.vue';
import GameDashboard from '@/components/page/GameDashboard.vue';

export default {
    name: 'AdminDashboard',
    components: {
        TopicDashboard,
        UserDashboard,
        GameDashboard,
    },
    data() {
        return {
            topics: [],
            users: [],
            games: [],
            openTab: 1,
            fetched: {
                topics: false,
                users: false,
                games: false,
            },
        };
    },

    mounted() {
        this.fetchUsers();
    },
    methods: {
        toggleTabs(tabNumber) {
            this.openTab = tabNumber;
        },
        fetchGames(force = false) {
            if (!this.fetched.games || force) {
                getGames().then((res) => {
                    this.games = res.data;
                    this.fetched.games = true;
                }).catch((err) => {
                    this.$notify({
                        title: this.$t('generic.error'),
                        text: err.response.data.error,
                        type: 'error',
                    });
                });
            }
        },
        fetchUsers(force = false) {
            if (!this.fetched.users || force) {
                getUsers().then((res) => {
                    this.users = res.data.filter((user) => user.deleted_at === null);
                    this.fetched.users = true;
                }).catch((err) => {
                    this.$notify({
                        title: this.$t('generic.error'),
                        text: err.response.data.error,
                        type: 'error',
                    });
                });
            }
        },
        fetchTopics(force = false) {
            if (!this.fetched.topics || force) {
                getTopics().then((res) => {
                    this.topics = res.data;
                    this.fetched.topics = true;
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

};
</script>
