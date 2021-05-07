<template>
    <div class="my-2 rounded shadow p-4 max-w-lg bg-gray-50">
        <h1 class="text-xl mb-4">
            {{ $t('dashboard.importTerms') }}
        </h1>
        <!-- TODO: translate -->
        <label class="text-l">{{ $t('dashboard.selectTopic') }}</label>
        <multiselect
            v-model="selectedTopic"
            track-by="id"
            label="name"
            select-label=""
            deselect-label=""
            :placeholder="$t('game.selectTopic')"
            :options="topics"
            :searchable="true"
            :allow-empty="false"
            class="border rounded mb-4 mt-2"
        />

        <label class="text-reader flex flex-col items-center p-2 bg-white text-blue rounded border cursor-pointer hover:bg-gray-100">
            <font-awesome-icon
                icon="upload"
                class="text-2xl cursor-pointer"
            />
            <span class="mt-2 text-base leading-normal">{{ $t('dashboard.selectFile') }}</span>
            <input
                ref="termsInput"
                type="file"
                class="hidden"
                accept="application/json"
                @change="onFileChange"
            >
        </label>
        <div
            class="my-4"
        >
            {{ message }}
        </div>
        <button
            v-show="fileUploadSuccess"
            class="bg-gray-900 hover:bg-gray-600 text-white font-bold p-3 rounded my-3 w-full"
            @click="createTerms()"
        >
            {{ $t('dashboard.importTerms') }}
        </button>
    </div>
</template>

<script>
import { createTerm } from '@/services/topic.service';

export default {
    name: 'TermImporter',
    props: {
        topics: {
            type: Array,
            default: () => [],
        },
    },
    data() {
        return {
            selectedTopic: {},
            jsonData: [],
            message: '',
            fileUploadSuccess: false,
        };
    },
    methods: {
        // Source: https://stackoverflow.com/questions/54617844/using-filereader-to-read-a-json-file
        onFileChange(e) {
            const files = e.target.files || e.dataTransfer.files;
            if (!files.length) return;
            this.readFile(files[0]);
        },
        readFile(file) {
            const reader = new FileReader();
            reader.readAsText(file);

            reader.onload = (e) => {
                this.jsonData = JSON.parse(e.target.result);
                this.message = this.$t('dashboard.foundTerms', { termCount: this.jsonData.length });
                this.fileUploadSuccess = true;
            };
        },

        createTerms() {
            const topicId = this.selectedTopic.id;
            if (topicId) {
                this.jsonData.every((term) => {
                    createTerm(topicId, { name: term }).then((res) => {
                        this.message = this.$t('dashboard.importedTerm', { termName: res.data.name });
                    }).catch((err) => {
                        this.$notify({
                            title: this.$t('dashboard.importSuccess'),
                            text: err.response.error,
                            type: 'error',
                        });
                    });
                    return true;
                });

                this.$notify({
                    title: this.$t('dashboard.importSuccess'),
                    type: 'success',
                });
                this.message = '';
                this.$refs.termsInput.value = null;
            }
        },

    },
};
</script>
