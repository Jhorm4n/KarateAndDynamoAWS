package factories;

import models.Item;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ItemFactory {

    private static final Faker faker = new Faker();


    private static double randomPrice(double min, double max) {
        double v = ThreadLocalRandom.current().nextDouble(min, max);
        return BigDecimal.valueOf(v)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static Item makeItem(Item overrides) {
        Item item = new Item();

        item.setProductId("p-" + java.util.UUID.randomUUID());
        item.setName(faker.commerce().productName());
        item.setCategory(faker.commerce().department());
        item.setQuantity(faker.number().numberBetween(1, 100));
        item.setPrice(randomPrice(10, 500));

        if (overrides != null) {
            item = overrides;
        }
        return item;
    }

    public static Item makeItem() {
        return makeItem(null);
    }
} 