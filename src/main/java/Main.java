import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Инициализируем склад (он внутри уже подключен к БД через ProductDAO)
        Warehouse warehouse = new Warehouse();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- WAREHOUSE WMS (SQL EDITION) ---");
            System.out.println("1. Список товаров");
            System.out.println("2. Добавить товар");
            System.out.println("3. Удалить товар");
            System.out.println("4. Изменить товар (Update)");
            System.out.println("5. Общая стоимость склада");
            System.out.println("6. Поиск по категории");
            System.out.println("7. Продать товар");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    List<Product> products = warehouse.getAllProducts();
                    if (products.isEmpty()) {
                        System.out.println("Склад пуст.");
                    } else {
                        System.out.println("ID | Название | Категория | Цена | Остаток");
                        for (Product p : products) {
                            System.out.printf("%d | %s | %s | %.2f | %d%n",
                                    p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                        }
                    }
                }
                case "2" -> {
                    System.out.print("Название: ");
                    String name = scanner.nextLine();
                    System.out.print("Категория: ");
                    String category = scanner.nextLine();

                    System.out.print("Цена: "); // Валидацию пока опустим для краткости
                    double price = Double.parseDouble(scanner.nextLine());

                    System.out.print("Количество: ");
                    int stock = Integer.parseInt(scanner.nextLine());

                    warehouse.addProduct(name, category, price, stock);
                }
                case "3" -> {
                    System.out.print("Введите ID товара для удаления: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    warehouse.deleteProduct(id);
                }
                case "4" -> {
                    System.out.print("Введите ID товара для обновления: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    // Сначала ищем, есть ли такой товар
                    Product existing = warehouse.findProductById(id);
                    if (existing == null) {
                        System.out.println("Товар с таким ID не найден.");
                        break;
                    }

                    System.out.println("Введите новые данные (или старые, если не меняем):");
                    System.out.print("Новое название: ");
                    String name = scanner.nextLine();
                    System.out.print("Новая категория: ");
                    String category = scanner.nextLine();
                    System.out.print("Новая цена: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    System.out.print("Новое количество: ");
                    int stock = Integer.parseInt(scanner.nextLine());

                    // Создаем объект с ТЕМ ЖЕ ID, но новыми данными
                    Product updatedProduct = new Product(id, name, category, price, stock);
                    warehouse.updateProduct(updatedProduct);
                }
                case "5" -> System.out.println("Общая стоимость: " + warehouse.getTotalValue());
                case "6" -> {

                    System.out.println("Поиск по категории");
                    String searchCategory = scanner.nextLine();
                    List<Product> products = warehouse.findByCategory(searchCategory);
                    warehouse.findByCategory(searchCategory);
                    for (Product p : products) {
                        System.out.printf("%d | %s | %s | %.2f | %d%n",
                                p.getId(), p.getName(), p.getCategory(), p.getPrice(), p.getStock());
                    }
                }
                case "7" -> {
                    System.out.println("Меню продажи товаров");
                    System.out.print("Введите ID товара для поиска: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.println("Введите количество товара");
                    int quantity = Integer.parseInt(scanner.nextLine());
                    warehouse.sellProduct(id ,quantity);
                }
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверная команда.");
            }
        }
    }
}