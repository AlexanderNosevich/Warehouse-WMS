import java.io.Serializable;

public class Product implements Serializable {
    private int id; // Уникальный номер (генерируется автоматически)
    private String name; // Навание товара
    private String category; // Категория (Еда, электроника и т.д)
    private double price; // Цена за единицу
    private int stock; // Количество на складе

    // Создаем конструктор
    public Product(int id, String name, String category, double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public Product(String name, String category, double price, int stock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    //Создаем Геттеры
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public double getPrice() {
        return price;
    }
    public int getStock() {
        return stock;
    }

    //Создаем Сеттеры
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }

    //Создаем метод toString для вывода в консоль
    @Override
    public String toString() {
        return "ID товара " + getName() + " ---" + getId() + ". Категория ---" +
                getCategory() + ". Цена за единицу = " + getPrice() +
                ". В наличии на складе " + getStock() + " шт.";
    }

}
