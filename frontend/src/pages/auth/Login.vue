<template>
    <div class="h-full flex flex-col">
        <div
            class="container max-w-sm mx-auto flex-1 flex flex-col items-center justify-center px-2"
        >
            <h1 class="mb-8 text-4xl md:text-5xl text-center">
                {{ $t('login.login') }}
            </h1>

            <form
                class="bg-gray-100 shadow-xl px-6 py-8 rounded text-black w-full"
                @submit.prevent="handleSubmit"
            >
                <div class="form-group">
                    <label
                        class="block text-gray-700 text-sm mb-2"
                        for="username"
                    >
                        {{ $t('signup.username') }}</label>
                    <input
                        v-model="user.username"
                        type="text"
                        class="block border border-grey-light w-full p-3 rounded mb-4"
                        name="username"
                        :placeholder="$t('signup.username')"
                    >
                </div>
                <div class="form-group">
                    <label
                        class="block text-gray-700 text-sm mb-2"
                        for="password"
                    >
                        {{ $t('signup.password') }}</label>
                    <input
                        v-model="user.password"
                        type="password"
                        class="block border border-grey-light w-full p-3 rounded mb-4"
                        name="password"
                        :placeholder="$t('signup.password')"
                    >
                </div>
                <div class="form-group">
                    <button
                        type="submit"
                        class="w-full text-center py-3 bg-gray-900 rounded text-white hover:bg-gray-700 my-2"
                    >
                        {{ $t('login.login') }}
                    </button>
                </div>
            </form>

            <div class="mt-6">
                {{ $t('login.noAccountYet') }}
                <router-link
                    class="underline"
                    :to="{ name: 'signup' }"
                >
                    {{ $t('signup.signup') }}
                </router-link>
            </div>
        </div>
    </div>
</template>
<script>
import * as AuthService from '@/services/auth.service';

export default {
    data() {
        return {
            user: {
                username: '',
                password: '',
            },
        };
    },
    methods: {
        handleSubmit() {
            AuthService.login(this.user).then(() => {
                this.$notify({
                    title: this.$t('login.messages.loginSuccess'),
                    type: 'success',
                });
                this.$router.push('/');
            }).catch(() => {
                this.$notify({
                    title: this.$t('login.errors.wrongCredentials'),
                    type: 'error',
                });
            });
        },
    },

};
</script>
