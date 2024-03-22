package ui;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public AuthData register(UserData user) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, user, null, AuthData.class);
    }

    public AuthData login(String username, String password) throws Exception {
        var path = "/session";

        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);

        return this.makeRequest("POST", path, loginData, null, AuthData.class);
    }

    public void logout(String token) throws Exception {
        var path = "/session";
        makeRequest("DELETE", path, null, token, Object.class);
    }

    public HashSet<GameData> list(String token) throws Exception {
        var path = "/game";
        return makeRequest("GET", path, null, token, HashSet.class);
    }

    public int create(String name, String authToken) throws Exception {
        var path = "game";
        return makeRequest("POST", path, name, authToken, int.class);
    }

    public void join(String color, int gameID, String authToken) throws Throwable{
        var path = "/game";

        Map<String, String> joinData = new HashMap<>();
        joinData.put("playerColor", color);
        joinData.put("gameID", Integer.toString(gameID));

        makeRequest("PUT", path, joinData, authToken, Object.class);
    }

    public Object clear() throws Throwable{
        var path = "/db";
        return makeRequest("DELETE", path, null, null, Object.class);
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws Exception{
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null){
                http.setRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            return readBody(http, responseClass);
        } catch (Exception e){
            throw e;
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
