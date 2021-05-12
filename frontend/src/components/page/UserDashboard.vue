<template>
    <div>
        <section>
            <table class="min-w-full divide-y divide-gray-200 bg-white w-full mb-6 shadow rounded">
                <thead class="bg-gray-50">
                    <tr>
                        <th
                            v-for="column in columns"
                            :key="column"
                            class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                        >
                            {{ column }}
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr
                        v-for="(user, index) in users"
                        :key="user.id"
                        class="cursor-pointer hover:bg-gray-200"
                        :class="{ 'bg-gray-50': index % 2 !== 0}"
                        @click="gotoUserProfile(user)"
                    >
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {{ user.id }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {{ user.username }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {{ user.email }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {{ user.role }}
                        </td>
                        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                            {{ registrationDate(user.created_at) }}
                        </td>
                        <td
                            v-show="isUserLoggedIn && isAdmin"
                            class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"
                        >
                            <font-awesome-icon
                                icon="trash"
                                class="text-l cursor-pointer"
                                @click.stop="deleteUser(user)"
                            />
                        </td>
                    </tr>
                </tbody>
            </table>
        </section>
    </div>
</template>

<script>
import { deleteUser } from '@/services/user.service';

export default {
    name: 'UserDashboard',
    props: {
        users: {
            type: Array,
            default: () => {},
        },
    },
    data() {
        return {
            columns: [
                'ID',
                this.$t('signup.username'),
                this.$t('signup.email'),
                this.$t('generic.role'),
                this.$t('profile.registrationDate')],
        };
    },
    computed: {
        isUserLoggedIn() {
            return this.$store.getters['user/isLoggedIn'];
        },
        isAdmin() {
            return this.$store.getters['user/isAdmin'];
        },
    },
    watch: {
        users() {},
    },
    mounted() {
        if (this.isUserLoggedIn && this.isAdmin) {
            this.columns.push(this.$t('dashboard.options'));
        }
    },
    methods: {
        deletGame(user) {
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
                            deleteUser(user.id).then(() => {
                                this.$notify({
                                    title: this.$t('dashboard.messages.userDeleteSuccess'),
                                    text: this.$t('dashboard.messages.userDeleteSuccessMessage', { userName: user.username }),
                                    type: 'success',
                                });
                                this.$emit('fetchUsers');
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
        registrationDate(timestamp) {
            return new Date(timestamp).toLocaleDateString();
        },
        gotoUserProfile(user) {
            this.$router.push({ path: `/profile/${user.id}` });
        },
    },
};
</script>
