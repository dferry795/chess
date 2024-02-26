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

    public AuthData login(UserData user, memoryDB data){

    }

    public void logout(UserData user, memoryDB data){

    }
}
