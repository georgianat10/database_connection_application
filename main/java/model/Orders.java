package model;


public class Orders {
	
	private int order_id;
	private int product_id;
	private int customer_id;
	private int quantity;
	
	public Orders() {
		super();
	}
	
	public Orders( int product_id, int customer_id, int quantity) {
		super();
		this.product_id = product_id;
		this.customer_id = customer_id;
		this.quantity = quantity;
	}  
	

	public Orders(int order_id, int product_id, int customer_id, int quantity) {
		super();
		this.order_id = order_id;
		this.product_id = product_id;
		this.customer_id = customer_id;
		this.quantity = quantity;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quatity) {
		this.quantity = quatity;
	}
	
	
}
