public class InvoiceApp {
    public static void main(String[] args) {

        Product product = new BaseProduct("Laptop", 1000);

        product = new VATTax(product);
        product = new LuxuryTax(product);

        TaxCalculationStrategy strategy = new StandardTaxStrategy();

        Invoice invoice = new Invoice(product, strategy);

        System.out.println("=== ADD TAX ===");
        invoice.addTax("VAT");

        System.out.println("\n=== FINALIZE ===");
        invoice.finalizeInvoice();

        System.out.println("\n=== PAY ===");
        invoice.pay();

        System.out.println("\n=== PRINT TOTAL ===");
        invoice.printTotal();
    }
}

interface InvoiceState {
    void addTax(Invoice invoice, String taxType);
    void finalizeInvoice(Invoice invoice);
    void payInvoice(Invoice invoice);
}

class DraftState implements InvoiceState {
    @Override
    public void addTax(Invoice invoice, String taxType) {
        System.out.println("Adding tax: " + taxType);
    }

    @Override
    public void finalizeInvoice(Invoice invoice) {
        System.out.println("Invoice finalized.");
        invoice.setState(new FinalizedState());
    }

    @Override
    public void payInvoice(Invoice invoice) {
        System.out.println("Cannot pay. Invoice is still DRAFT.");
    }
}

class FinalizedState implements InvoiceState {
    @Override
    public void addTax(Invoice invoice, String taxType) {
        System.out.println("Cannot add tax. Already finalized.");
    }

    @Override
    public void finalizeInvoice(Invoice invoice) {
        System.out.println("Already finalized.");
    }

    @Override
    public void payInvoice(Invoice invoice) {
        System.out.println("Invoice paid successfully.");
        invoice.setState(new PaidState());
    }
}

class PaidState implements InvoiceState {
    @Override
    public void addTax(Invoice invoice, String taxType) {
        System.out.println("Cannot modify. Invoice already PAID.");
    }

    @Override
    public void finalizeInvoice(Invoice invoice) {
        System.out.println("Already paid.");
    }

    @Override
    public void payInvoice(Invoice invoice) {
        System.out.println("Already paid.");
    }
}

interface TaxCalculationStrategy {
    double calculateFinalPrice(Product product);
}

class StandardTaxStrategy implements TaxCalculationStrategy {
    @Override
    public double calculateFinalPrice(Product product) {
        return product.getPrice();
    }
}

class TaxExemptStrategy implements TaxCalculationStrategy {
    @Override
    public double calculateFinalPrice(Product product) {
        return 0; // miễn thuế
    }
}

interface Product {
    double getPrice();
    String getDescription();
}

class BaseProduct implements Product {
    private String name;
    private double price;

    public BaseProduct(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return name;
    }
}

abstract class TaxDecorator implements Product {
    protected Product product;

    public TaxDecorator(Product product) {
        this.product = product;
    }
}

class VATTax extends TaxDecorator {
    public VATTax(Product product) {
        super(product);
    }

    @Override
    public double getPrice() {
        return product.getPrice() * 1.1; // +10%
    }

    @Override
    public String getDescription() {
        return product.getDescription() + " + VAT";
    }
}

class ConsumptionTax extends TaxDecorator {
    public ConsumptionTax(Product product) {
        super(product);
    }

    @Override
    public double getPrice() {
        return product.getPrice() * 1.05; // +5%
    }

    @Override
    public String getDescription() {
        return product.getDescription() + " + ConsumptionTax";
    }
}

class LuxuryTax extends TaxDecorator {
    public LuxuryTax(Product product) {
        super(product);
    }

    @Override
    public double getPrice() {
        return product.getPrice() * 1.2; // +20%
    }

    @Override
    public String getDescription() {
        return product.getDescription() + " + LuxuryTax";
    }
}

class Invoice {
    private InvoiceState state;
    private TaxCalculationStrategy taxStrategy;
    private Product product;
    private double basePrice;

    public Invoice(Product product, TaxCalculationStrategy strategy) {
        this.state = new DraftState();
        this.product = product;
        this.taxStrategy = strategy;
        this.basePrice = product.getPrice();
    }

    public void setState(InvoiceState state) {
        this.state = state;
    }

    public void addTax(String taxType) {
        state.addTax(this, taxType);
    }

    public void finalizeInvoice() {
        state.finalizeInvoice(this);
    }

    public void pay() {
        state.payInvoice(this);
    }

    public void printTotal() {
        double finalPrice = taxStrategy.calculateFinalPrice(product);
        System.out.println("Product: " + product.getDescription());
        System.out.println("Total: $" + finalPrice);
    }
}