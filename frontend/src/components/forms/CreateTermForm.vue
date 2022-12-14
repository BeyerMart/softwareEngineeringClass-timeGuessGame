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
                        {{ $t('dashboard.createTopic') }}
                    </h3>
                    <div class="mt-2">
                        <form
                            class="mt-5 sm:mt-0"
                            @submit.prevent="handleSubmit"
                        >
                            <div class="form-group">
                                <label
                                    class="block text-gray-700 text-sm mb-2"
                                    for="term"
                                >{{ $t('dashboard.term') }}</label>
                                <div
                                    v-show="submitted && $v.form.term.$error"
                                    class="has-errors py-1 text-xs"
                                >
                                    {{ $t('errors.validation.termNameRequired') }}
                                </div>
                                <input
                                    id="termName"
                                    v-model="form.term"
                                    type="text"
                                    name="term-name"
                                    class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                                    :class="{ 'border-red-500 !important': submitted && $v.form.term.$error }"
                                    :placeholder="$t('dashboard.term')"
                                >
                            </div>
                            <div class="form-group mt-5 sm:mt-6">
                                <button
                                    type="submit"
                                    class="w-full inline-flex justify-center rounded bg-gray-900 text-white hover:bg-gray-700 px-4 py-2 focus:ring-2 focus:ring-offset-2 focus:outline-none focus:ring-indigo-500"
                                >
                                    {{ $t('dashboard.createTerm') }}
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
import { createTerm } from '@/services/topic.service';

export default {
    name: 'CreateTermForm',
    props: {
        topic: {
            type: Object,
            default: () => {},
        },
    },
    data() {
        return {
            form: {
                term: '',
            },
            submitted: false,
        };
    },
    validations: {
        form: {
            term: { required },
        },
    },

    methods: {
        handleSubmit() {
            this.submitted = true;
            this.$v.$touch();
            if (this.$v.$invalid) {
                return;
            }

            if (this.topic.id) {
                console.log(this.form);
                createTerm(this.topic.id, this.form.term).then((res) => {
                    this.$notify({
                        title: this.$t('dashboard.messages.termCreateSuccess'),
                        text: this.$t('dashboard.messages.termCreateSuccessMessage', { termName: res.data.name }),
                        type: 'success',
                    });
                    this.$emit('close');
                    this.$emit('fetchTerms');
                }).catch((err) => {
                    this.$notify({
                        title: this.$t('generic.error'),
                        text: err.response.data.error,
                        type: 'error',
                    });
                });
            } else {
                this.$notify({
                    title: this.$t('generic.error'),
                    text: this.$t('dashboard.noTopicSelected'),
                    type: 'error',
                });
            }
        },
    },

};
</script>
