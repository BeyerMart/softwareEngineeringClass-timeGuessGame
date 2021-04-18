<template>
    <div class="h-full flex flex-col">
        <div
            class="container max-w-sm mx-auto flex-1 flex flex-col items-center justify-center px-2"
        >
            <h1 class="mb-8 text-4xl md:text-5xl text-center">
                {{ $t('signup.signup') }}
            </h1>

            <form
                class="bg-gray-100 shadow-xl px-6 py-8 rounded text-black w-full"
                @submit.prevent="handleSubmit"
            >
                <div class="form-group">
                    <label
                        class="block text-gray-700 text-sm mb-2"
                        for="email"
                    >
                        {{ $t('signup.email') }}</label>
                    <div
                        v-show="submitted && $v.user.email.$error"
                        class="has-errors py-1 text-xs"
                    >
                        <span v-show="!$v.user.email.required">{{ $t('signup.errors.emailRequired') }}</span>
                        <span v-show="!$v.user.email.email">{{ $t('signup.errors.emailInvalid') }}</span>
                    </div>
                    <input
                        id="email"
                        v-model="user.email"
                        type="email"
                        name="email"
                        class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                        :class="{ 'border-red-500': submitted && $v.user.email.$error }"
                        :placeholder="$t('signup.email')"
                    >
                </div>
                <div class="form-group">
                    <label
                        class="block text-gray-700 text-sm mb-2"
                        for="username"
                    >{{ $t('signup.username') }}</label>
                    <div
                        v-show="submitted && !$v.user.username.required"
                        class="has-errors py-1 text-xs"
                    >
                        {{ $t('signup.errors.usernameRequired') }}
                    </div>
                    <input
                        id="username"
                        v-model="user.username"
                        type="text"
                        name="username"
                        class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                        :class="{ 'border-red-500': submitted && $v.user.username.$error }"
                        :placeholder="$t('signup.username')"
                    >
                </div>
                <div class="form-group">
                    <label
                        class="block text-gray-700 text-sm mb-2"
                        for="password"
                    >{{ $t('signup.password') }}</label>
                    <div
                        v-show="submitted && $v.user.password.$error"
                        class="has-errors py-1 text-xs"
                    >
                        <span v-show="!$v.user.password.required">{{ $t('signup.errors.passwordRequired') }}</span>
                    </div>
                    <input
                        id="password"
                        v-model="user.password"
                        type="password"
                        name="password"
                        class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                        :class="{ 'border-red-500': submitted && $v.user.password.$error }"
                        :placeholder="$t('signup.password')"
                    >
                </div>
                <div class="form-group">
                    <label
                        class="block text-gray-700 text-sm mb-2"
                        for="confirmPassword"
                    >{{ $t('signup.confirmPassword') }}</label>
                    <div
                        v-show="submitted && $v.user.confirmPassword.$error"
                        class="has-errors py-1 text-xs"
                    >
                        <span v-if="!$v.user.confirmPassword.required">{{ $t('signup.errors.confirmPasswordRequired') }}</span>
                        <span v-else-if="!$v.user.confirmPassword.sameAsPassword">{{ $t('signup.errors.passwordsMatch') }}</span>
                    </div>
                    <input
                        id="confirmPassword"
                        v-model="user.confirmPassword"
                        type="password"
                        name="confirmPassword"
                        class="form-control block border border-grey-light w-full p-3 rounded mb-4"
                        :class="{ 'border-red-500': submitted && $v.user.confirmPassword.$error }"
                        :placeholder="$t('signup.confirmPassword')"
                    >
                </div>
                <div class="form-group">
                    <button class="w-full text-center py-3 bg-gray-900 rounded text-white hover:bg-gray-700 my-2">
                        {{ $t('signup.createAccount') }}
                    </button>
                </div>
            </form>

            <div class="mt-6">
                {{ $t('signup.accountExists') }}
                <router-link
                    class="underline"
                    :to="{ name: 'login' }"
                >
                    {{ $t('login.login') }}
                </router-link>
            </div>
        </div>
    </div>
</template>

<script>
import {
    required, email, sameAs,
} from 'vuelidate/lib/validators';

import axios from 'axios';

export default {
    name: 'Signup',
    data() {
        return {
            user: {
                email: '',
                username: '',
                password: '',
                confirmPassword: '',
            },
            submitted: false,
        };
    },
    validations: {
        user: {
            email: { required, email },
            username: { required },
            password: { required },
            confirmPassword: {
                required,
                sameAsPassword: sameAs('password'),
            },
        },
    },
    methods: {
        handleSubmit() {
            this.submitted = true;

            this.$v.$touch();
            if (this.$v.$invalid) {
                return;
            }
            axios.post('/api/auth/signup', this.user)
                .then(() => {
                    this.$notify({
                        title: this.$t('signup.messages.signupSuccess'),
                        type: 'success',
                    });
                    this.$router.push('/login');
                })
                .catch((err) => {
                    this.$notify({
                        title: this.$t('generic.error'),
                        text: err.response.error,
                        type: 'error',
                    });
                });
        },
    },
};
</script>
