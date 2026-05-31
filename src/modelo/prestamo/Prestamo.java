package modelo.prestamo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import modelo.cafe.Mesa;
import modelo.juego.CopiaJuego;

public class Prestamo {

    private Mesa mesa;
    private List<CopiaJuego> juegos;
    private boolean activo;

    // NUEVO
    private LocalDate fechaPrestamo;

    public Prestamo(Mesa mesa) {
        this.mesa = mesa;
        this.juegos = new ArrayList<>();
        this.activo = false;

        // NUEVO
        this.fechaPrestamo = LocalDate.now();
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void agregarJuego(CopiaJuego juego) {
        juegos.add(juego);
    }

    public boolean validarRestricciones() {

        for (CopiaJuego copia : juegos) {

            if (!copia.estaDisponible()) {
                return false;
            }

            if (!copia.getUso().equals("PRESTAMO")) {
                return false;
            }

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