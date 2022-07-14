package com.WebRunnerPackage;

import org.agrona.concurrent.AgentRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.shell.standard.ShellOption;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.System;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CommandRunner
{
    public static AgentRunner runner;
    private static final String[] EMPTY_ARGS = {};
    private static final String[] UNCASTASBLE_ARGS = {"INCORRECT INPUT ARGUMENT TYPE"};
    private static final String[] MISMATCH_NUM_ARGS = {"INCORRECT NUMBER OF ARGUMENTS"};
    private static final String SUCCESS = "SUCCESS!";

    public String executeCommand(String line) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
    {


        line = cleanWhiteSpace(line);

        if (line != null) {
            int firstSpaceIndex = line.indexOf(' ');

            String cmdName;
            String[] cmdArgs;

            if (firstSpaceIndex == -1) {
                cmdName = line;
                cmdArgs = EMPTY_ARGS;
            }
            else {
                cmdName = line.substring(0, firstSpaceIndex);
                cmdArgs = line.substring(firstSpaceIndex + 1).split(" ");
            }

            return executeCommandWithArgs(cmdName, cmdArgs);
        }

        return "No Command";
    }

    public String executeCommandWithArgs(String cmd, String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebRunner.configSource);

        String cmdName = cleanWhiteSpace(cmd);
        if (cmdName != null)
        {
            String[] cmdArgs;

            if (args.length == 0) {
                cmdArgs = EMPTY_ARGS;
            } else {
                cmdArgs = cleanWhiteSpaceArray(args);
                System.err.println(Arrays.toString(cmdArgs));
//                if (hasEmpty(cmdArgs)) {
//                    System.err.println("empty");
//                    return "Arguments contain empty input";
//                }
            }

            Class commandClass = getClass(cmdName);

            if (commandClass == null) {
                return "Method not found";
            }

            Object classObj  = context.getBean(commandClass);

            System.err.println(cmdName);

            try {
                Method commandMethod = getMethod(cmdName);

                if (commandMethod == null) {
                    return "Method not found";
                }

                commandMethod.setAccessible(true);


                Class[] cArgTypes = getArgumentTypes(cmdName);
                Object[] cArgs = createArguments(cArgTypes, cmdArgs);

                if (Arrays.equals(cArgs, MISMATCH_NUM_ARGS)) {
                    return "Wrong number of arguments";
                }

                if (Arrays.equals(cArgs, UNCASTASBLE_ARGS)) {
                    return "Incorrect input argument type";
                }

                try {
                    if (cmdArgs.length == 0){
                        commandMethod.invoke(classObj);
                    } else {
                        commandMethod.invoke(classObj, cArgs);
                    }

                    System.err.println("Successful method call!");
                    return SUCCESS;
                }
                catch (InvocationTargetException e) {
                    throw new InvocationTargetException(e);
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return "Unknown Error";
    }

    // Return an array of the types of the command method parameters.
    Class<?>[] getArgumentTypes(String cmdName)
    {
        Method method = getMethod(cmdName);

        if (method != null) {
            return method.getParameterTypes();
        }

        return null;
    }

    public String[] getArgumentTypesInString(String cmdName)
    {
        Method method = getMethod(cmdName);

        assert(method != null);
        Class<?>[] parameterTypes = method.getParameterTypes();

        String[] parameterTypeNames = new String[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameterTypeNames[i] = parameterTypes[i].getSimpleName();
        }

        return parameterTypeNames;
    }

    public static String[] getParameterNames(String methodName) {
        Method method = getMethod(methodName);
        assert(method != null);

        Annotation[][] annotationArray = method.getParameterAnnotations();

        String[] parameterNames = new String[annotationArray.length];
        // Loop through each parameter
        for (int i  = 0; i < annotationArray.length; i++) {
            Annotation annotation = annotationArray[i][0];
            ShellOption customAnnotation = (ShellOption) annotation;
            int len = customAnnotation.value().length;
            parameterNames[i] = customAnnotation.value()[len - 1].replaceAll("[-_+^]*","").trim();
        }
        return parameterNames;
    }

    // TODO : Dynamically cast from String to other types
    // Cast from parameter string input to their appropriate types required by the method.
    private Object[] createArguments(Class[] argTypes, String[] cmdArgs)
    {
        if (argTypes.length != cmdArgs.length) {
            return MISMATCH_NUM_ARGS;
        }

        Object[] args = new Object[cmdArgs.length];
        for (int i = 0; i < args.length; i++) {
            try{
                if (argTypes[i].equals(int.class)) {
                    args[i] = Integer.parseInt(cmdArgs[i]);
                } else if (argTypes[i].equals(double.class)) {
                    args[i] = Double.parseDouble(cmdArgs[i]);
                } else if (argTypes[i].equals(long.class)) {
                    args[i] = Long.parseLong(cmdArgs[i]);
                } else if (argTypes[i].equals(boolean.class)) {
                    args[i] = Boolean.parseBoolean(cmdArgs[i]);
                } else {
                    args[i] = cmdArgs[i];
                }
            } catch (NumberFormatException e) {
                return UNCASTASBLE_ARGS;
            }

        }
        return args;
    }

    public static Method getMethod(String cmdName) {
        Class commandClass = getClass(cmdName);
        assert (commandClass != null);
        Method[] allMethods = commandClass.getDeclaredMethods();

        for (Method method : allMethods) {
            if (method.getName().equals(cmdName)) {
                return method;
            }
        }
        return null;
    }

    private static Class getClass(String cmdName)
    {
        Map<Class, List<Method>> classMethodMap = CommandsDisplay.getClassMethodTable();
        for (Class cla : classMethodMap.keySet())
        {
            for (Method method : classMethodMap.get(cla))
            {
                if (method.getName().equals(cmdName))
                {
                    return cla;
                }
            }
        }
        return null;
    }

    public String[] cleanWhiteSpaceArray(String[] strList)
    {
        String[] cleanList = strList.clone();

        for (int i = 0; i < cleanList.length; i++) {
            cleanList[i] = cleanWhiteSpace(strList[i]);
        }

        return cleanList;
    }

    private static boolean hasEmpty(String[] input) {
        if (input.length == 0) {
            return false;
        }

        for (String str : input) {
            if (str.equals("")) {
                return true;
            }
        }

        return false;
    }

    public String cleanWhiteSpace(String str) {
        return str.trim().replaceAll(" +", " ");
    }
}
