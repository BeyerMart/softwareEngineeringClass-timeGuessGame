<template>
    <div class="rounded-lg bg-white overflow-hidden shadow">
        <div class="bg-gray-100 shadow-xl p-6">
            <div class="sm:flex sm:items-center sm:justify-between">
                <div class="sm:flex sm:space-x-5">
                    <div class="flex-shrink-0">
                        <img
                            class="mx-auto h-20 w-20 rounded-full"
                            src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80"
                            alt=""
                        >
                    </div>
                    <div class="mt-4 text-center sm:mt-0 sm:pt-1 sm:text-left">
                        <p class="text-xl font-bold text-gray-900 sm:text-2xl mt-2">
                            {{ user.username }}
                        </p>
                        <p class="text-sm font-medium text-gray-600">
                            {{ user.email }}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'UserCard',
    props: {
        user: Object,
    },
};
</script>
