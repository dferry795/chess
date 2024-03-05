package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static dataAccess.DatabaseManager.createDatabase;
import static dataAccess.DatabaseManager.getConnection;

public class MemoryUserDOA implements UserDataInterface {

    private final ArrayList<UserData> userList;

    public MemoryUserDOA(){
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

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode(user.password());

                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.email());

                preparedStatement.executeUpdate();
            }
        }
    }

    public void clear(){
        this.userList.clear();


    }
}
