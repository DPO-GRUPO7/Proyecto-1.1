package modelo.prestamo;

import java.util.ArrayList;
import java.util.List;

import modelo.cafe.Mesa;
import modelo.juego.CopiaJuego;

public class Prestamo {

    private Mesa mesa;
    private List<CopiaJuego> juegos;
    private boolean activo;

    public Prestamo(Mesa mesa) {
        this.mesa = mesa;
        this.juegos = new ArrayList<>();
        this.activo = false;
    }

    public void agregarJuego(CopiaJuego juego) {
        juegos.add(juego);
    }

    public boolean validarRestricciones() {
        for (CopiaJuego copia : juegos) {

            // 1. Debe estar disponible
            if (!copia.estaDisponible()) {
                return false;
            }

            // 2. Debe ser para préstamo
            if (!copia.getUso().equals("PRESTAMO")) {
                return false;
            }

            // 3. Validar con la mesa
            if (!mesa.esAptaParaJuego(copia.getJuego())) {
                return false;
            }
        }
        return true;
    }

    public void iniciarPrestamo() {
        if (validarRestricciones()) {
            this.activo = true;
        }
    }

    public void finalizarPrestamo() {
        this.activo = false;
    }

    public boolean estaActivo() {
        return activo;
    }

    public List<CopiaJuego> getJuegos() {
        return juegos;
    }
}