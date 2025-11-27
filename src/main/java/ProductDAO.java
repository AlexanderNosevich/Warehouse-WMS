import java.sql.*;
import java.util.List;
import java.util.ArrayList;
public class ProductDAO {

    // Данные для подключения вынеси в константы или отдельный класс Config, но пока можно тут
    private static final String URL = "jdbc:postgresql://localhost:5432/warehouse_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root"; //

    // Метод для добавления товара в БД
    public void save(Product product) {
        String sql = "INSERT INTO products (name, category, price, stock) VALUES (?, ?, ?, ?)";

        // try-with-resources: само закроет connection и statement
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             // RETURN_GENERATED_KEYS нужен, чтобы узнать, какой ID база присвоила товару
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Заполняем "вопросики" данными из объекта
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStock());

            // Выполняем запрос
            statement.executeUpdate();

            // Получаем сгенерированный ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1)); // Проставляем ID обратно в объект
                    System.out.println("Товар сохранен с ID: " + product.getId());
                }
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении данных:");
            e.printStackTrace();
        }

    }
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement(); // Тут параметры не нужны, поэтому просто Statement
             ResultSet resultSet = statement.executeQuery(sql)) {

            // Бежим по строкам таблицы, пока они есть
            while (resultSet.next()) {
                // 1. Вытаскиваем значения из колонок текущей строки
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String category = resultSet.getString("category");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("stock");

                // 2. Создаем объект Java (маппинг)
                // Используем конструктор, который принимает ID!
                Product product = new Product(id, name, category, price, stock);

                // 3. Добавляем в список
                products.add(product);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при загрузке данных:");
            e.printStackTrace();
        }

        return products;
    }
    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, category = ?, price = ?, stock = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStock());
            statement.setInt(5, product.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Товар с ID " + product.getId() + " обновлен!");
            } else {
                System.out.println("Не удалось найти товар с ID " + product.getId());
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при обновлении");
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Товар с ID " + id + " удален");
            } else {
                System.out.println("Товар с ID " + id + " не найден");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении");
            e.printStackTrace();
        }
    }
    // Возвращает Product или null, если не нашел
    public Product getById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        Product product = null;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    product = new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("category"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> findByCategory(String category) {
        List<Product> results = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product (resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock")
                );
                results.add(product);

            }
        } catch (SQLException e) {
            System.out.println("Ошибка поиска");
            e.printStackTrace();
        }
        return results;
    }



}

