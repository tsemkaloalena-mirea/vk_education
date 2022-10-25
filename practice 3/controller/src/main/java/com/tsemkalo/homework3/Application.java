package com.tsemkalo.homework3;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Path to file with books: ");
        String filePath = scanner.nextLine();
        System.out.print("Library capacity: ");
        while (!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.print("Library capacity must be integer. Try again: ");
        }
        Integer capacity = scanner.nextInt();
        final Injector injector = Guice.createInjector(new InjectionModule(filePath));
        Library library = injector.getInstance(LibraryFactory.class).library(capacity);
    }
}