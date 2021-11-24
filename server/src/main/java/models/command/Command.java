package models.command;

public class Command {
    private Command _instance = new Command();

    public String create(Object params) {return null;}
    public String login(Object params) {return null;}
    public String join() {return null;}
    public String Register() {return null;}

    public Command get_instance() {
        return _instance;
    }

}
