package cse.opa.and.Classes;

/**
 * Created by Peter on 28/3/2018.
 */

public class Agent {
    private int id;
    private String serial_number;
    private boolean visited;

    public Agent(int id, String serial_number, boolean visited) {
        this.id = id;
        this.serial_number = serial_number;
        this.visited = visited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public void send_trap(){

    }
}
