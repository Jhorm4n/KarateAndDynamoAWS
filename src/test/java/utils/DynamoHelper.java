package utils;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.core.SdkBytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DynamoHelper implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamoHelper.class);
    private final DynamoDbClient client;
    private final String tableName;
    private final String primaryKeyName;

    public DynamoHelper(String tableName, String accessKey, String secretKey, String region) {
        this(tableName, accessKey, secretKey, region, "productId");
    }

    public DynamoHelper(String tableName, String accessKey, String secretKey, String region, String primaryKeyName) {
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
        LOGGER.info("DynamoHelper inicializado para tabla: {}", tableName);
    }

    // ---------- Ingesta ----------
    public void putItem(Map<String, Object> item) {
        try {
            Map<String, AttributeValue> av = toAttributeMap(item);
            client.putItem(PutItemRequest.builder()
                    .tableName(tableName)
                    .item(av)
                    .build());
            LOGGER.debug("Item insertado correctamente: {}", item.get(primaryKeyName));
        } catch (Exception e) {
            LOGGER.error("Error al insertar item", e);
            throw new RuntimeException("Error inserting item", e);
        }
    }

    // ---------- Consulta ----------
    public Map<String, Object> getById(String id) {
        try {
            Map<String, AttributeValue> key = Map.of(primaryKeyName, AttributeValue.builder().s(id).build());
            GetItemResponse res = client.getItem(GetItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .consistentRead(true)
                    .build());
            if (res.hasItem()) {
                LOGGER.debug("Item encontrado: {}", id);
                return fromAttributeMap(res.item());
            }
            LOGGER.warn("Item no encontrado: {}", id);
            return null;
        } catch (Exception e) {
            LOGGER.error("Error al obtener item: {}", id, e);
            throw new RuntimeException("Error getting item", e);
        }
    }

    public List<Map<String, Object>> scanAll() {
        List<Map<String, Object>> out = new ArrayList<>();
        try {
            ScanRequest req = ScanRequest.builder().tableName(tableName).build();
            ScanResponse res;
            do {
                res = client.scan(req);
                if (res.hasItems()) {
                    out.addAll(res.items().stream().map(DynamoHelper::fromAttributeMap).collect(Collectors.toList()));
                }
                req = req.toBuilder().exclusiveStartKey(res.lastEvaluatedKey()).build();
            } while (res.lastEvaluatedKey() != null && !res.lastEvaluatedKey().isEmpty());
            LOGGER.debug("Scan completado. Total items: {}", out.size());
        } catch (Exception e) {
            LOGGER.error("Error al escanear tabla", e);
            throw new RuntimeException("Error scanning table", e);
        }
        return out;
    }

    // ---------- Borrado ----------
    public void deleteById(String id) {
        try {
            Map<String, AttributeValue> key = Map.of(primaryKeyName, AttributeValue.builder().s(id).build());
            client.deleteItem(DeleteItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .build());
            LOGGER.debug("Item eliminado: {}", id);
        } catch (Exception e) {
            LOGGER.error("Error al eliminar item: {}", id, e);
            throw new RuntimeException("Error deleting item", e);
        }
    }

    @Override
    public void close() {
        try {
            client.close();
            LOGGER.info("DynamoHelper cerrado correctamente");
        } catch (Exception e) {
            LOGGER.error("Error al cerrar DynamoHelper", e);
        }
    }

    // ---------- Helpers de conversión ----------
    private static Map<String, AttributeValue> toAttributeMap(Map<String, Object> src) {
        Map<String, AttributeValue> out = new HashMap<>();
        for (Map.Entry<String, Object> e : src.entrySet()) {
            Object v = e.getValue();
            AttributeValue av;
            if (v == null) {
                continue;
            } else if (v instanceof String) {
                av = AttributeValue.builder().s((String) v).build();
            } else if (v instanceof Number) {
                av = AttributeValue.builder().n(v.toString()).build();
            } else if (v instanceof byte[]) {
                av = AttributeValue.builder().b(SdkBytes.fromByteArray((byte[]) v)).build();
            } else if (v instanceof Boolean) {
                av = AttributeValue.builder().bool((Boolean) v).build();
            } else if (v instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) v;
                av = AttributeValue.builder().l(list.stream()
                        .map(o -> AttributeValue.builder().s(String.valueOf(o)).build())
                        .collect(Collectors.toList())).build();
            } else if (v instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> m = (Map<String, Object>) v;
                av = AttributeValue.builder().m(toAttributeMap(m)).build();
            } else {
                av = AttributeValue.builder().s(String.valueOf(v)).build();
            }
            out.put(e.getKey(), av);
        }
        return out;
    }

    private static Map<String, Object> fromAttributeMap(Map<String, AttributeValue> src) {
        Map<String, Object> out = new HashMap<>();
        for (Map.Entry<String, AttributeValue> e : src.entrySet()) {
            AttributeValue av = e.getValue();
            if (av.s() != null) out.put(e.getKey(), av.s());
            else if (av.n() != null) out.put(e.getKey(), parseNumber(av.n()));
            else if (av.bool() != null) out.put(e.getKey(), av.bool());
            else if (av.hasM()) out.put(e.getKey(), fromAttributeMap(av.m()));
            else if (av.hasL()) out.put(e.getKey(), av.l().stream()
                    .map(x -> x.s() != null ? x.s() : (x.n() != null ? parseNumber(x.n()) : null))
                    .collect(Collectors.toList()));
            else out.put(e.getKey(), null);
        }
        return out;
    }

    private static Number parseNumber(String n) {
        try {
            if (n.contains(".") || n.contains("e") || n.contains("E")) {
                return Double.valueOf(n);
            }
            return Long.valueOf(n);
        } catch (NumberFormatException e) {
            LOGGER.warn("Error al parsear número: {}", n, e);
            return 0L;
        }
    }
}