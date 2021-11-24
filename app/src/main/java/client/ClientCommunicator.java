package client;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import models.data.Result;

public class ClientCommunicator /*implements IClient */{
    final static String HOST = "10.0.2.2";
    final static String PORT = "8080";


    /*
    This Class is soley responsible for passing commamnds to the server and
    accepting the results. This class serializes and deserializes the Json,
    handles server exceptions, and general works to contain all internet
    protocal logic for the client side*/


    public Result send(GeneralCommand command) {
        try {
            URL url = new URL("http://" + HOST + ":" + PORT + "/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");

            OutputStream os = http.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = mapper.writeValueAsString(command);
            writeString(jsonStr, os);

            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = http.getInputStream();
                Reader read = new InputStreamReader(is);
                return mapper.readValue(read, Result.class);//result;

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String send(String json) {
        try {
            URL url = new URL("http://" + HOST + ":" + PORT + "/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");

            OutputStream os = http.getOutputStream();
            writeString(json, os);

            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = http.getInputStream();
                return readString(is);//result;

            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private static String readString(InputStream is) throws IOException {
        int ch;
        StringBuilder sb = new StringBuilder();
        while ((ch = is.read()) != -1) {
            sb.append((char)ch);
        }
        return sb.toString();
    }
}
