<template>
    <h1>{{ gameId }}</h1>
</template>

<script>
export default {
    name: 'Game',
    props: {
        gameId: {
            type: Number,
            required: true,
        },
    },
};
</script>

<style scoped>

</style>
