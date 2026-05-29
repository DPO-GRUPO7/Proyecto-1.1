package Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import modelo.usuario.Cliente;

//PanelRegistro 

public class PanelRegistro extends JPanel {

    private VentanaPrincipal ventana;

    private JTextField campoNombre;
    private JTextField campoLogin;
    private JPasswordField campoPassword;
    private JPasswordField campoRepetir;
    private JLabel lblError;

    public PanelRegistro(VentanaPrincipal ventana) {
        this.ventana = ventana;
        construirUI();
    }

    private void construirUI() {
        setBackground(VentanaPrincipal.COLOR_FONDO);
        setLayout(new GridBagLayout());

        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(VentanaPrincipal.COLOR_PANEL);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Título
        JLabel lblTitulo = new JLabel("Crear cuenta");
        lblTitulo.setFont(VentanaPrincipal.FUENTE_TITULO);
        lblTitulo.setForeground(VentanaPrincipal.COLOR_ACENTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos
        campoNombre   = crearCampo();
        campoLogin    = crearCampo();
        campoPassword = crearCampoPassword();
        campoRepetir  = crearCampoPassword();

        // Error
        lblError = new JLabel(" ");
        lblError.setFont(VentanaPrincipal.FUENTE_PEQUENA);
        lblError.setForeground(new Color(255, 100, 100));
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botón registrar
        JButton btnRegistrar = crearBoton("Crear cuenta", VentanaPrincipal.COLOR_ACENTO2, Color.BLACK);
        btnRegistrar.addActionListener(e -> intentarRegistro());

        // Volver al login
        JButton btnVolver = new JButton("← Volver al login");
        btnVolver.setFont(VentanaPrincipal.FUENTE_PEQUENA);
        btnVolver.setForeground(VentanaPrincipal.COLOR_TEXTO_TENUE);
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.TARJETA_LOGIN));

        // Ensamblar
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(etiqueta("Nombre completo"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoNombre);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(etiqueta("Login (único)"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoLogin);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(etiqueta("Contraseña"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoPassword);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(etiqueta("Repetir contraseña"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoRepetir);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblError);
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(btnRegistrar);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(btnVolver);

        add(tarjeta);
    }

    private void intentarRegistro() {
        String nombre   = campoNombre.getText().trim();
        String login    = campoLogin.getText().trim();
        String pass     = new String(campoPassword.getPassword()).trim();
        String repetir  = new String(campoRepetir.getPassword()).trim();

        if (nombre.isEmpty() || login.isEmpty() || pass.isEmpty() || repetir.isEmpty()) {
            lblError.setText("Todos los campos son obligatorios.");
            return;
        }
        if (!pass.equals(repetir)) {
            lblError.setText("Las contraseñas no coinciden.");
            return;
        }
        if (ventana.getSistema().buscarCliente(login) != null) {
            lblError.setText("Ese login ya está en uso. Elige otro.");
            return;
        }

        int nuevoId = ventana.getSistema().getUsuarios().size() + 1;
        Cliente nuevoCliente = new Cliente(nuevoId, nombre, login, pass);
        ventana.getSistema().agregarCliente(nuevoCliente);

        JOptionPane.showMessageDialog(this,
                "¡Cuenta creada exitosamente!\nYa puedes iniciar sesión.",
                "Registro exitoso",
                JOptionPane.INFORMATION_MESSAGE);

        limpiar();
        ventana.mostrarPanel(VentanaPrincipal.TARJETA_LOGIN);
    }

    private void limpiar() {
        campoNombre.setText("");
        campoLogin.setText("");
        campoPassword.setText("");
        campoRepetir.setText("");
        lblError.setText(" ");
    }

    

    private JLabel etiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(VentanaPrincipal.FUENTE_PEQUENA);
        lbl.setForeground(VentanaPrincipal.COLOR_TEXTO_TENUE);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField crearCampo() {
        JTextField c = new JTextField();
        c.setFont(VentanaPrincipal.FUENTE_NORMAL);
        c.setBackground(VentanaPrincipal.COLOR_FONDO);
        c.setForeground(VentanaPrincipal.COLOR_TEXTO);
        c.setCaretColor(VentanaPrincipal.COLOR_ACENTO);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.COLOR_TEXTO_TENUE),
                new EmptyBorder(8, 10, 8, 10)));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private JPasswordField crearCampoPassword() {
        JPasswordField c = new JPasswordField();
        c.setFont(VentanaPrincipal.FUENTE_NORMAL);
        c.setBackground(VentanaPrincipal.COLOR_FONDO);
        c.setForeground(VentanaPrincipal.COLOR_TEXTO);
        c.setCaretColor(VentanaPrincipal.COLOR_ACENTO);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.COLOR_TEXTO_TENUE),
                new EmptyBorder(8, 10, 8, 10)));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        return c;
    }

    private JButton crearBoton(String texto, Color fondo, Color letra) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(fondo);
        btn.setForeground(letra);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        return btn;
    }
}
