import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {


    private static final String URL = "jdbc:postgresql://localhost:5432/warehouse_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "root"; // <--- Впиши сюда свой пароль от Postgres

    // Статический метод, чтобы открывать соединение одной строчкой
    public static Connection open() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // Если соединение не удалось — кричим об ошибке
            throw new RuntimeException("Ошибка подключения к базе данных!", e);
        }
    }
}