<template>
    <div>
        <pre>{{ terms }}</pre>
        <table class="min-w-full divide-y divide-gray-200 bg-white w-full mb-6 shadow rounded">
            <thead class="bg-gray-50">
                <tr>
                    <th
                        v-for="column in columns"
                        :key="column.id"
                        class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                    >
                        {{ column }}
                    </th>
                </tr>
            </thead>
            <tbody>
                <tr
                    v-for="(term, index) in terms"
                    :key="term.id"
                    :class="{ 'bg-gray-50': index % 2 === 0}"
                >
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ term.id }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ term.name }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ term.correct_guesses }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ term.appearances }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        <font-awesome-icon
                            icon="pen"
                            class="text-l cursor-pointer mr-5"
                        />

                        <font-awesome-icon
                            icon="trash"
                            class="text-l cursor-pointer"
                            @click.stop="deleteTerm(term.topic_id, term.id)"
                        />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>

import { deleteTerm } from '@/services/topic.service';

export default {
    name: 'TermTable',
    components: {
    },
    props: {
        terms: {
            type: Array,
            default: () => [],
        },
    },
    data() {
        return {
            columns: ['ID', this.$t('game.name'), this.$t('dashboard.correctGuess'), this.$t('dashboard.numOfAppearances'), 'options'],
        };
    },
    watch: {
        terms() {},
    },
    methods: {
        deleteTerm(topicId, termId) {
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
                            deleteTerm(topicId, termId).then(() => {
                                this.$notify({
                                    title: this.$t('dashboard.messages.deleteTermSuccess'),
                                    type: 'success',
                                });
                                this.$emit('fetchTerms');
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
    },
};
</script>
