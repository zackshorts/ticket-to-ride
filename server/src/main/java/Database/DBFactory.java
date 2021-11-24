package Database;

public interface DBFactory {
    public DBFacade getFacade() throws Exception;
}
