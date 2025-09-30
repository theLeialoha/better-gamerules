package dev.leialoha.bettergamerules;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Executable {

    public static void main(String[] args) throws IOException {
        printResource("HEADER.md");
        printResource("HOW-TO-USE.md");
    }

    private static void printResource(String resource) throws IOException {
        InputStream stream = Executable.class.getClassLoader().getResourceAsStream(resource);
        String message = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(message);
    }

}
