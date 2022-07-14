package com.WebRunnerPackage;

public class CommandResult
{
    private String command;

    private String status;

    private String message;

    private static final String defaultMessage = "No Message to Display";
    private static final String commandNotFoundMessafe = "Error - Invalid Command.";

    public CommandResult(){}

    public CommandResult(String command)
    {
        this.command = command;
        this.message = defaultMessage;
    }

    public CommandResult(String id, String command, String status)
    {
        this.command = command;
        this.status = status;
        this.message = defaultMessage;
    }

    public void cannotFindCommand(){
        setStatus("FAIL!");
        setMessage(commandNotFoundMessafe);
    }
    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return "CommandResult{" +
                ", command='" + command + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
