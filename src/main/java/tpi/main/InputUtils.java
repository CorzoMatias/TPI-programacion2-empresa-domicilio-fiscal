package tpi.main;

import java.util.Scanner;

final class InputUtils {
    private final Scanner sc;

    InputUtils(Scanner sc) { this.sc = sc; }

    String readNonEmpty(String label) {
        while (true) {
            System.out.print(label + ": ");
            String s = sc.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("  * Campo obligatorio, reintente.");
        }
    }

    String readNullable(String label) {
        System.out.print(label + " (ENTER para dejar vacío): ");
        String s = sc.nextLine().trim();
        return s.isBlank() ? null : s;
    }

    Integer readPositiveIntNullable(String label) {
        System.out.print(label + " (ENTER para null): ");
        String s = sc.nextLine().trim();
        if (s.isBlank()) return null;
        try {
            int n = Integer.parseInt(s);
            if (n < 0) throw new NumberFormatException();
            return n;
        } catch (NumberFormatException ex) {
            System.out.println("  * Debe ser número entero >= 0. Intente de nuevo.");
            return readPositiveIntNullable(label);
        }
    }

    Long readId(String label) {
        while (true) {
            System.out.print(label + " (número): ");
            String s = sc.nextLine().trim();
            try {
                long v = Long.parseLong(s);
                if (v <= 0) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("  * ID inválido. Intente de nuevo.");
            }
        }
    }

    boolean confirm(String label) {
        System.out.print(label + " [S/n]: ");
        String s = sc.nextLine().trim().toUpperCase();
        return s.isBlank() || s.equals("S") || s.equals("SI") || s.equals("SÍ");
    }
}
