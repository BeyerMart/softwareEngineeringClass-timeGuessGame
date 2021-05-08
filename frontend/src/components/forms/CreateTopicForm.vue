<template>
    <div
        class="fixed z-10 inset-0 modal h-full flex justify-center"
        aria-labelledby="modal-title"
        role="dialog"
        aria-modal="true"
    >
        <div class="container max-w-sm mx-auto flex items-center ">
            <div
                class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity"
                aria-hidden="true"
            />

            <div class="bg-white rounded-lg p-6 shadow-xl transform w-full">
                <font-awesome-icon
                    icon="times-circle"
                    class="text-2xl cursor-pointer block m-auto mr-0"
                    @click="$emit('close')"
                />
                <div class="mt-3 sm:mt-5">
                    <h3
                        id="modal-title"
                        class="text-lg leading-6 font-medium text-gray-900"
                    >
                        {{ $t('dashboard.createNewTopic') }}
                    </h3>
                    <div class="mt-2">
                        <form
                            class="mt-5 sm:mt-0"
                            @submit.prevent="handleSubmit"
                        >
                            <div class="form-group">
                                <label
                                    class="block text-gray-700 text-sm mb-2"
                                    for="topic"
                                >{{ $t('generic.name') }}</label>
                                <div
                                    v-show="submitted && $v.form.topic.$error"
                                    class="has-errors py-1 text-xs"
                                >
                                    {{ $t('errors.validation.nameRequired') }}
                                </div>
                                <input
                                    id="topicName"
                                    v-model="form.topic"
                                    type="text"
                                    name="topic-name"
                                    class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                                    :class="{ 'border-red-500 !important': submitted && $v.form.topic.$error }"
                                    :placeholder="$t('generic.name')"
                                >
                            </div>
                            <div class="form-group mt-5 sm:mt-6">
                                <button
                                    type="submit"
                                    class="w-full inline-flex justify-center rounded bg-gray-900 text-white hover:bg-gray-700 px-4 py-2 focus:ring-2 focus:ring-offset-2 focus:outline-none focus:ring-indigo-500"
                                >
                                    {{ $t('dashboard.createTopic') }}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import {
    required,
} from 'vuelidate/lib/validators';
import { createTopic } from '@/services/topic.service';

export default {
    data() {
        return {
            form: {
                topic: '',
            },
            submitted: false,
        };
    },
    validations: {
        form: {
            topic: { required },
        },
    },

    methods: {
        handleSubmit() {
            this.submitted = true;
            this.$v.$touch();
            if (this.$v.$invalid) {
                return;
            }

            createTopic(this.form.topic).then((res) => {
                this.$notify({
                    title: this.$t('dashboard.messages.topicCreateSuccess'),
                    text: this.$t('dashboard.messages.topicCreateSuccessMessage', { topicName: res.data.name }),
                    type: 'success',
                });
                this.$emit('close');
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
