package Gui;

import javax.swing.*;



import java.awt.*;
import logica.SistemaCafe;
import modelo.usuario.Administrador;
import modelo.usuario.Cliente;
import modelo.usuario.Empleado;
import modelo.usuario.Usuario;


public class VentanaPrincipal extends JFrame {

    public static final Color COLOR_FONDO        = new Color(30, 30, 40);
    public static final Color COLOR_PANEL        = new Color(42, 42, 58);
    public static final Color COLOR_ACENTO       = new Color(255, 180, 50);  
    public static final Color COLOR_ACENTO2      = new Color(100, 200, 150);  
    public static final Color COLOR_TEXTO        = new Color(230, 230, 230);
    public static final Color COLOR_TEXTO_TENUE  = new Color(150, 150, 170);
    public static final Font  FUENTE_TITULO      = new Font("SansSerif", Font.BOLD, 22);
    public static final Font  FUENTE_NORMAL      = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font  FUENTE_PEQUENA     = new Font("SansSerif", Font.PLAIN, 12);

    // ── Estado ─────────────────────────────────────────────────────────
    private SistemaCafe sistema;
    private CardLayout cardLayout;
    private JPanel contenedor;
    private Usuario usuarioActual;

   
    public static final String TARJETA_LOGIN          = "LOGIN";
    public static final String TARJETA_REGISTRO       = "REGISTRO";
    public static final String TARJETA_ADMIN          = "ADMIN";//p2
    public static final String TARJETA_CLIENTE        = "CLIENTE";//p2
    public static final String TARJETA_EMPLEADO       = "EMPLEADO";//p2
    public static final String TARJETA_GRAFICAS       = "GRAFICAS";   //p3

   //el builder
    public VentanaPrincipal() {
        sistema = new SistemaCafe();
        sistema.cargarDatos();

        configurarVentana();
        inicializarPaneles();
        mostrarPanel(TARJETA_LOGIN);
    }

    // Jframe
    private void configurarVentana() {
        setTitle("Dulces & Dados — Board Game Café");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(800, 550));
        setLocationRelativeTo(null);   // centrar en pantalla


     // Cerrar app
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
            	sistema.guardarDatos();
                dispose();
                System.exit(0);
            }
        });
        // Layout raíz
        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        contenedor.setBackground(COLOR_FONDO);
        setContentPane(contenedor);
    }

    //Registro inicial de paneles
    private void inicializarPaneles() {
       
        contenedor.add(new PanelLogin(this), TARJETA_LOGIN);
        contenedor.add(new PanelRegistro(this), TARJETA_REGISTRO);

        //Paneles persona 2-ADmin cliente empleado
        contenedor.add(new PanelAdministrador(this), TARJETA_ADMIN);
        contenedor.add(new PanelCliente(this), TARJETA_CLIENTE);
        contenedor.add(new PanelEmpleado(this), TARJETA_EMPLEADO);

        // Panel de gráficas — Persona 3
        contenedor.add(crearPlaceholder("Panel Gráficas\n(Persona 3)"),        TARJETA_GRAFICAS);
    }


    
    
    private JPanel crearPlaceholder(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO);
        JLabel lbl = new JLabel("<html><center>" + texto.replace("\n", "<br>") + "</center></html>",
                SwingConstants.CENTER);
        lbl.setFont(FUENTE_TITULO);
        lbl.setForeground(COLOR_TEXTO_TENUE);
        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    //

 
    public void mostrarPanel(String nombre) {
        cardLayout.show(contenedor, nombre);
    }


    public void registrarPanel(JPanel panel, String nombre) {
        contenedor.add(panel, nombre);
        contenedor.revalidate();
    }


    public SistemaCafe getSistema() {
        return sistema;
    }


    public void redirigirUsuario(Usuario usuario) {
        this.usuarioActual=usuario;

        if(usuario instanceof Administrador) {
            mostrarPanel(TARJETA_ADMIN);
        }
        else if(usuario instanceof Cliente) {
            mostrarPanel(TARJETA_CLIENTE);
        }
        else if(usuario instanceof Empleado) {
            mostrarPanel(TARJETA_EMPLEADO);
        }
    }
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    //  main 
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new VentanaPrincipal().setVisible(true);
        });
    }
}
