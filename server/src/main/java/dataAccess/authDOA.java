package dataAccess;

import model.AuthData;

public class authDOA {
    public AuthData getAuth(String username, memoryDB data){
        for (AuthData auth: data.authList){
            if (username == auth.username()){
                return auth;
            } else{
                return null;
            }
        }
    }


}
