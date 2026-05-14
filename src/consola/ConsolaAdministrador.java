package consola;
import java.util.Scanner;
import logica.SistemaCafe;
import modelo.usuario.Administrador;

public class ConsolaAdministrador {

    private SistemaCafe sistema;
    private Scanner scanner;

    public ConsolaAdministrador(SistemaCafe sistema, Scanner scanner) {
        this.sistema = sistema;
        this.scanner = scanner;
    }

    public void ejecutar(Administrador admin) {
        System.out.println("Entraste como administrador.");
    }
}