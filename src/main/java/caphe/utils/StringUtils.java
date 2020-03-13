package caphe.utils;

public class StringUtils {

    /**
     * Remove the UTF_BOM(uFEFF) at the beginning of string
     *
     * @param input
     * @return String
     */
    public static String removeUTF8BOM(String input) {
        String UTF8_BOM = "\uFEFF";
        if (input.startsWith(UTF8_BOM)) {
            input = input.substring(1);
        }
        return input;
    }
}
