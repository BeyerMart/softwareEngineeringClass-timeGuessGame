<template>
    <button
        v-if="canCopy"
        class="flex items-center gap-3 bg-green-500 hover:bg-green-400 text-white p-2 px-3 rounded"
        @click="copy()"
    >
        <font-awesome-icon
            icon="share"
            class="text-l cursor-pointer"
        />
        {{ $t('copy2clipboard.copyButtonText') }}
    </button>
</template>

<script>
export default {
    name: 'Copy2Clipboard',
    data() {
        return {
            canCopy: false,
        };
    },
    created() {
        this.canCopy = !!navigator.clipboard;
    },
    methods: {
        copy() {
            console.log(window.location.href);
            navigator.clipboard.writeText(this.$route.page).then(() => {
                this.$notify({
                    title: this.$t('copy2clipboard.copySuccess'),
                    type: 'success',
                });
            }).catch(() => {
                this.$notify({
                    title: this.$t('errors.somethingWrong'),
                    text: this.$t('copy2clipboard.copyError'),
                    type: 'error',
                });
            });
        },
    },
};
</script>
