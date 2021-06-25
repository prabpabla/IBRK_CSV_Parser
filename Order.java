
public class Order {

	private String symbol;
	private String time;
    private int quantity;
    private float price;
    private float realizedPNL;
    private float commissions;
    
    public Order(String symbol, String time, int quantity, float price, float realizedPNL, float commissions) {
    	this.symbol = symbol;
        this.time = time;
        this.quantity = quantity;
        this.price = price;
        this.realizedPNL = realizedPNL;
        this.commissions = commissions;
    }
    
    public String getSymbol() {
    	return this.symbol;
    }
    
    public String getTime() {
    	return this.time;
    }
    
    public int getQuantityl() {
    	return this.quantity;
    }
    
    public float getPrice() {
    	return this.price;
    }
    
    public float getRealizedPNL() {
    	return this.realizedPNL;
    }
    
    public float getCommissions() {
    	return this.commissions;
    }
    
    public String toString() {
    	return symbol+" "+time+" "+quantity+" "+price+" "+realizedPNL+" "+commissions;
    }
}
