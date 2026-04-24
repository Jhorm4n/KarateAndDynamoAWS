package utils;

import net.datafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ItemFactory {

    private static final Faker faker = new Faker(new Locale("es", "CO"));


    private static double randomPrice(double min, double max) {
        double v = ThreadLocalRandom.current().nextDouble(min, max);
        return BigDecimal.valueOf(v)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static Map<String, Object> makeItem(Map<String, Object> overrides) {
        Map<String, Object> item = new HashMap<>();

        String id = "p-" + java.util.UUID.randomUUID();
        String name = faker.commerce().productName();
        String category = faker.commerce().department();
        int quantity = faker.number().numberBetween(1, 100);
        double price = randomPrice(10, 500);

        item.put("productId", id);
        item.put("name", name);
        item.put("category", category);
        item.put("quantity", quantity);
        item.put("price", price);

        if (overrides != null) {
            item.putAll(overrides);
        }
        return item;
    }

    public static Map<String, Object> makeItem() {
        return makeItem(null);
    }
} 