# Preamble
This project uses half-duplex websockets, meaning that only the server sends messages to clients using websockets (except e.g. connection setup, ...). Therefore, this page does not cover sending messages from a client to the backend. 

# Sending messages (Backend)
Sending messages is quite easy. Follow these instructions to send messages in a controller of your choice:
1. Import the `SimpMessagingTemplate` class. 
```import org.springframework.messaging.simp.SimpMessagingTemplate;```
2. Inject this dependency inside the controller:
```
@Autowired
SimpMessagingTemplate template;
```
3. Send a message (e.g. `Hey x`) to a channel (e.g. `/topic/fun`) of your choice:
```template.convertAndSend("/topic/fun", (new WebsocketResponse("Hey x", WSResponseType.VERSION)).toString());```

To see all available response types, see `./documentation/rest_websocket_doc/websocket_documentation.md`.

### A short note on channels:
We focus sending messages to channels, instead of clients (messages don't include sensitive data + client sockets need to be authenticated). This especially fits our needs if we consider e.g. games, as we don't have to track user sockets manually. We'd just send messages to e.g. `/game/1` (Game 1).
 
# Receiving messages (Frontend)
(Un-)Subscribing channels and receiving messages is easy as well. A socket connection is automatically setup during the first page visit.  (Un-)Subscribing channels can be done like this (using the service functions located in `@/services/websocket.service.js`):
```js
import {subChannel, unsubChannel} from '@/services/websocket.service';
const channel = '/topic/fun';
const handler = (message) => {
    //Do something magic if the incomming message
    console.log(message);
}
//Subscribe to a channel
subChannel(channel, handler);

//Unsubscribe channel
unsubChannel(channel);
```

### Debugging websockets
As debugging websockets isn't that easy (considering that there aren't many public tools available), a debugtool is provided and may be accessed at `<base-url>/wsdebug`. (*Hint: This is a debug tool and hence not available in production. To enable debug mode, edit the property `VUE_APP_DEBUG` located in `.env` (Frontend-root dir)*). 


The tool itself consists of an input field for subscribing channels and an output (*There isn't a message input field as in this project, websockets are half-duplex*). To restart an existing websocket connection / init a connection, press the "Connect" button. 

![chrome_nBia0rAwa9](uploads/da6bae37162fa205cf63689168c2c926/chrome_nBia0rAwa9.png)