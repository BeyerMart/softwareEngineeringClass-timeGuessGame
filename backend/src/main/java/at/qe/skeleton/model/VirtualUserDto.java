package at.qe.skeleton.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VirtualUserDto {
    private Long virtual_id;

    @NotBlank
    @Size(max = 128)
    private String username;

    private Timestamp created_at;

    private Long creator_id;

    public VirtualUserDto() {
    }

    @JsonProperty(value = "virtual_id")
    public Long getVirtual_id() {
        return virtual_id;
    }

    @JsonProperty(value = "virtual_id")
    public void setVirtual_id(Long virtual_id) {
        this.virtual_id = virtual_id;
    }

    @JsonProperty(value = "username")
    public String getUsername() {
        return username;
    }

    @JsonProperty(value = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty(value = "created_at")
    public Timestamp getCreatedAt() {
        return created_at;
    }

    @JsonProperty(value = "created_at")
    public void setCreatedAt(Timestamp created_at) {
        this.created_at = created_at;
    }

    @JsonProperty(value = "creator_id")
    public long getCreator_id() {
        return creator_id;
    }

    @JsonProperty(value = "creator_id")
    public void setCreator_id(Long creator_id) {
        this.creator_id = creator_id;
    }
}