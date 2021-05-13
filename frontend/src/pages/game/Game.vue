<template>
    <div class="bg-white">
        <Loading
            message="Warten auf andere Spieler"
        />
        <div
            class="max-w-7xl mx-auto py-24 px-4 sm:px-6 lg:px-8"
        >
            <div class="sm:flex sm:flex-col sm:align-center py-y px-4 ">
                <h1 class="text-6xl font-extrabold text-gray-900 sm:text-center">
                    {{ getTimer }}
                </h1>
                <p class="mt-5 text-5xl text-gray-600 sm:text-center">
                    Augenlied
                </p>
                <p class="mt-5 text-xl text-gray-600 sm:text-center">
                    Runde 3 - TEAM SFS - hansp
                </p>
                <p class="mt-5 text-xl text-gray-600 sm:text-center">
                    2 Punkte - Pantomime
                </p>
            </div>
            <div>
                <div
                    class="mx-auto py-2 px-4 max-w-7xl sm:p-0"
                >
                    <div class="space-y-2">
                        <div class="space-y-2 sm:space-y-4 md:max-w-xl lg:max-w-3xl xl:max-w-none">
                            <h2 class="text-3xl font-bold tracking-tight sm:text-4xl">
                                Teams
                            </h2>
                        </div>
                        <ul
                            class="space-y-4 sm:grid sm:grid-cols-2 sm:gap-6 sm:space-y-0 lg:grid-cols-4 lg:gap-8"
                        >
                            <li
                                v-for="team in game.teams"
                                :key="team.name"
                                class="py-10 px-6 bg-gray-800 text-center rounded-lg xl:px-10 xl:text-left"
                            >
                                <VirtualTeam
                                    :team="team"
                                    :host-id="room.host_id"
                                    :is-gameplay="true"
                                />
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="my-6 sm:my-10 text-center">
                <div class="inline-flex flex-col justify-center gap-3 md:flex-row text-base shadow p-5 bg-gray-100">
                    <button
                        class="flex items-center gap-3 bg-green-600 hover:bg-green-700 text-white p-2 rounded"
                    >
                        <font-awesome-icon
                            icon="check"
                            class="text-xl cursor-pointer"
                        />
                        Begriff erraten
                    </button>
                    <button
                        class="flex items-center gap-3 bg-red-500 hover:bg-gray-600 text-white p-2 rounded"
                    >
                        <font-awesome-icon
                            icon="times-circle"
                            class="text-xl cursor-pointer"
                        />
                        Begriff nicht erraten
                    </button>

                    <button
                        class="flex items-center gap-3 bg-black hover:bg-gray-600 text-white p-2 rounded"
                    >
                        <font-awesome-icon
                            icon="skull-crossbones"
                            class="text-xl cursor-pointer"
                        />
                        Regelverstoß
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { subChannel, unsubChannel } from '@/services/websocket.service';
import { mapActions, mapGetters } from 'vuex';
import * as GameService from '@/services/game.service';
import * as RoomService from '@/services/room.service';
import * as TeamService from '@/services/team.service';
import VirtualTeam from '@/components/page/VirtualTeam';

export default {

    name: 'Game',
    components: {
        VirtualTeam,
    },
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
                    loopTeam.players = [];
                    return loopTeam;
                });
                this.game = tempGame;
                tempGame.teams.map((team) => TeamService.getTeamUsers(team.id).then((teamUsersResponse) => {
                    const loopTeam = team;
                    loopTeam.players = teamUsersResponse.data;
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
