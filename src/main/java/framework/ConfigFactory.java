package framework;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.CaseFormat;

import framework.util.FileUtils;
import framework.util.JsonUtils;

public class ConfigFactory {
    public static <T> T loadConfig(Class<T> clazz, Properties arguments) {
        LinkedHashMap<String, JsonNode> configurations = new LinkedHashMap<>();
        String configFileName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, clazz.getSimpleName());
        File file = new File(clazz.getClassLoader().getResource(configFileName + ".json").getFile());
        if (file.exists()) {
            configurations.putAll(convertJsonStringToFlattenMap(FileUtils.readFileToString(file)));
        }
        if (arguments.containsKey(configFileName + ".file")) {
            File overrideConfigFile = new File(arguments.getProperty(configFileName + ".file"));
            if (overrideConfigFile.exists()) {
                configurations.putAll(convertJsonStringToFlattenMap(FileUtils.readFileToString(overrideConfigFile)));
            }
        }
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        arguments.stringPropertyNames().stream().filter(key -> !key.endsWith(".file")).forEach(key -> {
            configurations.put(key, objectMapper.createObjectNode().textNode(arguments.getProperty(key)));
        });
        return JsonUtils.createObjectFromJsonString(mergeConfigurationsToJsonString(configurations), clazz);
    }

    public static String mergeConfigurationsToJsonString(LinkedHashMap<String, JsonNode> configurations) {
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        ObjectNode data = objectMapper.createObjectNode();
        for (Entry<String, JsonNode> entry : configurations.entrySet()) {
            String[] keyParts = StringUtils.split(entry.getKey(), ".");
            int keyLength = keyParts.length;
            ObjectNode currentNode = data;
            for (int i = 0; i < keyLength; i++) {
                String key = keyParts[i];
                if (i == keyLength - 1) {
                    currentNode.set(key, entry.getValue());
                    break;
                }
                if (!currentNode.has(key) || currentNode.get(key) == NullNode.getInstance()) {
                    currentNode.set(key, objectMapper.createObjectNode());
                }
                currentNode = (ObjectNode) currentNode.get(key);
            }
        }
        return JsonUtils.toJsonString(data);
    }

    public static LinkedHashMap<String, JsonNode> convertJsonStringToFlattenMap(String json) {
        JsonNode data = JsonUtils.createJsonNodeJsonString(json);
        List<String> possibleKeys = JsonUtils.getPossibleKeys(data, null);
        LinkedHashMap<String, JsonNode> configurations = new LinkedHashMap<>();
        for (String key : possibleKeys) {
            configurations.put(key, JsonUtils.getNodeByPath(data, key));
        }
        return configurations;
    }
}
