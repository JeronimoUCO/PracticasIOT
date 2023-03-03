package backend.team.backend.dto;

import java.time.LocalDateTime;

public class ScheduleDTO {
    private long id;
    private String type;
    private int warehouseId;
    private String description;
    private int userId;
    private LocalDateTime date;
    private String code;

    public ScheduleDTO() {
    }

    public ScheduleDTO(long id, String type, int warehouseId, String description, int userId, LocalDateTime date) {
        this.id = id;
        this.type = type;
        this.warehouseId = warehouseId;
        this.description = description;
        this.userId = userId;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWarehouse() {
        return warehouseId;
    }

    public void setWarehouse(int warehouse) {
        this.warehouseId = warehouse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser() {
        return userId;
    }

    public void setUser(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



}

