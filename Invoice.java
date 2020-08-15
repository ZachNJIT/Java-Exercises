import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class Invoice {
    private final String partNumber;
    private final String partDescription;
    private int quantity;
    private double pricePerItem;

    // constructor
    public Invoice(String partNumber, String partDescription, int quantity,
                   double pricePerItem) {
        if (quantity < 0) { // validate quantity
            throw new IllegalArgumentException("Quantity must be >= 0");
        }

        if (pricePerItem < 0.0) { // validate pricePerItem
            throw new IllegalArgumentException("Price per item must be >= 0");
        }

        this.quantity = quantity;
        this.partNumber = partNumber;
        this.partDescription = partDescription;
        this.pricePerItem = pricePerItem;
    } //end constructor

    // get part number
    public String getPartNumber() {return partNumber;} //should validate

    // get description
    public String getPartDescription() {return partDescription;}

    //set quantity
    public void setQuantity(int quantity) {
        if (quantity < 0) // validate quantity
            throw new IllegalArgumentException("Quantity must be >=0");

        this.quantity = quantity;
    }

    // get quantity
    public int getQuantity() {return quantity;}

    public String getPartDescriptionAndQuantity() {
        return String.format("%-20s %-10s", getPartDescription(), getQuantity());
    }

    public String getPartDescriptionAndValue() {
        return String.format("%-20s %.2f", getPartDescription(), getPaymentAmount());
    }

    public void setPricePerItem(double pricePerItem) {
        if (pricePerItem < 0.0) // vaildate pricePerItem
            throw new IllegalArgumentException("Price per item must be >= 0");

        this.pricePerItem = pricePerItem;
    }

    // get price per item
    public double getPricePerItem() {return pricePerItem;}

    // return String representation of Invoice object
    public String toString() {
        return String.format("%-15s %-20s %-10d %.2f", getPartNumber(), getPartDescription(), getQuantity(), getPricePerItem());
    }

    public double getPaymentAmount() {
        return getQuantity() * getPricePerItem(); // calculate total cost
    }

    public static void main(String[] args) {
        Invoice[] invoices = {
                new Invoice("83", "Electric sander", 7, 57.98),
                new Invoice("24", "Power saw", 18, 99.99),
                new Invoice("7", "Sledge hammer", 11, 21.50),
                new Invoice("77", "Hammer", 76, 11.99),
                new Invoice("77", "Lawn Mower", 3, 79.50),
                new Invoice("39", "Screwdriver", 106, 6.99),
                new Invoice("68", "Jig saw", 21, 11.00),
                new Invoice("56", "Wrench", 34, 7.50)};

        List<Invoice> list = Arrays.asList(invoices);

        System.out.println("First let us look at our invoices:");
        System.out.printf("%-15s %-20s %-10s %s%n", "Part number", "Part description", "Quantity", "Price");
        list.stream().forEach(System.out::println);
        System.out.println();

        System.out.println("Part (a): We sort the invoices by part description");
        System.out.printf("%-15s %-20s %-10s %s%n", "Part number", "Part description", "Quantity", "Price");
        list.stream().sorted(Comparator.comparing(Invoice::getPartDescription)).forEach(System.out::println);
        System.out.println();

        System.out.println("Part (b): We sort the invoices by part description");
        System.out.printf("%-15s %-20s %-10s %s%n", "Part number", "Part description", "Quantity", "Price");
        list.stream().sorted(Comparator.comparing(Invoice::getPricePerItem)).forEach(System.out::println);
        System.out.println();

        System.out.println("Part (c): We map invoices to part description and quantity and sort by quantity");
        System.out.printf("%-20s %-10s%n", "Part description", "Quantity");
        list.stream().sorted(Comparator.comparing(Invoice::getQuantity)).map(Invoice::getPartDescriptionAndQuantity).forEach(System.out::println);
        System.out.println();

        System.out.println("Part (d): We map invoices to part description and the total value of the invoice");
        System.out.printf("%-20s %-10s%n", "Part description", "Total Value");
        list.stream().sorted(Comparator.comparing(Invoice::getPaymentAmount)).map(Invoice::getPartDescriptionAndValue).forEach(System.out::println);
        System.out.println();

        Predicate<Invoice> twoToFiveHundred = e -> (e.getPaymentAmount() >= 200 && e.getPaymentAmount() <= 500);

        System.out.println("Part (e): We filter the invoices in Part (d) to find only those between $200 and $500");
        System.out.printf("%-20s %-10s%n", "Part description", "Total Value");
        list.stream().filter(twoToFiveHundred).sorted(Comparator.comparing(Invoice::getPaymentAmount)).map(Invoice::getPartDescriptionAndValue).forEach(System.out::println);
        System.out.println();
    }
}
