import java.util.Arrays;
import java.util.List;

class Transaction {

    private String transactionId;
    private String status;
    private String category;
    private double amount;

    public Transaction(String transactionId, String status,
                       String category, double amount) {
        this.transactionId = transactionId;
        this.status = status;
        this.category = category;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "[" + transactionId + "] " +
               status + " | " +
               category + " | $" +
               amount;
    }
}

class SalesAnalyzer {

    public double calculateElectronicsRevenue(
            List<Transaction> transactions) {

        return transactions.stream()
                .filter(t -> "COMPLETED".equals(t.getStatus()))
                .filter(t -> "ELECTRONICS".equals(t.getCategory()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double calculateElectronicsRevenueParallel(
            List<Transaction> transactions) {

        return transactions.parallelStream()
                .filter(t -> "COMPLETED".equals(t.getStatus()))
                .filter(t -> "ELECTRONICS".equals(t.getCategory()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public void printRevenueByCategory(
            List<Transaction> transactions) {

        System.out.println(
                "\n--- Revenue by Category (COMPLETED only) ---");

        transactions.stream()
                .filter(t -> "COMPLETED".equals(t.getStatus()))
                .map(Transaction::getCategory)
                .distinct()
                .sorted()
                .forEach(category -> {

                    double total = transactions.stream()
                            .filter(t -> "COMPLETED".equals(t.getStatus()))
                            .filter(t -> category.equals(t.getCategory()))
                            .mapToDouble(Transaction::getAmount)
                            .sum();

                    System.out.printf(
                            "  %-15s $%.2f%n",
                            category,
                            total
                    );
                });
    }
}

public class EcommerceSalesReport {

    public static void main(String[] args) {

        List<Transaction> transactions = Arrays.asList(
                new Transaction("TXN-001", "COMPLETED",
                        "ELECTRONICS", 1200.00),

                new Transaction("TXN-002", "COMPLETED",
                        "CLOTHING", 350.00),

                new Transaction("TXN-003", "PENDING",
                        "ELECTRONICS", 800.00),

                new Transaction("TXN-004", "COMPLETED",
                        "ELECTRONICS", 2500.00),

                new Transaction("TXN-005", "REFUNDED",
                        "ELECTRONICS", 600.00),

                new Transaction("TXN-006", "COMPLETED",
                        "BOOKS", 45.00),

                new Transaction("TXN-007", "COMPLETED",
                        "ELECTRONICS", 3100.00),

                new Transaction("TXN-008", "COMPLETED",
                        "ELECTRONICS", 750.00),

                new Transaction("TXN-009", "PENDING",
                        "CLOTHING", 200.00),

                new Transaction("TXN-010", "COMPLETED",
                        "ELECTRONICS", 1850.00)
        );

        System.out.println(
                "===== ECOMMERCE SALES ANALYZER =====\n");

        System.out.println(
                "Total transactions in batch: " +
                transactions.size()
        );

        SalesAnalyzer analyzer = new SalesAnalyzer();

        double revenue =
                analyzer.calculateElectronicsRevenue(transactions);

        System.out.printf(
                "%nCompleted Electronics Revenue (sequential): $%.2f%n",
                revenue
        );

        double revenueParallel =
                analyzer.calculateElectronicsRevenueParallel(
                        transactions
                );

        System.out.printf(
                "Completed Electronics Revenue (parallel):   $%.2f%n",
                revenueParallel
        );

        analyzer.printRevenueByCategory(transactions);

        System.out.println(
                "\n===== REPORT COMPLETE ====="
        );
    }
}
