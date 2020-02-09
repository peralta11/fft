import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
public class Main {
    public static void main(String[] args) {
        long start;
        long finish;
        String s1 = "99866699999999870000000000000000";

        String s2 = "99999999786576540000000000000000";


        PolynomialMultiplication.multiply(s1,s2);



        BigDecimal b1 = new BigDecimal(s1);
        BigDecimal b2 = new BigDecimal(s2);
        start=System.nanoTime();
        b1.multiply(b2);
        finish=System.nanoTime();
        System.out.println("time taken for bigDecimal multiplication " + TimeUnit.NANOSECONDS.toMillis(finish-start));
        System.out.println(b1.multiply(b2).toString());

    }
}
