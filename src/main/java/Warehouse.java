import java.util.List;

public class Warehouse {

    // 1. БОЛЬШЕ НЕТ MAP
    // Было: private Map<Integer, Product> products = new HashMap<>();

    // СТАЛО: Подключаем наш слой работы с БД
    private final ProductDAO productDAO = new ProductDAO();

    // 2. Метод добавления продукта
    // Мы больше не считаем ID сами. Мы просто отдаем объект в DAO.
    public void addProduct(String name, String category, double price, int stock) {
        // Создаем продукт без ID (в базе он присвоится сам)
        // Если у тебя в конструкторе Product обязательно нужен ID,
        // передай туда 0 или сделай второй конструктор без ID.
        Product product = new Product(0, name, category, price, stock);

        productDAO.save(product);
        System.out.println("✅ Продукт успешно добавлен в базу данных.");
    }

    // 3. Метод получения списка (вместо вывода из Map)
    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    // 4. Расчет общей стоимости
    // Раньше: products.values().stream()...
    // Сейчас: берем всё из базы и считаем.
    // (P.S. Позже мы перенесем это в SQL запрос "SELECT SUM...", это будет быстрее)
    public double getTotalValue() {
        List<Product> allProducts = productDAO.getAll();

        return allProducts.stream()
                .mapToDouble(p -> p.getPrice() * p.getStock())
                .sum();
    }

    // 5. Поиск по ID (нужен для меню "Поиск" и для "Update/Delete")
    // Тебе нужно добавить метод getById в ProductDAO, если его нет.
    // Если пока нет, можно сделать "костыль" через стрим (но это временно!):
    public Product findProductById(int id) {
        return productDAO.getAll().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // 6. Обновление (вызываем метод DAO)
    public void updateProduct(Product product) {
        productDAO.update(product);
    }

    // 7. Удаление (вызываем метод DAO)
    public void deleteProduct(int id) {
        productDAO.delete(id);
    }

    public List<Product> findByCategory(String category) {
        return productDAO.findByCategory(category);
    }
}