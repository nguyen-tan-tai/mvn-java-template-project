package framework.util;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ArgumentsUtils {

    public static Properties toProperties(String[] args) {
        try {
            Options options = new Options();
            Option.Builder builder = Option.builder().longOpt("D").argName("property=value").hasArgs().valueSeparator()
                    .numberOfArgs(2);
            options.addOption(builder.build());
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);
            if (!commandLine.hasOption("D")) {
                return new Properties();
            }
            return commandLine.getOptionProperties("D");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
