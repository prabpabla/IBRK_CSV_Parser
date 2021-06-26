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
		
		//debugOrderMap();
		//debugOrderMap2();
		System.out.println(totalTrades());
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
		int totalQuantity = 0;
		String symbol = "Symbol";
		String position = "Long/Short";
		String timeOpen = "00:00";
		String timeClose = "99:99";
		String timeHold = "00:00";
		int shares = 1234;
		float priceOpen = 0;
		float priceClose = 10000;
		float commissions = 0;
		float capital = 0;
		float partials = 0;
		float results = 0;
		String scalps = "N";
		
		File file = new File(savePath);
		FileWriter output = new FileWriter(file);
        CSVWriter write = new CSVWriter(output);
        String[] header = {"Symbol", "Long/Short", "T. Price Open", "T. Price Close", "Shares", "Time Open", "Time Close", "Time Hold", "Capital", "Results", "Partials", "Scalp"};
        write.writeNext(header, false);
        for(HashMap.Entry<String, List<Order>> entry: symbolToOrderList.entrySet()) {
			for(Order orderEntry: entry.getValue()) {
				if(orderEntry.getQuantity()!=0) {
					totalQuantity += orderEntry.getQuantity();
					if(totalQuantity==0) {
						
						symbol = orderEntry.getSymbol();
						position = totalQuantity > 0 ? "Long" : "Short";
						timeOpen = orderEntry.getTime();
						shares = orderEntry.getQuantity();
						priceOpen = orderEntry.getPrice();
						commissions = orderEntry.getCommissions();
						capital = shares*priceOpen;
						partials++;
						scalps = partials > 1? "Y" : "N";
					}
					else {
						symbol = orderEntry.getSymbol();
						timeClose = orderEntry.getTime();
						//timeHold = String.valueOf(Float.parseFloat(timeOpen)-Float.parseFloat(timeClose));
						priceClose = orderEntry.getPrice();
						commissions += orderEntry.getCommissions();
						results = orderEntry.getRealizedPNL();
						String[] trade = {symbol, position, String.valueOf(priceOpen), String.valueOf(priceClose), String.valueOf(shares), 
											timeOpen, timeClose, timeHold, String.valueOf(capital), String.valueOf(results), String.valueOf(partials), scalps};
						write.writeNext(trade, false);
						commissions = 0;
						partials = 0;
					}
				}
			}
		}
        write.close();
	}
	
	
	public int totalTrades() {
		int tradeCount = 0;
		int totalQuantity = 0;
		for(HashMap.Entry<String, List<Order>> entry: symbolToOrderList.entrySet()) {
			for(Order orderEntry: entry.getValue()) {
				if(orderEntry.getQuantity()!=0) {
					totalQuantity += orderEntry.getQuantity();
					if (totalQuantity == 0)
						tradeCount++;
				}
			}
		}
		return tradeCount;
	}
	
	public boolean tradeOpen() {
		boolean tradeOpen = false;
		int totalQuantity = 0;
		for(HashMap.Entry<String, List<Order>> entry: symbolToOrderList.entrySet()) {
			for(Order orderEntry: entry.getValue()) {
				if(orderEntry.getQuantity()!=0) {
					totalQuantity += orderEntry.getQuantity();
					if (totalQuantity != 0)
						tradeOpen = true;
					else
						tradeOpen =false;
				}
			}
		}
		return tradeOpen;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		new TradeLog_Parser("C:\\Users\\prab_\\Downloads\\DU3292618_20210616.csv");
		
	}
	
}
