package dataAccess;

import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static dataAccess.DatabaseManager.createDatabase;
import static dataAccess.DatabaseManager.getConnection;

public class UserDOA {

    private final ArrayList<UserData> userList;

    public UserDOA(){
        this.userList = new ArrayList<>();
    }
    public UserData getUser(String username, String password){
        for (UserData user: this.userList){
            if (Objects.equals(user.username(), username) && Objects.equals(user.password(), password)){
                return user;
            }
        }
        return null;
    }

    public void createUser(UserData user) throws DataAccessException, SQLException {
        this.userList.add(user);

        try (var con = getConnection()) {
            createDatabase();

            con.setCatalog("chess");

            var createUserTable = """
                    CREATE TABLE  IF NOT EXISTS user (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL
                    )""";

            try (var createTableStatement = con.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            }

            try (var preparedStatement = con.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());

                preparedStatement.executeUpdate();
            }
        }
    }

    public void clear(){
        this.userList.clear();
    }
}
