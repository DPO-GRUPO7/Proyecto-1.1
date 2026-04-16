package modelo.cafe;

import modelo.juego.Juego;

public class Mesa {

    private int id;
    private int capacidad;
    private int numPersonas;
    private boolean hayNinos;

    public Mesa(int id, int capacidad) {
        this.id = id;
        this.capacidad = capacidad;
    }

    public void asignarPersonas(int numPersonas, boolean hayNinos) {
        this.numPersonas = numPersonas;
        this.hayNinos = hayNinos;
    }

    public boolean esAptaParaJuego(Juego juego) {
        return numPersonas <= capacidad && juego.esAptoParaPersonas(numPersonas);
    }

    public boolean puedeRecibirBebida(boolean esAlcoholica) {
        if (hayNinos && esAlcoholica) {
            return false;
        }
        return true;
    }

    public int getNumPersonas() {
        return numPersonas;
    }
}