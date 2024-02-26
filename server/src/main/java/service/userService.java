package service;

import dataAccess.DataAccessException;
import dataAccess.memoryDB;
import model.AuthData;
import model.UserData;

public class userService {
    public AuthData register(UserData user, memoryDB data) throws DataAccessException {
        if (userDoa.getUser(user.username()) == null){
            return userDoa.createUser(user);
        } else{
            throw DataAccessException;
        }

    }

    public AuthData login(String username, String password, memoryDB data){
        if (userDoa.getUser(username) != null && userDoa.getUser(username).password == password){
            
        }
    }

    public void logout(UserData user, memoryDB data){

    }
}
