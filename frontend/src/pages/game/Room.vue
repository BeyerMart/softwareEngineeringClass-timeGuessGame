<template>
    <div>
        Welcome to your room {{ room }}
        The following Users have connected: {{ usersInRoom }}
        The following Teams are in this room {{ teams }}
    </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';
import { subChannel, unsubChannel } from '@/services/websocket.service';
import * as RoomService from '@/services/room.service';

export default {

    name: 'Room',
    data() {
        return {
            id: this.$route.params.id,
            room: undefined,
        };
    },
    computed: {
        ...mapGetters({
            getUsers: 'user/getUsers',
            getUser: 'user/getUser',
        }),
        usersInRoom() {
            if (this.room) return this.getUsersFromUserContainer(Object.values(this.room.players));
            return undefined;
        },
        teams() {
            if (this.room) {
                return Object.keys(this.room.teams).forEach(function (key) {
                    this.room.teams[key] = this.getUsersFromUserContainer(this.room.teams[key].players);
                });
            }
            return undefined;
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
        // TODO: Make sure user is part of this room
        subChannel(`/rooms/${this.$route.params.id}`, (message) => {
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
                this.$notify({
                    title: message.data.username + this.$t('game.messages.userJoined'),
                    type: 'error',
                });
                break;
            case 'USER_LEFT_ROOM':
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
            fetchUser: 'user/fetchUser',
        }),
        async getUsersFromUserContainer(containers) {
            const result = [];
            const self = this;
            containers.forEach((container) => {
                if (!self.getUsers.some((storageUser) => container.user_id === storageUser.user_id)) self.fetchUser(container.userId);
                const localUser = self.getUsers.find((storageUser) => container.user_id === storageUser.user_id);
                result.push(localUser);
                Object.values(container.virtualUsers).forEach((virtualUser) => result.push(virtualUser));
            });
            return result;
        },
    },
};
</script>

<style scoped>

</style>
