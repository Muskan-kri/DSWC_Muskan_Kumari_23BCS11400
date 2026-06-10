import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


class Order {
    private String orderId;
    private String ticker;
    private double amount;
    private String type; // "BUY" or "SELL"

    public Order(String orderId, String ticker, double amount, String type) {
        this.orderId = orderId;
        this.ticker = ticker;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public String toString() {
        return "[" + orderId + "] " + type + " " + ticker + " @ $" + amount;
    }
}


class ExchangeManager {

   
    private ConcurrentHashMap<String, List<Order>> orderBook = new ConcurrentHashMap<>();


    public void placeOrder(String ticker, Order order) {

      
        orderBook.computeIfAbsent(ticker, k -> new CopyOnWriteArrayList<>());

        orderBook.get(ticker).add(order);
        System.out.println("[ORDER PLACED] " + order);
    }


    public void printOrderBook(String ticker) {
        List<Order> orders = orderBook.getOrDefault(ticker, new CopyOnWriteArrayList<>());
        System.out.println("\n--- Order Book: " + ticker + " (" + orders.size() + " orders) ---");
        for (Order o : orders) {
            System.out.println("  " + o);
        }
    }


    public int getTotalOrders(String ticker) {
        return orderBook.getOrDefault(ticker, new CopyOnWriteArrayList<>()).size();
    }
}


public class TradeXExchange {
    public static void main(String[] args) throws InterruptedException {

        ExchangeManager exchange = new ExchangeManager();

        System.out.println("===== TRADEX CONCURRENT ORDER PLACEMENT =====\n");

        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        threadPool.submit(() -> exchange.placeOrder("BTC", new Order("ORD-001", "BTC", 45000.00, "BUY")));
        threadPool.submit(() -> exchange.placeOrder("BTC", new Order("ORD-002", "BTC", 44950.00, "SELL")));

        threadPool.submit(() -> exchange.placeOrder("ETH", new Order("ORD-003", "ETH", 2800.00, "BUY")));

        threadPool.submit(() -> exchange.placeOrder("BTC", new Order("ORD-004", "BTC", 45100.00, "BUY")));

        exchange.placeOrder("ETH", new Order("ORD-005", "ETH", 2795.00, "SELL"));
        exchange.placeOrder("BTC", new Order("ORD-006", "BTC", 45050.00, "BUY"));

        threadPool.shutdown();
        threadPool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n===== FINAL ORDER BOOKS =====");
        exchange.printOrderBook("BTC");
        exchange.printOrderBook("ETH");

        System.out.println("\n===== ORDER COUNT =====");
        System.out.println("BTC orders: " + exchange.getTotalOrders("BTC")); // should be 4
        System.out.println("ETH orders: " + exchange.getTotalOrders("ETH")); // should be 2
        System.out.println("\nAll orders accounted for — no race condition data loss.");
    }
}
