package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class userService {

    private final userDOA userData;
    private final authDOA authData;
    private final gameDOA gameDate;

    public userService(){
        this.userData = new userDOA();
        this.authData = new authDOA();
        this.gameDate = new gameDOA();
    }
    public AuthData register(UserData user, memoryDB data) throws DataAccessException {
        if (userData.getUser(user.username(), data) == null){
            userData.createUser(user, data);

            String authToken = UUID.randomUUID().toString();
            AuthData auth = new AuthData(authToken, user.username());
            authData.createAuth(auth, data);
            return auth;
        } else{
            throw new DataAccessException("Error: description");
        }

    }

    public AuthData login(String username, String password, memoryDB data) throws DataAccessException {
        if (userData.getUser(username, data) != null){
            if (userData.getUser(username, data).password() == password) {
                String authToken = UUID.randomUUID().toString();
                AuthData auth = new AuthData(authToken, username);
                authData.createAuth(auth, data);
                return auth;
            } else {
                throw new DataAccessException("Error: description");
            }
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void logout(String auth, memoryDB data) throws DataAccessException {
        if (authData.getAuth(auth, data) != null){
            AuthData token = authData.getAuth(auth, data);
            authData.deleteAuth(token, data);
        }
    }
}
