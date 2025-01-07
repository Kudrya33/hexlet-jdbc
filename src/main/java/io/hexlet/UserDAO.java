package io.hexlet;

import java.sql.*;
import java.util.Optional;

public class UserDAO {
    private final Connection connection;

    public UserDAO(Connection conn) {
        this.connection = conn;
    }

    public void save(User user) throws SQLException {
        if (user.getId() == null) {
            String sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPhone());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Inserting user failed, no rows affected.");
                }
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("DB did not return an id after saving an entity");
                    }
                }
            }
        }
    }

    public Optional<User> find(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String phone = resultSet.getString("phone");
                    User user = new User(username, phone);
                    user.setId(id);
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
        }
    }
}
