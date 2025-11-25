    import java.util.HashMap;
    import java.util.Map;
    import java.util.*;
    import java.io.*;
    import java.util.stream.Collectors;
    public class Warehouse {
        //Создаем карту товаров
        private Map<Integer, Product> productMap = new HashMap<>();
        //Создаем имя файла, куда будет сохранена вся информация
        String filename = "data_warehouse.bin";
        //


        //
        public void addProduct(String name, String category, double price, int stock) {
            int newId = productMap.keySet().stream() // Ищем все ID через keySet
                    .max(Integer::compareTo)  // Ищем максимальный ID через Integer::compareTo
                    .orElse(0) + 1; // Если список товаров пуст, вернет 0 и прибавит 1

            //Присваиваем новому товару новый ID
            Product product = new Product(newId, name, category, price, stock);
            //Кладем этот товар с новым ID в наш список товаров
            productMap.put(newId, product);
            System.out.println("Товар успешно добавлен с ID: " + newId);
        }

        public void editProduct(int id, String newName, String newCategory, double newPrice, int newStock) {
            //Ищем товар по ID
            Product product = productMap.get(id);

            //Если товара нет - выходим
            if (product == null) {
                System.out.println("Товар не найден");
                return;
            }

            //Обновляем Сеттеры
            product.setName(newName);
            product.setCategory(newCategory);
            product.setPrice(newPrice);
            product.setStock(newStock);

            System.out.println("Товар был обновлен");
        }

        public Map<Integer, Product> getAllProduct() {
            return productMap;
        }

        public Product getProductById(int id) {
            return productMap.get(id);
        }

        public double getTotalValue() {
            return productMap.values().stream()
                    .mapToDouble(p -> p.getPrice() * p.getStock()) // Превращаем товар в число (Цена * кол-во)
                    .sum();


        }

        public List<Product> getLowStockProducts(int limit) {
            return productMap.values().stream()
                    .filter(product -> product.getStock() < limit)
                    .collect(Collectors.toList());

        }

        public List<Product> getProductsByCategory(String category) {
            return productMap.values().stream()
                    .filter(p -> p.getCategory().equalsIgnoreCase(category))
                    .sorted(Comparator.comparing(Product::getName)) // Сортируем по имени
                    .collect(Collectors.toList());
        }

        public void save() {
            try {

                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
                out.writeObject(productMap);
                out.close();
                System.out.println("База сохранена в файл " + filename);
            } catch (IOException e) {
                System.out.println("Ошибка записи");
            }


        }

        @SuppressWarnings("unchekced") // Чтобы Java не ругалась на кастинг
        public void load() {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Файл сохранения не найден. Создан пустой файл");
                return;
            }
            try {

                ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
                this.productMap =(HashMap<Integer, Product>)in.readObject();


                in.close();

                System.out.println("База успешно загружена");

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка загрузки файла" + e.getMessage());
            }
        }


    }