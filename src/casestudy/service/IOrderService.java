package casestudy.service;

import java.util.List;
import java.util.Map;

import casestudy.exceptions.InvalidCategryException;
import casestudy.model.Order;

public interface IOrderService {
	
	public List<Order> getAllOrdersByCategory(String category)throws InvalidCategryException;
	public int getTotalOrderCost(String category)throws InvalidCategryException;
	public List<Order> getAllCanceledOrder();
	public Map<String, List<Order>> createOrderMapByUser(); // key is username , value is List of orders
	public List<Order> filterOrders(String action); 
	public List<Order> getOrdersBasedOnOrderValue(); // sort the orders
	public boolean generatePDFReports(); // method will count the number of orders on cash payment , online payment and remains unpaid, delivered or cancelled

	
}
