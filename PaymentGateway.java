import java.util.*;

interface PaymentStrategy{
    boolean pay(double amount);
}

class UPIPayment implements PaymentStrategy{
    public boolean pay(double amount){
        System.out.println("Processing the UPI Payment amount of: ₹" + amount);
        return true;
    }
}

class CreditCardPayment implements PaymentStrategy{
    public boolean pay(double amount) {
        System.out.println("Processing the Credit Card Payment amount of: ₹" + amount);
        return true;
    }
}

class DebitCardPayment implements PaymentStrategy {
    public boolean pay(double amount) {
        System.out.println("Processing the Debit Card Payment of: ₹" + amount);
        return true;
    }
}

class PaymentFactory{
    public static PaymentStrategy getPaymentMethod(String type){
        if(type.equalsIgnoreCase("UPI")){
            return new UPIPayment();
        }
        else if(type.equalsIgnoreCase("Credit Card")){
            return new CreditCardPayment();
        }
        else if(type.equalsIgnoreCase("Debit Card")){
            return new DebitCardPayment();
        }

        throw new IllegalArgumentException("Invalid Payment Type");
    }
}

enum PaymentStatus{
    SUCCESS,
    FAILED
}

class PaymentService{
    public PaymentStatus processPayment(String type, double amount){
        PaymentStrategy strategy = PaymentFactory.getPaymentMethod(type);
        boolean result = strategy.pay(amount);
        return result ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
    }
}

public class PaymentGateway {
    public static void main(String[] args) {
        PaymentService service = new PaymentService();

        System.out.println(service.processPayment("UPI", 500));
        System.out.println(service.processPayment("Credit Card", 1000));
        System.out.println(service.processPayment("Debit Card", 700));
    }
}
