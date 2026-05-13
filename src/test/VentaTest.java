package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.producto.Bebida;
import modelo.usuario.Cliente;
import modelo.venta.Venta;

public class VentaTest {

    private Cliente cliente;
    private Venta venta;

    @BeforeEach
    public void setup() {

        cliente = new Cliente(1, "Juan", "juan123", "1234");

        venta = new Venta(cliente);
    }

    @Test
    public void calcularTotalTest() {

        Bebida cafe = new Bebida(1, "Cafe", 5000, false, true);

        venta.agregarItem(cafe);

        double total = venta.calcularTotal();

        assertEquals(5900, total);
    }

    @Test
    public void calcularImpuestosTest() {

        Bebida cafe = new Bebida(1, "Cafe", 5000, false, true);

        venta.agregarItem(cafe);

        double impuestos = venta.calcularImpuestos();

        assertEquals(400, impuestos);
    }

    @Test
    public void generarPuntosTest() {

        Bebida cafe = new Bebida(1, "Cafe", 5000, false, true);

        venta.agregarItem(cafe);

        venta.calcularTotal();
        venta.generarPuntos();

        assertTrue(cliente.getPuntosFidelidad() > 0);
    }
}