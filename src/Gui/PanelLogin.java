package Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import modelo.usuario.Usuario;

//Login

public class PanelLogin extends JPanel {

    private VentanaPrincipal ventana;
    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;

    
    private JTextField campologin;
    private JPasswordField campoPassword;
    private JLabel lblError;
    private JButton btnEntrar;

    public PanelLogin(VentanaPrincipal ventana) {
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
        tarjeta.setMaximumSize(new Dimension(400, 500));

        
        JLabel lblTitulo = new JLabel(" Dulces & Dados");
        lblTitulo.setFont(VentanaPrincipal.FUENTE_TITULO);
        lblTitulo.setForeground(VentanaPrincipal.COLOR_ACENTO);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Board Game Café");
        lblSub.setFont(VentanaPrincipal.FUENTE_PEQUENA);
        lblSub.setForeground(VentanaPrincipal.COLOR_TEXTO_TENUE);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        campologin    = crearCampoTexto("Usuario");
        campoPassword = crearCampoPassword("Contraseña");

        
        lblError = new JLabel(" ");
        lblError.setFont(VentanaPrincipal.FUENTE_PEQUENA);
        lblError.setForeground(new Color(255, 100, 100));
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnEntrar = crearBoton("Ingresar", VentanaPrincipal.COLOR_ACENTO, Color.BLACK);
        btnEntrar.addActionListener(e -> intentarLogin());

        
        ActionListener enterLogin = e -> intentarLogin();
        campologin.addActionListener(enterLogin);
        campoPassword.addActionListener(enterLogin);

        
        JButton btnRegistro = new JButton("¿No tienes cuenta? Regístrate aquí");
        btnRegistro.setFont(VentanaPrincipal.FUENTE_PEQUENA);
        btnRegistro.setForeground(VentanaPrincipal.COLOR_ACENTO2);
        btnRegistro.setBorderPainted(false);
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistro.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.TARJETA_REGISTRO));

        
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(lblSub);
        tarjeta.add(Box.createVerticalStrut(30));
        tarjeta.add(crearEtiqueta("Usuario"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campologin);
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(crearEtiqueta("Contraseña"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(campoPassword);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblError);
        tarjeta.add(Box.createVerticalStrut(16));
        tarjeta.add(btnEntrar);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(btnRegistro);

        add(tarjeta);
    }

    

    private void intentarLogin() {
        if (intentosFallidos >= MAX_INTENTOS) return;

        String login    = campologin.getText().trim();
        String password = new String(campoPassword.getPassword()).trim();

        if (login.isEmpty() || password.isEmpty()) {
            mostrarError("Completa todos los campos.");
            return;
        }

        Usuario usuario = autenticar(login, password);

        if (usuario != null) {
            resetearFormulario();
            ventana.redirigirUsuario(usuario);
        } else {
            intentosFallidos++;
            int restantes = MAX_INTENTOS - intentosFallidos;
            if (restantes > 0) {
                mostrarError("Credenciales inválidas. Intentos restantes: " + restantes);
            } else {
                mostrarError("Demasiados intentos. Reinicia la aplicación.");
                btnEntrar.setEnabled(false);
                campologin.setEnabled(false);
                campoPassword.setEnabled(false);
            }
        }
    }

    private Usuario autenticar(String login, String password) {
        for (Usuario u : ventana.getSistema().getUsuarios()) {
            if (u.autenticar(login, password)) return u;
        }
        return null;
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
    }

    
    public void resetearFormulario() {
        campologin.setText("");
        campoPassword.setText("");
        lblError.setText(" ");
        intentosFallidos = 0;
        btnEntrar.setEnabled(true);
        campologin.setEnabled(true);
        campoPassword.setEnabled(true);
    }

    

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(VentanaPrincipal.FUENTE_PEQUENA);
        lbl.setForeground(VentanaPrincipal.COLOR_TEXTO_TENUE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField();
        campo.setFont(VentanaPrincipal.FUENTE_NORMAL);
        campo.setBackground(VentanaPrincipal.COLOR_FONDO);
        campo.setForeground(VentanaPrincipal.COLOR_TEXTO);
        campo.setCaretColor(VentanaPrincipal.COLOR_ACENTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.COLOR_TEXTO_TENUE),
                new EmptyBorder(8, 10, 8, 10)));
        campo.setMaximumSize(new Dimension(240, 40));
        campo.setPreferredSize(new Dimension(240, 40));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return campo;
    }

    private JPasswordField crearCampoPassword(String placeholder) {
        JPasswordField campo = new JPasswordField();
        campo.setFont(VentanaPrincipal.FUENTE_NORMAL);
        campo.setBackground(VentanaPrincipal.COLOR_FONDO);
        campo.setForeground(VentanaPrincipal.COLOR_TEXTO);
        campo.setCaretColor(VentanaPrincipal.COLOR_ACENTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VentanaPrincipal.COLOR_TEXTO_TENUE),
                new EmptyBorder(8, 10, 8, 10)));
        campo.setMaximumSize(new Dimension(240, 40));
        campo.setPreferredSize(new Dimension(240, 40));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return campo;
    }

    private JButton crearBoton(String texto, Color fondo, Color letra) {
    	JButton btn=new JButton(texto);
    	btn.setFont(new Font("SansSerif", Font.BOLD, 14));
    	btn.setBackground(fondo);
    	btn.setForeground(letra);
    	btn.setFocusPainted(false);
    	btn.setBorderPainted(false);
    	btn.setOpaque(true);
    	btn.setContentAreaFilled(true);
    	btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    	btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    	btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    	return btn;
    }
}
