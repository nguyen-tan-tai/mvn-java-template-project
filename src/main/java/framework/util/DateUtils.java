package framework.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    
    
    public static String toYyyyMmDdWithHyphen(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String toLocalDate(String string) {
        // TODO Auto-generated method stub
        return null;
    }

    public static String toLocalDateTime(String string) {
        // TODO Auto-generated method stub
        return null;
    }
}
