<template>
    <!-- Source: https://school.geekwall.in/p/egCtppvk -->
    <div class="flex items-center flex-col">
        <div class="legend flex gap-2 mb-4">
            <div class="flex items-center gap-1">
                <span
                    class="inline-block w-4 h-2"
                    :style="{ 'background-color': pieData[0].color }"
                />
                <span>Wins</span>
            </div>
            <div class="flex items-center gap-1">
                <span
                    class="inline-block w-4 h-2"
                    :style="{ 'background-color': pieData[1].color }"
                />
                <span>Losses</span>
            </div>
        </div>
        <div
            class="rounded-full w-32 h-32"
            :style="pieStyles"
        />
    </div>
</template>

<script>
export default {
    props: {
        pieData: {
            type: Array,
            require: true,
            default: () => [],
        },
    },
    computed: {
        pieStyles() {
            let acum = 0;
            /* eslint-disable no-return-assign */
            const styles = this.pieData.map(
                (segment) => `${segment.color} 0 ${(acum += segment.value)}%`,
            );

            return {
                background: `conic-gradient( ${styles.join(',')} )`,
            };
        },
    },
};
</script>
