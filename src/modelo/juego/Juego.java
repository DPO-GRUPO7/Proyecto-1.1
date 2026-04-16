package modelo.juego;

public class Juego {
	private String nombre;
	private int anioPublicacion;
	private String empresa;
	private String categoria;
	private int minJugadores;
	private int maxJugadores;
	private int edadMinima;
	private int esDificil;
	private String estado;
	public Juego (String nombre, int anioPublicacion, String empresa, String categoria, int minJugadores, int maxJugadores, int edadMinima, int esDificil, String estado) {
		 this.nombre = nombre;
		 this.anioPublicacion = anioPublicacion;
		 this.empresa = empresa;
		 this.categoria = categoria; 
		 this.minJugadores = minJugadores; 
		 this.maxJugadores = maxJugadores; 
		 this.edadMinima = edadMinima; 
		 this.esDificil = esDificil;
		 this.estado = estado;
	}
	 public boolean esAptoParaPersonas(int numPersonas) {
	        return numPersonas >= minJugadores && numPersonas <= maxJugadores;
	    }

	    public boolean esAptoPorEdad(int edad) {
	        return edad >= edadMinima;
	    }

	    public boolean esDificil() {
	        return esDificil >= 4;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public int getEdadMinima() {
	        return edadMinima;
	    }
}
