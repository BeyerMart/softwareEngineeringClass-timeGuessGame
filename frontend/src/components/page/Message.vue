<template>
    <div class="max-w-2xl mx-auto text-l shadow-xl text-center rounded bg-green-100">
        <div class="text-xl p-7 inline-flex flex-col items-center">
            <font-awesome-icon
                icon="dice-d6"
                class="waiting-icon text-5xl text-green-500 opacity-75 mb-2"
            />
            <span class="mt-2 text-gray-700">
                {{ title }}
                <p class="mt-2 italic">{{ message }}</p>
            </span>
        </div>
    </div>
</template>
<script>
export default {
    name: 'Message',
    props: {
        title: {
            type: String,
            required: true,
        },
        message: {
            type: String,
            required: false,
            default: () => '',
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
