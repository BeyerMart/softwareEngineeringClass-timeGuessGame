<template>
    <div>
        <section>
            <UserTable
                :users="users"
            />
        </section>
    </div>
</template>

<script>
import UserTable from '@/components/page/UserTable.vue';

export default {
    name: 'UserDashboard',
    components: {
        UserTable,
    },
    props: {
        users: {
            type: Array,
            default: () => {},
        },
    },
};
</script>
