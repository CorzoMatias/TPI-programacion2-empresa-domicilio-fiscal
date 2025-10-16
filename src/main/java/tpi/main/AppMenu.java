package tpi.main;

import java.util.Scanner;

public class AppMenu {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== MENÚ ===");
            System.out.println("1) (pendiente)"); 
            System.out.println("0) Salir");
            System.out.print("Seleccione opción: ");
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "0" -> { System.out.println("Saliendo…"); exit = true; }
                default -> System.out.println("Opción inválida (aún sin CRUD).");
            }
        }
    }
}
