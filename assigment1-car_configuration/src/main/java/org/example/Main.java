package org.example;

import org.example.car.Car;
import org.example.car.CarBuilder;
import org.example.car.enums.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            CarBuilder builder = new CarBuilder();

            // 1) Model
            CarModel model = askEnum(sc, "Select model", CarModel.values());
            builder.setModel(model);

            // 2) Engine
            EngineType engine = askEnum(sc, "Select engine", EngineType.values());
            builder.setEngine(engine);

            // 3) Transmission
            Transmission transmission = askEnum(sc, "Select transmission", Transmission.values());
            builder.setTransmission(transmission);

            // 4) Exterior (optional)
            Color color = askEnum(sc, "Select color", Color.values());
            builder.setColor(color);

            Rims rims = askEnum(sc, "Select rims", Rims.values());
            builder.setRims(rims);

            boolean sunroof = askYesNo(sc, "Add sunroof? (y/n)");
            builder.setSunroof(sunroof);

            // 5) Interior features (optional)
            System.out.println("Add interior features (y/n):");
            for (InteriorFeature f : InteriorFeature.values()) {
                if (askYesNo(sc, " - " + f + "? (y/n)")) {
                    builder.addInterior(f);
                }
            }

            // 6) Safety features (some may be required by rules)
            System.out.println("Add safety features (y/n):");
            for (SafetyFeature f : SafetyFeature.values()) {
                if (askYesNo(sc, " - " + f + "? (y/n)")) {
                    builder.addSafety(f);
                }
            }

            // Build + validate
            Car car = builder.build();
            System.out.println("\nReady for ordering:\n" + car);

        } catch (Exception e) {
            System.out.println("\nConfiguration failed: " + e.getMessage());
            System.out.println("Fix your selections and try again.");
        } finally {
            sc.close();
        }
    }

    private static <T extends Enum<T>> T askEnum(Scanner sc, String prompt, T[] values) {
        while (true) {
            System.out.println("\n" + prompt + ":");
            for (int i = 0; i < values.length; i++) {
                System.out.println((i + 1) + ") " + values[i]);
            }
            System.out.print("> ");

            String input = sc.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= values.length) {
                    return values[choice - 1];
                }
            } catch (NumberFormatException ignored) { }

            System.out.println("Invalid choice. Enter a number between 1 and " + values.length + ".");
        }
    }

    private static boolean askYesNo(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Please answer y/n.");
        }
    }
}
