<template>
    <div class="flex flex-wrap justify-center items-center">
        <div class="w-28">
            <div class="shadow w-1/2 rounded border-2 border-gray-400 flex my-1 relative">
                <div
                    class="border-r-8 h-4 rounded-r absolute flex border-gray-400 ml-14 mt-1 z-10"
                />
                <div
                    :class="batteryColor"
                    class="cursor-default text-xs font-bold leading-none flex items-center justify-center m-1 py-2 text-center text-white"
                    :style="{ width: `${level}%`}"
                >
                    <div
                        v-show="level >= 5"
                        class="absolute left-0 mx-4 text-gray-700"
                    >
                        {{ level }}
                    </div>
                    <div
                        v-show="level < 5"
                        class="absolute left-0 mx-4 text-red-400"
                    >
                        <font-awesome-icon
                            icon="bolt"
                            class="ml-1 text-base"
                        />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        level: {
            type: Number,
            default: 3,
            required: 0,
        },
    },

    computed: {
        batteryColor() {
            if (this.level >= 50) return 'bg-green-400';
            if (this.level < 50 && this.level >= 25) return 'bg-yellow-400';
            if (this.level < 25 && this.level >= 10) return 'bg-yellow-500';
            if (this.level < 10 && this.level >= 5) return 'bg-red-500';
            return 'bg-gray-400';
        },
    },
};
</script>
