<template>
    <div class="lg:w-1/2 w-3/4 m-auto">
        <div class="flex flex-col md:flex-row items-start md:items-center rounded-t-xl bg-indigo-100 p-10">
            <span class="mr-10 font-bold">Message:</span>
            <div class="flex w-full">
                <input
                    v-model="input"
                    type="text"
                    class="border border-gray-400 rounded-l-xl w-full p-2 focus:outline-none"
                    placeholder="Hey x"
                >
                <button class="rounded-r-xl bg-indigo-500 p-2 min-w-max text-white font-bold px-5 border border-indigo-500 focus:outline-none hover:bg-indigo-600 hover:border-indigo-600">
                    Send
                </button>
            </div>
        </div>
        <div class="bg-indigo-400 rounded-b-xl">
            <pre
                class="rounded-b-xl h-48 bg-black bg-opacity-75 text-white p-6 overflow-y-auto"
                v-html="output"
            />
        </div>
    </div>
</template>
<script>
import { initSocket, subChannel } from '@/services/websocket.service';

export default {
    name: 'Wsdebug',
    data() {
        return {
            input: '',
            output: '',
        };
    },
    mounted() {
        initSocket().then(() => {
            subChannel('/topic/fun', (message) => {
                this.output += `[${(new Date()).toLocaleTimeString()}] ${message}<br/>`;
            });
        });
    },
};
</script>
