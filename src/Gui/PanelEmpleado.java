package Gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import modelo.torneo.Torneo;
import modelo.usuario.Empleado;

public class PanelEmpleado extends JPanel {

	private VentanaPrincipal ventana;
	private JTextArea areaInfo;

	public PanelEmpleado(VentanaPrincipal ventana) {
		this.ventana=ventana;
		construirUI();
	}

	private void construirUI() {
		setBackground(VentanaPrincipal.COLOR_FONDO);
		setLayout(new BorderLayout(20, 20));
		setBorder(new EmptyBorder(30, 40, 30, 40));

		JLabel titulo=new JLabel("Panel Empleado");
		titulo.setFont(VentanaPrincipal.FUENTE_TITULO);
		titulo.setForeground(VentanaPrincipal.COLOR_ACENTO);
		add(titulo, BorderLayout.NORTH);

		JPanel panelBotones=new JPanel(new GridLayout(0, 1, 10, 10));
		panelBotones.setBackground(VentanaPrincipal.COLOR_FONDO);

		JButton btnTorneos=crearBoton("Ver torneos");
		JButton btnInscribir=crearBoton("Inscribirme a torneo");
		JButton btnDesinscribir=crearBoton("Desinscribirme");
		JButton btnTurno=crearBoton("Consultar turno");
		JButton btnSalir=crearBoton("Cerrar sesión");

		panelBotones.add(btnTorneos);
		panelBotones.add(btnInscribir);
		panelBotones.add(btnDesinscribir);
		panelBotones.add(btnTurno);
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
		btnTurno.addActionListener(e -> consultarTurno());
		btnSalir.addActionListener(e -> ventana.mostrarPanel(VentanaPrincipal.TARJETA_LOGIN));
	}

	private Empleado getEmpleado() {
		return (Empleado) ventana.getUsuarioActual();
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

		String cuposTexto=JOptionPane.showInputDialog(this, "Cantidad de cupos (1-3):");
		if(cuposTexto==null) return;

		try {
			int id=Integer.parseInt(idTexto);
			int cupos=Integer.parseInt(cuposTexto);

			String error=ventana.getSistema().inscribirEnTorneo(id, getEmpleado(), cupos);

			if(error==null) {
				JOptionPane.showMessageDialog(this, "Inscripción realizada correctamente.");
				mostrarTorneos();
			}
			else {
				JOptionPane.showMessageDialog(this, error);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.");
		}
	}

	private void desinscribirse() {
		String idTexto=JOptionPane.showInputDialog(this, "ID del torneo:");
		if(idTexto==null) return;

		try {
			int id=Integer.parseInt(idTexto);
			String error=ventana.getSistema().desinscribirDeTorneo(id, getEmpleado());

			if(error==null) {
				JOptionPane.showMessageDialog(this, "Desinscripción realizada correctamente.");
				mostrarTorneos();
			}
			else {
				JOptionPane.showMessageDialog(this, error);
			}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(this, "Ingrese un ID válido.");
		}
	}

	private void consultarTurno() {
		Empleado empleado=getEmpleado();

		areaInfo.setText("----- MI TURNO -----\n\n");
		areaInfo.append("Nombre: " + empleado.getNombre() + "\n");
		areaInfo.append("Login: " + empleado.getLogin() + "\n\n");

		if(empleado.getTurno()==null) {
			areaInfo.append("No tienes turno asignado.");
		}
		else {
			areaInfo.append("Día del turno: " + empleado.getTurno().getDia());
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