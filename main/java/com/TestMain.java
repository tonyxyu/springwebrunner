package com;
import com.WebRunnerPackage.WebRunner;
import com.simpleTests.TestConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;


@EnableAutoConfiguration(exclude = {JLineShellAutoConfiguration.class,
        StandardCommandsAutoConfiguration.class})
@Configuration
@Import({
    WebRunner.class
})
public class TestMain
{
    public static void main(String[] args) {
        final String source = "com.cantor.aqdev.web.runner.simpleTests";
        final Class cla = TestConfiguration.class;
        WebRunner.setSources(source, cla);

        SpringApplication.run(TestMain.class, args);
    }
}