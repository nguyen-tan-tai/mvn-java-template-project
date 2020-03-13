package caphe;

import java.util.Properties;

import caphe.model.dao.JCharacterDao;
import caphe.model.entity.impl.JCharacter;
import framework.ConfigFactory;
import framework.ContainerRegistry;
import framework.model.DataSource;
import framework.util.ArgumentsUtils;
import framework.util.JsonUtils;

public class Application {
    public static void main(String... args) {
        Properties properties = ArgumentsUtils.toProperties(args);
        ContainerRegistry.bind(Config.class, ConfigFactory.loadConfig(Config.class, properties));
        ContainerRegistry.selfRegister(DataSource.class);
        ContainerRegistry.selfRegister(JCharacterDao.class);
        JCharacterDao jCharacterDao = ContainerRegistry.get(JCharacterDao.class);
        System.out.println(JsonUtils.toPrettyJsonString(jCharacterDao.selectAll()));

        JCharacter c = jCharacterDao.selectById(1L);
        c.strokeCount = 3;
        jCharacterDao.update(c);

    }
}
