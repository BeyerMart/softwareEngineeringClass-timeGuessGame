package at.qe.skeleton.controller;

public enum WSResponseType {
    ROOM_CREATED,
    ROOM_DELETED,
    ROOM_NAME_CHANGED,
    GAME_CREATED,
    ROOM_TOPIC_CHANGED,
    GAME_OVER,
    GAME_TOPIC_CHANGED,
    TEAM_CREATED,
    TEAM_DELETED,
    TEAM_NAME_CHANGED,
    TEAM_POINTS_CHANGED,
    USER_JOINED_ROOM,
    USER_LEFT_ROOM,
    USER_JOINED_GAME,
    USER_LEFT_GAME,
    VIRTUAL_USER_JOINED,
    VIRTUAL_USER_LEFT,
    USER_JOINED_TEAM,
    USER_LEFT_TEAM,
    PI_CONNECTED,
    START_SEARCHING,
    FOUND_AND_CONNECTED,
    NOT_FOUND,
    NOT_CONNECTED,
    START_FACET_NOTIFICATION,
    FACET_NOTIFICATION,
    FACET_REQUEST,
    STOP_FACET_NOTIFICATION,
    START_BATTERY_NOTIFICATION,
    BATTERY_NOTIFICATION,
    STOP_BATTERY_NOTIFICATION,
    CUBE_ERROR,
    CUBE_DISCONNECTED,
    PI_DISCONNECTING,
    OK,
    CONNECTION_TEST_TO_BACKEND,
    CONNECTION_TEST_TO_PI,
    VERSION
}