<template>
    <div class="bg-white">
        <div
            class="max-w-7xl mx-auto py-24 px-4 sm:px-6 lg:px-8"
        >
            <Message
                v-show="isWaitingForNextRound"
                title="Please wait"
                message="doing some stuff..."
            />
            <Message
                v-show="isPrepTime"
                title="Prepare yourself"
                :message="'Your term: ' + term + ' | activity: ' + activity"
            />
            <div
                v-show="isRollingDice"
                class="max-w-2xl mx-auto text-l shadow-xl text-center rounded bg-green-100"
            >
                <div class="text-xl p-7 inline-flex flex-col items-center">
                    <font-awesome-icon
                        icon="dice-d6"
                        class="waiting-icon text-5xl text-green-500 opacity-75 mb-2"
                    />
                    <span class="mt-2 text-gray-700">
                        {{ currentUser.username }} from {{ currentTeam.name }}
                        <p class="mt-2 italic">Please roll the dice</p>
                    </span>
                </div>
            </div>
            <div
                v-show="isValidating"
                class="max-w-2xl mx-auto text-l shadow-xl text-center rounded bg-green-100"
            >
                <div class="text-xl p-7 inline-flex flex-col items-center">
                    <font-awesome-icon
                        icon="dice-d6"
                        class="waiting-icon text-5xl text-green-500 opacity-75 mb-2"
                    />
                    <span class="mt-2 text-gray-700">
                        The other team is validating...
                        <p class="mt-2 italic">Please wait</p>
                    </span>
                </div>
            </div>
            <div
                class="sm:flex sm:flex-col sm:align-center py-y px-4 "
            >
                <pre>{{ status }} </pre>
                <h1 class="text-6xl font-extrabold text-gray-900 sm:text-center">
                    {{ getTimer }}
                </h1>
                <p class="mt-5 text-5xl text-gray-600 sm:text-center">
                    {{ term || 'AUGENLIED' }}
                </p>
                <p class="mt-5 text-xl text-gray-600 sm:text-center">
                    {{ round }} - {{ currentTeam.name }} - {{ currentUser.username }}
                </p>
                <p class="mt-5 text-xl text-gray-600 sm:text-center">
                    {{ activity }}
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
                        @click="confirmPointsHandler"
                    >
                        <font-awesome-icon
                            icon="check"
                            class="text-xl cursor-pointer"
                        />
                        Begriff erraten
                    </button>
                    <button
                        class="flex items-center gap-3 bg-red-500 hover:bg-gray-600 text-white p-2 rounded"
                        @click="rejectPointsHandlerHandler"
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
import Message from '@/components/page/Message.vue';

// TODO: isGuessingTeam computed
// TODO: v-show -> currentUser,

export default {

    name: 'Game',
    components: {
        VirtualTeam,
        Message,
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
            display: {
                waitingForPlayers: true,
            },
        };
    },
    computed: {
        ...mapGetters({
            getUsers: 'user/getUsers',
            getUser: 'user/getUser',
            topicList: 'topicList',
        }),
        showTerm() {
            return this.currentUser && this.currentTeam && (this.getUser.id === this.currentUser.id || this.getUser.id === this.currentUser.creator_id || !this.game.teams.find((team) => this.currentTeam.id === team.id).players.some((player) => player.id === this.currentUser.creator_id || player.id === this.currentUser.id));
        },
        getTimer() {
            const mins = Math.floor(this.timer.remainingTime / 60);
            const seconds = this.timer.remainingTime - (mins * 60);
            return `${mins < 10 ? `0${mins}` : mins}:${seconds < 10 ? `0${seconds}` : seconds}`;
        },
        waitForPlayers() {
            return this.game && this.game.teams && this.game.teams.filter((team) => team.players).length <= 1;
        },
        isWaitingForNextRound() {
            return this.status === 'WAIT_FOR_NEXT_ROUND';
        },
        isValidating() {
            return this.status === 'POINT_VALIDATION_START';
        },
        isPrepTime() {
            return this.status === 'PREPARATION_TIME';
        },
        isGuessing() {
            return this.status === 'GUESS';
        },
        isRollingDice() {
            return this.status === 'ROLL_DICE';
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
        subChannel(`/rooms/${this.$route.params.id}`, (message) => {
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
        subChannel(`/game/${this.gameId}`, (message) => {
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
                if (this.status !== 'WAIT_FOR_NEXT_ROUND') this.status = 'PREPARATION_TIME';
                this.setCountDown(message.data.pre_round_time);
                break;
            case 'GAMEPLAY_TIMER':
                if (this.status !== 'WAIT_FOR_NEXT_ROUND') this.status = 'GUESS';
                this.setCountDown(message.data.time);
                break;
            case 'GAME_OVER':
                // eslint-disable-next-line no-alert
                alert(`${message.data.winner.name} won!`);
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
                this.status = 'POINT_VALIDATION_START';
                break;
            case 'TEAM_POINTS_CHANGED':
                this.game.teams.map((team) => (message.data.id === team.id ? { ...team, ...message.data } : team));
                break;
            case 'VIRTUAL_USER_JOINED':
            case 'USER_JOINED_TEAM':
                this.game.teams.find((team) => team.id === message.data.team.id).players.push(message.data.user);
                break;
            case 'TEAM_DELETED':
                this.game.teams.filter((team) => team.id !== message.data.id);
                break;
            case 'USER_LEFT_TEAM':
                this.game.teams.find((team) => team.id === message.data.team.id).players.filter((user) => user.virtual_id || user.id !== message.data.user.id);
                break;
            case 'VIRTUAL_USER_LEFT':
                this.game.teams.find((team) => team.id === message.data.team.id).players.filter((user) => user.id || user.virtual_id !== message.data.user.virtual_id);
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
        rejectPointsHandlerHandler() { // TODO: Only during validation
            GameService.rejectPoints(this.gameId).then(() => {
                this.notifySuccess('Voted: Points rejected'); // TODO: Translate
            }).catch((error) => console.error(error));
        },
        confirmPointsHandler() { // TODO: Only during validation
            GameService.confirmPoints(this.gameId).then(() => {
                this.notifySuccess('Voted: Points confirmed'); // TODO: Translate
            }).catch((error) => console.error(error));
        },
        leaveGameHandler() {
            const myTeam = this.game.teams.find((team) => team.players.some((player) => player.id && player.id === this.getUser.id));
            this.game.teams.forEach((team) => {
                team.players.forEach((player) => {
                    if (player.creator_id && player.creator_id === this.getUser.id) {
                        TeamService.leaveRoom(myTeam.id, player);
                    }
                });
            });
            TeamService.leaveRoom(myTeam.id).then(() => {
                this.notifySuccess('successfully left Room'); // TODO: translate
            });
        },
        notifyError(message) {
            this.$notify({
                title: message,
                type: 'error',
            });
        },
        notifySuccess(message) {
            this.$notify({
                title: message,
                type: 'success',
            });
        },
    },
};
</script>
<style scoped>
.waiting-icon {
  animation: spin-die 2.5s infinite;
  display: inline-block;
}

@keyframes spin-die {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
