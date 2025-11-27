import java.util.List;

public class DbRun {
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();
        System.out.println("--Список товаров с БД--");
        List<Product> allProducts = dao.getAll();

        for (Product product : allProducts) {
            System.out.println(product.getId() + ", " + product.getName() + " - " + product.getPrice());
        }
        int targetId = 4;
        Product chair = dao.getById(targetId);
        if (chair != null) {
            System.out.println("До: " + chair.getName() + " цена - " + chair.getPrice());

            chair.setPrice(4000.00);
            chair.setStock(20);

            dao.update(chair);

            Product updatedChair = dao.getById(targetId);
            System.out.println("После: " + updatedChair.getName() + " цена - " + updatedChair.getPrice());
        } else {
            System.out.println("Товар с ID: " + targetId + " не найден");

        }

    }
}
