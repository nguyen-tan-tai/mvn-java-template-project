package framework.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {
    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public static ObjectNode createObjectNode() {
        return getObjectMapper().createObjectNode();
    }

    public static JsonNode createJsonNodeJsonString(String json) {
        try {
            return new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createObjectFromJsonString(String json, TypeReference<T> typeReference) {
        try {
            return new ObjectMapper().readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createObjectFromJsonString(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toPrettyJsonString(Object object) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getPossibleKeys(JsonNode root, String path) {
        List<String> keys = new ArrayList<>();
        JsonNode datum = root;
        if (path != null) {
            datum = getNodeByPath(root, path);
        }
        datum.fields().forEachRemaining(s -> {
            String cp = (path == null ? "" : path + ".") + s.getKey();
            if (s.getValue().isContainerNode()) {
                if (s.getValue().isArray()) {
                    keys.add(cp);
                } else if (s.getValue().toString().equals("{}")) {
                    keys.add(cp);
                } else {
                    keys.addAll(getPossibleKeys(root, cp));
                }
            } else {
                keys.add(cp);
            }
        });
        return keys;
    }

    public static JsonNode getNodeByPath(JsonNode root, String path) {
        String[] nodeNames = StringUtils.split(path, ".");
        JsonNode datum = root;
        for (String nodeName : nodeNames) {
            if (!datum.has(nodeName)) {
                return MissingNode.getInstance();
            }
            datum = datum.get(nodeName);
        }
        return datum;
    }
}
