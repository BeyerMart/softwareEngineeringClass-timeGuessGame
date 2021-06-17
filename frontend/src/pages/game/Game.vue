<template>
    <div class="bg-white">
        <div
            class="max-w-7xl mx-auto py-24 px-4 sm:px-6 lg:px-8"
        >
            <Message
                v-show="isWaitingForNextRound"
                :title="$t('generic.pleaseWait')"
                :message="$t('game.messages.waitingForNextRound')"
                class="my-4"
            />
            <div
                v-show="isPrepTime"
                class="max-w-full mx-auto text-l shadow-xl text-center rounded bg-gray-800"
            >
                <div class="text-xl p-7 inline-flex flex-col items-center">
                    <font-awesome-icon
                        icon="dice-d6"
                        class="waiting-icon text-5xl text-green-400 opacity-75 mb-2"
                    />
                    <span class="mt-2 text-white">
                        {{ getTimer }}
                        <p class="mt-2 italic">
                            <span v-show="!showTerm">{{ term }} - {{ $t('game.activity') }}: {{ activity }}</span>
                            <span v-show="showTerm">{{ currentUser.username }} {{ $t('game.messages.lookUpTheTerm') }}</span>
                        </p>
                    </span>
                </div>
            </div>
            <div
                v-show="isRollingDice"
                class="max-w-full mx-auto text-l shadow-xl text-center rounded bg-gray-800"
            >
                <div class="text-xl p-7 inline-flex flex-col items-center">
                    <font-awesome-icon
                        icon="dice-d6"
                        class="waiting-icon text-5xl text-green-400 opacity-75 mb-2"
                    />
                    <span class="mt-2 text-white">
                        {{ currentUser.username }} {{ $t('generic.from') }} {{ currentTeam.name }}
                        <p class="mt-2 italic">{{ $t('game.rollTheDice') }}</p>
                    </span>
                </div>
            </div>
            <div
                v-show="isValidating"
                class="max-w-full mx-auto text-l shadow-xl text-center rounded bg-gray-800"
            >
                <div class="text-xl p-7 inline-flex flex-col items-center">
                    <font-awesome-icon
                        icon="dice-d6"
                        class="waiting-icon text-5xl text-green-400 opacity-75 mb-2"
                    />
                    <span class="mt-2 text-white">
                        <span v-show="!showTerm && !spectator">{{ $t('game.messages.validatePoints') }}</span>
                        <span v-show="showTerm || spectator">
                            {{ $t('game.messages.beingValidated') }}
                            <p class="mt-2 italic">{{ $t('generic.pleaseWait') }}</p>
                        </span>
                    </span>
                </div>
            </div>
            <div
                v-show="isGuessing"
                class="bg-gray-800  sm:flex sm:flex-col sm:align-center p-4 shadow-lg rounded my-5"
            >
                <p class="mt-5 text-xl text-gray-300 sm:text-center">
                    {{ $t('generic.round') }}: {{ round }}
                </p>
                <p class="text-6xl font-extrabold text-white sm:text-center mt-4">
                    {{ getTimer }}
                </p>
                <p
                    v-show="!showTerm"
                    class="mt-5 text-5xl text-gray-300 sm:text-center"
                >
                    {{ term }}
                </p>
                <p class="mt-5 text-xl text-gray-300 sm:text-center">
                    {{ currentTeam.name }} - {{ currentUser.username }}
                </p>
                <p class="mt-5 text-xl text-gray-300 sm:text-center">
                    {{ potentialPoints }} - {{ activity }}
                </p>
            </div>
            <div>
                <div
                    class="mx-auto py-2 px-4 max-w-7xl sm:p-0 mt-6"
                >
                    <div class="space-y-2">
                        <div class="space-y-2 sm:space-y-4 md:max-w-xl lg:max-w-3xl xl:max-w-none">
                            <h2 class="text-3xl font-bold tracking-tight sm:text-4xl">
                                {{ $t('game.teams') }}
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
                        class="flex items-center gap-3 bg-green-600 hover:bg-green-700 text-white p-2 rounded disabled:opacity-70"
                        :disabled="showTerm || timeOver || !isValidating || spectator"
                        @click="confirmPointsHandler"
                    >
                        <font-awesome-icon
                            icon="check"
                            class="text-xl cursor-pointer"
                        />
                        {{ $t('game.guessedTerm') }}
                    </button>
                    <button
                        class="flex items-center gap-3 bg-red-500 hover:bg-red-600 text-white p-2 rounded  disabled:opacity-70"
                        :disabled="showTerm || !timeOver || !isValidating || spectator"
                        @click="confirmPointsHandler"
                    >
                        <font-awesome-icon
                            icon="times-circle"
                            class="text-xl cursor-pointer"
                        />
                        {{ $t('game.notGuessedTerm') }}
                    </button>

                    <button
                        :disabled="showTerm || !isValidating || spectator"
                        class="flex items-center gap-3 bg-black hover:bg-gray-600 text-white p-2 rounded disabled:opacity-70"
                        @click="rejectPointsHandlerHandler"
                    >
                        <font-awesome-icon
                            icon="skull-crossbones"
                            class="text-xl cursor-pointer"
                        />
                        {{ $t('game.ruleViolation') }}
                    </button>

                    <button
                        class="flex items-center gap-3 bg-gray-900 hover:bg-gray-600 text-white p-2 rounded"
                        @click="leaveGameHandler()"
                    >
                        <font-awesome-icon
                            icon="sign-out-alt"
                            class="text-l cursor-pointer"
                        />
                        {{ $t('game.leaveGame') }}
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { subChannel, unsubChannel } from '@/services/websocket.service';
import { mapActions, mapGetters, mapMutations } from 'vuex';
import * as GameService from '@/services/game.service';
import * as RoomService from '@/services/room.service';
import * as TeamService from '@/services/team.service';
import VirtualTeam from '@/components/page/VirtualTeam';
import Message from '@/components/page/Message.vue';

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
        spectator: {
            type: Boolean,
            required: false,
            default: false,
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
            potentialPoints: 0,
            roundTime: 0,
            timer: {
                remainingTime: 0,
                nonce: 0,
            },
            display: {
                waitingForPlayers: true,
            },
            gameOver: false,
            timeOver: false,
        };
    },
    computed: {
        ...mapGetters({
            getUsers: 'user/getUsers',
            getUser: 'user/getUser',
            topicList: 'topicList',
        }),
        showTerm() {
            return !this.spectator && this.currentUser && this.currentTeam && this.game.teams.find((team) => this.currentTeam.id === team.id).players.some((player) => player.id === this.getUser.id);
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
        if (!this.spectator) window.addEventListener('beforeunload', this.leaveGame);
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
                this.gameOver = true;
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
                this.gameOver = true;
                // eslint-disable-next-line no-alert
                alert(`${message.data.winner.name} won!`);
                this.$emit('leftGame');
                break;
            case 'ROOM_DELETED':
                this.gameOver = true;
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
                this.timeOver = message.data.timeOver;
                this.status = 'POINT_VALIDATION_START';
                break;
            case 'TEAM_POINTS_CHANGED':
                this.game.teams.map((team) => {
                    if (message.data.id === team.id) {
                        /* eslint-disable-next-line no-param-reassign */
                        team.points = message.data.points;
                    }
                    return team;
                });
                break;
            case 'VIRTUAL_USER_JOINED':
            case 'USER_JOINED_TEAM':
                if (this.game.teams) {
                    const team = this.game.teams.find((loopTeam) => loopTeam.id === message.data.team.id);
                    if (!team.players.some((player) => (player.id && message.data.user.id && player.id === message.data.user.id) || (player.virtual_id && message.data.user.virtual_id && player.virtual_id === message.data.user.virtual_id))) team.players.push(message.data.user);
                }
                break;
            case 'TEAM_DELETED':
                this.game.teams = this.game.teams.filter((team) => team.id !== message.data.id);
                break;
            case 'USER_LEFT_TEAM':
                this.removeUser(this.game.teams.findIndex((team) => team.id === message.data.team.id), message.data.user);
                break;
            case 'VIRTUAL_USER_LEFT':
                this.removeVirtualUser(this.game.teams.findIndex((team) => team.id === message.data.team.id), message.data.user);
                break;
            default:
                break;
            }
        });
    },
    beforeDestroy() {
        unsubChannel(`/game/${this.gameId}`);
        unsubChannel(`/rooms/${this.room.id}`);
        if (!this.spectator) {
            this.leaveGame();
            window.removeEventListener('beforeunload', this.leaveGame);
        }
    },
    methods: {
        ...mapMutations({
            creatorOn: 'creatorOn',
        }),
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
        rejectPointsHandlerHandler() {
            GameService.rejectPoints(this.gameId).then(() => {
                this.notifySuccess(this.$t('game.message.resultConfirmed'));
            }).catch((error) => console.error(error));
        },
        confirmPointsHandler() {
            GameService.confirmPoints(this.gameId).then(() => {
                this.notifySuccess(this.$t('game.message.resultConfirmed'));
            }).catch((error) => console.error(error));
        },
        leaveGameHandler() {
            this.creatorOn();
            this.$emit('leftGame');
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
        removeVirtualUser(teamIndex, virtualUser) {
            const team = this.game.teams[teamIndex];
            if (team) team.players = team.players.filter((player) => player.id || player.virtual_id !== virtualUser.virtual_id);
        },
        removeUser(teamIndex, user) {
            const team = this.game.teams[teamIndex];
            if (team) team.players = team.players.filter((player) => player.creator_id !== user.id && player.id !== user.id);
        },
        leaveGame() {
            if (!this.gameOver) {
                const myTeam = this.game.teams.find((team) => team.players.some((player) => player.id && player.id === this.getUser.id));
                TeamService.leaveTeam(myTeam.id).then(() => {
                    this.notifySuccess(this.$t('game.message.youLeftGame'));
                });
            }
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
