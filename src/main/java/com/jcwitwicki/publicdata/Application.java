package com.jcwitwicki.publicdata;

import com.jcwitwicki.publicdata.controller.Prompter;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {

        Prompter myPrompter = new Prompter();
        myPrompter.promptMenu(); 

    }
}
