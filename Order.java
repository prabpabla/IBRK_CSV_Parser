
import java.sql.Time;
import java.util.Date;
public class Order {
	
	private String header;
	private String dataDiscriminator;
	private String symbol;
	private Time time;
    private int quantity;
    private float tPrice;
    private float proceeds;
    private float realizedPNL;
    private float commissions;
    
    
    public Order(String header, String dataDiscriminator, String symbol, Time time, int quantity, float tPrice, float proceeds, float realizedPNL, float commissions) {
    	this.header = header;
    	this.dataDiscriminator = dataDiscriminator;
    	this.symbol = symbol;
        this.time = time;
        this.quantity = quantity;
        this.tPrice = tPrice;
        this.proceeds = proceeds;
        this.realizedPNL = realizedPNL;
        this.commissions = commissions;
    }
    
    public String getHeader() {
    	return this.header;
    }
    
    public String getDataDiscriminator() {
    	return this.dataDiscriminator;
    }
    
    public String getSymbol() {
    	return this.symbol;
    }
    
    public Time getTime() {
    	return this.time;
    }
    
    public int getQuantity() {
    	return this.quantity;
    }
    
    public float getPrice() {
    	return this.tPrice;
    }
    
    public float getProceeds() {
    	return this.proceeds;
    }
    
    public float getRealizedPNL() {
    	return this.realizedPNL;
    }
    
    public float getCommissions() {
    	return this.commissions;
    }
    
    public String toString() {
    	return header+" "+dataDiscriminator+" "+symbol+" "+time+" "+quantity+" "+tPrice+" "+proceeds+" "+realizedPNL+" "+commissions;
    }
}
