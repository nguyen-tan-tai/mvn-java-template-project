package caphe;

import java.util.Properties;

import javax.inject.Inject;

import framework.ContainerRegistry;

public class Application {

    @Inject
    public Config config;

    public static void main(String... args) throws Exception {
        ContainerRegistry.init(new Properties());
        Application app = ContainerRegistry.selfRegister(Application.class);
        System.out.println(app.config.getDatabase().getHost());
    }
}
