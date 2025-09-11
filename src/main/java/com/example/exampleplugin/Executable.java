package com.example.exampleplugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Executable {

    public static void main(String[] args) throws IOException {
        InputStream stream = Executable.class.getClassLoader().getResourceAsStream("HOW-TO-USE.md");
        String message = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(message);
    }

}
