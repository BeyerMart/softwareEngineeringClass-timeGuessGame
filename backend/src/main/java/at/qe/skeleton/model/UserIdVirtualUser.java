package at.qe.skeleton.model;

import java.util.concurrent.ConcurrentHashMap;

public class UserIdVirtualUser {
    Long user_id;
    ConcurrentHashMap<Long, VirtualUser> virtualUsers;

    public UserIdVirtualUser(Long userId) {
        this.user_id = userId;
        this.virtualUsers = new ConcurrentHashMap<>();
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public ConcurrentHashMap<Long, VirtualUser> getVirtualUsers() {
        return virtualUsers;
    }

    public void setVirtualUsers(ConcurrentHashMap<Long, VirtualUser> virtualUsers) {
        this.virtualUsers = virtualUsers;
    }

    public int getAmount() {
        return virtualUsers.size() + 1;
    }
}
