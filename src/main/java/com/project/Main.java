package com.project;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

           Loader csvf = new jsonLoader("taco");
           csvf.load();

    }
}