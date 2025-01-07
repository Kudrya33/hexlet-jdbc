package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException {
        var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test");

        var dao = new UserDAO(conn);

        var user = new User("Maria", "888888888");
        user.getId();
        dao.save(user);
        user.getId();

        var user2 = dao.find(user.getId()).get();
        boolean b = user2.getId() == user.getId();
    }
}
