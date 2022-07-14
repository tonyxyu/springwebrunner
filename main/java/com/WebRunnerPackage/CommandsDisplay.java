package com.WebRunnerPackage;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.reflections.Reflections;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

@Component("commandDisplay")
public class CommandsDisplay
{
    private static String packageName = WebRunner.getMethodSource();

    public static TreeMap<String, List<String>> getCommandTable(boolean showArguments)
    {

        TreeMap<String, List<String>> commandTable = new TreeMap<String, List<String>>();

        List<Class> commandClasses = getAnnotatedClasses();

        for (Class commandClass : commandClasses)
        {
            List<String> CommandList = getCommandsList(commandClass, showArguments);
            String CommandType = getCommandType(commandClass);
            commandTable.put(CommandType, CommandList);
        }

        return commandTable;
    }

    // Return a Hashmap from Class object to list of Method objects
    public static HashMap<Class, List<Method>> getClassMethodTable()
    {

        HashMap<Class, List<Method>> classMethodTable = new HashMap<Class, List<Method>>();

        List<Class> commandClasses = getAnnotatedClasses();

        for (Class commandClass : commandClasses)
        {
            List<Method> methodList = getMethodsWithAnnotation(commandClass, ShellMethod.class);
            classMethodTable.put(commandClass, methodList);
        }
        return classMethodTable;
    }


    // Return all methods in the form of a list.
    public static List<Method> getCommandMethodList()
    {
        List<Class> commandClasses = getAnnotatedClasses();
        List<Method> methodList = new ArrayList<>();

        for (Class commandClass : commandClasses)
        {
            Method[] methods = commandClass.getDeclaredMethods();
            methodList.addAll(Arrays.asList(methods));
        }

        return methodList;
    }

    //Get all methods in a specified class.
    private static List<Method> getMethodsWithAnnotation(Class<?> commandClass, Class<? extends Annotation> annotation)
    {
        List<Method> methodList = new ArrayList<>();

        for (final Method method : commandClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                methodList.add(method);
            }
        }

        return methodList;
    }

    // Return names of the commands inside a class in a list of String
    private static List<String> getCommandsList(Class<?> commandClass, boolean showArguments)
    {
        Method[] methods = getMethodsWithAnnotation(commandClass, ShellMethod.class).toArray(new Method[0]);
        List<String> methodNames = new ArrayList<>();

        for (Method method : methods) {
            String methodName = method.getName();

            if (showArguments) {
                Class[] parameterNames = method.getParameterTypes();
                String[] simpleNames = new String[parameterNames.length];

                for (int i = 0; i < parameterNames.length; i++) {
                    simpleNames[i] = parameterNames[i].getSimpleName();
                }
                methodName += Arrays.toString(simpleNames)
                        .replace("[", "(")
                        .replace("]", ")");
            }
            methodNames.add(methodName);
        }

        Collections.sort(methodNames);

        return methodNames;
    }

    private static String getCommandType(Class<?> commandClass)
    {
        return commandClass.getSimpleName();
    }

    private static List<Class> getAnnotatedClasses()
    {
        List<Class> commandClasses = new ArrayList<Class>();
        Set<Class<?>> classWithAnnotationSet = getJDKFunctinalInterfaces();

        for (Class classInstance : classWithAnnotationSet)
        {
            commandClasses.add(classInstance);
        }
        return commandClasses;
    }

    //    TODO : use autoservice to fetch class names
    private static Set<Class<?>> getJDKFunctinalInterfaces()
    {
        Reflections reflections = new Reflections(packageName);
        System.err.println(packageName);
        return reflections.getTypesAnnotatedWith (ShellComponent.class);
    }

    public static String getPackageName()
    {
        return packageName;
    }

    public static void setPackageName(String packageName)
    {
        CommandsDisplay.packageName = packageName;
    }

}
