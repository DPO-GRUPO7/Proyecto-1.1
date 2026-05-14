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
            System.out.println("\n----- BOARD GAME CAFE -----");
            System.out.println("1. Iniciar sesión");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            int opcion = leerEntero();

            if (opcion == 1) {
                iniciarSesion();
            } else if (opcion == 0) {
                sistema.guardarDatos();
                continuar = false;
                System.out.println("Hasta luego.");
            } else {
                System.out.println("Opción inválida.");
            }
        }
    }

    private void iniciarSesion() {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        Usuario usuario = autenticar(login, password);

        if (usuario == null) {
            System.out.println("Credenciales inválidas.");
            return;
        }
        System.out.println("Bienvenido/a " + usuario.getNombre());

        if (usuario instanceof Administrador) {
            ConsolaAdministrador consolaAdmin = new ConsolaAdministrador(sistema, scanner);
            consolaAdmin.ejecutar((Administrador) usuario);
        } else if (usuario instanceof Cliente) {
            ConsolaCliente consolaCliente = new ConsolaCliente(sistema, scanner);
            consolaCliente.ejecutar((Cliente) usuario);
        } else if (usuario instanceof Empleado) {
            ConsolaEmpleado consolaEmpleado = new ConsolaEmpleado(sistema, scanner);
            consolaEmpleado.ejecutar((Empleado) usuario);
        }
    }

    private Usuario autenticar(String login, String password) {
        for (Usuario u : sistema.getUsuarios()) {
            if (u.autenticar(login, password)) {
                return u;
            }
        }
        return null;
    }

    private int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    public static void main(String[] args) {
        ConsolaPrincipal consola = new ConsolaPrincipal();
        consola.ejecutar();
    }
}