<template>
    <div>
        <h1>{{ gameId }}</h1>
        <span>Timer: {{ getTimer }}</span>
    </div>
</template>

<script>
import { subChannel, unsubChannel } from '@/services/websocket.service';
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
            status: 'WAIT_FOR_NEXT_ROUND',
            round: 0,
            currentTeam: '',
            currentUser: '',
            term: '',
            activity: '',
            potentialPoints: '',
            roundTime: 0,
            timer: {
                remainingTime: 0,
                nonce: 0,
            },
        };
    },
    computed: {
        ...mapGetters({
            getUsers: 'user/getUsers',
            getUser: 'user/getUser',
            topicList: 'topicList',
        }),
        getTimer() {
            const mins = Math.floor(this.timer.remainingTime / 60);
            const seconds = this.timer.remainingTime - (mins * 60);
            return `${mins < 10 ? `0${mins}` : mins}:${seconds < 10 ? `0${seconds}` : seconds}`;
        },
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
        subChannel(`rooms/${this.$route.params.id}`, (message) => {
            console.log('You got a room message:');
            console.log(message);
            switch (message.type) {
            case 'ROOM_CHANGED':
                this.room = (message.data);
                break;
            case 'ROOM_DELETED':
                this.$router.push('/');
                break;
            default:
                break;
            }
        });
        subChannel(`game/${this.gameId}`, (message) => {
            console.log('You got a games message:');
            console.log(message);
            switch (message.type) {
            case 'GAME_DELETED':
                this.$emit('leftGame');
                break;
            case 'ROLL_DICE':
                this.status = 'ROLL_DICE';
                break;
            case 'GAMEPLAY_ROUND_START':
                this.round = message.data.round;
                break;
            case 'GAMEPLAY_PRE_ROUND_TIMER':
                this.status = 'PREPARATION_TIME';
                console.log(message.data.message.pre_round_time);
                // TODO: start timer
                break;
            case 'GAMEPLAY_TIMER':
                this.status = 'GUESS';
                console.log(message.data.time);
                // TODO: start timer
                break;
            case 'GAME_OVER':
                break;
            case 'ROOM_DELETED':
                this.$router.push('/');
                break;
            case 'GAMEPLAY_CUBE_INFORMATION':
                this.potentialPoints = message.data.points;
                this.time = message.data.time;
                this.term = message.data.term;
                this.activity = message.data.activity;
                break;
            case 'GAMEPLAY_CURRENT_TEAMUSER':
                this.currentUser = message.data.user;
                this.currentTeam = message.data.team;
                break;
            case 'POINT_VALIDATION_STOP':
                this.status = 'WAIT_FOR_NEXT_ROUND';
                break;
            case 'POINT_VALIDATION_START':
                this.status = 'validation';
                break;
            default:
                break;
            }
        });
    },
    beforeDestroy() {
        unsubChannel(`game/${this.gameId}`);
        unsubChannel(`rooms/${this.$route.params.id}`);
    },
    methods: {
        setCountDown(duration) {
            this.timer.remainingTime = duration;
            this.timer.nonce = Math.random();
            this.countDownLoop(this.timer.nonce);
        },
        /**
         * Actively reduces timer time
         * @param nonce used to exit if multiple reductions are running
         */
        countDownLoop(nonce) {
            setTimeout(() => {
                if (nonce === this.timer.nonce) {
                    if (this.timer.remainingTime > 0) {
                        this.timer.remainingTime--;
                        this.countDownLoop(nonce);
                    }
                }
            }, 1000);
        },
    },
};
</script>
<style scoped>

</style>
