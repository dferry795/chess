package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashSet;

public class memoryDB {
    public final HashSet<UserData> userList;
    public final HashSet<AuthData> authList;
    public final HashSet<GameData> gameList;

    public memoryDB(){
        this.userList = new HashSet<UserData>();
        this.authList = new HashSet<AuthData>();
        this.gameList = new HashSet<GameData>();
    }
}
