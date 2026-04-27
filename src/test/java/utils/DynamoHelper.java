package utils;

import models.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

public class DynamoHelper implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoHelper.class);
    private final DynamoDbClient client;
    private final String tableName;
    private final String primaryKeyName;

    public DynamoHelper(String tableName,
                        String accessKey,
                        String secretKey,
                        String region) {
        this(tableName, accessKey, secretKey, region, "productId");
    }

    public DynamoHelper(String tableName,
                        String accessKey,
                        String secretKey,
                        String region,
                        String primaryKeyName) {

        this.client = DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();

        this.tableName = tableName;
        this.primaryKeyName = primaryKeyName;

        LOGGER.info("DynamoHelper inicializado para tabla '{}'", tableName);
    }

    public void putItem(Item item) {
        try {
            Map<String, AttributeValue> av = itemToAttributeMap(item);

            client.putItem(PutItemRequest.builder()
                    .tableName(tableName)
                    .item(av)
                    .build());

            LOGGER.debug("Item insert: {}", item.getProductId());

        } catch (Exception e) {
            LOGGER.error("Error inserting item: ", e);
            throw new RuntimeException("Error inserting item", e);
        }
    }

    public Item getById(String id) {
        try {
            Map<String, AttributeValue> key = Map.of(
                    primaryKeyName,
                    AttributeValue.builder().s(id).build()
            );

            GetItemResponse res = client.getItem(GetItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .consistentRead(true)
                    .build());

            if (!res.hasItem()) {
                LOGGER.warn("Item not found: {}", id);
                return null;
            }

            LOGGER.debug("Item found: {}", id);
            return attributeMapToItem(res.item());

        } catch (Exception e) {
            LOGGER.error("Error getting item: {}", id, e);
            throw new RuntimeException("Error getting item", e);
        }
    }

    public List<Item> scanAll() {
        List<Item> result = new ArrayList<>();

        try {
            ScanRequest request = ScanRequest.builder()
                    .tableName(tableName)
                    .build();

            ScanResponse response;

            do {
                response = client.scan(request);

                if (response.hasItems()) {
                    result.addAll(
                            response.items().stream()
                                    .map(DynamoHelper::attributeMapToItem)
                                    .toList()
                    );
                }

                request = request.toBuilder()
                        .exclusiveStartKey(response.lastEvaluatedKey())
                        .build();

            } while (response.lastEvaluatedKey() != null && !response.lastEvaluatedKey().isEmpty());

            LOGGER.debug("Scan succesful. Total items: {}", result.size());

        } catch (Exception e) {
            LOGGER.error("Error scanning table: ", e);
            throw new RuntimeException("Error scanning table", e);
        }

        return result;
    }

    public void deleteById(String id) {
        try {
            Map<String, AttributeValue> key = Map.of(
                    primaryKeyName,
                    AttributeValue.builder().s(id).build()
            );

            client.deleteItem(DeleteItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .build());

            LOGGER.debug("Delete item: {}", id);
        } catch (Exception e) {
            LOGGER.error("Error deleting item: {}", id, e);
            throw new RuntimeException("Error deleting item", e);
        }
    }

    @Override
    public void close() {
        try {
            client.close();
            LOGGER.info("DynamoHelper closed successfully");
        } catch (Exception e) {
            LOGGER.error("Error closed DynamoHelper", e);
        }
    }


    private static Map<String, AttributeValue> itemToAttributeMap(Item item) {
        Map<String, AttributeValue> map = new HashMap<>();

        map.put("productId", AttributeValue.builder().s(item.getProductId()).build());
        map.put("name", AttributeValue.builder().s(item.getName()).build());
        map.put("category", AttributeValue.builder().s(item.getCategory()).build());
        map.put("quantity", AttributeValue.builder().n(String.valueOf(item.getQuantity())).build());
        map.put("price", AttributeValue.builder().n(String.valueOf(item.getPrice())).build());

        return map;
    }

    private static Item attributeMapToItem(Map<String, AttributeValue> map) {
        Item item = new Item();

        item.setProductId(map.get("productId").s());
        item.setName(map.get("name").s());
        item.setCategory(map.get("category").s());
        item.setQuantity(Integer.parseInt(map.get("quantity").n()));
        item.setPrice(Double.parseDouble(map.get("price").n()));

        return item;
    }
}