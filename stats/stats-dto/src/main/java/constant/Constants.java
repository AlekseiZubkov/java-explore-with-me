package constant;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String PATTERN_FOR_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER_FOR_DATETIME = DateTimeFormatter.ofPattern(PATTERN_FOR_DATETIME);

}