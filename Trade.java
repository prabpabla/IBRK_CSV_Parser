

public class Trade {

	private String symbol;
	private Order[] order;
    private float quantity; 
    public Trade(Order[] order) {
    	this.order = order;
    }
    
    public String getSymbol() {
    	return this.symbol;
    }

	public Order[] getOrder() {
		return order;
	}

	public void setOrder(Order[] order) {
		this.order = order;
	}
	
    public boolean isOpen(Order[] order) {
    	
    	for (Order eachOrder : order) {
    		symbol = eachOrder.getSymbol();
    		quantity = 0;   		
    		for (Order symbolInOrder : order) {
    			if (symbolInOrder.getSymbol().equals(symbol)) {
    				quantity += symbolInOrder.getQuantity();
    			}
    		}
    		if (quantity != 0) {
    			return true;
    		}
    	}
    	return false;
    }
}
