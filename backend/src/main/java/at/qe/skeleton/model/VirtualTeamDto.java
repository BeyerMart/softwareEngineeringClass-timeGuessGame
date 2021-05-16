package at.qe.skeleton.model;

import java.util.List;

public class VirtualTeamDto {
    private String name;
    private List<Object> players;

    public VirtualTeamDto(String name, List<Object> players) {
        this.name = name;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getPlayers() {
        return players;
    }

    public void setPlayers(List<Object> players) {
        this.players = players;
    }
}
