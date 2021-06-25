import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import org.apache.commons.csv.*;



public class TradeLog_Parser {


	//Creating a HashMap of Sting to List of Type Order
	HashMap<String, List<Order>> symbolToOrderList; //null
	
	public TradeLog_Parser(String FilePath) throws IOException{
		File csvDataFile = new File(FilePath);
		CSVFormat format = CSVFormat.newFormat(',').withHeader();
		CSVParser parser = CSVParser.parse(csvDataFile, Charset.defaultCharset(), format);

		System.out.println(parser.getHeaderNames());
		
		this.symbolToOrderList = new HashMap<>(); //{}
		
		for (CSVRecord csvRecord : parser) {
			if(csvRecord.get("DataDiscriminator").equals("Order")) {
				
				String symbol = csvRecord.get("Symbol");
				String time = csvRecord.get("Date/Time");
				int quantity = Integer.parseInt(csvRecord.get("Quantity"));
				float price = Float.parseFloat(csvRecord.get("T. Price"));
				float realizedPNL = Float.parseFloat(csvRecord.get("Realized P/L"));
				float commissions = Float.parseFloat(csvRecord.get("Comm/Fee"));
				
				Order order = new Order(symbol, time, quantity, price, realizedPNL, commissions);

				//Check is symbol exits in the map, Initialization if statement
				if(!symbolToOrderList.containsKey(symbol)) { //symbol not in symbolToOrderList
					List<Order> newList = new ArrayList<>(); //[]
					symbolToOrderList.put(symbol, newList); //symbolToOrderList[symbol] = []
				}
				List<Order> list = symbolToOrderList.get(symbol); //list = symbolToOrderList[symbol] 
				list.add(order); //list.append(order)
					
			} 
			
		}
		
		debugOrderMap();
		
		/*symbolToOrderList = {AAPL: [{symbol:AAPL, price:1, quantity:100},{symbol:AAPL, price:1, quantity:200}], 
								CCL: [{symbol:CCL, price:1, quantity:100}]}
		*/
	
		
		
	}
	
	private void debugOrderMap() {
		symbolToOrderList.forEach((symbol, listOfOrders) -> {
            System.out.println("Symbol:"+symbol);
            listOfOrders.forEach((order)-> {
            	System.out.println("Order:"+order);
            });
        });
	}
	

	public static void main(String[] args) throws IOException {
		new TradeLog_Parser("C:\\Users\\prab_\\Downloads\\DU3292618_20210616.csv");
	}
	
	
}
