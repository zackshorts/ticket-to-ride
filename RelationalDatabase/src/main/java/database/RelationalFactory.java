package database;
import Database.DBFacade;
import Database.DBFactory;

public class RelationalFactory implements DBFactory {
    @Override
    public DBFacade getFacade() throws Exception {
        return new RDBFacade();
    }
}
