package modelo.usuario;

public abstract class Usuario {

    protected int id;
    protected String nombre;
    protected String login;
    protected String password;
    private double bonoDescuento;

    public Usuario(int id, String nombre, String login, String password) {
        this.id = id;
        this.nombre = nombre;
        this.login = login;
        this.password = password;
        this.bonoDescuento = 0.0;
    }

    public boolean autenticar(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
    }


    public boolean asignarBono(double porcentaje) {
        if (bonoDescuento > 0.0) {
            return false;        }
        this.bonoDescuento = porcentaje;
        return true;
    }

    public double usarBono() {
        double bono = this.bonoDescuento;
        this.bonoDescuento = 0.0;
        return bono;
    }

    public boolean tieneBono() {
        return bonoDescuento > 0.0;
    }

    public double getBonoDescuento() {
        return bonoDescuento;
    }

    public void setBonoDescuento(double bonoDescuento) {
        this.bonoDescuento = bonoDescuento;
    }


    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
