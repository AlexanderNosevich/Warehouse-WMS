import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbTest {
    public static void main(String[] args) {
        // 1. Адрес нашей базы (как в DBeaver)
        String url = "jdbc:postgresql://localhost:5432/warehouse_db";
        // 2. Твой логин
        String user = "postgres";
        // 3. ТВОЙ ПАРОЛЬ (впиши сюда тот, что ты вводил в DBeaver)
        String password = "root";

        try {
            // 4. Пытаемся подключиться
            Connection connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("✅ УРА! Java подключилась к базе данных!");
                connection.close(); // Всегда закрывай соединение!
            }
        } catch (SQLException e) {
            System.out.println("❌ Ошибка подключения:");
            e.printStackTrace();
        }
    }
}