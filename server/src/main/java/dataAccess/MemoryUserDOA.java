package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDOA implements UserDataInterface {

    private final ArrayList<UserData> userList = new ArrayList<>();

    public UserData getUser(String username, String password){
        for (UserData user: this.userList){
            if (Objects.equals(user.username(), username) && Objects.equals(user.password(), password)){
                return user;
            }
        }
        return null;
    }

    public void createUser(UserData user) {
        this.userList.add(user);
    }

    public void clear(){
        this.userList.clear();
    }
}
