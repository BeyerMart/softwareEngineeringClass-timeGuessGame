package at.qe.skeleton.model;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VirtualTeam {
    @NotNull
    private String name;
    private Set<UserIdVirtualUser> players;

    public VirtualTeam(String name, UserIdVirtualUser firstUser) {
        this.name = name;
        this.players = ConcurrentHashMap.newKeySet();
        players.add(firstUser);
    }

    public VirtualTeam() {
    }

    public int getAmountOfPlayers() {
        return players.stream().mapToInt(UserIdVirtualUser::getAmount).sum();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserIdVirtualUser> getPlayers() {
        return players;
    }

    public void setPlayers(Set<UserIdVirtualUser> players) {
        this.players = players;
    }
}
