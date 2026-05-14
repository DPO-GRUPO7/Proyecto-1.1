package consola;

import java.util.Scanner;
import logica.SistemaCafe;
import modelo.usuario.Empleado;

public class ConsolaEmpleado {

    private SistemaCafe sistema;
    private Scanner scanner;

    public ConsolaEmpleado(SistemaCafe sistema, Scanner scanner) {
        this.sistema = sistema;
        this.scanner = scanner;
    }

    public void ejecutar(Empleado empleado) {
        System.out.println("Entraste como empleado.");
    }
}