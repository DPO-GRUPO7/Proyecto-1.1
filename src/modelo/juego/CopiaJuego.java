package modelo.juego;

public class CopiaJuego {
    private Juego juego;
    private String estado; 
    private String uso; // prestamo o venta 
    private double precioVenta;
    
    public CopiaJuego(Juego juego, String estado, String uso, double precioVenta) {
        this.juego = juego;
        this.estado = estado;
        this.uso = uso;
        this.precioVenta = precioVenta;

    }
    public boolean estaDisponible() {
        return estado.equals("BUENO");
    }

    public boolean aptoParaVenta() {
        return uso.equals("VENTA") && estado.equals("BUENO");
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public Juego getJuego() {
        return juego;
    }
    public String getUso() {
        return uso;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
