package modelo.usuario;

public abstract class Usuario {

    protected int id;
    protected String nombre;
    protected String login;
    protected String password;

    public Usuario(int id, String nombre, String login, String password) {
        this.id = id;
        this.nombre = nombre;
        this.login = login;
        this.password = password;
    }


    public boolean autenticar(String login, String password) {
        return this.login.equals(login) && this.password.equals(password);
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