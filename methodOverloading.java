import java.util.*;

class Calculator{
    public int add(int a, int b){
        return a + b;
    }

    public int add(int a, int b, int c){
        return a + b + c;
    }

    public double add(double a, double b){
        return a + b;
    }
}

public class methodOverloading {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        int res1 = calc.add(4, 3);
        System.out.println(res1);
        int res2 = calc.add(3, 4, 3);
        System.out.println(res2);
        double res3 = calc.add(10.0, 25.0);
        System.out.println(res3);
    }
}
