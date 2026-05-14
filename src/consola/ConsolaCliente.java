package consola;

import java.util.Scanner;
import logica.SistemaCafe;
import modelo.usuario.Cliente;

public class ConsolaCliente {

    private SistemaCafe sistema;
    private Scanner scanner;

    public ConsolaCliente(SistemaCafe sistema, Scanner scanner) {
        this.sistema = sistema;
        this.scanner = scanner;
    }

    public void ejecutar(Cliente cliente) {
        System.out.println("Entraste como cliente.");
    }
}