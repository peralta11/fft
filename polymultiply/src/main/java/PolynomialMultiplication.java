import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexField;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.util.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PolynomialMultiplication {

    public static void multiply(String s1, String s2) {
        List<Integer> a1 = new ArrayList<Integer>();
        List<Integer> a2 = new ArrayList<>();
        a1 = Arrays.stream(s1.split("")).map(Integer::valueOf).
                collect(Collectors.toList());
        a2 = Arrays.stream(s2.split("")).map(Integer::valueOf).
                collect(Collectors.toList());
        fft(a1);
        fft(a2);
    }

    private static List<Complex> fft(List<Integer> vector) {
        if(vector.size() == 1){
            List<Complex> base = new ArrayList<Complex>();
            base.add(new Complex(vector.get(0)));
            return base;
        }
        int n = vector.size();
        Complex Wn = new Complex((MathUtils.TWO_PI/n),(MathUtils.TWO_PI/n));
        Complex w = new Complex(1);
        ArrayList aEven;
        ArrayList aOdd;
        ArrayList<Complex> resultVector = new ArrayList<Complex>();
        aEven = (ArrayList) IntStream.range(0, vector.size()).filter(i ->  i % 2 == 0).mapToObj(vector::get).collect(Collectors.toList());
        aOdd = (ArrayList) IntStream.range(0, vector.size()).filter(i ->  i % 2 == 1).mapToObj(vector::get).collect(Collectors.toList());
        List aResultEven = fft(aEven);
        List aResultOdd = fft(aOdd);
        int k=0;
        do{
            Complex v1 = (Complex) aResultEven.get(k);
            Complex v2 = (Complex) aResultOdd.get(k);
            resultVector.add(k,(v2.multiply(w)).add(v1));
            w = Wn.multiply(w);
            k++;
        }while (k<(n/2)-1);
        return resultVector;
    }
}
