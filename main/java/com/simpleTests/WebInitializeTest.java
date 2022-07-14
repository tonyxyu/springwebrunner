package com.simpleTests;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class WebInitializeTest
{
    @ShellMethod("Print input string to stderr")
    public void write(@ShellOption(value = {"input"}) String str){
        System.err.println(str);
    }

    @ShellMethod("Take input and do not do anything")
    public void doNothing(@ShellOption(value = {"input"}) String str){

    }

    @ShellMethod("Take input and do not do anything")
    public void doSomething(@ShellOption(value = {"input"}) String str){
        System.err.println("Do it");
    }
}
