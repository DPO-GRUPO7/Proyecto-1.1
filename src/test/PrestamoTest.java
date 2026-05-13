package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.cafe.Mesa;
import modelo.juego.CopiaJuego;
import modelo.juego.Juego;
import modelo.prestamo.Prestamo;

public class PrestamoTest {

    private Prestamo prestamo;
    private CopiaJuego copia;

    @BeforeEach
    public void setup() {

        Juego juego = new Juego(
                "Catan",
                1995,
                "Devir",
                "Estrategia",
                3,
                4,
                10,
                3,
                "BUENO"
        );

        copia = new CopiaJuego(juego, "BUENO", "PRESTAMO", 0);

        Mesa mesa = new Mesa(1, 4);

        mesa.asignarPersonas(4, false);

        prestamo = new Prestamo(mesa);
    }

    @Test
    public void prestamoValidoTest() {

        prestamo.agregarJuego(copia);

        assertTrue(prestamo.validarRestricciones());
    }

    @Test
    public void prestamoMesaExcedidaTest() {

        Mesa mesa = new Mesa(2, 4);

        mesa.asignarPersonas(5, false);

        Prestamo prestamoGrande = new Prestamo(mesa);

        prestamoGrande.agregarJuego(copia);

        assertFalse(prestamoGrande.validarRestricciones());
    }
}