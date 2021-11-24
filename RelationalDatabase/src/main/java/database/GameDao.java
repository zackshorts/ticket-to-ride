package database;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.ArrayList;

import models.data.Game;


public class GameDao {
    Database db = new Database();
    ObjectMapper om = new ObjectMapper();

    public GameDao() throws Exception {
        try {
            db.openConnection();
            db.createGameTable();
            db.closeConnection(true);
        } catch (Exception e) {
            throw new Exception("openConnection failed", e);
        }
    }

    /**
     * adds one game to database
     *
     * @param game the game you want to add
     */
    public boolean add(Game game) {
        boolean added = false;
        try {
            db.openConnection();
            PreparedStatement ps = null;
            ps = db.getConn().prepareStatement("INSERT INTO Game (gameName, game) VALUES(?, ?);");
            String blob = om.writeValueAsString(game);
            ps.setString(1, game.getGameName());
            ps.setString(2, blob);
            ps.executeUpdate();
            added = true;
        } catch (Exception e) {
            System.out.println("Failed to add game to Game table.");
        } finally {
            try {
                db.closeConnection(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return added;
    }

    /**
     * deletes one game specified by the parameter
     *
     * @param game the game you want to delete
     */
    public void delete(Game game) {
        try {
            db.openConnection();
            PreparedStatement ps = db.getConn().prepareStatement("DELETE FROM Game " +
                    "WHERE" +
                    " gameName = ?;");
            ps.setString(1, game.getGameName());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Failed to delete game from Game table.");
        } finally {
            try {
                db.closeConnection(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * deletes all the information in the Game table
     */
    public void clear() {
        try {
            db.openConnection();
            Statement stmt = db.getConn().createStatement();
            stmt.executeUpdate("DELETE FROM Game ");
        } catch (Exception e) {
            System.out.println("Failed to clear Game table.");
        } finally {
            try {
                db.closeConnection(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Game> getAllGames() {
        ArrayList<Game> games = new ArrayList<>();
        try {
            db.openConnection();
            ResultSet rs = db.getConn().prepareStatement("SELECT * FROM Game;").executeQuery();
            while (rs.next()) {
                games.add(om.readValue(rs.getString("game"), Game.class));
            }
        } catch (Exception e) {
            System.out.println("Could not get game from Game table.");
            return null;
        } finally {
            try {
                db.closeConnection(true);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return games;
    }
}
