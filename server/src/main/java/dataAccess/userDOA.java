package dataAccess;

import model.UserData;

public class userDOA {
    public UserData getUser(String username, memoryDB data){
        for (UserData user: data.userList){
            if (user.username() == username){
                return user;
            }
        }
        return null;
    }

    public void createUser(UserData user, memoryDB data){
        data.userList.add(user);
    }


}
