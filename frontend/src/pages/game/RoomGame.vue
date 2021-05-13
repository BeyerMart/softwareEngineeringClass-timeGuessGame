<template>
    <div>
        <Room
            v-if="!gameId"
            @joinedGame="joinedGameHandler"
        />
        <Game
            v-else
            :game-id="gameId"
        />
    </div>
</template>

<script>
import Room from '@/pages/game/Room';
import Game from '@/pages/game/Game';

export default {
    name: 'RoomGame',
    components: { Game, Room },
    data() {
        return {
            gameId: null,
        };
    },
    methods: {
        joinedGameHandler(gameId) {
            this.gameId = gameId;
        },
    },
};
</script>

<style scoped>

</style>
