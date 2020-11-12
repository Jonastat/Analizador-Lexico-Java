package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextArea;

import Analizador.AnalizadorLexico;
import Analizador.Token;

public class VentanaPrincipal extends JFrame implements ActionListener {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private JPanel contentPane;
	private JTextArea textArea;
	private JButton btnAnalizar, btnLimpiarVentana;
	private DefaultTableModel modeloTabla;
	private JScrollPane scrollTabla, scrollTextArea;
	private JTable tabla;

	public VentanaPrincipal() throws FileNotFoundException, ClassNotFoundException, IOException {
		setTitle("Analizador Léxico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		modeloTabla = new DefaultTableModel();
		modeloTabla.addColumn("Palabra");
		modeloTabla.addColumn("Categoría");
		modeloTabla.addColumn("Fila");
		modeloTabla.addColumn("Columna");
		
		scrollTabla = new JScrollPane();
		scrollTabla.setBounds(10, 368, 988, 350);
		tabla = new JTable(modeloTabla);
		scrollTabla.setViewportView(tabla);
		contentPane.add(scrollTabla);
		
		scrollTextArea = new JScrollPane();
		scrollTextArea.setBounds(10, 45, 988, 303);
		textArea = new JTextArea();
		scrollTextArea.setViewportView(textArea);
		contentPane.add(scrollTextArea);
		
		btnAnalizar = new JButton("Analizar C\u00F3digo Fuente");
		btnAnalizar.setBounds(10, 11, 163, 23);
		contentPane.add(btnAnalizar);
		btnAnalizar.addActionListener(this);
		
		btnLimpiarVentana = new JButton("Limpiar Ventana");
		btnLimpiarVentana.setBounds(183, 11, 163, 23);
		contentPane.add(btnLimpiarVentana);
		btnLimpiarVentana.addActionListener(this);

	}

	public void borrarTabla () {
		modeloTabla.setNumRows(0);
	}
	
	public void borrarTextArea () {
		textArea.setText("");
	}
	
	public void analizarCodigo (String codigoFuente) {
		
		AnalizadorLexico al = new AnalizadorLexico(codigoFuente);
		al.analizar();
		ArrayList<Token> listaTokens = new ArrayList<Token>();
		listaTokens = al.getListaTokens();

		for (Token obj : listaTokens) {

			modeloTabla.addRow(new Object[] {obj.getPalabra(), obj.getCategoria(), obj.getFila(), obj.getColumna()});

		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btnAnalizar) {
			
			borrarTabla();
			analizarCodigo(textArea.getText());
		}
		
		if (e.getSource() == btnLimpiarVentana) {
			
			borrarTabla();
			borrarTextArea();
		}

	}
}
