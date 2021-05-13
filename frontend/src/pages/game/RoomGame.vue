<template>
    <div>
        <Room
            v-if="!gameId"
            @joinedGame="joinedGameHandler"
        />
        <Game
            v-else
            :game-id="gameId"
            @leftGame="leftGameHandler"
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
        leftGameHandler() {
            this.gameId = null;
        },
    },
};
</script>

<style scoped>

</style>
