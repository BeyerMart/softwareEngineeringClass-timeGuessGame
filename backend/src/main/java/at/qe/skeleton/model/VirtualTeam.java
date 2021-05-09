package at.qe.skeleton.model;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualTeam {
    @NotNull
    private String team_name;
    private Set<UserIdVirtualUser> players;

    public VirtualTeam(String team_name, UserIdVirtualUser firstUser) {
        this.team_name = team_name;
        this.players = ConcurrentHashMap.newKeySet();
        players.add(firstUser);
    }

    public VirtualTeam() {
    }

    public int getAmountOfPlayers() {
        return players.stream().mapToInt(UserIdVirtualUser::getAmount).sum();
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public Set<UserIdVirtualUser> getPlayers() {
        return players;
    }

    public void setPlayers(Set<UserIdVirtualUser> players) {
        this.players = players;
    }
}
