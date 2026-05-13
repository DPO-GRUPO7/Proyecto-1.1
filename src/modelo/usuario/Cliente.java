package modelo.usuario;

import java.util.ArrayList;
import java.util.List;

import modelo.juego.Juego;

public class Cliente extends Usuario {

    private double puntosFidelidad;
    private List<Juego> juegosFavoritos;

    public Cliente(int id, String nombre, String login, String password) {
        super(id, nombre, login, password);
        this.puntosFidelidad = 0;
        this.juegosFavoritos = new ArrayList<>();
    }

    public double getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void acumularPuntos(double valor) {
        this.puntosFidelidad += valor;
    }

    public void redimirPuntos(double valor) {
        if (valor <= puntosFidelidad) {
            puntosFidelidad -= valor;
        }
    }

    public void agregarFavorito(Juego juego) {
        for (Juego j : juegosFavoritos) {
            if (j.getNombre().equalsIgnoreCase(juego.getNombre())) {
                return;
            }
        }
        juegosFavoritos.add(juego);
    }

    public List<Juego> getJuegosFavoritos() {
        return juegosFavoritos;
    }

    public boolean esFanaticoDe(Juego juego) {
        for (Juego j : juegosFavoritos) {
            if (j.getNombre().equalsIgnoreCase(juego.getNombre())) {
                return true;
            }
        }
        return false;
    }
}
