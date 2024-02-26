package dataAccess;

import model.UserData;

import java.util.HashSet;

public class memoryDB {
    private final HashSet<UserData> userList;
    private final Object authList;
    private final Object gameList;

    public memoryDB(){
        this.userList = null;
        this.authList = null;
        this.gameList = null;
    }
}
