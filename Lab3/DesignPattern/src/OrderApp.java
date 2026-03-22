public class OrderApp {
    public static void main(String[] args) {
        OrderComponent base = new BaseOrder();
        OrderComponent giftWrapped = new GiftWrapDecorator(base);

        ShippingStrategy shipping = new ExpressShipping();

        Order order = new Order(giftWrapped, shipping);

        System.out.println("=== CREATE ORDER ===");
        order.printInvoice();

        System.out.println("\n=== PROCESS ORDER ===");
        order.process();

        System.out.println("\n=== CANCEL ORDER ===");
        order.cancel();
    }
}

interface OrderState {
    void processOrder(Order order);
    void cancelOrder(Order order);
}

class NewState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Order is now PROCESSING...");
        order.setState(new ProcessingState());
    }

    @Override
    public void cancelOrder(Order order) {
        System.out.println("Order canceled from NEW state.");
    }
}

class ProcessingState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Order DELIVERED!");
        order.setState(new DeliveredState());
    }

    @Override
    public void cancelOrder(Order order) {
        System.out.println("Order canceled during PROCESSING.");
    }
}

class DeliveredState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Order already delivered. No action.");
    }

    @Override
    public void cancelOrder(Order order) {
        System.out.println("Cannot cancel. Order already delivered.");
    }
}

interface ShippingStrategy {
    void ship();
}

class StandardShipping implements ShippingStrategy {
    @Override
    public void ship() {
        System.out.println("Shipping via STANDARD delivery (3-5 days)");
    }
}

class ExpressShipping implements ShippingStrategy {
    @Override
    public void ship() {
        System.out.println("Shipping via EXPRESS delivery (1-2 days)");
    }
}

interface OrderComponent {
    double getCost();
    String getDescription();
}

class BaseOrder implements OrderComponent {
    @Override
    public double getCost() {
        return 100.0;
    }

    @Override
    public String getDescription() {
        return "Base Order";
    }
}

abstract class OrderDecorator implements OrderComponent {
    protected OrderComponent wrappedOrder;

    public OrderDecorator(OrderComponent order) {
        this.wrappedOrder = order;
    }
}

class GiftWrapDecorator extends OrderDecorator {
    public GiftWrapDecorator(OrderComponent order) {
        super(order);
    }

    @Override
    public double getCost() {
        return wrappedOrder.getCost() + 10.0;
    }

    @Override
    public String getDescription() {
        return wrappedOrder.getDescription() + " + Gift Wrap";
    }
}

class Order {
    private OrderState state;
    private ShippingStrategy strategy;
    private OrderComponent orderDetails;

    public Order(OrderComponent orderDetails, ShippingStrategy strategy) {
        this.state = new NewState();
        this.orderDetails = orderDetails;
        this.strategy = strategy;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void process() {
        state.processOrder(this);
        strategy.ship();
    }

    public void cancel() {
        state.cancelOrder(this);
    }

    public void printInvoice() {
        System.out.println("Order: " + orderDetails.getDescription());
        System.out.println("Cost: $" + orderDetails.getCost());
    }
}