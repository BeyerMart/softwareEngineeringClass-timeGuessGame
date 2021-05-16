package at.qe.skeleton.model;

public class Cube {
    private int facet;
    private String piName;
    private int roomId;
    private int batteryLevel;

    public Cube() {
    }

    public Cube(int batteryLevel, int roomId, int facet) {
        this.batteryLevel = batteryLevel;
        this.roomId = roomId;
        this.facet = facet;
    }

    public int getFacet() {
        return facet;
    }

    public void setFacet(int facet) {
        this.facet = facet;
    }

    public String getPiName() {
        return piName;
    }

    public void setPiName(String piName) {
        this.piName = piName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int level) {
        batteryLevel = level;
    }
}
