package presentation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import model.Customer;
import model.Orders;
import model.Product;

/**
 * Aceasta clasa se ocupa cu creeare interfetei grafice.
 * @author Georgiana 
 *
 */

public class View extends JFrame {
	
	private Font font;

	private JTabbedPane mainPane;
	private JPanel productPanel;
	private JPanel orderPanel;
	private JPanel customerPanel;

	// product panel
	private JPanel btnPanelProduct;
	private JButton addProductBtn;
	private JTable productTable;
	private JScrollPane productTableScrollPanel;
	private JPopupMenu tableMenuProduct;
	private JMenuItem menuDeleteItemProduct;
	private JMenuItem menuEditItemProduct;

	// customer panel
	private JPanel btnPanelCustomer;
	private JButton addCustomerBtn;
	private JTable customerTable;
	private JScrollPane customerTableScrollPanel;
	private JPopupMenu tableMenuCustomer;
	private JMenuItem menuDeleteItemCustomer;
	private JMenuItem menuEditItemCustomer;

	// order panel
	private JButton addOrderBtn;
	private JLabel productLabel;
	private JLabel customerLabel;
	private JLabel quantityLabel;
	private JComboBox<Product> productComboBox;
	private JComboBox<Customer> customerComboBox;
	private JSpinner quantitySlider;

	public View(ArrayList<Product> products, ArrayList<Customer> customers, ArrayList<Orders> orders) {
		
		font = new Font("", 1, 15);
		
		mainPane = new JTabbedPane();
		productPanel = new JPanel();
		orderPanel = new JPanel();
		customerPanel = new JPanel();
		mainPane.addTab("Products", productPanel);
		mainPane.addTab("Customers", customerPanel);
		mainPane.addTab("Orders", orderPanel);
		this.add(mainPane);

		creataProductPanel();
		populateTable(products, productTable);

		creataCustomerPanel();
		populateTable(customers, customerTable);

		creataOrderPanel(products, customers);

		this.setVisible(true);
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Creeaza panel-ul dedicat produselor.
	 */

	private void creataProductPanel() {

		btnPanelProduct = new JPanel();
		addProductBtn = new JButton("Add Product");
		btnPanelProduct.add(addProductBtn);

		productTable = new JTable();

		tableMenuProduct = new JPopupMenu();
		menuDeleteItemProduct = new JMenuItem(" Remove Current Row ");
		menuEditItemProduct = new JMenuItem("Edit Current Row");
		tableMenuProduct.add(menuEditItemProduct);
		tableMenuProduct.addSeparator();
		tableMenuProduct.add(menuDeleteItemProduct);

		productTable.setComponentPopupMenu(tableMenuProduct);

		productTableScrollPanel = new JScrollPane(productTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		BorderLayout b = new BorderLayout();
		b.setHgap(20);
		b.setVgap(20);
		productPanel.setLayout(b);
		productPanel.add(btnPanelProduct, BorderLayout.PAGE_START);
		productPanel.add(productTableScrollPanel);
	}

	/**
	 * Creeaza panel-ul dedicat clientilor.
	 */
	private void creataCustomerPanel() {

		btnPanelCustomer = new JPanel();
		addCustomerBtn = new JButton("Add Customer");
		btnPanelCustomer.add(addCustomerBtn);

		customerTable = new JTable();

		tableMenuCustomer = new JPopupMenu();
		menuDeleteItemCustomer = new JMenuItem(" Remove Current Row ");
		menuEditItemCustomer = new JMenuItem("Edit Current Row");
		tableMenuCustomer.add(menuEditItemCustomer);
		tableMenuCustomer.addSeparator();
		tableMenuCustomer.add(menuDeleteItemCustomer);

		customerTable.setComponentPopupMenu(tableMenuCustomer);

		customerTableScrollPanel = new JScrollPane(customerTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		BorderLayout b = new BorderLayout();
		b.setHgap(20);
		b.setVgap(20);
		customerPanel.setLayout(b);
		customerPanel.add(btnPanelCustomer, BorderLayout.PAGE_START);
		customerPanel.add(customerTableScrollPanel);
	}
	
	/**
	 * Creeaza panel-ul dedita comenzii
	 * @param products Produsele din care se poate alege pentru comanda.
	 * @param customers Clinetii din care se poate alege pentru comanda.
	 */

	private void creataOrderPanel(ArrayList<Product> products, ArrayList<Customer> customers) {

		JPanel btnPanel = new JPanel();
		addOrderBtn = new JButton("Add Order");
		addOrderBtn.setFont(font);
		btnPanel.add(addOrderBtn);
		
		JPanel customer = new JPanel();
		customerLabel = new JLabel(" Customer ");
		customerLabel.setFont(font);
		Customer[] customersArray = new Customer[customers.size()];
		customers.toArray(customersArray);
		customerComboBox = new JComboBox<>(customersArray);
		customerComboBox.setFont(font);
		customer.add(customerLabel);
		customer.add(customerComboBox);

		JPanel product = new JPanel();
		productLabel = new JLabel(" Product ");
		productLabel.setFont(font);
		Product[] productsArray = new Product[products.size()];
		products.toArray(productsArray);
		productComboBox = new JComboBox<>(productsArray);
		productComboBox.setFont(font);
		product.add(productLabel);
		product.add(productComboBox);

		JPanel quantity = new JPanel();
		quantityLabel = new JLabel("Quantity");
		quantityLabel.setFont(font);
		quantitySlider = new JSpinner( new SpinnerNumberModel(1, 0, 100, 1) );
		quantitySlider.setFont(font);
		quantity.add(quantityLabel);
		quantity.add(quantitySlider);

		GridLayout b = new GridLayout(4, 1);
		b.setHgap(20);
		b.setVgap(20);
		orderPanel.setLayout(b);
		orderPanel.add(customer);
		orderPanel.add(product);
		orderPanel.add(quantity);
		orderPanel.add(btnPanel);
	}
	
	/**
	 * Poluleaza tabele din interfata cu utilizatorul
	 * @param rows Datele care vor popula tabelul.
	 * @param table Tabelul de populat.
	 */

	public <T> void populateTable(ArrayList<T> rows, JTable table) {

		if (rows.size() == 0)
			return;

		T o = rows.get(0);

		DefaultTableModel model = new DefaultTableModel();
		String[] columns = new String[o.getClass().getDeclaredFields().length];

		int i = 0;
		for (Field field : o.getClass().getDeclaredFields()) {
			columns[i++] = field.getName();
		}
		model.setColumnIdentifiers(columns);

		table.setModel(model);

		String[] data = new String[rows.getClass().getDeclaredFields().length];
		try {
			for (T obj : rows) {
				i = 0;
				for (Field field : o.getClass().getDeclaredFields()) {
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), o.getClass());
					Method method = propertyDescriptor.getReadMethod();
					Object s = method.invoke(obj);
					data[i++] = s + "";
				}
				model.addRow(data);
			}

		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

		Font font = new Font("", 1, 14);
		table.setFont(font);
		table.setRowHeight(30);

		this.repaint();
		this.revalidate();

	}

	/**
	 * Creeaza un popup in care utilizatorul poate introduce date folosite mai apoi la procesul de inserare sau actualizare.
	 * @param type Contine metadate despre tipul de date T.
	 * @param instance O instanta a Clasei T
	 * @param result Variabila retine datele care au fost introduse in text field-uri.
	 * @param fillText Un indicator care arata daca text field-uri sa fie sau nu populate cu text in prealabil.
	 * @return
	 */
	public <T> int createPopup(Class type, T instance, ArrayList<String> result, int fillText) {

		Object[] obj = new Object[type.getDeclaredFields().length * 2];
		int i = 0;

		ArrayList<JTextArea> textAreas = new ArrayList<JTextArea>();

		try {
			for (Field field : type.getDeclaredFields()) {
				if (!field.getName().contains("id")) {
					obj[i++] = new JLabel(field.getName());
					textAreas.add(new JTextArea(1, 10));
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getReadMethod();
					if (fillText == 1) {
						textAreas.get(i / 2).setText(method.invoke(instance) + "");
					}
					obj[i++] = textAreas.get(i / 2 - 1);
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int s = JOptionPane.showConfirmDialog(null, obj, "", JOptionPane.OK_CANCEL_OPTION);

		for (JTextArea text : textAreas)
			result.add(text.getText());
		return s;
	}
	/**
	 * @return Tabelul in care sunt reprezentate produsele.
	 */
	public JTable getProductsTable() {
		return productTable;
	}
	
	/**
	 * @return Tabelul in care sunt reprezentate clientii.
	 */
	public JTable getCustomersTable() {
		return customerTable;
	}
	
	/**
	 * In momentul in care utilizatorul apasa click pe un rand din tabel acesta sa se selecteze automat.
	 * @param m Ascultator pentur tabelul dedicat produselor.
	 */
	public void addPoductTableListener(MouseListener m) {
		productTable.addMouseListener(m);
	}

	/**
	 * In momentul in care utilizatorul apasa click pe un rand din tabel acesta sa se selecteze automat.
	 * @param m Ascultator pentur tabelul dedicat clientilor.
	 */
	public void addCustomerTableListener(MouseListener m) {
		customerTable.addMouseListener(m);
	}

	/**
	 * Adauga ascultatoare pe elementele din MenuPopup dedicat produselor.
	 * @param delete Ascultator pentru item-ul de delete.
	 * @param edit Ascultator pentru item-ul de edit.
	 */
	public void addProductPopupMenuListener(ActionListener delete, ActionListener edit) {
		menuDeleteItemProduct.addActionListener(delete);
		menuEditItemProduct.addActionListener(edit);
	}

	/**
	 * Adauga ascultatoare pe elementele din MenuPopup dedicat clientilor.
	 * @param delete Ascultator pentru item-ul de delete.
	 * @param edit Ascultator pentru item-ul de edit.
	 */
	public void addCustomerPopupMenuListener(ActionListener delete, ActionListener edit) {
		menuDeleteItemCustomer.addActionListener(delete);
		menuEditItemCustomer.addActionListener(edit);
	}

	public void addAddProductListener(ActionListener a) {
		addProductBtn.addActionListener(a);
	}

	public void addAddCustomerListener(ActionListener a) {
		addCustomerBtn.addActionListener(a);
	}
	
	public void addAddOrderListener (ActionListener a) {
		addOrderBtn.addActionListener(a); 
	}
	
	public Product getProductSelected() {
		return (Product) productComboBox.getSelectedItem();
	}
	
	public Customer getCustomerSelected() {
		return (Customer) customerComboBox.getSelectedItem();
	}

	public int getQuantity () {
		return Integer.parseInt( quantitySlider.getValue() + "");
	}
	
	public void updateProductComboBox(ArrayList<Product> products) {
		productComboBox.removeAllItems();
		
		for (Product p : products)
			productComboBox.addItem(p);
		productComboBox.repaint();
	}
	
	public void updateCustomerComboBox(ArrayList<Customer> customers) {
		customerComboBox.removeAll();
		
		for (Customer c : customers)
			customerComboBox.addItem( c );
	}
	
	public void showError(String errMessage) {
		JOptionPane.showMessageDialog(this, errMessage);
	}
}
