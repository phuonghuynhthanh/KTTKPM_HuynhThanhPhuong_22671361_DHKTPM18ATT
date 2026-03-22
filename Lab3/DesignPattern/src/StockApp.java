import java.util.ArrayList;
import java.util.List;

public class StockApp {
    public static void main(String[] args) {

        Stock stock = new Stock("AAPL", 150);

        Observer investor1 = new Investor("Alice");
        Observer investor2 = new Investor("Bob");

        stock.attach(investor1);
        stock.attach(investor2);

        stock.setPrice(155);
        stock.setPrice(160);

        stock.detach(investor1);

        stock.setPrice(170);
    }
}

//////////////////////////////////////////////////////////

interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}

//////////////////////////////////////////////////////////

interface Observer {
    void update(String stockSymbol, double price);
}

//////////////////////////////////////////////////////////

class Stock implements Subject {
    private String symbol;
    private double price;
    private List<Observer> observers = new ArrayList<>();

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public void setPrice(double price) {
        this.price = price;
        notifyObservers();
    }

    public double getPrice() {
        return price;
    }

    public void attach(Observer o) {
        observers.add(o);
    }

    public void detach(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(symbol, price);
        }
    }
}

//////////////////////////////////////////////////////////

class Investor implements Observer {
    private String name;

    public Investor(String name) {
        this.name = name;
    }

    public void update(String stockSymbol, double price) {
        System.out.println(name + " notified: " + stockSymbol + " price = $" + price);
    }
}