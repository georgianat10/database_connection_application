package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;
import model.Product;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Since: Apr 03, 2017
 * @Source http://www.java-blog.com/mapping-javaobjects-database-reflection-generics
 */
public class AbstractDAO<T> {
	protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

	private final Class<T> type;

	@SuppressWarnings("unchecked")
	public AbstractDAO() {
		this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	}
	
	/**
	 * @param field Reprezinta campul dupa care se face cautarea.
	 * @return Query-ul pentru selectarea randurilor care au campul field = ?
	 */
	private String createSelectByIdQuery(String field) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(" * ");
		sb.append(" FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE " + field + " =?");
		return sb.toString();
	}

	/**
	 * 
	 * @return Query care returneaza toate liniile si toate coloanele dintr-un tabel. 
	 */
	private String createSelectQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append(" * ");
		sb.append(" FROM ");
		sb.append(type.getSimpleName());
		return sb.toString();
	}

	public String createInsertQuery(T obj) {

		StringBuilder fields = new StringBuilder();
		StringBuilder vars = new StringBuilder();

		try {

			for (Field field : type.getDeclaredFields()) {
				String name = field.getName();
				if (!name.equals("id")) {
					if (fields.length() > 1) {
						fields.append(", ");
						vars.append(", ");
					}
					fields.append(name);

					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getReadMethod();
					Object s = method.invoke(obj);
					vars.append("'" + s + "'");
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		String Sql = "INSERT INTO " + type.getSimpleName() + " (" + fields.toString() + ") VALUES (" + vars.toString()
				+ ");";
		System.out.println(Sql);
		return Sql;
	}
	/**
	 * 
	 * @param obj Contine datele care se vor inlocui la linia din tabel.
	 * @param confField Reprezinta id elementului care se va actualiza.
	 * @return Query-ul de actualizare a elementului cu id-ul egal cu confField. 
	 */

	public String createUpdateQuery(T obj, String confField) {

		StringBuilder set = new StringBuilder();

		try {

			for (Field field : type.getDeclaredFields()) {
				String name = field.getName();
				if (!name.equals("id")) {
					if (set.length() > 1) {
						set.append(", ");
					}
					set.append(field.getName());
					set.append(" = ");
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getReadMethod();
					Object s = method.invoke(obj);
					set.append("'" + s + "'");

				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		
		String Sql = "UPDATE " + type.getSimpleName() + " SET " + set.toString() + " WHERE " + confField + " = ?;";
		return Sql;
	}
	
	/**
	 * 
	 * @param field Id-ul elementului de sters.
	 * @return Query-ul de stergere a elementului cu id-ul egal cu fiel.
	 */
	private String createDeleteQuery(String field) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE ");
		sb.append(" FROM ");
		sb.append(type.getSimpleName());
		sb.append(" WHERE " + field + " =?");
		return sb.toString();
	}
	
	/**
	 * 
	 * @return Un ArrayList de Obiecte care reprezinta toate elementele dint-un tabel. Fiecare obiect reprezinta un rand din tabel.
	 */

	public ArrayList<T> findAll() {

		ArrayList<T> list = new ArrayList<T>();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectQuery();

		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				T instance = type.newInstance();
				for (Field field : type.getDeclaredFields()) {
					Object value = resultSet.getObject(  field.getName() );
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getWriteMethod();
		
					method.invoke(instance, value);
				}
				list.add(instance);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}

		return null;
	}
	
	/**
	 * Metoda returneaza un obiect de tipul T care corespunde inregistrarii  cu id-ul egal cu id din tabel.
	 * @param id Reprezinta id-ul inregistrarii de cautat. 
	 * @return Obiecul corespunzator inregistrarii rezultate dupa executia query-ului.
	 */

	public T findById(int id) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String query = createSelectByIdQuery(type.getDeclaredFields()[0].getName());
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();

			return createObjects(resultSet).get(0);
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
		} finally {
			ConnectionFactory.close(resultSet);
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return null;
	}
	/**
	 * Face conversia de la rezultatul unei interogari la obiectele din Java corespunzatoare tabelelor din baza de date.
	 * @param resultSet Rezultatul query-ului.
	 * @return Converia rezultatului in obiecte din java.
	 */

	private ArrayList<T> createObjects(ResultSet resultSet) {

		ArrayList<T> list = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				T instance = type.newInstance();
				for (Field field : type.getDeclaredFields()) {
					Object value = resultSet.getObject(field.getName());
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
					Method method = propertyDescriptor.getWriteMethod();
					method.invoke(instance, value);
				}
				list.add(instance);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * Insereaza elementul T in tabelul corespunzator.
	 * @param t Contine date ce trebuie introduse in tabel.
	 * @return Numarul de randuri inserate in tabel.
	 */

	public int insert(T t) {
		Connection connection = null;
		PreparedStatement statement = null;
		int resultSet = 0;
		String query = createInsertQuery(t);
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			resultSet = statement.executeUpdate();

			return resultSet;

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}

		return 0;
	}
	
	/**
	 * Actualizeaza elementul care are ID=ul = id, cu datele stocate in obiectul T. 
	 * @param t Datele cu care se va actualiza inregistrarea.
	 * @param id ID-ul inegistrarii de actualizat.
	 * @return Numarul de inregistrati actualizate.
	 */

	public int update(T t, int id) {
		Connection connection = null;
		PreparedStatement statement = null;
		int resultSet = 0;
		String query = createUpdateQuery(t, type.getDeclaredFields()[0].getName());
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			resultSet = statement.executeUpdate();

			return resultSet;

		} catch (SecurityException | IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}
		return 0;
	}
	
	/**
	 * Sterge elementul cu ID egal cu id din baza de date.
	 * @param id ID-ul elementului de sters.
	 * @return Numarul de inregistrari sterse.
	 */
	
	public int delete (int id) {
		
		Connection connection = null;
		PreparedStatement statement = null;
		int resultSet = 0;
		String query = createDeleteQuery(type.getDeclaredFields()[0].getName());
		try {
			connection = ConnectionFactory.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			resultSet = statement.executeUpdate();

			return resultSet;

		} catch (SecurityException | IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionFactory.close(statement);
			ConnectionFactory.close(connection);
		}

		return 0;
		
	}
}
