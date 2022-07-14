# Spring Web Runner

A simple package to run spring shell method on a web server.

## Description

The Spring Web Runner package allows users to run shell
methods with specific annotations on a web server. The web UI
provides options to run the methods both in an input box or in
a separate page with each argument in a separate input box. 
 
Please note that all method classes of interest must be appropriately
annotated, as well as each method and its parameters. 

In this version, the classes
containing methods to be run must be annotated with @ShellComponent. Methods must be 
annotated with @ShellMethod, and the corresponding parameters must be annotated
with @ShellOption with the value field set to the name of the parameter. 
## Getting Started

### Dependencies

* springframework.boot
* springframework.shell
* User's custom configurations

### Installing

* How/where to download your program
* Any modifications needed to be made to files/folders

### Executing program

* After importing the WebRunnerPackage, use the @import annotation to import the WebRunner class.
* Use WebRunner.setSources() to set the path to the working directory as well as the configuration bean containing the methods to run.
* The title and description of the console can be customized before launching the server. 
* Start web server with SpringApplication.run(YourMainClass.class, args);
```
@EnableAutoConfiguration(exclude = {JLineShellAutoConfiguration.class,
        StandardCommandsAutoConfiguration.class})
@Configuration
@Import({
    WebRunner.class
})

public class TestMain
{
    public static void main(String[] args) {
        final String sourcePath = "web.runner.test";
        final Class configClass = TestConfiguration.class;
        WebRunner.setSources(sourcePath, cla);

        SpringApplication.run(TestMain.class, configClass);
    }
}
```

[//]: # ()
[//]: # (## Help)

[//]: # ()
[//]: # (Any advise for common problems or issues.)

[//]: # (```)

[//]: # (command to run if program contains helper info)

[//]: # (```)

## Authors

Xintong Yu [@DomPizzie](https://twitter.com/dompizzie)

## Version History

* 0.1
    * Initial Release


