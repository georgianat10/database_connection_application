package bll;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


import bll.validators.Validator;
import dao.AbstractDAO;
import dao.OrderDAO;
import model.Orders;


public class OrderBLL {

	private List<Validator<Orders>> validators;
	private OrderDAO OrderDAO;

	public OrderBLL() {
		validators = new ArrayList<Validator<Orders>>();
		OrderDAO = new OrderDAO();
	}

	/**
	 * Cauta in tabel Orders din baza de date elementul care are ID-ul = id.
	 * @param id Valoarea id-ul de cautat in tabel.
	 * @return Produsul cu ID_ul = id.
	 */
	public Orders findOrderById(int id) {
		Orders st = OrderDAO.findById(id);
		if (st == null) {
			throw new NoSuchElementException("The Order with id =" + id + " was not found!");
		}
		return st;
	}
	
	/**
	 * Sterge din baza de date comanda care are campul ID = id.
	 * @param id ID-ul elementului de sters.
	 */
	public void deleteOrder(int id) {
		OrderDAO.delete(id);

	}
	
	/**
	 * Insereaza in baza de date o noua comanda.
	 * @param order Comanda de inserat.
	 * @return Numarul de randuri inserate.
	 */
	public int insertOrder(Orders order) {
		return OrderDAO.insert(order);

	}
	
	/**
	 * Actualizeaza in baza de date o comanda.
	 * @param order Comanda de actualizat.
	 * @return Numarul de randuri actualizate
	 */
	public int updateOrder(Orders order) {
		return OrderDAO.update(order, order.getOrder_id() );

	}
	
	/**
	 * Converteste randurile din tabelul Orders in obiecte Orders.
	 * @return ArrayList de Orders care reprezinta inerogarile din baza da date.
	 */
	public ArrayList<Orders> getOrders (){
		return OrderDAO.findAll();
	}

}
