<template>
    <div>
        <div class="my-8">
            <div
                class="max-w-3xl mx-auto px-4 sm:px-6 lg:max-w-7xl lg:px-8"
            >
                <h1 class="mb-8 text-4xl md:text-5xl">
                    {{ $t('dashboard.dashboard') }}
                </h1>
                <div class="admin-actions">
                    <button
                        class="bg-gray-900 hover:bg-gray-600 text-white font-bold p-3 rounded mb-2"
                        @click="showImporter = !showImporter; fetchTopics();"
                    >
                        {{ $t('dashboard.termImporter') }}
                    </button>
                </div>
                <TermImporter
                    v-show="showImporter"
                    :topics="topics"
                />
                <!-- TABS https://www.creative-tim.com/learning-lab/tailwind-starter-kit/documentation/vue/tabs/text-->
                <ul class="flex mb-0 list-none flex-wrap pt-3 pb-4 flex-row">
                    <li class="-mb-px mr-2 last:mr-0 flex-auto text-center cursor-pointer">
                        <a
                            class="text-xs font-bold uppercase px-5 py-3 shadow rounded block leading-normal"
                            :class="{'text-gray-900 bg-white': openTab !== 1, 'bg-gray-100': openTab === 1}"
                            @click="toggleTabs(1)"
                        >
                            {{ $t('dashboard.players') }}
                        </a>
                    </li>
                    <li class="-mb-px mr-2 last:mr-0 flex-auto text-center cursor-pointer">
                        <a
                            class="text-xs font-bold uppercase px-5 py-3 shadow rounded block leading-normal"
                            :class="{'text-gray-900 bg-white': openTab !== 2, 'bg-gray-100': openTab === 2}"
                            @click="toggleTabs(2)"
                        >
                            {{ $t('dashboard.games') }}
                        </a>
                    </li>
                    <li class="-mb-px mr-2 last:mr-0 flex-auto text-center cursor-pointer">
                        <a
                            class="text-xs font-bold uppercase px-5 py-3 shadow rounded block leading-normal"
                            :class="{'text-gray-900 bg-white': openTab !== 3, 'bg-gray-100': openTab === 3}"
                            @click="toggleTabs(3); fetchTopics();"
                        >
                            {{ $t('dashboard.topics') }}
                        </a>
                    </li>
                </ul>
                <div class="relative flex flex-col min-w-0 break-words bg-white w-full mb-6 shadow rounded">
                    <div class="flex-auto">
                        <div class="tab-content tab-space">
                            <div :class="{'hidden': openTab !== 1, 'block': openTab === 1}">
                                <p>
                                    test
                                </p>
                            </div>
                            <div :class="{'hidden': openTab !== 2, 'block': openTab === 2}">
                                <p>
                                    Completely synergize resource taxing relationships via
                                    premier niche markets. Professionally cultivate one-to-one
                                    customer service with robust ideas.
                                    <br>
                                    <br>
                                    Dynamically innovate resource-leveling customer service for
                                    state of the art customer service.
                                </p>
                            </div>

                            <div :class="{'hidden': openTab !== 3, 'block': openTab === 3}">
                                <p>
                                    <table class="min-w-full divide-y divide-gray-200">
                                        <thead class="bg-gray-50">
                                            <tr>
                                                <th
                                                    scope="col"
                                                    class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                >
                                                    ID
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
                                                    {{ $t('dashboard.createdBy') }}
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr
                                                v-for="(topic, index) in topics"
                                                :key="topic.id"
                                                :class="{ 'bg-gray-50': index % 2 === 0}"
                                            >
                                                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                    {{ topic.id }}
                                                </td>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                    {{ topic.name }}
                                                </td>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                    {{ topic.creator_id }}
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
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
import * as TopicService from '@/services/topic.service';
import TermImporter from '@/components/page/TermImporter.vue';

export default {
    name: 'AdminDashboard',
    components: {
        TermImporter,
    },
    data() {
        return {
            topics: [],
            openTab: 1,
            showImporter: false,
            fetched: false,
        };
    },
    computed: {
        isAdmin() {
            return this.$store.getters['user/isAdmin'];
        },

    },
    methods: {
        toggleTabs(tabNumber) {
            this.openTab = tabNumber;
        },

        fetchTopics() {
            if (!this.fetched) {
                TopicService.getTopics().then((res) => {
                    this.topics = res.data;
                    this.fetched = true;
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
