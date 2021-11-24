package helper;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import models.data.Request;
import models.data.Result;

public class HttpHelper {
    Gson gson = new Gson();

    public String httpRequestToJson(HttpExchange exchange) throws IOException {

        // Extract the JSON string from the HTTP request body
        // Get the request body input stream
        InputStream reqBody = exchange.getRequestBody();
        // Read JSON string from the input stream
        return readString(reqBody);
    }

    public String requestToJson(Request request) {
        return gson.toJson(request);
    }

    public boolean sendHttpResponse(HttpExchange exchange, String jsonResponse) throws IOException {

        // Start sending the HTTP response to the client, starting with
        // the status code and any defined headers.
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        // Get the response body output stream.
        OutputStream respBody = exchange.getResponseBody();
        // Write the JSON string to the output stream.
        writeString(jsonResponse, respBody);
        // Close the output stream.  This is how Java knows we are done
        // sending data and the response is complete
        respBody.close();

        return true;
    }


    /*
    The readString method shows how to read a String from an InputStream.
    */
    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public Result stringToRegisterResult(String regString) {
        Result result = gson.fromJson(regString, Result.class);
        return result;
    }

    /*
		The writeString method shows how to write a String to an OutputStream.
	*/
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    public Result stringToResult(String respData) {
        Result result = gson.fromJson(respData, Result.class);
        return result;
    }

/*
    public EventAllResult stringToGetAllEvents(String respData) {
        EventAllResult eventAllResult = gson.fromJson(respData, EventAllResult.class);
        return eventAllResult;
    }

*/
}
