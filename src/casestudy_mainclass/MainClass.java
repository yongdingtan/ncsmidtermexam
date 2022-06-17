package casestudy_mainclass;

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

import casestudy.exceptions.InvalidCategryException;
import casestudy.model.Order;
import casestudy.service.IOrderService;

public class MainClass implements IOrderService{
	
	Scanner sc = new Scanner(System.in);
	
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
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\Order.csv"));
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
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\Order.csv"));
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
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\Order.csv"));
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
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\Order.csv"));
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

					if(!ordersByUsers.containsKey(username))
					{
						List<Order> ordersByOneUser = new ArrayList<Order>();
						Order order = new Order(username, categoryData, orderCost, dateOfOrder, action);
						ordersByOneUser.add(order);
						ordersByUsers.put(username, ordersByOneUser);
					}
					else
					{
						List<Order> previousOrders = ordersByUsers.get(username);
						Order order = new Order(username, categoryData, orderCost, dateOfOrder, action);
						previousOrders.add(order);
						ordersByUsers.put(username, previousOrders);
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
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\Order.csv"));
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
			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\Order.csv"));
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
        PDPageContentStream content;
		String line;
			try{
				content = new PDPageContentStream(document, page);
                content.setFont(PDType1Font.TIMES_ROMAN, 12);
				BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\Order.csv"));
				int countOne = 0;
				String[] newString = new String[25];
			    while((line = br.readLine())!=null)
				{
					String data[] = line.split(",");
					String compilation = data[0]+" "+data[1]+" "+data[2]+" "+data[3]+" "+data[4];
					
					newString[countOne++] = compilation;
					
				}
		        int count=1;
		        for (String cell : newString){
		        	 	content.beginText();
		                content.newLineAtOffset(100, 800 - 20f * count);
		                content.showText(cell);
		                content.endText(); 
		                ++count;
		            }
			content.close();
			document.save(new File("C:\\Users\\yongd\\OneDrive\\Desktop\\Mid Term 17-Jun-22-20220617\\CoreJavaTest-main\\CaseStudy_CoreJava\\test.pdf"));
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		};
		
		return true;
		
	}

}
