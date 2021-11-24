package database;

import java.sql.*;

public class Database {

    private Connection conn;


    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void removeTables(Statement stmt) {
        try {
            try {

                stmt.executeUpdate("drop table Game;\n" +
                        "drop table User;\n" +
                        "drop table Command;");
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() throws Exception {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:database.sqlite";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new Exception("openConnection failed", e);
        }
    }

    public void closeConnection(boolean commit) throws Exception {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            throw new Exception("closeConnection failed", e);
        }
    }

    public void createGameTable() {

        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Game (\n" +
                        "gameName     TEXT NOT NULL PRIMARY KEY,\n" +
                        "game         TEXT NOT NULL\n" +
                        ");");
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createUserTable() {
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS User (\n" +
                        "userName   TEXT NOT NULL PRIMARY KEY,\n" +
                        "password   TEXT NOT NULL,\n" +
                        "trackGame  TEXT,\n" +
                        "FOREIGN KEY(trackGame) REFERENCES Game(gameName)\n" +
                        ");");
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCommandTable() {
        try {
            Statement stmt = null;
            try {
                stmt = conn.createStatement();

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Command (\n" +
                        "commandID      INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "commandText    TEXT,\n" +
                        "trackGame      TEXT NOT NULL,\n" +
                        "FOREIGN KEY(trackGame) REFERENCES Game(gameName)\n" +
                        ");");
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}



