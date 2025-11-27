import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class WarehouseTest {

    // ТЕСТ 1: Проверяет, что товар просто добавляется
    @Test
    public void testAddProduct() {
        Warehouse wh = new Warehouse();

        // Добавляем: Цена 100, кол-во 10
        wh.addProduct("Phone", "Electronics", 100.0, 10);

        Map<Integer, Product> products = wh.getAllProduct();

        // Проверяем, что товар добавился
        assertEquals(1, products.size());

        // Проверяем цену (Ждем 100)
        Product p = products.get(1);
        assertEquals(100.0, p.getPrice());
    }

    // ТЕСТ 2: Проверяет математику (Сумму склада)
    @Test
    public void testTotalValue() {
        Warehouse wh = new Warehouse();

        // Добавляем: Цена 50, кол-во 2. (Итого должно быть 100)
        wh.addProduct("Plane", "Aviation", 50.0, 2);

        // Считаем общую стоимость
        double total = wh.getTotalValue();

        // Проверяем: 50 * 2 должно быть 100
        assertEquals(100.0, total, 0.001);
    }
}