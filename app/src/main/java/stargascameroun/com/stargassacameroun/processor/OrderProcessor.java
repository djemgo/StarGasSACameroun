package stargascameroun.com.stargassacameroun.processor;

/**
 * Created by 623990 on 3/17/2015.
 */

import stargascameroun.com.stargassacameroun.model.CustomerOrder;

/**
 * Created by 623990 on 3/10/2015.
 */
public class OrderProcessor {
    private CustomerOrder customerOrder;

    public OrderProcessor(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public void processOrder(){

    }
}
