package casestudy.model;

import java.util.Date;

public class Order {
	
	private String username;
	private String category;
	private int orderCost;
	private Date dateOfOrder;
	private String action;
	
	public Order(String username, String category, int orderCost, Date dateOfOrder, String action) {
		super();
		this.username = username;
		this.category = category;
		this.orderCost = orderCost;
		this.dateOfOrder = dateOfOrder;
		this.action = action;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(int orderCost) {
		this.orderCost = orderCost;
	}

	public Date getDateOfOrder() {
		return dateOfOrder;
	}

	public void setDateOfOrder(Date dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public String toString()
	{
		return "Username: "+username+" Category: "+category+" Order Cost: "+orderCost+" Date of Order: "+dateOfOrder+" Action: "+action;
	}

}
