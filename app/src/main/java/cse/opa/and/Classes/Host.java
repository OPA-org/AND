package cse.opa.and.Classes;

/**
 * Created by Peter on 28/3/2018.
 */

public class Host extends Agent{
    private Interface anInterface;

    public Host(int id, String serial_number,Interface anInterface) {
        super(id,serial_number, false);
        this.anInterface = anInterface;
    }

    public Interface getAnInterface() {
        return anInterface;
    }

    public void setAnInterface(Interface anInterface) {
        this.anInterface = anInterface;
    }
}
