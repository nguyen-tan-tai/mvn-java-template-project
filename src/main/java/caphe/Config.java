package caphe;

import lombok.Data;

@Data
public class Config {
    private Database database;

    @Data
    public static class Database {
        private String host;
        private String username;
        private String password;
    }
}
