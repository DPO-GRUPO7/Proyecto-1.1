package modelo.cafe;

import modelo.usuario.Empleado;

public class SolicitudCambioTurno {

    private Empleado empleado;
    private Turno nuevoTurno;
    private String estado; //pentidente, aprovado o rechazado

    public SolicitudCambioTurno(Empleado empleado, Turno nuevoTurno) {
        this.empleado = empleado;
        this.nuevoTurno = nuevoTurno;
        this.estado = "PENDIENTE";
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean esAprobada() {
        return estado.equals("APROBADO");
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Turno getNuevoTurno() {
        return nuevoTurno;
    }
}