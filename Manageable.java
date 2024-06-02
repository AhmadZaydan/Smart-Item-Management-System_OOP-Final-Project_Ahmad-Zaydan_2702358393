import java.util.List;
import java.util.Map;

public interface Manageable {
    void addItem(AbstractItem item);
    void retrieveItem(String name, String category, String subCategory, int quantity);
    void updateItem(String name, String category, String subCategory, int quantity);
    void removeItem(String name, String category, String subCategory);
    void removeCategory(String category);
    void removeSubCategory(String category, String subCategory);
    void saveToFile(String filename);
    void loadFromFile(String filename);
    void viewItems();
    Map<String, Map<String, List<AbstractItem>>> getItems();
}

