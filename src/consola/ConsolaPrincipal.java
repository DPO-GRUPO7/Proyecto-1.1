package consola;

import java.util.Scanner;

import logica.SistemaCafe;
import modelo.usuario.Administrador;
import modelo.usuario.Cliente;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;

public class ConsolaPrincipal {

    private SistemaCafe sistema;
    private Scanner scanner;

    public ConsolaPrincipal() {
        sistema = new SistemaCafe();
        scanner = new Scanner(System.in);
    }

    public void ejecutar() {
        sistema.cargarDatos();
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n===== BOARD GAME CAFÉ =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarme como cliente");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1: iniciarSesion(); break;
                case 2: registrarCliente(); break;
                case 0:
                    sistema.guardarDatos();
                    continuar = false;
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void iniciarSesion() {
        int intentos = 0;
        while (intentos < 3) {
            System.out.print("Login: ");
            String login = scanner.nextLine().trim();
            System.out.print("Contraseña: ");
            String password = scanner.nextLine().trim();

            Usuario usuario = autenticar(login, password);

            if (usuario != null) {
                System.out.println("Bienvenido/a, " + usuario.getNombre() + "!");
                redirigir(usuario);
                return;
            }

            intentos++;
            int restantes = 3 - intentos;
            if (restantes > 0) {
                System.out.println("Credenciales inválidas. Intentos restantes: " + restantes);
            } else {
                System.out.println("Demasiados intentos fallidos. Volviendo al menú principal.");
            }
        }
    }

    private void registrarCliente() {
        System.out.println("\n--- Registro de nuevo cliente ---");

        System.out.print("Nombre completo: ");
        String nombre = leerTextoNoVacio();

        String login;
        while (true) {
            System.out.print("Login (único): ");
            login = leerTextoNoVacio();
            if (sistema.buscarCliente(login) == null) break;
            System.out.println("Ese login ya está en uso. Elige otro.");
        }

        System.out.print("Contraseña: ");
        String password = leerTextoNoVacio();

        int nuevoId = sistema.getUsuarios().size() + 1;
        Cliente nuevoCliente = new Cliente(nuevoId, nombre, login, password);
        sistema.agregarCliente(nuevoCliente);

        System.out.println("Cuenta creada exitosamente. Ya puedes iniciar sesión.");
    }

    private void redirigir(Usuario usuario) {
        if (usuario instanceof Administrador) {
            new ConsolaAdministrador(sistema, scanner).ejecutar((Administrador) usuario);
        } else if (usuario instanceof Cliente) {
            new ConsolaCliente(sistema, scanner).ejecutar((Cliente) usuario);
        } else if (usuario instanceof Empleado) {
            new ConsolaEmpleado(sistema, scanner).ejecutar((Empleado) usuario);
        }
    }

    private Usuario autenticar(String login, String password) {
        for (Usuario u : sistema.getUsuarios()) {
            if (u.autenticar(login, password)) return u;
        }
        return null;
    }

    // ── Helpers de lectura ────────────────────────────────────────────────

    int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    int leerEntero(int min, int max) {
        while (true) {
            int v = leerEntero();
            if (v >= min && v <= max) return v;
            System.out.print("Ingrese un número entre " + min + " y " + max + ": ");
        }
    }

    double leerDouble() {
        while (true) {
            try {
                double v = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                if (v >= 0) return v;
                System.out.print("Ingrese un valor positivo: ");
            } catch (Exception e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    String leerTextoNoVacio() {
        while (true) {
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.print("El campo no puede estar vacío: ");
        }
    }

    boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (s/n): ");
            String r = scanner.nextLine().trim().toLowerCase();
            if (r.equals("s")) return true;
            if (r.equals("n")) return false;
            System.out.print("Responda 's' o 'n': ");
        }
    }

    public static void main(String[] args) {
        new ConsolaPrincipal().ejecutar();
    }
}