<template>
    <div>
        <Room
            v-if="!gameId"
            @joinedGame="joinedGameHandler"
        />
        <Game
            v-else
            :game-id="gameId"
            :spectator="spectator"
            @leftGame="leftGameHandler"
        />
    </div>
</template>

<script>
import Room from '@/pages/game/Room';
import Game from '@/pages/game/Game';
import * as RoomService from '@/services/room.service';

export default {
    name: 'RoomGame',
    components: { Game, Room },
    data() {
        return {
            gameId: null,
            roomId: this.$route.params.id,
            spectator: false,
        };
    },
    created() {
        window.addEventListener('beforeunload', this.leaveRoom);
    },
    beforeDestroy() {
        this.leaveRoom();
        window.removeEventListener('beforeunload', this.leaveRoom);
    },
    methods: {
        joinedGameHandler(gameId, spectator) {
            this.gameId = gameId;
            this.spectator = !!spectator;
        },
        leftGameHandler() {
            this.gameId = null;
        },
        leaveRoom() {
            RoomService.leaveRoom(this.roomId).catch((error) => console.error(error));
        },
    },
};
</script>

<style scoped>

</style>
