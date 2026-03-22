public class PaymentApp {
    public static void main(String[] args) {

        PaymentAmount amount = new BaseAmount(100);
        amount = new ProcessingFeeDecorator(amount);
        amount = new DiscountDecorator(amount);

        PaymentStrategy strategy = new CreditCardStrategy();

        PaymentTransaction transaction = new PaymentTransaction(amount, strategy);

        transaction.printInvoice();

        transaction.processTransaction();
        transaction.refundTransaction();
    }
}


interface TransactionState {
    void process(PaymentTransaction transaction);
    void refund(PaymentTransaction transaction);
}

class PendingState implements TransactionState {
    public void process(PaymentTransaction transaction) {
        boolean success = transaction.getStrategy().pay(transaction.getPaymentDetails().getAmount());
        if (success) {
            System.out.println("Payment processing...");
            transaction.setState(new ProcessingStatePay());
        } else {
            System.out.println("Payment failed.");
            transaction.setState(new FailedState());
        }
    }

    public void refund(PaymentTransaction transaction) {
        System.out.println("Cannot refund. Payment not processed.");
    }
}

class ProcessingStatePay implements TransactionState {
    public void process(PaymentTransaction transaction) {
        System.out.println("Payment completed.");
        transaction.setState(new CompletedState());
    }

    public void refund(PaymentTransaction transaction) {
        System.out.println("Cannot refund while processing.");
    }
}

class CompletedState implements TransactionState {
    public void process(PaymentTransaction transaction) {
        System.out.println("Already completed.");
    }

    public void refund(PaymentTransaction transaction) {
        System.out.println("Refund successful.");
        transaction.setState(new FailedState());
    }
}

class FailedState implements TransactionState {
    public void process(PaymentTransaction transaction) {
        System.out.println("Retrying payment...");
        transaction.setState(new PendingState());
    }

    public void refund(PaymentTransaction transaction) {
        System.out.println("Nothing to refund.");
    }
}

//////////////////////////////////////////////////////////

interface PaymentStrategy {
    boolean pay(double amount);
}

class CreditCardStrategy implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card");
        return true;
    }
}

class PayPalStrategy implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal");
        return true;
    }
}

//////////////////////////////////////////////////////////

interface PaymentAmount {
    double getAmount();
    String getDescription();
}

class BaseAmount implements PaymentAmount {
    private double amount;

    public BaseAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return "Base Amount";
    }
}

abstract class PaymentDecorator implements PaymentAmount {
    protected PaymentAmount wrappedPayment;

    public PaymentDecorator(PaymentAmount payment) {
        this.wrappedPayment = payment;
    }
}

class ProcessingFeeDecorator extends PaymentDecorator {
    public ProcessingFeeDecorator(PaymentAmount payment) {
        super(payment);
    }

    public double getAmount() {
        return wrappedPayment.getAmount() + 5;
    }

    public String getDescription() {
        return wrappedPayment.getDescription() + " + Processing Fee";
    }
}

class DiscountDecorator extends PaymentDecorator {
    public DiscountDecorator(PaymentAmount payment) {
        super(payment);
    }

    public double getAmount() {
        return wrappedPayment.getAmount() - 10;
    }

    public String getDescription() {
        return wrappedPayment.getDescription() + " + Discount";
    }
}

//////////////////////////////////////////////////////////

class PaymentTransaction {
    private TransactionState state;
    private PaymentStrategy strategy;
    private PaymentAmount paymentDetails;

    public PaymentTransaction(PaymentAmount paymentDetails, PaymentStrategy strategy) {
        this.state = new PendingState();
        this.paymentDetails = paymentDetails;
        this.strategy = strategy;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public TransactionState getState() {
        return state;
    }

    public PaymentStrategy getStrategy() {
        return strategy;
    }

    public PaymentAmount getPaymentDetails() {
        return paymentDetails;
    }

    public void processTransaction() {
        state.process(this);
    }

    public void refundTransaction() {
        state.refund(this);
    }

    public void printInvoice() {
        System.out.println("Payment: " + paymentDetails.getDescription());
        System.out.println("Amount: $" + paymentDetails.getAmount());
    }
}