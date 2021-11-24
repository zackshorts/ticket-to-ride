package database;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;

import models.data.Game;
import server.GeneralCommand;

public class CommandDao {
    Database db = new Database();
    ObjectMapper om = new ObjectMapper();

    public CommandDao() throws Exception{
        try {
            db.openConnection();
            db.createCommandTable();
            db.closeConnection(true);
        } catch (Exception e) {
            throw new Exception("openConnection failed", e);
        }
    }

    /**
     * adds one command to database
     * @param command the command you want to add
     */
    public boolean add(GeneralCommand command) {
        boolean added = false;
        int location;
        switch (command.get_methodName()) {
            case "postChatMessage":
            case "startGame":
                location = 0;
                break;
            case "requestTicketCard":
            case "requestDestinationCards":
            case "purchaseRoute":
            case "returnDestinationCards":
            case "joinGame":
                location = 1;
                break;
            default:
                System.out.println("Switch found an impossible method.");
                return false;
        }
        String gameName = (String)command.get_paramValues()[location];

        try {
            String blob = om.writeValueAsString(command);
            db.openConnection();
            PreparedStatement ps = db.getConn().prepareStatement("INSERT INTO Command (commandText, trackGame) VALUES(?, ?);");
            ps.setString(1, blob);
            ps.setString(2, gameName);
            ps.executeUpdate();
            added = true;
        }
        catch (Exception e) {
            System.out.println("Failed to add command to Command table.");
        }
        finally {
            try {
                db.closeConnection(true);
            }
            catch (Exception e ) {
                e.printStackTrace();
            }
        }
        return added;
    }

    /**
     * deletes one user specified by the parameter
     * @param gameName the game you want to delete commands from.
     */
    public void deleteAllFromGame(String gameName) {
        try {
            db.openConnection();
            PreparedStatement ps = null;
            ps = db.getConn().prepareStatement("DELETE FROM Command " +
                    "WHERE" +
                    " trackGame = ?;");
            ps.setString(1, gameName);
            ps.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("Failed to delete command from Command table.");
        }
        finally {
            try {
                db.closeConnection(true);
            }
            catch (Exception e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * deletes all the information in the Command table
     */
    public void clear () {
        try {
            db.openConnection();
            Statement stmt = db.getConn().createStatement();
            stmt.executeUpdate("DELETE FROM Command ");
        }
        catch (Exception e) {
            System.out.println("Failed to clear Command table.");
        }
        finally {
            try {
                db.closeConnection(true);
            }
            catch (Exception e ) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<GeneralCommand> getAll(Game game) {
        ArrayList<GeneralCommand> commands = new ArrayList<>();
        try {
            db.openConnection();
            PreparedStatement ps = db.getConn().prepareStatement("SELECT * FROM Command WHERE trackGame = ?;");
            ps.setString(1, game.getGameName());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                commands.add(om.readValue(rs.getString("commandText"), GeneralCommand.class));
            }
        }
        catch (Exception e) {
            System.out.println("Could not get Commands from Command table.");
        }
        finally {
            try {
                db.closeConnection(true);
            }
            catch (Exception e ) {
                e.printStackTrace();
            }
        }
        return commands;
    }
}
