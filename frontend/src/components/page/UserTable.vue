<template>
    <div>
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
                    :class="{ 'bg-gray-50': index % 2 === 0}"
                >
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ user.id }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ user.username }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ user.role }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ user.email }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        {{ registrationDate(user.created_at) }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                        <font-awesome-icon
                            icon="trash"
                            class="text-l cursor-pointer"
                            @click.stop="deleteuser(user.topic_id, user.id)"
                        />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>

export default {
    name: 'UserTable',
    props: {
        users: {
            type: Array,
            default: () => {},
        },
    },
    data() {
        return {
            columns: ['ID', this.$t('signup.username'), this.$t('generic.role'), this.$t('signup.email'), this.$t('profile.registrationDate'), this.$t('dashboard.options')],
        };
    },
    methods: {
        registrationDate(timestamp) {
            return new Date(timestamp).toLocaleDateString();
        },
    },
};
</script>
