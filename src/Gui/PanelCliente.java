package Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import modelo.torneo.Inscripcion;
import modelo.torneo.Torneo;
import modelo.usuario.Cliente;

public class PanelCliente extends JPanel {

	private VentanaPrincipal ventana;
	private JTextArea areaInfo;

	public PanelCliente(VentanaPrincipal ventana) {
		this.ventana=ventana;
		construirUI();
	}

	private void construirUI() {
		setBackground(VentanaPrincipal.COLOR_FONDO);
		setLayout(new BorderLayout(20, 20));
		setBorder(new EmptyBorder(30, 40, 30, 40));

		JLabel titulo=new JLabel("Panel Cliente");
		titulo.setFont(VentanaPrincipal.FUENTE_TITULO);
		titulo.setForeground(VentanaPrincipal.COLOR_ACENTO);
		add(titulo, BorderLayout.NORTH);

		JPanel panelBotones=new JPanel(new GridLayout(0, 1, 10, 10));
		panelBotones.setBackground(VentanaPrincipal.COLOR_FONDO);

		JButton btnTorneos=crearBoton("Ver torneos");
		JButton btnInscribir=crearBoton("Inscribirme a torneo");
		JButton btnDesinscribir=crearBoton("Desinscribirme");
		JButton btnMisInscripciones=crearBoton("Mis inscripciones");
		JButton btnPerfil=crearBoton("Mi perfil");
		JButton btnSalir=crearBoton("Cerrar sesión");

		panelBotones.add(btnTorneos);
		panelBotones.add(btnInscribir);
		panelBotones.add(btnDesinscribir);
		panelBotones.add(btnMisInscripciones);
		panelBotones.add(btnPerfil);
		panelBotones.add(btnSalir);

		add(panelBotones, BorderLayout.WEST);

		areaInfo=new JTextArea();
		areaInfo.setEditable(false);
		areaInfo.setFont(VentanaPrincipal.FUENTE_NORMAL);
		areaInfo.setBackground(VentanaPrincipal.COLOR_PANEL);
		areaInfo.setForeground(VentanaPrincipal.COLOR_TEXTO);
		areaInfo.setBorder(new EmptyBorder(15, 15, 15, 15));

		JScrollPane scroll=new JScrollPane(areaInfo);
		scroll.setBorder(null);
		add(scroll, BorderLayout.CENTER);

		btnTorneos.addActionListener(e -> mostrarTorneos());
		btnInscribir.addActionListener(e -> inscribirse());
		btnDesinscribir.addActionListener(e -> desinscribirse());
		btnMisInscripciones.addActionListener(e -> mostrarMisInscripciones());
		btnPerfil.addActionListener(e -> mostrarPerfil());
		btnSalir.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.TARJETA_LOGIN));
	}

	private Cliente getCliente() {
		return (Cliente) ventana.getUsuarioActual();
	}

	private void mostrarTorneos() {
		areaInfo.setText("----- TORNEOS DISPONIBLES -----\n\n");

		if(ventana.getSistema().getTorneos().isEmpty()) {
			areaInfo.append("No hay torneos registrados.");
			return;
		}

		for(Torneo t : ventana.getSistema().getTorneos()) {
			areaInfo.append(t.toString() + "\n");
			areaInfo.append("Cupos disponibles: " + t.cuposDisponibles() + "\n");
			areaInfo.append("Cupos preferenciales disponibles: " + t.cuposPreferencialesDisponibles() + "\n\n");
		}
	}

	private void inscribirse() {
		String idTexto=JOptionPane.showInputDialog(this, "ID del torneo:");
		if(idTexto==null) return;

		String cuposTexto=JOptionPane.showInputDialog(this, "Cantidad de cupos:");
		if(cuposTexto==null) return;

		try {
			int id=Integer.parseInt(idTexto);
			int cupos=Integer.parseInt(cuposTexto);

			String error=ventana.getSistema().inscribirEnTorneo(id, getCliente(), cupos);

			if(error==null) {
				JOptionPane.showMessageDialog(this, "Inscripción realizada correctamente");
				mostrarMisInscripciones();
			}
			else {
				JOptionPane.showMessageDialog(this, error);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos");
		}
	}

	private void desinscribirse() {
		String idTexto=JOptionPane.showInputDialog(this, "ID del torneo:");
		if(idTexto==null) return;

		try {
			int id=Integer.parseInt(idTexto);
			String error=ventana.getSistema().desinscribirDeTorneo(id, getCliente());

			if(error==null) {
				JOptionPane.showMessageDialog(this, "Desinscripción realizada correctamente");
				mostrarMisInscripciones();
			}
			else {
				JOptionPane.showMessageDialog(this, error);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Ingrese un ID válido");
		}
	}

	private void mostrarMisInscripciones() {
		Cliente cliente=getCliente();
		areaInfo.setText("----- MIS INSCRIPCIONES -----\n\n");

		boolean tiene=false;

		for(Torneo t : ventana.getSistema().getTorneos()) {
			Inscripcion ins=t.buscarInscripcion(cliente);

			if(ins!=null) {
				tiene=true;
				areaInfo.append("Torneo ID: " + t.getIdTorneo() + "\n");
				areaInfo.append("Juego: " + t.getJuego().getNombre() + "\n");
				areaInfo.append("Tipo: " + t.getTipo() + "\n");
				areaInfo.append("Día: " + t.getDia() + "\n");
				areaInfo.append("Cupos tomados: " + ins.getCuposTomados() + "\n\n");
			}
		}

		if(!tiene) {
			areaInfo.append("No estás inscrito en ningún torneo.");
		}
	}

	private void mostrarPerfil() {
		Cliente cliente=getCliente();

		areaInfo.setText("----- MI PERFIL -----\n\n");
		areaInfo.append("Nombre: " + cliente.getNombre() + "\n");
		areaInfo.append("Login: " + cliente.getLogin() + "\n");
		areaInfo.append("Puntos: " + cliente.getPuntosFidelidad() + "\n");

		if(cliente.tieneBono()) {
			areaInfo.append("Bono activo: " + (cliente.getBonoDescuento()*100) + "%\n");
		}
		else {
			areaInfo.append("Bono activo: No tienes bono.\n");
		}
	}

	private JButton crearBoton(String texto) {
		JButton btn=new JButton(texto);
		btn.setFont(VentanaPrincipal.FUENTE_NORMAL);
		btn.setBackground(VentanaPrincipal.COLOR_ACENTO);
		btn.setForeground(Color.BLACK);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setContentAreaFilled(true);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return btn;
	}
}
