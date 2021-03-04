package tech.iotait.model;

public class Players {

    String id;
    String fname;
    String name;

    public Players() {
    }

    public Players(String id, String fname, String name) {
        this.id = id;
        this.fname = fname;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public String getName() {
        return name;
    }
}
