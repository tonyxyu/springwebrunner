package com.simpleTests;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ComputationTest
{
    @ShellMethod("Addition")
    public void add(@ShellOption(value = {"arg1"}) int arg1,
                    @ShellOption(value = {"arg2"}) int arg2){
        int sum = arg1 + arg2;
        System.out.println(sum);
    }


}