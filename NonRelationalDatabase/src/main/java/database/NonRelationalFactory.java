package database;

import Database.DBFacade;
import Database.DBFactory;

public class NonRelationalFactory implements DBFactory {
    @Override
    public DBFacade getFacade() {
        return new NRDBFacade();
    }
}
