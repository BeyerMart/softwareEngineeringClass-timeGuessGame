<template>
    <div>
        <div v-if="room">
            <div id="Room Details">
                <h1>{{ room.name }}</h1>
            </div>
            <div>
                <h3>Payers yet to join a team</h3>
                <div
                    v-for="(player, index) in teamlessPlayers"
                    :key="index"
                >
                    <Player
                        :player="player"
                        :host="player.id ? player.id === room.host_id : false"
                    />
                </div>
            </div>
            <div id="teams">
                <div>
                    <div
                        v-for="team in teams"
                        :key="team.name"
                    >
                        <VirtualTeam
                            :team="team"
                            :host-id="room.host_id"
                        />
                    </div>
                </div>
                <div>
                    <button>
                        Create Team
                    </button>
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
import VirtualTeam from '@/components/page/VirtualTeam';
import Player from '@/components/page/Player';

export default {

    name: 'Room',
    components: { Player, VirtualTeam },
    data() {
        return {
            id: this.$route.params.id,
            room: undefined,
            topic: '',
            players: [],
            teams: [],
        };
    },
    computed: {
        ...mapGetters({
            getUsers: 'user/getUsers',
            getUser: 'user/getUser',
        }),
        topic_id() {
            return this.room.topic_id;
        },
        teamlessPlayers() {
            return this.players
                .filter((player) => !this.teams.some((team) => team.players.some((teamPlayer) => this.samePlayerCheck(teamPlayer, player))));
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
                this.$notify({
                    title: message.data.username + this.$t('game.messages.userJoined'),
                    type: 'success',
                });
                break;
            case 'USER_LEFT_ROOM':
                this.players = this.players.filter((playerEl) => !(message.data.virtual_id === playerEl.virtual_id && message.data.id === playerEl.id));
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
                if (!message.data.team.players.length) { this.teams = this.teams.filter((team) => team.name !== message.data.team.length); } else { this.teams = this.teams.map((team) => (team.name === message.data.team.name ? message.data.team : team)); }
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
            if (this.teams.some((team) => team.name === team.teamName)) this.notifyError('already exists'); // TODO: translate
            if (!this.room.game_id || this.room.game_id < 0) this.notifyError("You can't create a new Team after the Game has started"); // TODO: translate
            RoomService.joinTeam(this.room.id, { name: teamName }).then(() => {
                this.notifySuccess('Team created successfully'); // TODO: translate
            }).catch((error) => {
                console.error(error);
                this.notifyError(error.error);
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

</style>
