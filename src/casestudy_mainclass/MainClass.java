package casestudy_mainclass;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.datatable.DataTable;
import casestudy.exceptions.InvalidCategryException;
import casestudy.model.Order;
import casestudy.service.IOrderService;

public class MainClass implements IOrderService{
	
	Scanner sc = new Scanner(System.in);
	String path = "C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\order.csv";
	String output = "C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\test.pdf";
	
	public static void main(String[] args) throws IOException {
		
		MainClass obj = new MainClass();
		try (Scanner stringSc = new Scanner(System.in)) {
			while(true)
			{
				
				System.out.println("-----------------Menu------------------");
				System.out.println("1: Get all orders by category");
				System.out.println("2: Get total order cost");
				System.out.println("3: Get all cancelled orders");
				System.out.println("4: View orders based on category");
				System.out.println("5: Filter orders based on action");
				System.out.println("6: Sort orders based on order cost");
				System.out.println("7: Generate PDF report");
				System.out.println("0: Exit");
				
				System.out.print("Enter your choice: ");
				int option = obj.sc.nextInt();
				
				switch(option) {
				
				case 1: {
					try {
						System.out.print("Enter the category name: ");
						String choice = stringSc.nextLine();
						List<Order> orderCat = obj.getAllOrdersByCategory(choice);
						orderCat.forEach((order) -> {
							System.out.println(order.toString());
						});
					} catch (InvalidCategryException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				
				case 2: {
					try {
						System.out.print("Enter the category name: ");
						String choice = stringSc.nextLine();
						int sum = obj.getTotalOrderCost(choice);
						System.out.println("Total sum: "+sum);
					} catch (InvalidCategryException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				
				case 3: {
					List<Order> orderCancelled = obj.getAllCanceledOrder();
					orderCancelled.forEach((order) -> {
						System.out.println(order.toString());
					});
					break;
				}
				
				case 4: {
					Map<String, List<Order>> ordersByUsers = obj.createOrderMapByUser();
					ordersByUsers.forEach((k, v) -> 
					{
						System.out.println(k+": "+v);
					});
					break;
				}
				
				case 5: {
					System.out.print("Enter the action name (delivered/cancelled): ");
					String choice = stringSc.nextLine();
					List<Order> orderFilter = obj.filterOrders(choice);
					orderFilter.forEach((order) ->
					{
						System.out.println(order.toString());
					});
					break;
				}
				
				case 6: {
					List<Order> orderSum = obj.getOrdersBasedOnOrderValue();
					orderSum.forEach((order)->{
						System.out.println(order.toString());
					});
					break;
				}
				
				case 7: {
					obj.generatePDFReports();
					break;
				}
				
				case 0: {
					System.exit(0);
					break;
				}
				
				default:
					break;
				
				}
			}
		}
		
	}

	//method should return the list of orders based on category (Order.csv)
	@Override
	public List<Order> getAllOrdersByCategory(String category) throws InvalidCategryException {
		
		List<Order> allOrders = new ArrayList<Order>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			int firstLine = 0;

			try {
				while((line = br.readLine())!=null)
				{
					if(firstLine == 0)
					{
						firstLine++;
						continue;
					}
					String data[] = line.split(",");
					String username = data[0];
					String categoryData = data[1];
					int orderCost = Integer.parseInt(data[2]);
					Date dateOfOrder = new SimpleDateFormat("dd-MMM-yy").parse(data[3]);
					String action = data[4];

					if(categoryData.equals(category))
					{
						Order order = new Order(username, categoryData, orderCost, dateOfOrder, action);
						allOrders.add(order);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}catch(NumberFormatException e) {
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return allOrders;
	}
	
	//Method should return the total value of the order based on category and action should be only delivered  
	@Override
	public int getTotalOrderCost(String category) throws InvalidCategryException {
		int sum = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			int firstLine = 0;

			try {
				while((line = br.readLine())!=null)
				{
					if(firstLine == 0)
					{
						firstLine++;
						continue;
					}
					String data[] = line.split(",");
					String categoryData = data[1];
					int orderCost = Integer.parseInt(data[2]);

					if(categoryData.equals(category))
					{
						sum+=orderCost;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch(NumberFormatException e){
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return sum;
	}
	
	//method returns all the cancel orders.
	@Override
	public List<Order> getAllCanceledOrder() {
		List<Order> allOrders = new ArrayList<Order>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			int firstLine = 0;

			try {
				while((line = br.readLine())!=null)
				{
					if(firstLine == 0)
					{
						firstLine++;
						continue;
					}
					String data[] = line.split(",");
					String username = data[0];
					String categoryData = data[1];
					int orderCost = Integer.parseInt(data[2]);
					Date dateOfOrder = new SimpleDateFormat("dd-MMM-yy").parse(data[3]);
					String action = data[4];

					if(action.equalsIgnoreCase("cancelled"))
					{
						Order order = new Order(username, categoryData, orderCost, dateOfOrder, action);
						allOrders.add(order);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}catch(NumberFormatException e){
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return allOrders;
	}

	//Business stake holders are interested to view orders based on category , so store order in the form of Map , 
	//where key is username , value is List of orders
	@Override
	public Map<String, List<Order>> createOrderMapByUser() {
		
		Map<String, List<Order>> ordersByUsers = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			int firstLine = 0;

			try {
				while((line = br.readLine())!=null)
				{
					if(firstLine == 0)
					{
						firstLine++;
						continue;
					}
					String data[] = line.split(",");
					String username = data[0];
					String categoryData = data[1];
					int orderCost = Integer.parseInt(data[2]);
					Date dateOfOrder = new SimpleDateFormat("dd-MMM-yy").parse(data[3]);
					String action = data[4];

					if(!ordersByUsers.containsKey(categoryData))
					{
						List<Order> ordersByOneUser = new ArrayList<Order>();
						Order order = new Order(username, categoryData, orderCost, dateOfOrder, action);
						ordersByOneUser.add(order);
						ordersByUsers.put(categoryData, ordersByOneUser);
					}
					else
					{
						List<Order> previousOrders = ordersByUsers.get(categoryData);
						Order order = new Order(username, categoryData, orderCost, dateOfOrder, action);
						previousOrders.add(order);
						ordersByUsers.put(categoryData, previousOrders);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}catch(NumberFormatException e) {
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return ordersByUsers;
	}

	//Filter order based on action.
	@Override
	public List<Order> filterOrders(String action) {
		
		if(!action.equalsIgnoreCase("delivered")||!action.equalsIgnoreCase("cancelled"))
		{
			System.out.println("Please enter the correct name.");
		}
		
		List<Order> allOrders = new ArrayList<Order>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			int firstLine = 0;

			try {
				while((line = br.readLine())!=null)
				{
					if(firstLine == 0)
					{
						firstLine++;
						continue;
					}
					String data[] = line.split(",");
					String username = data[0];
					String categoryData = data[1];
					int orderCost = Integer.parseInt(data[2]);
					Date dateOfOrder = new SimpleDateFormat("dd-MMM-yy").parse(data[3]);
					String actionData = data[4];

					if(actionData.equalsIgnoreCase(action))
					{
						Order order = new Order(username, categoryData, orderCost, dateOfOrder, action);
						allOrders.add(order);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}catch(NumberFormatException e) {
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return allOrders;
	}

	//sort the orders based on order cost 
	@Override
	public List<Order> getOrdersBasedOnOrderValue() {
		List<Order> allOrders = new ArrayList<Order>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			int firstLine = 0;

			try {
				while((line = br.readLine())!=null)
				{
					if(firstLine == 0)
					{
						firstLine++;
						continue;
					}
					String data[] = line.split(",");
					String username = data[0];
					String categoryData = data[1];
					int orderCost = Integer.parseInt(data[2]);
					Date dateOfOrder = new SimpleDateFormat("dd-MMM-yy").parse(data[3]);
					String action = data[4];

					Order order = new Order(username, categoryData, orderCost, dateOfOrder, action);
					allOrders.add(order);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}catch(NumberFormatException e) {
			}
			
			Collections.sort(allOrders, (o1, o2) -> (o1.getOrderCost() -o2.getOrderCost()));
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return allOrders;
	}

	//method will count the number of orders based on cash payment , 
	//online payment and unpaid orders , delivered or cancelled
	@Override
	public boolean generatePDFReports() {
		
		PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);
		String line;
		
	try {
	BaseTable table = new BaseTable(800, 500, 50, 200, 50, document, page, true, true);
	//Create Header row
	Row<PDPage> headerRow = table.createRow(15f);
	Cell<PDPage> cell = headerRow.createCell(95, "PDF Report Sheet");
	cell.setFont(PDType1Font.HELVETICA_BOLD);
	table.addHeaderRow(headerRow);
	BufferedReader br = new BufferedReader(new FileReader(path));
	
	List<String[]> stringArray = new ArrayList<String[]>();
	int totalOrders = -1;
	int deliveredOrders = 0;
	int cancelledOrders = 0;
    while((line = br.readLine())!=null)
	{
    	String[] data = line.split(",");
    	for (String string : data) {
			if(string.equalsIgnoreCase("delivered"))
			{
				deliveredOrders++;
			}else if(string.equals("cancelled"))
			{
				cancelledOrders++;
			}
					
		}
		totalOrders++;
		
	}
    
    String[] string = new String[2];
    string[0] = "Total orders: ";
    string[1] = String.valueOf(totalOrders);
    
    String[] stringOne = new String[2];
    stringOne[0] = "Delivered Orders";
    stringOne[1] = String.valueOf(deliveredOrders);
    
    String[] stringTwo = new String[2];
    stringTwo[0] = "Cancelled Orders: ";
    stringTwo[1] = String.valueOf(cancelledOrders);

    stringArray.add(string);
    stringArray.add(stringOne);
    stringArray.add(stringTwo);
    
	for (String[] stringCheck : stringArray) {
				Row<PDPage> row = table.createRow(10f);
				cell = row.createCell((100 / 3.0f) * 2, stringCheck[0] );
				for (int i = 1; i < stringCheck.length; i++) {
				   cell = row.createCell((250 / 9f), stringCheck[i]);
				}
	}
	table.draw();
	document.save(new File(output));
	document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
	}

}
