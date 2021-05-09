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
                <div class="">
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
                                    <section
                                        v-show="!display.showTermTable"
                                    >
                                        <div class="flex justify-end mb-4 gap-2">
                                            <button
                                                class="flex items-center gap-3 bg-gray-900 hover:bg-gray-600 text-white p-2 rounded"
                                                @click="display.showImporter = !display.showImporter; fetchTopics();"
                                            >
                                                <font-awesome-icon
                                                    icon="file-import"
                                                    class="text-l cursor-pointer"
                                                />
                                                {{ $t('dashboard.termImporter') }}
                                            </button>
                                            <button
                                                class="flex items-center gap-3 p-2 rounded bg-gray-900 hover:bg-gray-600 text-white"
                                                @click="display.showTopicForm = true"
                                            >
                                                <font-awesome-icon
                                                    icon="plus"
                                                    class="text-l cursor-pointer"
                                                />
                                                {{ $t('dashboard.createTopic') }}
                                            </button>
                                            <CreateTopicForm
                                                v-show="display.showTopicForm"
                                                @close="display.showTopicForm = false"
                                                @fetchTopics="fetchTopics(true)"
                                            />
                                            <EditTopicForm
                                                v-show="display.showTopicEditForm"
                                                v-bind="topicToEdit"
                                                @close="display.showTopicEditForm = false"
                                                @fetchTopics="fetchTopics(true)"
                                            />
                                        </div>
                                        <TermImporter
                                            v-show="display.showImporter"
                                            class="m-auto mr-0 mb-3"
                                            :topics="topics"
                                        />
                                        <table class="min-w-full divide-y divide-gray-200 shadow rounded">
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
                                                    <th
                                                        scope="col"
                                                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                    >
                                                        {{ $t('dashboard.options') }}
                                                    </th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr
                                                    v-for="(topic, index) in topics"
                                                    :key="topic.id"
                                                    class="cursor-pointer hover:bg-gray-200"
                                                    :class="{ 'bg-gray-50': index % 2 === 0}"
                                                    @click="fetchTerms(topic);"
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
                                                    <td
                                                        class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"
                                                        @click.stop
                                                    >
                                                        <font-awesome-icon
                                                            icon="pen"
                                                            class="text-xl cursor-pointer mr-5"
                                                            @click.stop="editTopic(topic)"
                                                        />

                                                        <font-awesome-icon
                                                            icon="trash"
                                                            class="text-xl cursor-pointer"
                                                            @click.stop="deleteTopic(topic.id)"
                                                        />
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </section>

                                    <section v-show="display.showTermTable">
                                        <div class="flex justify-between mb-4">
                                            <button
                                                class="flex justify-items-center gap-4 py-2"
                                                @click="display.showTermTable = false; selectedTopic = {}"
                                            >
                                                <font-awesome-icon
                                                    icon="chevron-left"
                                                    class="text-2xl cursor-pointer"
                                                />
                                                {{ $t('generic.back') }}
                                            </button>
                                            <button
                                                class="flex items-center gap-3 p-2 rounded bg-gray-900 hover:bg-gray-600 text-white"
                                                @click="display.showTermForm = true"
                                            >
                                                <font-awesome-icon
                                                    icon="plus"
                                                    class="text-l cursor-pointer"
                                                />
                                                {{ $t('dashboard.createTerm') }}
                                            </button>
                                            <CreateTermForm
                                                v-show="display.showTermForm"
                                                :topic="selectedTopic"
                                                @close="display.showTermForm = false"
                                                @fetchTerms="fetchTerms(selectedTopic)"
                                            />
                                        </div>
                                        <TermTable
                                            :terms="terms"
                                            @fetchTerms="fetchTerms(selectedTopic)"
                                        />
                                    </section>
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
import TermTable from '@/components/page/TermTable.vue';
import CreateTopicForm from '@/components/forms/CreateTopicForm.vue';
import EditTopicForm from '@/components/forms/EditTopicForm.vue';
import CreateTermForm from '@/components/forms/CreateTermForm.vue';

export default {
    name: 'AdminDashboard',
    components: {
        TermImporter,
        TermTable,
        CreateTopicForm,
        CreateTermForm,
        EditTopicForm,
    },

    data() {
        return {
            topics: [],
            terms: [],
            openTab: 1,
            fetched: false,
            selectedTopic: {},
            topicToEdit: {},
            display: {
                showTopicForm: false,
                showTopicEditForm: false,
                showTermForm: false,
                showImporter: false,
                showTermTable: false,
            },
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

        fetchTopics(force = false) {
            if (!this.fetched || force) {
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
        editTopic(topic) {
            this.topicToEdit = topic;
            this.display.showTopicEditForm = true;
        },
        deleteTopic(topicId) {
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
                            TopicService.deleteTopic(topicId).then(() => {
                                this.$notify({
                                    title: this.$t('dashboard.messages.deleteTopicSuccess'),
                                    type: 'success',
                                });
                                this.fetched = false;
                                this.fetchTopics();
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
        fetchTerms(topic) {
            console.log('FETCH me ');
            TopicService.getTermsForTopic(topic.id).then((res) => {
                this.terms = res.data;
                this.display.showTermTable = true;
                this.selectedTopic = topic;
            }).catch((err) => {
                this.$notify({
                    title: this.$t('generic.error'),
                    text: err.response.data.error,
                    type: 'error',
                });
            });
        },
    },

};
</script>
