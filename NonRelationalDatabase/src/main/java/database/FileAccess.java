package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FileAccess {

    //    This will update the blob game object.
    public void checkpointUpdate(String object,String gameName) throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/games/";
        String filename = OUTFOLDER + gameName + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(object);
        writer.write("\n");
        writer.close();
    }

    //    This will delete whatever file you want
    public void removeFile(String type, String id) throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/" + type + "/";

        String filename = OUTFOLDER + id + ".txt";
        File file = new File(filename);
        file.delete();
    }

    //    This will update the command file
    public void addUpdate(String command, String gameName) throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/commands/";
        String filename = OUTFOLDER + gameName + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        writer.append(command);
        writer.append("\n");
        writer.close();
    }

    public void addUser(String user) throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/users/";
        String filename = OUTFOLDER + "current_users.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
        writer.append(user);
        writer.append("\n");
        writer.close();
    }

    //    This will get the commands, line by line, from the text files.
    public List<String> getDeltasForObject(String gameName) throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/commands/";

        String filename = OUTFOLDER + gameName + ".txt";
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        // Start at one to skip object json
//        lines.remove(0);
        return lines;
    }

    public List<String> getUsers() throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/users/";

        String filename = OUTFOLDER + "current_users.txt";
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        br.close();
        // Start at one to skip object json
//        lines.remove(0);
        return lines;
    }

    public void joinGame(String userName, String gameName) throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/users/";
        String tempFilename = OUTFOLDER + "temp_users.txt";
        String filename = OUTFOLDER + "current_users.txt";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();
        List<String> lines = new ArrayList<>();
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilename, true));
        File file = new File(filename);
        File tempFile = new File(tempFilename);
        while (line != null) {

            if (line.contains("\"username\":\"" + userName + "\"")) {
                line = line.substring(0, line.length() - 5) + "\"" + gameName + "\"}";
                writer.append(line);
                writer.append("\n");
            } else {
                writer.append(line);
                writer.append("\n");
            }
            line = br.readLine();
        }
        writer.close();
        if(tempFile.renameTo(file)) {
            System.out.println("rename successful");
        }
//        file.delete();
        br.close();
    }

    public List<String> getGames() throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/games/";
        List<String> lines = new ArrayList<>();
        File folder = new File(OUTFOLDER);
        File[] files = folder.listFiles();
        try{
            for (File file : files) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                lines.add(line);
                br.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        // Start at one to skip object json
//        lines.remove(0);
        return lines;
    }

    public Map<String, ArrayList<String>> getAllCommands() throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/commands/";

        Map<String, ArrayList<String>> lines = new HashMap<>();
        File folder = new File(OUTFOLDER);
        File[] files = folder.listFiles();
        try{

            for (File file : files) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                lines.put(file.getName(),new ArrayList<String>());
                br.readLine();
                String line = br.readLine();
                while (line != null) {
                    if(!line.equals("")){
                        ArrayList<String> fileArrayList = lines.get(file.getName());
                        lines.put(file.getName(),fileArrayList);
                        fileArrayList.add(line);
                        line = br.readLine();
                    }
                }
                br.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        // Start at one to skip object json
        return lines;
    }

//    Probably won't use this.
//    public List<String> getAllObjects(String type) throws IOException {
//        List<String> objects = new ArrayList<>();
//        String OUTFOLDER = System.getProperty("user.dir") + "/files/";
//        File folder = new File(OUTFOLDER);
//        File[] files = folder.listFiles();
//        for (File file : files) {
//            if(file.getName().startsWith(type)) {
//                BufferedReader br = new BufferedReader(new FileReader(OUTFOLDER + file.getName()));
//                String line = br.readLine();
//                objects.add(line);
//                br.close();
//            } else {
//                continue;
//            }
//        }
//        return objects;
//    }

    //    Remove all files from storage.
    public void deleteFilesInDirectory(String type) throws IOException {
        String OUTFOLDER = System.getProperty("user.dir") + "/files/" + type;

        File folder = new File(OUTFOLDER);
        File[] files = folder.listFiles();
        try{
            for (File file : files) {
                file.delete();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        FileAccess access = new FileAccess();

        System.out.println(access.getUsers());
        access.joinGame("aaaaa", "JOINEDGAME");
        access.joinGame("ooooo", "JOINEDGAME");

//        String object = "Example Game Data 1";
//        String gameName = "game1";
//        String gameName2 = "game2";
//        try {
//            access.checkpointUpdate(object, gameName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String delta = "Game 1 Delta 1";
//        try {
//            access.addUpdate(delta, gameName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String user = "user1";
//        String user2 = "user2";
//
//        try {
//            access.addUser(user);
//            access.addUser(user2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            List<String> deltas = access.getDeltasForObject(gameName);
//            System.out.print("\n" + gameName + " Deltas\n");
//
//            for (String del : deltas) {
//                System.out.println("  " + del);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            object = "Party time is over";
//            access.checkpointUpdate(object, gameName2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String delta2 = "Game 2 Delta 2";
//        try {
//            access.addUpdate(delta2, gameName2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            List<String> deltas = access.getDeltasForObject(gameName2);
//            System.out.print("\n" + gameName2 + " Deltas\n");
//            for (String del : deltas) {
//                System.out.println("  " + del);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            access.deleteFilesInDirectory("games");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//       try {
//            access.removeFile("games", "game2");
//           access.removeFile("games", "game1");
//           access.removeFile("commands", "game2");
//           access.removeFile("commands", "game1");
//
//
//       } catch (IOException e) {
//            e.printStackTrace();
//        }
//
    }
}
