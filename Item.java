import java.io.Serializable;

public class Item extends AbstractItem {
    public Item(String name, int quantity, String category, String subCategory) {
        super(name, quantity, category, subCategory);
    }

    @Override
    public String toString() {
        return "Item: " + name + ", quantity: " + quantity;
    }
}
