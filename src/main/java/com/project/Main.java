package com.project;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        LoaderFactory jsonfactory = new jsonFactory();
        client jsonClient = new client(jsonfactory);
        jsonClient.executeLoad();

    }
}