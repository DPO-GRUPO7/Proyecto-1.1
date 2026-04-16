package modelo.cafe;

import modelo.usuario.Cliente;

public class SugerenciaPlatillo {

    private Cliente cliente;
    private String descripcion;
    private String estado; //pentiente, aprovado o rechazado

    public SugerenciaPlatillo(Cliente cliente, String descripcion) {
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.estado = "PENDIENTE";
    }

    public void aprobar() {
        this.estado = "APROBADO";
    }

    public void rechazar() {
        this.estado = "RECHAZADO";
    }

    public String getDescripcion() {
        return descripcion;
    }
}