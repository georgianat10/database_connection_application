package presentation;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import bll.CustomerBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import model.Customer;
import model.Orders;
import model.Product;

/**
 * Aceasta clasa se ocupa cu logica din spate a aplicatiei (listeneri pentru componentele din interfata grafica).
 * @author Georgiana
 *
 */

public class Controller {

	View view;

	public Controller(View view) {
		this.view = view;
		view.addPoductTableListener(new TableMouseListener(view.getProductsTable()));
		view.addCustomerTableListener(new TableMouseListener(view.getCustomersTable()));

		view.addProductPopupMenuListener(new DeletePopupMenuListener(view.getProductsTable()),
				new EditPopupMenuListener(view.getProductsTable(), 0));
		view.addCustomerPopupMenuListener(new DeletePopupMenuListener(view.getCustomersTable()),
				new EditPopupMenuListener(view.getCustomersTable(), 1));

		view.addAddProductListener(new AddElementListener(view.getProductsTable(), 0));
		view.addAddCustomerListener(new AddElementListener(view.getCustomersTable(), 1));

		view.addAddOrderListener(new AddOrder());
	}
	
	/**
	 * Ascultator pentru tabel.
	 * @author Georgiana
	 *
	 */
	private class TableMouseListener extends MouseAdapter {

		private JTable table;

		public TableMouseListener(JTable table) {
			this.table = table;
		}

		@Override
		public void mousePressed(MouseEvent event) {
			// selects the row at which point the mouse is clicked
			Point point = event.getPoint();
			int currentRow = table.rowAtPoint(point);
			table.setRowSelectionInterval(currentRow, currentRow);
		}

	}
	/**
	 * Asculatator pentru fildul de edit din meniu.
	 * @author Georgiana
	 *
	 */
	private class EditPopupMenuListener implements ActionListener {

		private JTable table;
		private int type;

		public EditPopupMenuListener(JTable table, int type) {
			this.table = table;
			this.type = type;
		}

		public void actionPerformed(ActionEvent event) {
			int selectedRow = table.getSelectedRow();
			if (type == 0) { // 0 inseamna ca vream sa editam un produs
				ProductBLL productBLL = new ProductBLL();
				Product product = productBLL.findProductById(Integer.parseInt(table.getValueAt(selectedRow, 0) + ""));
				ArrayList<String> values = new ArrayList<String>();
				int i = view.createPopup(Product.class, product, values, 1);
				if (i == 0) {
					product.setName(values.get(0));
					product.setProducer(values.get(1));
					product.setPrice(Double.parseDouble(values.get(2)));
					product.setObservation(values.get(3));
					product.setStock(Integer.parseInt(values.get(4)));
					try {
						productBLL.updateProduct(product);
					} catch (IllegalArgumentException e) {
						System.out.println("Bad Input");
					}
					view.populateTable(productBLL.getProducts(), table);
					view.updateProductComboBox(productBLL.getProducts());
				}
			} else if (type == 1) { // 1 inseamna ca vrem sa editam un costomer
				CustomerBLL customertBLL = new CustomerBLL();
				Customer customer = customertBLL
						.findCustomerById(Integer.parseInt(table.getValueAt(selectedRow, 0) + ""));
				ArrayList<String> values = new ArrayList<String>();
				int i = view.createPopup(Customer.class, customer, values, 1);
				if (i == 0) {
					customer.setFirst_name(values.get(0));
					customer.setLast_name(values.get(1));
					customer.setAddress(values.get(2));
					customer.setEmail(values.get(3));
					customer.setPhone_number(values.get(4));
					try {
						customertBLL.updateCustomer(customer);
					} catch (IllegalArgumentException e) {
						System.out.println("Bad Input");
					}
					view.populateTable(customertBLL.getCustomers(), table);
					view.updateCustomerComboBox(customertBLL.getCustomers());
				}
			}
		}
	}

	/**
	 * Asculatator pentru fildul de delete din meniu.
	 * @author Georgiana
	 *
	 */
	private class DeletePopupMenuListener implements ActionListener {

		private JTable table;

		public DeletePopupMenuListener(JTable table) {
			this.table = table;
		}

		public void actionPerformed(ActionEvent event) {

			int selectedRow = table.getSelectedRow();
			String id = (String) table.getValueAt(selectedRow, 0);

			if (table.equals(view.getProductsTable())) {
				ProductBLL productBll = new ProductBLL();
				productBll.deleteProduct(Integer.parseInt(id));
				view.populateTable(productBll.getProducts(), table);
			} else if (table.equals(view.getCustomersTable())) {
				CustomerBLL customerBll = new CustomerBLL();
				customerBll.deleteCustomer(Integer.parseInt(id));
				view.populateTable(customerBll.getCustomers(), table);
			}
		}
	}

	/**
	 * Ascultator pentru a adauga un produs/client in baza de date.
	 * @author Georgiana
	 *
	 */
	private class AddElementListener implements ActionListener {

		private JTable table;
		private int type;

		public AddElementListener(JTable table, int type) {
			this.table = table;
			this.type = type;
		}

		public void actionPerformed(ActionEvent event) {
			int selectedRow = table.getSelectedRow();
			if (type == 0) { // 0 inseamna ca vream sa editam un produs
				ProductBLL productBLL = new ProductBLL();
				Product product = new Product();
				ArrayList<String> values = new ArrayList<String>();
				int i = view.createPopup(Product.class, product, values, 0);
				if (i == 0) {
					product.setName(values.get(0));
					product.setProducer(values.get(1));
					product.setPrice(Double.parseDouble(values.get(2)));
					product.setObservation(values.get(3));
					product.setStock(Integer.parseInt( values.get(4) ));
					try {
						productBLL.insertProduct(product);
					} catch (IllegalArgumentException e) {
						System.out.println("Bad Input");
					}
					view.populateTable(productBLL.getProducts(), table);
				}
			} else if (type == 1) { // 1 inseamna ca vrem sa editam un costomer
				CustomerBLL customertBLL = new CustomerBLL();
				Customer customer = new Customer();
				ArrayList<String> values = new ArrayList<String>();
				int i = view.createPopup(Customer.class, customer, values, 0);
				if (i == 0) {
					customer.setFirst_name(values.get(0));
					customer.setLast_name(values.get(1));
					customer.setAddress(values.get(2));
					customer.setEmail(values.get(3));
					customer.setPhone_number(values.get(4));
					try {
						customertBLL.insertCustomer(customer);
					} catch (IllegalArgumentException e) {
						System.out.println("Bad Input");
					}
					view.populateTable(customertBLL.getCustomers(), table);
				}
			}
		}

	}

	/**
	 * Asculatator pentru adaugarea unei comenzi in baza de date si creearea unei facturi.
	 * @author Georgiana
	 *
	 */
	private class AddOrder implements ActionListener {

		public void actionPerformed(ActionEvent a) {

			Product product = view.getProductSelected();
			Customer customer = view.getCustomerSelected();
			int quantity = view.getQuantity();

			if (product.getStock() - quantity >= 0) {
				product.setStock(product.getStock() - quantity);

				CustomerBLL customerBLL = new CustomerBLL();
				ProductBLL productBLL = new ProductBLL();
				OrderBLL orderBLL = new OrderBLL();

				try {
					productBLL.updateProduct(product);
				} catch (IllegalArgumentException e) {
					System.out.println("Bad Input");
				}
				Orders order = new Orders(product.getId(), customer.getCustomer_id(), quantity);

				orderBLL.insertOrder(order);
				
				order.setOrder_id( customer.getCustomer_id()+ product.getId() );

				view.populateTable(productBLL.getProducts(), view.getProductsTable());
				view.updateProductComboBox(productBLL.getProducts());
				view.updateCustomerComboBox(customerBLL.getCustomers());

				try (FileWriter writer = new FileWriter("Order" + order.getOrder_id() + ".txt");
						BufferedWriter bw = new BufferedWriter(writer)) {
					bw.write("ORDER ID " + order.getOrder_id());
					bw.newLine();
					bw.newLine();
					bw.write("##########################################################");
					bw.newLine();
					bw.newLine();
					bw.write("Costomer details ");
					bw.newLine();
					bw.newLine();
					bw.write("ID: " + customer.getCustomer_id() + "");
					bw.newLine();
					bw.write("NAME: " + customer.getFirst_name() + " " + customer.getLast_name());
					bw.newLine();
					bw.write("EMAIL: " + customer.getEmail());
					bw.newLine();
					bw.write("ADDRESS: " + customer.getAddress());
					bw.newLine();
					bw.write("PHONE NUMBER: " + customer.getPhone_number());
					bw.newLine();
					bw.newLine();
					bw.write("##########################################################");
					bw.newLine();
					bw.newLine();
					bw.write("PRODUCTS DETAILS");
					bw.newLine();
					bw.newLine();
					bw.write(String.format("%-20s %-20s  %-20s%n", "NAME", "PRICE", "QUANTITY"));
					bw.write(String.format("%-20s %-20s %-20s%n", product.getName(), product.getPrice() + "",
							order.getQuantity() + ""));
					bw.newLine();
					bw.newLine();
					bw.write("TOTAL PRICE: " + (order.getQuantity() * product.getPrice()));
					bw.newLine();

				} catch (IOException e) {
					System.err.format("IOException: %s%n", e);
				}
			}

		}

	}
}
