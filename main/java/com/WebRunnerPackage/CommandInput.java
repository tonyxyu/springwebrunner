package com.WebRunnerPackage;

public class CommandInput
{
    private String id;

    private String command;

    public CommandInput(){}

    public CommandInput(String id, String command)
    {
        this.id = id;
        this.command = command;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    @Override
    public String toString()
    {
        return "commandInput{" +
                "id='" + id + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}