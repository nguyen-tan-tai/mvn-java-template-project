package framework;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import caphe.Config;

public class ContainerRegistry {
    private static Map<Class<?>, Object> container = new HashMap<>();

    public static <T> T bind(Class<?> clazz, T instance) {
        container.put(clazz, instance);
        return instance;
    }

    public static <T> T selfRegister(Class<T> clazz) {
        try {
            T instance = newInstance(clazz);
            bind(clazz, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            T instance = get(clazz);
            if (instance == null) {
                instance = clazz.newInstance();
            }
            for (Field field : clazz.getFields()) {
                if (field.getAnnotation(Inject.class) != null) {
                    if (exist(field.getType())) {
                        field.set(instance, get(field.getType()));
                    } else {
                        field.set(instance, newInstance(field.getType()));
                    }
                }
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> clazz) {
        return (T) container.get(clazz);
    }

    public static <T> boolean exist(Class<T> clazz) {
        return container.containsKey(clazz);
    }

    public static void init(Properties properties, Class<?>... clazzes) {
        ContainerRegistry.bind(Config.class, ConfigFactory.loadConfig(Config.class, properties));
        ContainerRegistry.bind(Logger.class, LogManager.getLogger());
        for (Class<?> clazz : clazzes) {
            ContainerRegistry.selfRegister(clazz);
        }
    }
}
