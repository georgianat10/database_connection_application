package model;

public class Product {

	private int id;
	private String name;
	private String producer;
	private double price;
	private String observation;
	private int stock;
	
	public Product() { }
	
	public Product(int id, String name, String producer, double price, String observation, int stock) {
		this.id = id;
		this.name = name;
		this.producer = producer;
		this.price = price;
		this.observation = observation;
		this.stock = stock;
	}
	
	
	public Product(String name, String producer, double price, String observation, int stock) {
		this.name = name;
		this.producer = producer;
		this.price = price;
		this.observation = observation;
		this.stock = stock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String toString() {
		return "Product [id=" + id + ", name=" + name +  ", price=" + price +  ", stock= " + stock+"]";
	}


}
