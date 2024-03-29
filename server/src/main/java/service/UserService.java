package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class UserService {

    private final UserDataInterface userData;
    private final AuthDataInterface authData;

    public UserService(UserDataInterface user, AuthDataInterface auth){
        this.userData = user;
        this.authData = auth;
    }
    public AuthData register(UserData user) throws DataAccessException, SQLException {
        if (user.username() != null && user.password() != null && user.email() != null) {

            if (userData.getUser(user.username(), user.password()) == null) {
                userData.createUser(user);
                String authToken = UUID.randomUUID().toString();
                AuthData auth = new AuthData(authToken, user.username());
                authData.createAuth(auth);
                return auth;
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else {
            throw new DataAccessException("Error: bad request");
        }
    }

    public AuthData login(String username, String password) throws DataAccessException {
        if (userData.getUser(username, password) != null) {
            String authToken = UUID.randomUUID().toString();
                AuthData auth = new AuthData(authToken, username);
                authData.createAuth(auth);
                return auth;
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        if (authToken != null && authData.getAuth(authToken) != null){
            AuthData token = authData.getAuth(authToken);
            authData.deleteAuth(token);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
