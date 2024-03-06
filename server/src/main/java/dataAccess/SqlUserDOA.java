package dataAccess;

import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

import static dataAccess.DatabaseManager.createDatabase;
import static dataAccess.DatabaseManager.getConnection;

public class SqlUserDOA implements UserDataInterface{

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserData getUser(String username, String password) {
        try (var con = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (var ps = con.prepareStatement(statement)){
                ps.setString(1, username);

                try (var rs = ps.executeQuery()){
                    if (rs.next()){
                        if (encoder.matches(password, rs.getString("password"))) {
                            return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                        }
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void createUser(UserData user) {
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

                String hashedPassword = encoder.encode(user.password());


                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, user.email());


                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public void clear() {
        try (var con = getConnection()) {
            var statement = "TRUNCATE user";
            var clearTableStatement = con.prepareStatement(statement);
            clearTableStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
