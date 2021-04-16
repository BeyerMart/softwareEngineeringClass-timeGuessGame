import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

let socket = null;
let stompClient = null;
let connected = false;

export function initSocket() {
    return new Promise((resolve, reject) => {
        socket = new SockJS(`${process.env.VUE_APP_BASE_URL}/websocket`);
        stompClient = Stomp.over(socket);
        if (!process.env.VUE_APP_DEBUG.toLocaleLowerCase() !== 'true') stompClient.debug = () => {};
        stompClient.connect({}, () => {
            connected = true;
            resolve();
        }, (error) => {
            connected = false;
            reject(error);
            throw new Error('[WEBSOCKET] Error initializing socket!');
        });
    });
}

export function subChannel(channel, handler) {
    if ((stompClient === null) || (socket === null)) throw new Error('[WEBSOCKET] Socket is not initialized!');

    stompClient.subscribe(channel, (packet) => {
        handler(packet.body);
    });
}

export function disconnect() {
    if (stompClient !== null) stompClient.disconnect();
    connected = false;
}

export function isConnected() {
    return connected;
}
