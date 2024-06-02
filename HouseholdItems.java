import java.io.*;
import java.util.*;

public class HouseholdItems {
    private static final String FILE_PATH = "household_items.txt";

    public static String[] getTheCategoryAndSubcategory(String item) {
        Map<String, String[]> itemToCategoryMap = new HashMap<>();
        loadItemsFromFile(itemToCategoryMap);

        return checkItemExistsAndGetCategory(itemToCategoryMap, item);
    }

    private static void loadItemsFromFile(Map<String, String[]> itemToCategoryMap) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            String currentCategory = "";
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("###")) {
                    currentCategory = line.replace("###", "").trim();
                } else if (line.startsWith("-")) {
                    String[] parts = line.split(":");
                    String subCategory = parts[0].replace("-", "").trim();
                    String[] items = parts[1].split(",");
                    for (String item : items) {
                        itemToCategoryMap.put(item.trim().toLowerCase(), new String[]{currentCategory, subCategory});
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] checkItemExistsAndGetCategory(Map<String, String[]> itemToCategoryMap, String item) {
        String itemLowerCase = item.toLowerCase();
        if (itemToCategoryMap.containsKey(itemLowerCase)) {
            return itemToCategoryMap.get(itemLowerCase);
        }
        return new String[]{"Miscellaneous", "General"};
    }
}

