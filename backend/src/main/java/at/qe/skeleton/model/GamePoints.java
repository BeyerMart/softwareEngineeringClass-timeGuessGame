package at.qe.skeleton.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Set;

public class GamePoints {
    private int rejections = 0;
    private int confirmations = 0;
    private ArrayList<Long> userIds = new ArrayList<>();

    private Team currentTeam;

    public GamePoints(Team currentTeam) {
        this.currentTeam = currentTeam;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public int getRejections() {
        return rejections;
    }

    public void incrementRejections() {
        this.rejections++;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void incrementConfirmations() {
        this.confirmations++;
    }

    public ArrayList<Long> getUserIds() {
        return userIds;
    }

    public void addUserId(Long userId) {
        this.userIds.add(userId);
    }
}
