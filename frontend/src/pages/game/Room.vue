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
                                    :options="piNamesTest"
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
                        <h3 class="font-bold tracking-tight sm:text-2xl my-6">
                            Players yet to join a team
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
            piNamesTest: ['pi1', 'pi2'],
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

    },
    mounted() {
        RoomService.fetchRoomById(this.$route.params.id)
            .then((response) => {
                this.room = response.data;
            }).catch((error) => {
                console.error(error);
                this.$router.push('/');
            });
        if (!this.topicList) this.fetchTopics();
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
                this.notifySuccess(`${message.data.user.username} joined team ${message.data.team.name}`); // TODO: translate
                break;
            case 'USER_LEFT_TEAM':
                if (!message.data.team.players.length) { this.teams = this.teams.filter((team) => team.name !== message.data.team.name); } else { this.teams = this.teams.map((team) => (team.name === message.data.team.name ? message.data.team : team)); }
                this.notifyError(`${message.data.user.username} left team ${message.data.team.name}`); // TODO: translate
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
                this.notifyError('already exists'); // TODO: translate
                return;
            }
            if (this.room.game_id >= 0) {
                this.notifyError("You can't create a new Team after the Game has started"); // TODO: translate
                return;
            }
            RoomService.joinTeam(this.room.id, { name: teamName }).then(() => {
                this.notifySuccess('Team created successfully'); // TODO: translate
                this.newTeamName = '';
            }).catch((error) => {
                console.error(error);
                this.notifyError(error.error);
            });
        },
        joinTeam(teamName) {
            RoomService.joinTeam(this.room.id, { name: teamName }).then(() => {
                this.notifySuccess('Team joined successfully');
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
                this.notifySuccess('left room successfully'); // TODO: Translation
            }).catch((error) => {
                console.error(error);
            });
        },
        addVirtualUser(username) {
            RoomService.joinRoom(this.room.id, { username }).then(() => {
                this.notifySuccess(`${username} created successfully`); // TODO: Translation
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
                this.notifySuccess(`Trying to connect Pi ${piName}`); // TODO: Translation
            }).catch((error) => {
                console.error(error);
                this.notifyError('Connection attempt failed'); // TODO: Translation
            });
        },
        disconnectPi(piName) {
            RoomService.connectPi(piName, this.room.id).then(() => {
                this.notifySuccess(`Trying to disconnect Pi ${piName}`);
            }).catch((error) => {
                console.error(error);
                this.notifyError('Disconnection attempt failed');
            });
        },
        refreshPis() {
            CubeService.getCubes().then((response) => {
                this.piNames = response.data;
                this.notifySuccess('Pis refreshed');
            }).catch((error) => {
                console.error(error);
            });
        },
        createGame() {
            if (this.room.host_id !== this.getUser.id) {
                this.notifyError('Only a host can create a new game');
            }
        },
        joinGame() {
            // TODO
        },
    },
};
</script>

<style scoped>

</style>
