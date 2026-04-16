package modelo.cafe;

import java.util.ArrayList;
import java.util.List;
import modelo.usuario.Empleado;

public class Turno {

    private int idTurno;
    private String dia;
    private boolean activo;
    private List<Empleado> empleados;

    public Turno(int idTurno, String dia, boolean activo) {
        this.idTurno = idTurno;
        this.dia = dia;
        this.activo = activo;
        this.empleados = new ArrayList<>();
    }

    public void asignarEmpleado(Empleado empleado) {
        empleados.add(empleado);
    }

    public void removerEmpleado(Empleado empleado) {
        empleados.remove(empleado);
    }

    public boolean esValido() {
        return activo && empleados.size() > 0;
    }

    
    public int getIdTurno() {
        return idTurno;
    }

    public String getDia() {
        return dia;
    }

    public boolean isActivo() {
        return activo;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
