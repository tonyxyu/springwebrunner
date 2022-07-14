package com.WebRunnerPackage;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        CommandRunner.class,
        FormController.class,
        CommandsDisplay.class
})

public class WebRunner
{
    public static String methodSource;
    public static Class configSource;

    public static String h1Message = "Welcome to Spring Web Method Runner";

    public static String h2Message = "Please input your method and its required arguments";

    public static String customNameForMethod = "Method";

    public WebRunner() {}

    public WebRunner(String methodSource)
    {
        WebRunner.methodSource = methodSource;
    }

    public WebRunner(String methodSource, Class configSource)
    {
        WebRunner.methodSource = methodSource;
        WebRunner.configSource = configSource;
    }

    public static void setSources(String methodSource, Class configSource){
        setMethodSource(methodSource);
        setConfigSource(configSource);
    }

    public static String getMethodSource()
    {
        return methodSource;
    }

    public static void setMethodSource(String methodSource)
    {
        WebRunner.methodSource = methodSource;
    }

    public static Class getConfigSource()
    {
        return configSource;
    }

    public static void setConfigSource(Class configSource)
    {
        WebRunner.configSource = configSource;
    }

    public static String getH1Message()
    {
        return h1Message;
    }

    public static void setH1Message(String h1Message)
    {
        WebRunner.h1Message = h1Message;
    }

    public static String getH2Message()
    {
        return h2Message;
    }

    public static void setH2Message(String h2Message)
    {
        WebRunner.h2Message = h2Message;
    }

    public static String getCustomNameForMethod()
    {
        return customNameForMethod;
    }

    public static void setCustomNameForMethod(String customNameForMethod)
    {
        WebRunner.customNameForMethod = customNameForMethod;
    }
}