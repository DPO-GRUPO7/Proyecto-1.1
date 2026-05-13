package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.juego.Juego;
import modelo.torneo.TorneoAmistoso;
import modelo.torneo.TorneoCompetitivo;
import modelo.usuario.Administrador;
import modelo.usuario.Cliente;
import modelo.usuario.Mesero;

public class TorneoTest {

    private Juego juego;
    private Administrador admin;

    @BeforeEach
    public void setup() {

        juego = new Juego(
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

        admin = new Administrador(
                1,
                "Carlos",
                "admin",
                "1234"
        );
    }

    @Test
    public void inscripcionValidaTest() {

        TorneoAmistoso torneo = new TorneoAmistoso(
                juego,
                "Lunes",
                10,
                admin,
                0.15
        );

        Cliente cliente = new Cliente(
                2,
                "Juan",
                "juan",
                "1234"
        );

        String resultado = torneo.inscribir(cliente, 2);

        assertEquals(null, resultado);
        assertEquals(2, torneo.cuposTotalesOcupados());
    }

    @Test
    public void torneoSinCuposTest() {

        TorneoAmistoso torneo = new TorneoAmistoso(
                juego,
                "Martes",
                3,
                admin,
                0.10
        );

        Cliente cliente1 = new Cliente(1, "A", "a", "1");
        Cliente cliente2 = new Cliente(2, "B", "b", "1");

        torneo.inscribir(cliente1, 3);

        String resultado = torneo.inscribir(cliente2, 1);

        assertTrue(resultado != null);
    }

    @Test
    public void empleadoNoRecibePremioTest() {

        TorneoCompetitivo torneo = new TorneoCompetitivo(
                juego,
                "Viernes",
                10,
                admin,
                20000
        );

        Mesero mesero = new Mesero(
                3,
                "Pedro",
                "pedro",
                "1234",
                null
        );

        boolean puedeRecibir = torneo.puedeRecibirPremio(mesero);

        assertFalse(puedeRecibir);
    }

    @Test
    public void calcularPremioTest() {

        TorneoCompetitivo torneo = new TorneoCompetitivo(
                juego,
                "Sabado",
                10,
                admin,
                10000
        );

        Cliente cliente = new Cliente(
                4,
                "Laura",
                "laura",
                "1234"
        );

        torneo.inscribir(cliente, 2);

        double premio = torneo.calcularPremio();

        assertEquals(20000, premio);
    }
}