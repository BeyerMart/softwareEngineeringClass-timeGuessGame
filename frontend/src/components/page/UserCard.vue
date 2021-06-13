<template>
    <div class="rounded-lg bg-white overflow-hidden shadow">
        <div class="bg-gray-100 shadow-xl p-6 flex justify-center sm:justify-between">
            <div class="sm:flex sm:items-center sm:justify-between min-w-full">
                <div class="sm:flex sm:space-x-5">
                    <div class="flex-shrink-0">
                        <img
                            class="mx-auto h-20 w-20 rounded-full"
                            src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"
                            alt=""
                        >
                    </div>
                    <div
                        v-show="!isEditing"
                        class="mt-4 text-center sm:mt-0 sm:pt-1 sm:text-left"
                    >
                        <p class="text-xl font-bold text-gray-900 sm:text-2xl mt-2 flex items-center">
                            <span class="mr-2">{{ user.username }}</span>
                            <span
                                class="inline-block px-2 py-0.5 text-xs font-medium rounded-full text-white bg-green-500"
                            >{{ user.role }}</span>
                        </p>
                        <p class="text-sm font-medium text-gray-600">
                            {{ user.email }}
                        </p>
                    </div>

                    <form
                        v-show="isEditing"
                        class="mt-5 sm:mt-0 min-w-full"
                        @submit.prevent="handleSubmit"
                    >
                        <div class="form-group">
                            <label
                                class="block text-gray-700 text-sm mb-2"
                                for="username"
                            >{{ $t('signup.username') }}</label>
                            <div
                                v-show="submitted && $v.form.username.$error"
                                class="has-errors py-1 text-xs"
                            >
                                {{ $t('signup.errors.usernameRequired') }}
                            </div>
                            <input
                                id="username"
                                v-model="form.username"
                                type="text"
                                name="username"
                                class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                                :class="{ 'border-red-500': submitted && $v.form.username.$error }"
                                :placeholder="$t('signup.username')"
                            >
                        </div>
                        <div
                            v-show="isAdmin"
                            class="form-group"
                        >
                            <label
                                class="block text-gray-700 text-sm mb-2"
                                for="role"
                            >{{ $t('profile.role') }}</label>
                            <div
                                v-show="submitted && $v.form.role.$error"
                                class="has-errors py-1 text-xs"
                            >
                                {{ $t('errors.validation.roleRequired') }}
                            </div>
                            <multiselect
                                v-model="form.role"
                                select-label=""
                                deselect-label=""
                                :placeholder="$t('profile.selectRole')"
                                :options="roles"
                                :allow-empty="false"
                                class="mb-4"
                                :class="{ 'border rounded border-red-500': submitted && $v.form.role.$error }"
                            />
                        </div>
                        <div class="form-group">
                            <label
                                class="block text-gray-700 text-sm mb-2"
                                for="email"
                            >
                                {{ $t('signup.email') }}</label>
                            <div
                                v-show="submitted && $v.form.email.$error"
                                class="has-errors py-1 text-xs"
                            >
                                <span v-show="!$v.form.email.required">{{ $t('signup.errors.emailRequired') }}</span>
                                <span v-show="!$v.form.email.email">{{ $t('signup.errors.emailInvalid') }}</span>
                            </div>
                            <input
                                id="email"
                                v-model="form.email"
                                type="text"
                                name="email"
                                class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                                :class="{ 'border-red-500': submitted && $v.form.email.$error }"
                                :placeholder="$t('signup.email')"
                            >
                        </div>
                        <div class="form-group">
                            <button class="w-full text-center py-3 bg-gray-900 rounded text-white hover:bg-gray-700 my-2">
                                {{ $t('profile.updateUser') }}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            <font-awesome-icon
                v-if="isSelf"
                icon="ellipsis-v"
                class="text-2xl cursor-pointer"
                @click="editUser"
            />
        </div>
    </div>
</template>

<script>
import { updateUser } from '@/services/user.service';
import {
    required, email,
} from 'vuelidate/lib/validators';

export default {
    name: 'UserCard',
    props: {
        user: {
            type: Object,
            default: () => {},
        },
        isAdmin: {
            type: Boolean,
        },
        isSelf: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            roles: [
                'ROLE_ADMIN',
                'ROLE_MANAGER',
                'ROLE_USER',
            ],
            isEditing: false,
            submitted: false,
            form: {
                email: this.user.email,
                username: this.user.username,
                role: this.user.role,
            },
        };
    },
    watch: {
        user(newUser) {
            this.form.email = newUser.email;
            this.form.username = newUser.username;
            this.form.role = newUser.role;
        },
    },
    validations: {
        form: {
            username: { required },
            email: { required, email },
            role: { required },
        },
    },
    methods: {
        handleSubmit() {
            this.submitted = true;

            this.$v.$touch();
            if (this.$v.$invalid) {
                return;
            }
            const userData = {
                id: this.user.id,
                username: this.form.username,
                email: this.form.email,
                role: this.form.role,
            };
            updateUser(userData).then(() => {
                this.isEditing = false;
                if (this.isSelf) {
                    this.$notify({
                        title: this.$t('profile.userUpdatedSuccess'),
                        text: this.$t('login.loginAgain'),
                        type: 'success',
                    });
                    this.$router.push({ name: 'login' });
                } else {
                    this.$notify({
                        title: this.$t('profile.userUpdatedSuccess'),
                        type: 'success',
                    });
                    this.$emit('update-user');
                }
            }).catch((err) => {
                this.$notify({
                    title: this.$t('generic.error'),
                    text: err.response.error,
                    type: 'error',
                });
            });
        },
        editUser() {
            this.isEditing = !this.isEditing;
        },
    },
};
</script>
