package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicLong;

public class VirtualUser {
    @JsonIgnore
    private final static AtomicLong lastVirtualUserId = new AtomicLong(0);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long virtual_id;

    @NotBlank
    @Size(max = 128)
    private String username;

    private Long creator_id;

    @CreationTimestamp
    private Timestamp created_at;

    @UpdateTimestamp
    private Timestamp updated_at;

    public VirtualUser(String username) {
        this.username = username;
    }

    public VirtualUser() {

    }

    public static Long getNextId() {
        return lastVirtualUserId.incrementAndGet();
    }

    public Long getVirtual_id() {
        return virtual_id;
    }

    public void setVirtual_id(Long virtual_id) {
        this.virtual_id = virtual_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public long getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(Long creator_id) {
        this.creator_id = creator_id;
    }
}
