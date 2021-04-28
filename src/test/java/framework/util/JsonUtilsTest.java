package framework.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JsonUtilsTest {
    @Test
    public void testCreateObjectFromJsonString_String_TypeReference() {
        List<TestClass> testClasses = Arrays.asList(
                JsonUtils.createObjectFromJsonString("{\"hoge\":\"hoge\",\"fuga\":{\"piyo\":1000}}", TestClass.class),
                JsonUtils.createObjectFromJsonString("{\"hoge\":\"hoge\",\"fuga\":{\"piyo\":1000}}", TestClass.class));
        assertEquals(JsonUtils.toJsonString(testClasses),
                "[{\"hoge\":\"hoge\",\"fuga\":{\"piyo\":1000}},{\"hoge\":\"hoge\",\"fuga\":{\"piyo\":1000}}]");
    }

    @Test
    public void testCreateObjectFromJsonString_String_Class() {
        TestClass testClass = JsonUtils.createObjectFromJsonString("{\"hoge\":\"hoge\",\"fuga\":{\"piyo\":1000}}",
                TestClass.class);
        assertEquals(JsonUtils.toJsonString(testClass), "{\"hoge\":\"hoge\",\"fuga\":{\"piyo\":1000}}");
    }

    public static class TestClass {
        public String hoge = "hoge";
        public Fuga fuga = new Fuga();
    }

    public static class Fuga {
        public Integer piyo = 1000;
    }
}
