package framework;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

public abstract class AbstractTest {
    static {
        Properties properties = new Properties();
        properties.setProperty("config.file", "src/test/resources/config.test.json");
        ContainerRegistry.init(properties);
    }

    @Rule
    public TestName name = new TestName();

    public String getTestResourcePrefix() {
        return "src" + File.separator + "test" + File.separator + "data" + File.separator
                + Arrays.asList(StringUtils.split(this.getClass().getPackage().getName(), ".")).stream()
                        .collect(Collectors.joining(File.separator))
                + File.separator + this.getClass().getSimpleName() + File.separator + name.getMethodName()
                + File.separator;
    }

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
}
