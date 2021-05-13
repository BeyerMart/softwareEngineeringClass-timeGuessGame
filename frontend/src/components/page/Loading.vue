<template>
    <div
        class="text-center"
    >
        <div class="waiting shadow rounded text-3xl p-7 inline-flex flex-col items-center">
            <font-awesome-icon
                icon="dice-d6"
                class="waiting-icon text-5xl text-green-500 opacity-75 mb-2"
            />
            <span class="mt-2">{{ message }}</span>
        </div>
    </div>
</template>
<script>
export default {
    name: 'Loading',
    props: {
        message: {
            required: true,
            default: 'Waiting',
        },
    },
};
</script>
<style scoped>
.waiting-icon {
  animation: spin-die 2.5s infinite;
  display: inline-block;
}

@keyframes spin-die {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
