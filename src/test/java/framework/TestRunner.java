package framework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

public class TestRunner extends Runner {

    private Class<?> testClass;

    public TestRunner(Class<?> testClass) {
        super();
        this.testClass = testClass;
    }

    @Override
    public Description getDescription() {
        return Description.createTestDescription(testClass, "My runner description");
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            Object testObject = testClass.newInstance();
            for (Field field : this.testClass.getFields()) {
                if (field.getAnnotation(Inject.class) != null) {
                    if (ContainerRegistry.exist(field.getType())) {
                        field.set(testObject, ContainerRegistry.get(field.getType()));
                    } else {
                        field.set(testObject, ContainerRegistry.newInstance(field.getType()));
                    }
                }
            }
            for (Method method : testClass.getMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    notifier.fireTestStarted(Description.createTestDescription(testClass, method.getName()));
                    method.invoke(testObject);
                    notifier.fireTestFinished(Description.createTestDescription(testClass, method.getName()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
