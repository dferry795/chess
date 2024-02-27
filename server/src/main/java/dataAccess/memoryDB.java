package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashSet;

public class memoryDB {
    public final ArrayList<UserData> userList;
    public final ArrayList<AuthData> authList;
    public final ArrayList<GameData> gameList;

    public memoryDB(){
        this.userList = new ArrayList<>();
        this.authList = new ArrayList<>();
        this.gameList = new ArrayList<>();
    }
}
