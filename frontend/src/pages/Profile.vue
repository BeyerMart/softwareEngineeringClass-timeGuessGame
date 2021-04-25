<template>
    <div>
        <div class="my-8">
            <div
                class="max-w-3xl mx-auto px-4 sm:px-6 lg:max-w-7xl lg:px-8"
            >
                <h1 class="mb-8 text-4xl md:text-5xl">
                    {{ $t('generic.profile') }}
                </h1>
                <div class="grid grid-cols-1 gap-4 items-start lg:grid-cols-3 lg:gap-8">
                    <div class="grid grid-cols-1 gap-4 lg:col-span-2">
                        <!-- USER CARD -->
                        <section
                            aria-labelledby="user-card"
                            class="mb-5"
                        >
                            <UserCard
                                :user="user"
                                :is-admin="isAdmin"
                                :is-self="isSelf"
                                @update-user="getUser"
                            />
                        </section>
                        <section aria-labelledby="games-table">
                            <div class="flex flex-col">
                                <div class="-my-2 overflow-x-auto sm:-mx-6 lg:-mx-8">
                                    <div class="py-2 align-middle inline-block min-w-full sm:px-6 lg:px-8">
                                        <div class="shadow overflow-hidden border-b border-gray-200 sm:rounded-lg">
                                            <table class="min-w-full divide-y divide-gray-200">
                                                <thead class="bg-gray-50">
                                                    <tr>
                                                        <th
                                                            scope="col"
                                                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                        >
                                                            {{ $t('game.teamName') }}
                                                        </th>
                                                        <th
                                                            scope="col"
                                                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                        >
                                                            {{ $t('game.enemy') }}
                                                        </th>
                                                        <th
                                                            scope="col"
                                                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                        >
                                                            {{ $t('game.points') }}
                                                        </th>
                                                        <th
                                                            scope="col"
                                                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                                                        >
                                                            {{ $t('game.topic') }}
                                                        </th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr class="cursor-pointer">
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                            Ball of Duty
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            Goal Digger
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            15-7
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            Biologie
                                                        </td>
                                                    </tr>
                                                    <tr class="bg-gray-50 cursor-pointer">
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                                            Win or Booze
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            Goal Digger
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            7-12
                                                        </td>
                                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                            Geschichte
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                    <div class="grid grid-cols-1 gap-4">
                        <section>
                            <div class="rounded-lg bg-gray-100 overflow-hidden shadow">
                                <div class="p-6">
                                    <h2
                                        class="text-base font-medium text-gray-900"
                                    >
                                        {{ $t('profile.lastPlayedWith') }}
                                    </h2>
                                    <div class="flow-root mt-6">
                                        <ul class="-my-5 divide-y divide-gray-200">
                                            <li class="py-5">
                                                <div class="relative focus-within:ring-2 focus-within:ring-cyan-500">
                                                    <h3 class="text-sm font-semibold text-gray-800">
                                                        <a
                                                            href="#"
                                                            class="hover:underline focus:outline-none"
                                                        >
                                                            <span
                                                                class="absolute inset-0"
                                                                aria-hidden="true"
                                                            />
                                                            Hansp
                                                        </a>
                                                    </h3>
                                                </div>
                                            </li>
                                            <li class="py-5">
                                                <div class="relative focus-within:ring-2 focus-within:ring-cyan-500">
                                                    <h3 class="text-sm font-semibold text-gray-800">
                                                        <a
                                                            href="#"
                                                            class="hover:underline focus:outline-none"
                                                        >
                                                            <span
                                                                class="absolute inset-0"
                                                                aria-hidden="true"
                                                            />
                                                            jamesJB
                                                        </a>
                                                    </h3>
                                                </div>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import UserCard from '@/components/page/UserCard.vue';
import * as AuthService from '@/services/auth.service';
import { getUserById } from '@/services/user.service';

export default {
    name: 'Profile',
    components: {
        UserCard,
    },
    data() {
        return {
            user: {},
            isSelf: false,
            currrentUser: {},
        };
    },
    computed: {
        isAdmin() {
            return this.currrentUser.role === 'ROLE_ADMIN';
        },
    },
    mounted() {
        this.getUser();
    },
    methods: {
        getUser() {
            if (this.$route.params.id) {
                getUserById(this.$route.params.id).then((res) => {
                    this.user = res.data;
                }).catch((err) => {
                    this.$notify({
                        title: this.$t('generic.error'),
                        text: err.response.data.error,
                        type: 'error',
                    });
                    if (err.response.data.status === 404) {
                        this.$router.push('/404');
                    } else {
                        this.$router.push('/500');
                    }
                });
            } else {
                this.isSelf = true;
            }
            this.getCurrentUser();
        },

        getCurrentUser() {
            AuthService.getCurrentUser().then((res) => {
                this.currrentUser = res.data;
                if (this.isSelf) {
                    this.user = this.currrentUser;
                }
            }).catch((err) => {
                this.$notify({
                    title: this.$t('generic.error'),
                    text: err.response.data.error,
                    type: 'error',
                });
                if (err.response.data.status === 404) {
                    this.$router.push('/404');
                } else {
                    this.$router.push('/500');
                }
            });
        },
    },

};
</script>