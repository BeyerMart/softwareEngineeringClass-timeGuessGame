import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

let socket = null;
let stompClient = null;
let connected = false;

export function initSocket() {
    return new Promise((resolve, reject) => {
        socket = new SockJS('http://localhost:8080/websocket');
        stompClient = Stomp.over(socket);
        // stompClient.debug = () => {};
        stompClient.connect({}, () => {
            connected = true;
            resolve();
        }, () => {
            connected = false;
            reject();
            throw new Error('[WEBSOCKET] Error initializing socket!');
        });
    });
}

export function subChannel(channel, handler) {
    if ((stompClient === null) || (socket === null)) throw new Error('[WEBSOCKET] Socket is not initialized!');

    stompClient.subscribe(channel, (packet) => {
        console.log(packet);
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
