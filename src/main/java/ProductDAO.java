
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // 1. Убрали константы URL/USER/PASSWORD. Все настройки теперь живут в ConnectionManager.

    public void createTable() {
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            statement.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    category VARCHAR(100),
                    price DOUBLE PRECISION,
                    stock INT
                );
                """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS sales (
                    id SERIAL PRIMARY KEY,
                    product_id INT REFERENCES products(id),
                    quantity INT,
                    sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                );
                """);

            System.out.println("✅ Таблицы products и sales проверены/созданы.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE IF EXISTS sales");
            statement.execute("DROP TABLE IF EXISTS products");
            System.out.println("💥 Все таблицы удалены (Сброс базы).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Product product) {
        String sql = "INSERT INTO products (name, category, price, stock) VALUES (?, ?, ?, ?)";

        // ИСПРАВЛЕНО: Используем ConnectionManager
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getCategory());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStock());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                    System.out.println("✅ Товар сохранен с ID: " + product.getId());
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

        // ИСПРАВЛЕНО: Используем ConnectionManager
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock")
                );
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

        // ИСПРАВЛЕНО: Используем ConnectionManager
        try (Connection connection = ConnectionManager.open();
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
        // ИСПРАВЛЕНО: Используем ConnectionManager
        try (Connection connection = ConnectionManager.open();
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

    public Product getById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        Product product = null;

        // ИСПРАВЛЕНО: Используем ConnectionManager
        try (Connection connection = ConnectionManager.open();
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
        // ИСПРАВЛЕНО: Используем ConnectionManager
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                results.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка поиска");
            e.printStackTrace();
        }
        return results;
    }

    public void saveSale(int productId, int quantity) {
        String sql = "INSERT INTO sales (product_id, quantity) VALUES (?, ?)";
        // ИСПРАВЛЕНО: Используем ConnectionManager (тут так и было, но теперь это та же база)
        try (Connection connection = ConnectionManager.open();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, productId);
            statement.setInt(2, quantity);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Ошибка при записи в журнал");
            e.printStackTrace();
        }
    }
    }
