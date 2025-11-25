import java.io.*;
import java.util.Scanner;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Warehouse wh = new Warehouse();

        wh.load(); // Загрузка базы

        while(true) {
            System.out.println("\n---Система управления складом---");
            System.out.println("1. Вывести все товары");
            System.out.println("2. Добавить товар");
            System.out.println("3. Посчитать общую стоимость склада");
            System.out.println("4. Поиск по категории");
            System.out.println("5. Товары с малым остатком");
            System.out.println("6. Сохранить и выйти");
            System.out.print("Выбор: ");

            String line = input.nextLine();
            int command;

            try {
                command = Integer.parseInt(line);
            } catch (NumberFormatException e){
                System.out.println(">> Ошибка! Введите число (1-6).");
                continue;
            }

            switch (command) {
                case 1:
                    Map<Integer, Product> productMap = wh.getAllProduct();
                    if (productMap.isEmpty()) {
                        System.out.println(">> Склад пуст");
                    } else {
                        for (Product p : productMap.values()) {
                            System.out.println(p);
                        }
                    }
                    break;

                case 2:
                    System.out.print("Введите наименование товара: ");
                    String name = input.nextLine();

                    System.out.print("Введите категорию: ");
                    String category = input.nextLine();

                    // Читаем строку, парсим в число
                    double price = 0;
                    try {
                        System.out.print("Введите цену (например 10.5): ");
                        price = Double.parseDouble(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка цены! Установлено 0.0");
                    }

                    int stock = 0;
                    try {
                        System.out.print("Введите количество: ");
                        stock = Integer.parseInt(input.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка количества! Установлено 0");
                    }


                    wh.addProduct(name, category, price, stock);
                    break;

                case 3:
                    double total = wh.getTotalValue();
                    System.out.println("Общая стоимость товаров = " + total);
                    break;

                case 4:
                    System.out.println("Введите категорию для поиска: ");
                    String searchCategory = input.nextLine();
                    wh.getProductsByCategory(searchCategory).forEach(System.out::println);
                    break;

                case 5:
                    System.out.print("Введите лимит количества: ");
                    try {
                        int limit = Integer.parseInt(input.nextLine());
                        wh.getLowStockProducts(limit).forEach(System.out::println);
                    } catch (NumberFormatException e) {
                        System.out.println("Нужно ввести целое число!");
                    }
                    break;

                case 6:
                    wh.save();
                    System.out.println("До свидания!");
                    System.exit(0); // Полный выход
                    break;

                default:
                    System.out.println("Нет такой команды.");
            }
        }
    }
}