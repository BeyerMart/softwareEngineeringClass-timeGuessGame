<template>
    <div class="lg:w-1/2 w-3/4 m-auto">
        <div class="flex py-4 justify-between sm:justify-start">
            <button
                class="bg-green-400 rounded-lg p-2 min-w-max text-white font-bold px-5 hover:bg-green-500 focus:outline-none mr-2"
                @click="setupSocket()"
            >
                Connect
            </button>
            <button
                class="bg-red-400 rounded-lg p-2 min-w-max text-white font-bold px-5 hover:bg-red-500 focus:outline-none ml-2"
                @click="output = ''"
            >
                Clear Output
            </button>
        </div>
        <div class="flex flex-col md:flex-row items-start md:items-center rounded-t-xl bg-indigo-100 p-10">
            <span class="mr-10 font-bold">Channel:</span>
            <div class="flex w-full flex-col sm:flex-row">
                <input
                    v-model="channel"
                    type="text"
                    class="border border-gray-400 rounded-t-xl sm:rounded-t-none sm:rounded-l-xl w-full p-2 focus:outline-none"
                    placeholder="/topic"
                >
                <button
                    class="rounded-b-xl sm:rounded-b-none sm:rounded-r-xl bg-indigo-500 p-2 min-w-max text-white font-bold px-5 border border-indigo-500 focus:outline-none hover:bg-indigo-600 hover:border-indigo-600"
                    @click="subscribeChannel"
                >
                    Subscribe
                </button>
            </div>
        </div>
        <div class="bg-indigo-400 rounded-b-xl">
            <div class="rounded-b-xl h-48 break-words bg-black bg-opacity-75 text-white p-6 overflow-y-auto">
                <span
                    v-show="!loading"
                    v-html="output"
                />
                <div
                    v-show="loading"
                    class="w-full h-full flex items-center justify-center font-bold"
                >
                    Loading...
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import {
    initSocket, subChannel, isConnected, disconnect,
} from '@/services/websocket.service';

export default {
    name: 'Wsdebug',
    data() {
        return {
            channel: '',
            output: '',
            loading: false,
        };
    },
    beforeCreate() {
        if (process.env.VUE_APP_DEBUG.toLowerCase() !== 'true') this.$router.push('/');
    },
    mounted() {
        this.setupSocket();
    },
    methods: {
        setupSocket() {
            if (this.loading) return;
            this.loading = true;
            this.output = '';

            if (isConnected) {
                disconnect();
            }

            initSocket().then(() => {
                this.loading = false;
                this.output = '<span class=\'font-bold\'>Socket initialized</span><br/>';
            }).catch((error) => {
                this.output = `<span class='text-red-500'><span class='font-bold'>Error initializing socket:</span> <br/>${JSON.stringify(error)}</span><br/>`;
                this.loading = false;
            });
        },
        subscribeChannel() {
            if (!isConnected || this.channel === '') return;
            subChannel(this.channel, (message) => {
                this.output = `[${(new Date()).toLocaleTimeString()}] ${message}<br/>${this.output}`;
            });
            this.output = `<span class='font-bold'>Channel '${this.channel}' subscribed</span><br/>${this.output}`;
        },
    },
};
</script>
