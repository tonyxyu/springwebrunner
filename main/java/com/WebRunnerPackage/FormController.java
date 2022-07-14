package com.WebRunnerPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FormController
{
    private static final String SUCCESS = "SUCCESS!";

    @Autowired
    private CommandRunner cmdRunner;

    @Autowired
    private CommandsDisplay commandsDisplay;

    @GetMapping("/admin")
    public String greetingForm(Model model)
    {
        model.addAttribute("h1", WebRunner.h1Message);
        model.addAttribute("h2", WebRunner.h2Message);
        model.addAttribute("customName", WebRunner.customNameForMethod);
        model.addAttribute("input", new CommandInput());
        model.addAttribute("commandTable", CommandsDisplay.getCommandTable(true));

        return "input.html";
    }

    @GetMapping("/admin/command")
    public String commandForm(@RequestParam(value = "clicked") String cmd, Model model) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        String commandName = cleanString(cmd);

        String[] allParameterNames = CommandRunner.getParameterNames(commandName);
        String[] allParameterTypes = cmdRunner.getArgumentTypesInString(commandName);

        ArgsList argumentList = new ArgsList();
        argumentList.getArgs().add(0, new MethodArg());
        argumentList.getArgs().get(0).setMethodName(commandName);

        model.addAttribute("method", commandName);
        model.addAttribute("pNames", allParameterNames);
        model.addAttribute("pTypes", allParameterTypes);
        model.addAttribute("cmdName", commandName);
        model.addAttribute("argumentList", argumentList);

        return "commandForm.html";
    }

    @PostMapping("/admin/submit_form")
    public String FormSubmit(@ModelAttribute("argumentList") ArgsList args, Model model)
    {
        String[] argsArray = args.getListofArguments();

        String command = args.getMethodName();

        CommandResult result = new CommandResult(command);
        try {
            String message = cmdRunner.executeCommandWithArgs(command, argsArray);

            if (message.equals(SUCCESS)) {
                result.setStatus(SUCCESS);
                result.setMessage("Successful execution of command: " + command);
            } else {
                result.setStatus("FAIL!");
                result.setMessage(message);
            }
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return e.getMessage();
        }
        model.addAttribute("result", result);

        return "result.html";
    }

    @PostMapping("/admin")

    public String greetingSubmit(@ModelAttribute("input") CommandInput CurrentCommand, Model model)
    {
        String command = CurrentCommand.getCommand();
        CommandResult result = new CommandResult(CurrentCommand.getCommand());

        try {
            String message = cmdRunner.executeCommand(command);

            if (message.equals(SUCCESS)) {
                result.setStatus(SUCCESS);
                result.setMessage("Successful execution of command: " + command);
            } else {
                result.setStatus("FAIL!");
                result.setMessage(message);
            }
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return e.getMessage();
        }
        model.addAttribute("result", result);

        return "result.html";
    }

    public String cleanString(String cmd)
    {
        return cmd.trim().split("\\(")[0].trim();
    }
}

class ArgsList
{
    public List<MethodArg> args;
    private static final String[] EMPTY_ARGS = {};

    public ArgsList(){
        args = new ArrayList<>(100);
    }

    public String getMethodName(){
        assert(args.size() > 0);
        return args.get(0).getMethodName();
    }

    public String[] getListofArguments(){

        // TODO : use more robust way to check if the method takes no args
        if (args.get(0).getArg() == null){
            return EMPTY_ARGS;
        }

        String[] listOfArgs = new String[args.size()];

        for (int i = 0; i < args.size(); i++) {
            listOfArgs[i] = args.get(i).getArg().trim();
        }

        return listOfArgs;
    }

    public List<MethodArg> getArgs()
    {
        return args;
    }

    public void setArgs(List<MethodArg> args)
    {
        this.args = args;
    }
}

class MethodArg
{

    public String arg;

    // TODO : Now the methodName has to be passed to all parameters -- try to pass it only once.
    public String methodName;

    public String getArg()
    {
        return arg;
    }

    public void setArg(String arg)
    {
        this.arg = arg;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }
}
