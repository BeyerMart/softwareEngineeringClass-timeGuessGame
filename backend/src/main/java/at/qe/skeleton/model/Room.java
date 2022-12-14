package at.qe.skeleton.model;

import at.qe.skeleton.exceptions.UserNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Room {

    private long id;
    private String pi_name;
    private long host_id;
    private Map<Long, UserIdVirtualUser> players;
    private Map<String, VirtualTeam> teams;
    private long game_id = -1;
    private String name;
    private Long topic_id;
    private Cube cube;
    private int max_points = 20;

    public Room(long room_id, long host_id) {
        this.id = room_id;
        this.host_id = host_id;
        this.players = new ConcurrentHashMap<>();
        this.teams = new ConcurrentHashMap<>();
        players.put(host_id, new UserIdVirtualUser(host_id));
        this.name = "Room " + host_id;
    }

    public void removeFromAllTeams (Long userId) {
        teams.values().forEach(virtualTeam -> virtualTeam.getPlayers().removeIf(userIdVirtualUser -> userIdVirtualUser.getUser_id().equals(userId)));
        removeEmptyTeams();
    }

    /**
     * Sets a new host if the old is no longer present.
     * It must be checked that the room is not empty before.
     */
    public void checkHost() {
        if (!players.containsKey(host_id)) {
            host_id = players.keySet().stream().findAny().orElseThrow(() -> new UserNotFoundException(host_id));
        }
    }

    public int getAmountOfPlayers() {
        return players.values().stream().mapToInt(UserIdVirtualUser::getAmount).sum();
    }

    public void removeEmptyTeams () {
        teams.values().removeIf((virtualTeam -> virtualTeam.getPlayers().isEmpty()));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPi_name() {
        return pi_name;
    }

    public void setPi_name(String pi_name) {
        this.pi_name = pi_name;
    }

    public long getHost_id() {
        return host_id;
    }

    public void setHost_id(long host_id) {
        this.host_id = host_id;
    }

    public Map<Long, UserIdVirtualUser> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Long, UserIdVirtualUser> players) {
        this.players = players;
    }

    public Map<String, VirtualTeam> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, VirtualTeam> teams) {
        this.teams = teams;
    }

    public long getGame_id() {
        return game_id;
    }

    public void setGame_id(long game_id) {
        this.game_id = game_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(Long topic_id) {
        this.topic_id = topic_id;
    }

    public Cube getCube(){
        return cube;
    }

    public void setCube(Cube cube){
        this.cube = cube;
    }

    public int getMax_points() {
        return max_points;
    }

    public void setMax_points(int max_points) {
        this.max_points = max_points;
    }
}
