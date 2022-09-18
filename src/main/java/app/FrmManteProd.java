package app;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Categoria;
import model.Producto;
import model.Proveedor;
import model.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTable;

public class FrmManteProd extends JFrame {

	private JPanel contentPane;

	private JTextArea txtSalida;
	private JTextField txtCodigo;
	private JComboBox cboCategorias;
	private JComboBox cboProveedores;
	private JTextField txtDescripcion;
	private JTextField txtStock;
	private JTextField txtPrecio;
	private JTable tblSalida;
	
	DefaultTableModel modelo = new DefaultTableModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmManteProd frame = new FrmManteProd();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmManteProd() {
		setTitle("Mantenimiento de Productos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 577);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				registrar();
			}
		});
		btnRegistrar.setBounds(324, 29, 89, 23);
		contentPane.add(btnRegistrar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 171, 414, 143);
		contentPane.add(scrollPane);

		txtSalida = new JTextArea();
		scrollPane.setViewportView(txtSalida);

		JButton btnListado = new JButton("Listado");
		btnListado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listado();
			}
		});
		btnListado.setBounds(177, 322, 89, 23);
		contentPane.add(btnListado);

		txtCodigo = new JTextField();
		txtCodigo.setBounds(122, 11, 86, 20);
		contentPane.add(txtCodigo);
		txtCodigo.setColumns(10);

		JLabel lblCodigo = new JLabel("Id. Producto :");
		lblCodigo.setBounds(10, 14, 102, 14);
		contentPane.add(lblCodigo);

		cboCategorias = new JComboBox();
		cboCategorias.setBounds(122, 70, 86, 22);
		contentPane.add(cboCategorias);

		JLabel lblCategora = new JLabel("Categor\u00EDa");
		lblCategora.setBounds(10, 74, 102, 14);
		contentPane.add(lblCategora);

		JLabel lblNomProducto = new JLabel("Nom. Producto :");
		lblNomProducto.setBounds(10, 45, 102, 14);
		contentPane.add(lblNomProducto);

		txtDescripcion = new JTextField();
		txtDescripcion.setColumns(10);
		txtDescripcion.setBounds(122, 42, 144, 20);
		contentPane.add(txtDescripcion);

		JLabel lblStock = new JLabel("Stock:");
		lblStock.setBounds(10, 106, 102, 14);
		contentPane.add(lblStock);

		txtStock = new JTextField();
		txtStock.setColumns(10);
		txtStock.setBounds(122, 103, 77, 20);
		contentPane.add(txtStock);

		JLabel lblPrecio = new JLabel("Precio:");
		lblPrecio.setBounds(10, 134, 102, 14);
		contentPane.add(lblPrecio);

		txtPrecio = new JTextField();
		txtPrecio.setColumns(10);
		txtPrecio.setBounds(122, 131, 77, 20);
		contentPane.add(txtPrecio);

		JLabel lblProveedores = new JLabel("Proveedor:");
		lblProveedores.setBounds(230, 106, 102, 14);
		contentPane.add(lblProveedores);

		cboProveedores = new JComboBox();
		cboProveedores.setBounds(300, 104, 120, 22);
		contentPane.add(cboProveedores);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscar();
			}
		});
		btnBuscar.setBounds(324, 63, 89, 23);
		contentPane.add(btnBuscar);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 358, 414, 169);
		contentPane.add(scrollPane_1);
		
		tblSalida = new JTable();
		scrollPane_1.setViewportView(tblSalida);
		tblSalida.setModel(modelo);
		modelo.addColumn("C�digo");
		modelo.addColumn("Nombre");
		modelo.addColumn("Categor�a");
		modelo.addColumn("Proveedor");

		llenaCombo();
	}

	void llenaCombo() {
		// conexi�n
		EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("mysql");
		EntityManager em = fabrica.createEntityManager();
				
		// Combo1. obtener un listado de las categor�as
		List<Categoria> lstCategorias = em.createQuery("select c from Categoria c", Categoria.class).getResultList();		
		// pasar el listado al cboCategorias
		cboCategorias.addItem("Seleccione....");
		for (Categoria c : lstCategorias) {
			cboCategorias.addItem(c.getIdcategoria() + "-" + c.getDescripcion());
		}

		// Combo2. obtener un listado de los proveedores
		List<Proveedor> lstProveedores = em.createQuery("select p from Proveedor p", Proveedor.class).getResultList();		
		// pasar el listado al cboProveedores
		cboProveedores.addItem("Seleccione....");
		for (Proveedor p : lstProveedores) {
			cboProveedores.addItem(p.getIdprovedor() + "-" + p.getNombre_rs());
		}
		
		em.close();
		
	}

	void registrar() {
		// leer los campos
		String codigo = txtCodigo.getText(); // Ojo!! completar los m�todos
		String nombre = txtDescripcion.getText();
		int stock = Integer.parseInt(txtStock.getText());
		double precio = Double.parseDouble(txtPrecio.getText());
		int idcategoria = cboCategorias.getSelectedIndex();
		int idproveedor = cboProveedores.getSelectedIndex();
		int estado = 1; // True
		
		// validaciones
		if (codigo == null) {
			// aviso
			return;
		}
		// obj de nuevo producto
		Producto pr = new Producto();
		pr.setId_prod(codigo);
		pr.setDes_prod(nombre);
		pr.setStk_prod(stock);
		pr.setPre_prod(precio);
		pr.setIdcategoria(idcategoria);
		pr.setIdprovedor(idproveedor);
		pr.setEst_prod(estado);
		
		// guardar en la tabla
		EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("mysql");
		EntityManager em = fabrica.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(pr);
			em.getTransaction().commit();
			aviso("Registo OK", "Aviso Sistema",JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			aviso("Error al Registrar", "Aviso Sistema",JOptionPane.ERROR_MESSAGE);
		}
		em.close();
		
	}
	
	String leerCodigo() {
		if (!txtCodigo.getText().matches("[Pp][0-9]{4}")) {
			// aviso
			return null;
		}
		return txtCodigo.getText();
	}
	
	void aviso(String msg, String tit, int icono) {
		JOptionPane.showMessageDialog(this, msg, tit,icono);
	}

	void listado() {
		// Obtener la conexion
		EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("mysql");
		EntityManager em = fabrica.createEntityManager();
		
		// Generar un listado
		List<Producto> lstProductos = em.createQuery("select pr from Producto pr", Producto.class).getResultList();
		
		// Mostrar el listado en el txt Area
		for (Producto pr : lstProductos) {
			// muestra en el textArea
			imprimir("Id Producto..: " + pr.getId_prod());
			imprimir("Nombre.......: " + pr.getDes_prod());
			imprimir("Categoria....: " + 
					pr.getIdcategoria() + "-" + pr.getCategoria().getDescripcion());
			imprimir("Proveedor....: " + 
					pr.getIdprovedor() + "-" + pr.getProveedor().getNombre_rs());
			imprimir("------------------------------------------------");
			// muestra en la tabla
			Object datos [] = {pr.getId_prod(), pr.getDes_prod(), 
					pr.getIdcategoria() + "-" + pr.getCategoria().getDescripcion(),
					pr.getIdprovedor() + "-" + pr.getProveedor().getNombre_rs()};
			modelo.addRow(datos);
		}		
		// Cerrar
		em.close();
			
	}
	
	// M�todo para imprimir
		void imprimir(String s) {
		txtSalida.append(s + "\n");
	}
	
	void buscar() {
		// leer el codigo
		String codigo = leerCodigo();
		
		// buscar un Producto con el c�digo indicado
		EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("mysql");
		EntityManager em = fabrica.createEntityManager();				
		Producto pr = em.find(Producto.class, codigo);
		
		// si existe, muestra en los campos los valores correspondientes
		if (pr != null) {
			txtDescripcion.setText(pr.getDes_prod());
			txtStock.setText(pr.getStk_prod()+"");
			// completar.....
		} else {
			// si no existe muestra mensaje de error
			aviso("C�digo de producto no existe", "Aviso Sistema",JOptionPane.ERROR_MESSAGE);
		}
		em.close();		
	}
}
