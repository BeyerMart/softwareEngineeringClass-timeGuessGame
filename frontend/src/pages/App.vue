<template>
    <div>
        <RoomSelection />
    </div>
</template>

<script>

import RoomSelection from '@/pages/game/RoomSelection';

export default {
    name: 'App',
    components: {
        RoomSelection,
    },
};
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
