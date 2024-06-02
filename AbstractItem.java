import java.io.Serializable;

public abstract class AbstractItem implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected int quantity;
    protected String category;
    protected String subCategory;

    public AbstractItem(String name, int quantity, String category, String subCategory) {
        this.name = name;
        this.quantity = quantity;
        this.category = category;
        this.subCategory = subCategory;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public abstract String toString();
}
