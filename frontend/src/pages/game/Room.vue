<template>
    <div>
        Welcome to your room {{ room }}
        The following Users have connected: {{ players }}
        The following Teams are in this room {{ teams }}
    </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { subChannel, unsubChannel } from '@/services/websocket.service';
import * as RoomService from '@/services/room.service';
import * as TopicService from '@/services/topic.service';

export default {

    name: 'Room',
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
                this.$notify({
                    title: message.data.username + this.$t('game.messages.userJoined'),
                    type: 'error',
                });
                break;
            case 'USER_LEFT_TEAM':
                this.$notify({
                    title: message.data.username + this.$t('game.messages.userJoined'),
                    type: 'error',
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
    },
};
</script>

<style scoped>

</style>
