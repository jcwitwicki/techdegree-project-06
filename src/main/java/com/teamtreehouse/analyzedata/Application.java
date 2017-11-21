package com.teamtreehouse.analyzedata;

import com.teamtreehouse.analyzedata.controller.Prompter;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {

        Prompter myPrompter = new Prompter();
        myPrompter.promptMenu();

    }
}
