package bll;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


import bll.validators.Validator;
import dao.AbstractDAO;
import dao.ProductDAO;
import model.Product;


public class ProductBLL {

	private List<Validator<Product>> validators;
	private ProductDAO ProductDAO;

	public ProductBLL() {
		validators = new ArrayList<Validator<Product>>();
		ProductDAO = new ProductDAO();
	}

	/**
	 * Cauta in tabel Product din baza de date elementul care are ID-ul = id.
	 * @param id Valoarea id-ul de cautat in tabel.
	 * @return Produsul cu ID_ul = id.
	 */
	public Product findProductById(int id) {
		Product st = ProductDAO.findById(id);
		if (st == null) {
			throw new NoSuchElementException("The Product with id =" + id + " was not found!");
		}
		return st;
	}
	
	/**
	 * Sterge din baza de date produsul cu ID = id.
	 * @param id ID-ul elementului de sters.
	 */
	public void deleteProduct(int id) {
		ProductDAO.delete(id);

	}

	/**
	 * Insereaza in baza de date un nou Produs.
	 * @param product Produsul de inserat.
	 * @return Numarul de randuri inserate.
	 */
	public int insertProduct(Product product) {
		return ProductDAO.insert(product);

	}
	
	/**
	 * Actualizeaza in baza de date un produs.
	 * @param product Produsul de actualizat.
	 * @return Numarul de randuri actualizate
	 */
	public int updateProduct(Product product) {
		return ProductDAO.update(product, product.getId());

	}
	
	/**
	 * Converteste randurile din tabelul Product in obiecte Product.
	 * @return ArrayList de Product care reprezinta inerogarile din baza da date.
	 */
	public ArrayList<Product> getProducts (){
		return ProductDAO.findAll();
	}

}
