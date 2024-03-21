package ui;

import java.net.*;
import java.io.*;

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
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData login(String... loginData) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, loginData, AuthData.class);
    }

    public Object logout(String token) throws Exception {
        var path = "/session";
        return makeRequest("DELETE", path, token, String.class);
    }

    public GameData list(String token) throws Exception {
        var path = "/game";
        return makeRequest("GET", path, token, GameData.class);
    }

    public int create(String name) throws Exception {
        var path = "game";
        return makeRequest("POST", path, name, int.class);
    }

    public Object join()

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception{
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
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
