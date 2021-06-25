import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

import javax.lang.model.element.Element;

import org.apache.commons.csv.*;
import com.opencsv.CSVWriter;

public class TradeLog_Parser {

	//Creating a HashMap of Sting to List of Type Order
	HashMap<String, List<Order>> symbolToOrderList; //null
	
	public TradeLog_Parser(String filePath) throws IOException{
		File csvDataFile = new File(filePath);
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
					List<Order> emptyList = new ArrayList<>(); //[]
					symbolToOrderList.put(symbol, emptyList); //symbolToOrderList[symbol] = []
				}
				List<Order> orderList = symbolToOrderList.get(symbol); //list = symbolToOrderList[symbol] 
				orderList.add(order); //list.append(order)		
			} 
		}
		
		debugOrderMap();
		debugOrderMap2();
		formatOrderToCSV("C:\\Users\\prab_\\Downloads\\Output_20210616.csv");
		/*symbolToOrderList = {AAPL: [{symbol:AAPL, price:1, quantity:100},{symbol:AAPL, price:1, quantity:200}], 
								CCL: [{symbol:CCL, price:1, quantity:100}]}
		*/
	}
	
	private void debugOrderMap() {
		symbolToOrderList.forEach((symbol, listOfOrders) -> {
            System.out.println("Symbol:"+symbol);
            listOfOrders.forEach((order)-> {
            	System.out.println("Orders:"+order);
            });
        });
	}
	
	private void debugOrderMap2() {
		for(HashMap.Entry<String, List<Order>> entry: symbolToOrderList.entrySet()) {
			System.out.println("Symbol:"+ entry.getKey());
			for(Order orderEntry: entry.getValue()) {
				System.out.println("Orders:"+ orderEntry);
			}
		}
	}
	
	public void formatOrderToCSV(String savePath) throws IOException {
		//Write out the header below
		//Symbol, Long/Short, T. Price Open, T. Price Close, Shares, Time Open, Time Close, Time Hold, Capital, Results, Partials, Scalp
		//For each symbol, take into account of number of shares open
		//If shares != 0, take that Open T. Price, and Time Open
		//If total shares != 0, update Open T. Price, and Close T. Price
		//If total shares == 0, update Close T. Price, and Time Close, 
		File file = new File(savePath);
		FileWriter output = new FileWriter(file);
        CSVWriter write = new CSVWriter(output);
        String[] header = {"Symbol", "Long/Short", "T. Price Open", "T. Price Close", "Shares", "Time Open", "Time Close", "Time Hold", "Capital", "Results", "Partials", "Scalp"};
        write.writeNext(header, false);
        write.close();
	}
	
	/*public int totalTrades() {
		int tradeCount = 0;
		for(HashMap.Entry<String, List<Order>> entry: symbolToOrderList.entrySet()) {
			
		}
	}*/
	
	
	public static void main(String[] args) throws IOException {
		new TradeLog_Parser("C:\\Users\\prab_\\Downloads\\DU3292618_20210616.csv");
		
	}
	
}
