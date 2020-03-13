package framework;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import framework.util.JsonUtils;

public class ConfigFactoryTest extends AbstractTest {

    @Test
    public void testLoadConfig() {
        Properties arguments = new Properties();
        arguments.setProperty("testConfig.file", this.getTestResourcePrefix() + "testConfigOverride.json");
        arguments.setProperty("string", "from_properties");
        arguments.setProperty("piyo.bool", "false");
        arguments.setProperty("piyo.hoge.nLong", "5000");
        System.out.println(JsonUtils.toJsonString(ConfigFactory.loadConfig(TestConfig.class, arguments)));
    }

    public static class TestConfig {
        public String string;
        public Piyo piyo;
    }

    public static class Piyo {
        public Integer integer;
        public Boolean bool;
        public Hoge hoge;
    }

    public static class Hoge {
        public Long nLong;
    }

    @Test
    public void testConvertJsonStringToFlattenMap() {
        Map<String, JsonNode> a = ConfigFactory
                .convertJsonStringToFlattenMap("{\"database\":{\"host\":\"host\",\"username\":\"username\"}}");
        System.out.println(a);
    }

    @Test
    public void testMergeConfigurationsToJsonString() {
        LinkedHashMap<String, JsonNode> configurations = new LinkedHashMap<>();
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();
        configurations.put("hoge.fuga", objectMapper.createObjectNode().textNode("fuga"));
        configurations.put("hoge.piyo.string", objectMapper.createObjectNode().textNode("pasword"));
        configurations.put("hoge.piyo.float", objectMapper.createObjectNode().numberNode(1000.2f));
        configurations.put("hoge.piyo.long", objectMapper.createObjectNode().numberNode(3000L));
        configurations.put("hoge.piyo.boolean", objectMapper.createObjectNode().booleanNode(false));
        assertEquals(ConfigFactory.mergeConfigurationsToJsonString(configurations),
                "{\"hoge\":{\"fuga\":\"fuga\",\"piyo\":{\"float\":1000.2,\"long\":3000,\"string\":\"pasword\",\"boolean\":false}}}");
    }
}
