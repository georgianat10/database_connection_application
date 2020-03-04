package bll;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import bll.validators.EmailValidator;
import bll.validators.FirstName;
import bll.validators.LastName;
import bll.validators.Validator;
import dao.AbstractDAO;
import dao.CustomerDAO;
import model.Customer;

public class CustomerBLL {

	private List<Validator<Customer>> validators;
	private CustomerDAO CustomerDAO;

	public CustomerBLL() {
		validators = new ArrayList<Validator<Customer>>();
		validators.add(new EmailValidator());
		validators.add(new FirstName());
		validators.add(new LastName());
		CustomerDAO = new CustomerDAO();
	}

	/**
	 * Cauta in tabel Customer din baza de date elementul care are ID-ul = id.
	 * @param id Valoarea id-ul de cautat in tabel.
	 * @return Produsul cu ID_ul = id.
	 */
	public Customer findCustomerById(int id) {
		Customer st = CustomerDAO.findById(id);
		if (st == null) {
			throw new NoSuchElementException("The Customer with id =" + id + " was not found!");
		}
		return st;
	}

	/**
	 * Sterge din baza de date clientul care are campul ID = id.
	 * @param id ID-ul elementului de sters.
	 */
	public void deleteCustomer(int id) {
		CustomerDAO.delete(id);

	}

	/**
	 * Insereaza in baza de date un nou clinet.
	 * @param customer Clinetul de inserat.
	 * @return Numarul de randuri inserate.
	 */
	public int insertCustomer(Customer customer) {
		for (Validator<Customer> validator : validators)
			validator.validate(customer);
		return CustomerDAO.insert(customer);

	}

	/**
	 * Actualizeaza in baza de date un clinet.
	 * @param customer Clientul de actualizat.
	 * @return Numarul de randuri actualizate
	 */
	public int updateCustomer(Customer customer) {
		for (Validator<Customer> validator : validators)
			validator.validate(customer);
		return CustomerDAO.update(customer, customer.getCustomer_id());

	}

	/**
	 * Converteste randurile din tabelul Customer in obiecte Customer.
	 * @return ArrayList de Customer care reprezinta inerogarile din baza da date.
	 */
	public ArrayList<Customer> getCustomers() {
		return CustomerDAO.findAll();
	}
}
