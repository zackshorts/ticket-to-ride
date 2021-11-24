package database;

import java.sql.*;
import java.util.ArrayList;

import models.data.User;

public class UserDao {
    Database db = new Database();

    public UserDao() throws Exception{
        try {
            db.openConnection();
            db.createUserTable();
            db.closeConnection(true);
        } catch (Exception e) {
            throw new Exception("openConnection failed", e);
        }
    }
    /**
     * adds one user to database
     * @param u the user you want to add
     */
    public boolean add(User u) {
        boolean added = false;
        try {
            db.openConnection();
            PreparedStatement ps = null;
            ps = db.getConn().prepareStatement("INSERT INTO User (UserName, Password) VALUES(?, ?);");
            ps.setString(1,u.getUsername());
            ps.setString(2,u.getPassword());
            ps.executeUpdate();
            added = true;
        }
        catch (Exception e) {
            System.out.println("Failed to add user to User table.");
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
     * @param u the user you want to delete
     */
    public void delete(User u) {
        try {
            db.openConnection();
            PreparedStatement ps = null;
            ps = db.getConn().prepareStatement("DELETE FROM User " +
                    "WHERE" +
                    " userName = ?;");
            ps.setString(1, u.getUsername());
            ps.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("Failed to delete user from User table.");
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

    public boolean joinGame(User u, String gameName) {
        boolean added = false;
        try {
            db.openConnection();
            PreparedStatement ps = null;
            ps = db.getConn().prepareStatement("UPDATE User where username = ? SET trackGame = ?;");
            ps.setString(1, u.getUsername());
            ps.setString(2, gameName);
            ps.executeUpdate();
            added = true;
        }
        catch (Exception e) {
            System.out.println("Failed to update user from User table.");
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
     * deletes all the information in the User table
     */
    public void clear () {
        try {
            db.openConnection();
            Statement stmt = db.getConn().createStatement();
            stmt.executeUpdate("DELETE FROM User ");
        }
        catch (Exception e) {
            System.out.println("Failed to clear User table.");
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

    public ArrayList<User> getAll() {
        ArrayList<User> users = new ArrayList<>();
        try {
            db.openConnection();
            ResultSet rs = db.getConn().prepareStatement("SELECT * FROM User;").executeQuery();
            while (rs.next()) {
                String username = rs.getString("userName");
                String password = rs.getString("password");
                users.add(new User(username,password));
            }
        }
        catch (Exception e) {
            System.out.println("Could not get user from User table.");
            return null;
        }
        finally {
            try {
                db.closeConnection(true);
            }
            catch (Exception e ) {
                e.printStackTrace();
                return null;
            }
        }
        return users;
    }


}
