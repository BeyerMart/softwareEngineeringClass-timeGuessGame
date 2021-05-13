<template>
    <h1>{{ gameId }}</h1>
</template>

<script>
import { unsubChannel } from '@/services/websocket.service';
import { mapActions, mapGetters } from 'vuex';
import * as GameService from '@/services/game.service';
import * as RoomService from '@/services/room.service';
import * as TeamService from '@/services/team.service';

export default {

    name: 'Game',
    props: {
        gameId: {
            type: Number,
            required: true,
        },
    },
    data() {
        return {
            game: {},
            room: {},
        };
    },
    computed: {
        ...mapGetters({
            getUsers: 'user/getUsers',
            getUser: 'user/getUser',
            topicList: 'topicList',
        }),
    },
    ...mapActions({
        fetchTopics: 'fetchTopics',
    }),
    mounted() {
        if (!this.topicList) this.fetchTopics();
        RoomService.fetchRoomById(this.$route.params.id).then((response) => {
            this.room = response.data;
        }).catch((error) => {
            console.error(error);
        });
        let tempGame = {};
        GameService.getGame(this.gameId).then((gameResponse) => {
            tempGame = gameResponse.data;
            GameService.getAllTeams(tempGame.id).then((allTeamsResponse) => {
                tempGame.teams = allTeamsResponse.data;
                tempGame.teams.map((team) => {
                    const loopTeam = team;
                    loopTeam.users = [];
                    return loopTeam;
                });
                this.game = tempGame;
                tempGame.teams.map((team) => TeamService.getTeamUsers(team.id).then((teamUsersResponse) => {
                    const loopTeam = team;
                    loopTeam.users = teamUsersResponse.data;
                    return loopTeam;
                }).catch((error) => {
                    console.error(error);
                }));
            }).catch((error) => {
                console.error(error);
            });
        }).catch((error) => {
            console.error(error);
        });
    },
    beforeDestroy() {
        unsubChannel(`game/${this.gameId}`);
    },
};
</script>
<style scoped>

</style>
