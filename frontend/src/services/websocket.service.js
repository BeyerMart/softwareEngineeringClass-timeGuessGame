import SockJS from 'sockjs-client';
import Stomp from 'webstomp-client';

let socket = null;
let stompClient = null;
let connected = false;
let initPromise = null;

let subscriptions = [];

/**
 * Initializes socket
 * @returns {Promise} API Request
 */
export function initSocket() {
    initPromise = new Promise((resolve, reject) => {
        socket = new SockJS(`${process.env.VUE_APP_BASE_URL}/websocket`);
        stompClient = Stomp.over(socket);
        if (!process.env.VUE_APP_DEBUG.toLocaleLowerCase() !== 'true') stompClient.debug = () => {};
        stompClient.connect({}, () => {
            subscriptions = [];
            connected = true;
            resolve();
        }, (error) => {
            subscriptions = [];
            connected = false;
            reject(error);
            throw new Error('[WEBSOCKET] Error initializing socket!');
        });
    });
    return initPromise;
}

/**
 * Subscribes to channel
 * @param {string} channel
 * @param {function} handler
 */
export async function subChannel(channel, handler) {
    if (!connected) await initPromise;
    if (subscriptions.map((entry) => entry[0]).includes(channel)) throw new Error(`[WEBSOCKET] Already subscribed to '${channel}'!`);

    subscriptions.push([channel, stompClient.subscribe(channel, (packet) => {
        handler(JSON.parse(packet.body));
    })]);
}

/**
 * Unsubscribe from channel
 * @param {string} channel
 */
export async function unsubChannel(channel) {
    if (!connected) await initPromise;
    const index = subscriptions.map((entry) => entry[0]).indexOf(channel);

    if (index === -1) throw new Error(`[WEBSOCKET] Channel '${channel}' is not subscribed!`);
    const entry = subscriptions[index];
    entry[1].unsubscribe();

    subscriptions.splice(index, 1);
}

/**
 * Disconnect socket
 */
export function disconnect() {
    if (!connected) return;
    stompClient.disconnect();
    connected = false;
}

/**
 * Checks if socket is connected
 * @returns {boolean} connected
 */
export function isConnected() {
    return connected;
}
