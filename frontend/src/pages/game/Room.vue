<template>
    <div>
        <div
            v-if="room"
            class="my-8"
        >
            <div
                class="max-w-3xl mx-auto px-4 sm:px-6 lg:max-w-7xl lg:px-8"
            >
                <h1 class="text-4xl md:text-5xl my-5">
                    {{ room.name }} | {{ getTopicNameById(room.topic_id) }}
                </h1>
                <div v-if="room">
                    <div
                        v-show="isHost"
                        class="host-options my-2"
                    >
                        <div
                            v-show="connectedPi"
                            class="shadow inline-block p-4 bg-green-400 rounded"
                        >
                            Mit {{ connectedPi }} verbunden!
                        </div>
                        <div
                            v-show="!connectedPi"
                            class="w-full md:w-1/3"
                        >
                            <label class="block text-gray-700 text-sm mb-2">Wähle den PI aus</label>
                            <div class="flex gap-2">
                                <multiselect
                                    v-model="selectedPi"
                                    :options="piNames"
                                    :searchable="false"
                                    :close-on-select="true"
                                    :show-labels="false"
                                    placeholder="PI auswählen"
                                    class="inline-block"
                                    @select="connectPi"
                                />
                                <button
                                    class="flex items-center gap-3 bg-gray-900 hover:bg-gray-600 text-white p-2 rounded"
                                    @click="refreshPis"
                                >
                                    <font-awesome-icon
                                        icon="sync"
                                        class="text-l cursor-pointer"
                                    />
                                </button>
                            </div>
                        </div>
                    </div>
                    <div>
                        <h3
                            v-show="waitForPlayers"
                            class="font-bold tracking-tight sm:text-2xl my-6"
                        >
                            {{ $t('room.playersNeedToJoin') }}
                        </h3>
                        <ul
                            class="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4 my-5"
                        >
                            <li
                                v-for="(player, index) in teamlessPlayers"
                                :key="index"
                                class="col-span-1 bg-white rounded-lg shadow divide-y divide-gray-200"
                            >
                                <Player
                                    :player="player"
                                    :badges="room.host_id && player.id === room.host_id ? [{text: 'Host', colour: 'green'}] : null"
                                />
                            </li>
                        </ul>
                    </div>
                    <div
                        class="mx-auto py-y px-4 max-w-7xl sm:p-0"
                    >
                        <div class="space-y-12">
                            <div class="space-y-5 sm:space-y-4 md:max-w-xl lg:max-w-3xl xl:max-w-none">
                                <h2 class="text-3xl font-extrabold tracking-tight sm:text-4xl">
                                    Teams
                                </h2>
                            </div>
                            <ul
                                class="space-y-4 sm:grid sm:grid-cols-2 sm:gap-6 sm:space-y-0 lg:grid-cols-3 lg:gap-8"
                            >
                                <li
                                    v-for="team in teams"
                                    :key="team.name"
                                    class="py-10 px-6 bg-gray-800 text-center rounded-lg xl:px-10 xl:text-left"
                                >
                                    <VirtualTeam
                                        :team="team"
                                        :host-id="room.host_id"
                                        @joinTeam="joinTeam"
                                    />
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="my-6 sm:my-10">
                    <div class="flex flex-col justify-center gap-3 md:flex-row text-base shadow p-5 bg-gray-100">
                        <button
                            class="flex items-center gap-3 bg-gray-900 hover:bg-gray-600 text-white p-2 rounded"
                            @click="display.showTeamForm = true"
                        >
                            <font-awesome-icon
                                icon="plus"
                                class="text-l cursor-pointer"
                            />
                            {{ $t('room.createTeam') }}
                        </button>
                        <button
                            class="flex items-center gap-3 bg-gray-900 hover:bg-gray-600 text-white p-2 rounded"
                            @click="display.showVUserForm = true"
                        >
                            <font-awesome-icon
                                icon="sign-out-alt"
                                class="text-l cursor-pointer"
                            />
                            {{ $t('room.createVirtualUser') }}
                        </button>

                        <CreateVirtualUser
                            v-show="display.showVUserForm"
                            @close="display.showVUserForm = false"
                            @addVirtualUser="addVirtualUser"
                        />

                        <button
                            class="flex items-center gap-3 bg-gray-900 hover:bg-gray-600 text-white p-2 rounded"
                            @click="leaveRoom()"
                        >
                            <font-awesome-icon
                                icon="sign-out-alt"
                                class="text-l cursor-pointer"
                            />
                            {{ $t('room.leaveRoom') }}
                        </button>

                        <button
                            v-if="isHost && !gameIsStarted && room.cube"
                            class="flex items-center gap-3 bg-green-600 hover:bg-gray-600 text-white p-2 rounded"
                            @click="createGame"
                        >
                            <font-awesome-icon
                                icon="play-circle"
                                class="text-l cursor-pointer"
                            />
                            {{ $t('room.startGame') }}
                        </button>

                        <button
                            v-if="gameIsStarted && room.cube"
                            class="flex items-center gap-3 bg-green-600 hover:bg-gray-600 text-white p-2 rounded"
                            @click="joinGame"
                        >
                            <font-awesome-icon
                                icon="play-circle"
                                class="text-l cursor-pointer"
                            />
                            {{ $t('room.joinGame') }}
                        </button>

                        <CreateTeamForm
                            v-show="display.showTeamForm"
                            @close="display.showTeamForm = false"
                            @createTeam="createTeam"
                        />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { subChannel, unsubChannel } from '@/services/websocket.service';
import * as RoomService from '@/services/room.service';
import * as TopicService from '@/services/topic.service';
import * as CubeService from '@/services/cube.service';
import * as GameService from '@/services/game.service';
import * as TeamService from '@/services/team.service';
import VirtualTeam from '@/components/page/VirtualTeam';
import Player from '@/components/page/Player';
import CreateTeamForm from '@/components/forms/CreateTeamForm.vue';
import CreateVirtualUser from '@/components/forms/CreateVirtualUserForm.vue';

export default {

    name: 'Room',
    components: {
        Player, VirtualTeam, CreateTeamForm, CreateVirtualUser,
    },
    data() {
        return {
            id: this.$route.params.id,
            room: undefined,
            topic: '',
            players: [],
            teams: [],
            piNames: [],
            game: {},
            gameTeams: [],
            display: {
                showTeamForm: false,
                showVUserForm: false,
            },
            selectedPi: '',
        };
    },
    computed: {
        ...mapGetters({
            getUsers: 'user/getUsers',
            getUser: 'user/getUser',
            topicList: 'topicList',
        }),
        topic_id() {
            return this.room.topic_id;
        },
        teamlessPlayers() {
            return this.players
                .filter((player) => !this.teams.some((team) => team.players.some((teamPlayer) => this.samePlayerCheck(teamPlayer, player))));
        },
        connectedPi() {
            return this.room.pi_name;
        },
        isHost() {
            return this.room.host_id === this.getUser.id;
        },
        gameId() {
            if (!this.room) return -1;
            return this.room.game_id;
        },
        gameIsStarted() {
            return !(this.gameId < 0);
        },
        myTeam() {
            return this.teams.find((team) => team.players.some((player) => this.samePlayerCheck(player, this.getUser)));
        },
        waitForPlayers() {
            return this.teams.filter((team) => team.players).length <= 1;
        },
    },
    watch: {
        topicId(newVal) {
            TopicService.fetchTopic(newVal)
                .then((response) => {
                    this.topic = response.data;
                })
                .catch((error) => {
                    console.error(error);
                });
        },
        gameId(newVal) {
            if (!(newVal < 1) && (!this.game || this.game.id !== newVal)) {
                GameService.getGame(newVal).then((gameResponse) => {
                    this.game = gameResponse.data;
                    GameService.getAllTeams(this.game.id).then((teamResponse) => {
                        this.gameTeams = teamResponse.data;
                    }).catch((error) => {
                        console.error(error);
                    });
                }).catch((error) => {
                    console.error(error);
                });
            }
        },
    },
    mounted() {
        RoomService.fetchRoomById(this.$route.params.id)
            .then((response) => {
                this.room = response.data;
            }).catch((error) => {
                console.error(error);
                this.$router.push('/');
            });
        this.fetchTopics();
        CubeService.getCubes().then((response) => {
            this.piNames = response.data;
        });
        RoomService.getPlayers(this.$route.params.id)
            .then((response) => {
                this.players = response.data;
            })
            .catch((error) => {
                console.error(error);
            });
        RoomService.getTeams(this.$route.params.id)
            .then((response) => {
                this.teams = response.data;
            })
            .catch((error) => {
                console.error(error);
            });
        subChannel(`/rooms/${this.$route.params.id}`, (message) => {
            console.log('You got a message:');
            console.log(message);
            switch (message.type) {
            case 'ROOM_DELETED':
                this.$notify({
                    title: this.$t('game.messages.roomRemoved'),
                    type: 'error',
                });
                this.$router.push('/');
                break;
            case 'ROOM_CHANGED':
                this.room = (message.data);
                break;
            case 'USER_JOINED_ROOM':
                if (this.players.some((playerEl) => message.data.virtual_id === playerEl.virtual_id && message.data.id === playerEl.id)) break;
                this.players.push(message.data);
                // Make sure a virtual user joins his creator's team
                if (message.data.creator_id) {
                    this.teams.forEach((team) => {
                        if (team.players.some((player) => player.id && player.id === message.data.creator_id)) team.players.push(message.data);
                    });
                }
                this.$notify({
                    title: message.data.username + this.$t('game.messages.userJoined'),
                    type: 'success',
                });
                break;
            case 'USER_LEFT_ROOM':
                this.players = this.players.filter((playerEl) => !this.samePlayerCheck(message.data, playerEl) && !(message.data.id && playerEl.virtual_id && playerEl.creator_id === message.data.id));
                this.$notify({
                    title: message.data.username + this.$t('game.messages.userJoined'),
                    type: 'error',
                });
                break;
            case 'USER_JOINED_TEAM':
                if (this.teams.some((team) => team.name === message.data.team.name)) { this.teams = this.teams.map((team) => (team.name === message.data.team.name ? message.data.team : team)); } else { this.teams.push(message.data.team); }
                this.notifySuccess(`${message.data.user.username} ${this.$t('game.messages.userJoinedTeam')} ${message.data.team.name}`);
                break;
            case 'USER_LEFT_TEAM':
                if (!message.data.team.players.length) { this.teams = this.teams.filter((team) => team.name !== message.data.team.name); } else { this.teams = this.teams.map((team) => (team.name === message.data.team.name ? message.data.team : team)); }
                this.notifyError(`${message.data.user.username} ${this.$t('game.messages.userLeftTeam')} ${message.data.team.name}`);
                break;
            case 'GAME_CREATED':
                this.game = message.data;
                GameService.getAllTeams(this.game.id).then((response) => {
                    this.gameTeams = response.data;
                });
                break;
            default:
                break;
            }
        });
    },
    unmounted() {
        unsubChannel(`/rooms/${this.id}`);
    },
    methods: {
        ...mapActions({
            fetchTopics: 'fetchTopics',
        }),
        samePlayerCheck(player1, player2) {
            if (player1.virtual_id) return player1.virtual_id === player2.virtual_id;
            return player1.id === player2.id;
        },
        createTeam(teamName) {
            if (this.teams.some((team) => team.name === team.teamName)) {
                this.notifyError(`${this.$t('game.messages.teamExists')}`);
                return;
            }
            if (this.room.game_id >= 0) {
                this.notifyError(`${this.$t('game.messages.gameStartedCreateTeam')}`);
                return;
            }
            RoomService.joinTeam(this.room.id, { name: teamName }).then(() => {
                this.notifySuccess(`${this.$t('game.messages.teamCreated')}`);
                this.newTeamName = '';
            }).catch((error) => {
                console.error(error);
                this.notifyError(error.error);
            });
        },
        joinTeam(teamName) {
            RoomService.joinTeam(this.room.id, { name: teamName }).then(() => {
                this.notifySuccess(`${this.$t('game.messages.teamJoined')}`);
            }).catch((error) => console.error(error));
        },
        leaveTeam(teamName) {
            RoomService.leaveTeam(this.room.id, { name: teamName });
        },
        leaveRoom(userId, virtualUser) {
            let localUserId = userId;
            if (userId !== 0 && !userId) localUserId = this.getUser.id;
            RoomService.leaveRoom(this.room.id, virtualUser, localUserId).then(() => {
                this.$router.push('/');
                this.notifySuccess(`${this.$t('game.messages.leaveRoom')}`);
            }).catch((error) => {
                console.error(error);
            });
        },
        addVirtualUser(username) {
            RoomService.joinRoom(this.room.id, { username }).then(() => {
                this.notifySuccess(`${username} ${this.$t('game.messages.createVirtualUser')}`);
            }).catch((error) => console.error(error));
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
        getTopicNameById(topicId) {
            const res = this.topicList.find((topic) => topic.id === topicId);
            return res ? res.name : topicId;
        },
        connectPi(piName) {
            RoomService.connectPi(piName, this.room.id).then(() => {
                this.notifySuccess(`${this.$t('game.messages.tryConnectPi')} ${piName}`);
            }).catch((error) => {
                console.error(error);
                this.notifyError(`${this.$t('game.messages.connectPiFailed')}`);
            });
        },
        disconnectPi(piName) {
            RoomService.connectPi(piName, this.room.id).then(() => {
                this.notifySuccess(`${this.$t('game.messages.tryDisconnectPi')} ${piName}`);
            }).catch((error) => {
                console.error(error);
                this.notifyError(`${this.$t('game.messages.disconnectPiFailed')}`);
            });
        },
        refreshPis() {
            CubeService.getCubes().then((response) => {
                this.piNames = response.data;
                this.notifySuccess(`${this.$t('game.messages.refreshPis')}`);
            }).catch((error) => {
                console.error(error);
            });
        },
        createGame() {
            if (this.room.host_id !== this.getUser.id) {
                this.notifyError(`${this.$t('game.messages.noPermissionsToCreateGame')}`);
                return;
            }
            if (this.teams.length < 2) {
                this.notifyError(`${this.$t('game.messages.twoTeamsRequired')}`);
                return;
            }
            GameService.createGame(this.room.id).then((repsonse) => {
                this.notifySuccess(this.$t('game.messages.joinedGame'));
                this.$emit('joinedGame', repsonse.data.id);
            }).catch((error) => {
                console.error(error);
            });
        },
        joinGame() {
            if (!this.gameIsStarted) {
                console.error(`${this.$t('game.messages.gameNotStartedYet')}`);
            }
            const myTeamLocal = this.gameTeams.find((team) => team.name === this.myTeam.name);
            TeamService.joinTeam(myTeamLocal.id).then(() => {
                const myVirtualUsers = this.players.filter((player) => player.creator_id && player.creator_id === this.getUser.id);
                myVirtualUsers.forEach((virtualUser) => {
                    TeamService.joinTeam(myTeamLocal.id, virtualUser);
                });
                this.$emit('joinedGame', this.game.id);
            });
        },
    },
};
</script>

<style scoped>

</style>
