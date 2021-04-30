# Websocket documentation
This documentation describes websocket communication. Contained data follows a structure, however, concrete fields have to be defined along the implementation.

## Hint
In general, REST requests are performed. If realtime-updates are required, the webapp pushes websocket-messages to users. This is useful as realtime-updates can be realized (e.g. if a player joins a team), without the time- and performance-overhead using [long-polling](https://ably.com/blog/websockets-vs-long-polling).  

In this project **half-duplex** web-sockets are used. All websocket messages (*except for connection setup or similar*) are initiated by the server, as their purpose is to update data in real time at each desired recipient. A client may initiate REST requests which lead to websocket messages.

## Structure

The message structure looks like this:
```json
{
    'type' => 'USER_JOINED_GAME',
    'data' => {},
    'timestamp' => '2019-07-04T13:33:03.969Z'
}
```
The `type` property contains the message type (e.g. `USER_JOINED_GAME`) - clients may call actions according to this type. The `data` property contains associated data (e.g. the user which joins a game) and can therefore be a JSON object or array. The `timestamp` property contains the timestamp when the message was created.

## Message types

Below is a table containing all available message types. `TYPE` equals the message `type`.

| TYPE                | DESCRIPTION                       | RECIPIENTS                  |
|---------------------|-----------------------------------|-----------------------------|
| ROOM_CREATED        | A room was created                | All players                 |
| ROOM_DELETED        | A room was deleted                | All players                 |
| ROOM_NAME_CHANGED   | The room name was updated         | All players                 |
| GAME_CREATED        | The room is started               | All players of current room |
| ROOM_TOPIC_CHANGED  | The room topic has been updated   | All players of current room |
| GAME_OVER           | The game is over                  | All players of current room |
| GAME_TOPIC_CHANGED  | The game topic has been updated   | All players of current game |
| TEAM_CREATED        | A new team was created            | All players of current room |
| TEAM_DELETED        | A team was deleted                | All players of current room |
| TEAM_NAME_CHANGED   | A team name was updated           | All players of current room |
| TEAM_POINTS_CHANGED | The points of a team were updated | All players of current game |
| USER_JOINED_ROOM    | A user joined the room            | All players of current room |
| USER_LEFT_ROOM      | A user left the room              | All players of current room |
| USER_JOINED_GAME    | A user joined the game            | All players of current game |
| USER_LEFT_GAME      | A user left the game              | All players of current game |
| VIRTUAL_USER_JOINED | A virtual user joined a team      | All players of current room |
| VIRTUAL_USER_LEFT   | A virtual user left a team        | All players of current room |
| USER_JOINED_TEAM    | A user joined a team              | All players of current room |
| USER_LEFT_TEAM      | A user left a team                | All players of current room |
| VERSION             | Current game version              | All players + All Pis       |
| PI_CONNECTED        | A Pi connected to the backend     | The Backend                 |
| START_SEARCHING     | The Search for the best Timecube starts                         | The Pi corresponding to a room |
| FOUND_AND_CONNECTED | A Timeflip Cube was found, connection established               | The Backend                    |
| NOT_FOUND           | A TimeFlip Cube was not found                                   | The Backend                    |
| NOT_CONNECTED       | Connection to a Timeflip could not be established               | The Backend                    |
| START_FACET_NOTIFICATION   | The Backend wants to be informed on facets               | The Pi corresponding to a room |
| FACET_NOTIFICATION         | Notification with the new facet                          | The Backend                    |
| STOP_FACET_NOTIFICATION    | The backend does not need any notifications on facets anymore   | The Pi corresponding to a room |
| START_BATTERY_NOTIFICATION | The Backend wants to be informed on battery levels       | The Pi corresponding to a room |
| BATTERY_NOTIFICATION       | Notification with the new battery levels                 | The Backend                   |
| STOP_BATTERY_NOTIFICATION  | The backend does not need any notifications on battery anymore | The Pi corresponding to a room |
| CUBE_ERROR          | Unhandled Error with the cube     | The Backend                 |
| CUBE_DISCONNECTED   | TimeFlip disconnected             | The Backend                 |
| PI_DISCONNECTING    | Pi is disconnecting / shutting down | The Backend               |