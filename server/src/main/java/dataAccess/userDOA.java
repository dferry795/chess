package dataAccess;

import model.UserData;

import java.util.Objects;

public class userDOA {
    public UserData getUser(String username, String password, memoryDB data){
        for (UserData user: data.userList){
            if (Objects.equals(user.username(), username) && Objects.equals(user.password(), password)){
                return user;
            }
        }
        return null;
    }

    public void createUser(UserData user, memoryDB data){
        data.userList.add(user);
    }

    public void clear(memoryDB data){
        data.userList.clear();
    }
}
