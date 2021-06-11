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
                        {{ $t('room.createVirtualUser') }}
                    </h3>
                    <div class="mt-2">
                        <form
                            class="mt-5 sm:mt-0"
                            @submit.prevent="handleSubmit"
                        >
                            <div class="form-group">
                                <label
                                    class="block text-gray-700 text-sm mb-2"
                                    for="termName"
                                >{{ $t('signup.username') }}</label>
                                <div
                                    v-show="submitted && $v.form.vUsername.$error"
                                    class="has-errors py-1 text-xs"
                                >
                                    {{ $t('signup.errors.usernameRequired') }}
                                </div>
                                <input
                                    id="termName"
                                    v-model="form.vUsername"
                                    type="text"
                                    name="team-name"
                                    class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                                    :class="{ 'border-red-500 !important': submitted && $v.form.vUsername.$error }"
                                    :placeholder="$t('signup.username')"
                                >
                            </div>
                            <div class="form-group mt-5 sm:mt-6">
                                <button
                                    type="submit"
                                    class="w-full inline-flex justify-center rounded bg-gray-900 text-white hover:bg-gray-700 px-4 py-2 focus:ring-2 focus:ring-offset-2 focus:outline-none focus:ring-indigo-500"
                                >
                                    {{ $t('generic.create') }}
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

export default {
    name: 'CreateTeamForm',
    props: {
        vUser: {
            type: Object,
            default: () => {},
        },
        users: {
            type: Array,
            default: () => [],
        },
    },
    data() {
        return {
            form: {
                vUsername: '',
            },
            submitted: false,
        };
    },
    validations: {
        form: {
            vUsername: { required },
        },
    },

    methods: {
        handleSubmit() {
            this.submitted = true;
            this.$v.$touch();
            if (this.$v.$invalid) {
                return;
            }

            if (!this.isUsernameTaken(this.form.vUsername)) {
                this.$emit('addVirtualUser', this.form.vUsername);
                this.$emit('close');
            } else {
                this.$notify({
                    title: this.$t('game.messages.userExists', { username: this.form.vUsername }),
                    type: 'error',
                });
            }
        },
        isUsernameTaken(username) {
            return this.users.some((user) => user.username === username);
        },
    },

};
</script>
