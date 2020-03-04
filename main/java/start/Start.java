package start;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import bll.*;
import model.Customer;
import model.Product;
import presentation.Controller;
import presentation.View;

//import model.Student;
import java.lang.reflect.Field;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Since: Apr 03, 2017
 */
public class Start {
	protected static final Logger LOGGER = Logger.getLogger(Start.class.getName());

	public static void main(String[] args) throws SQLException {

		ProductBLL productBll = new ProductBLL();
		CustomerBLL customerBLL = new CustomerBLL();
		OrderBLL orderBLL = new OrderBLL();

		View view = new View(productBll.getProducts(), customerBLL.getCustomers(), orderBLL.getOrders());

		Controller controller = new Controller(view);

	}

}
