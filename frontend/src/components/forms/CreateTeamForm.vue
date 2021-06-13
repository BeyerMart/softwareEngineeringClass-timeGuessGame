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
                        {{ $t('room.createTeam') }}
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
                                >{{ $t('generic.name') }}</label>
                                <div
                                    v-show="submitted && $v.form.teamName.$error"
                                    class="has-errors py-1 text-xs"
                                >
                                    {{ $t('errors.validation.nameRequired') }}
                                </div>
                                <input
                                    id="termName"
                                    v-model="form.teamName"
                                    type="text"
                                    name="team-name"
                                    class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                                    :class="{ 'border-red-500 !important': submitted && $v.form.teamName.$error }"
                                    :placeholder="$t('generic.name')"
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
        team: {
            type: Object,
            default: () => {},
        },
        createdTeams: {
            type: Array,
            default: () => [],
        },
    },
    data() {
        return {
            form: {
                teamName: '',
            },
            submitted: false,
        };
    },
    validations: {
        form: {
            teamName: { required },
        },
    },

    methods: {
        handleSubmit() {
            this.submitted = true;
            this.$v.$touch();
            if (this.$v.$invalid) {
                return;
            }
            if (this.isTeamnameTaken(this.form.teamName)) {
                this.$notify({
                    title: this.$t(`${this.$t('game.messages.teamExists')}`),
                    type: 'error',
                });
                return;
            }

            this.$emit('createTeam', this.form.teamName);
            this.$emit('close');
        },
        isTeamnameTaken(teamname) {
            return this.createdTeams.some((team) => team.name === teamname);
        },
    },

};
</script>
