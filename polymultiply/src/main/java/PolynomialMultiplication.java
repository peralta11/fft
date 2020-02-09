import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexField;
import org.apache.commons.math3.complex.ComplexFormat;
import org.apache.commons.math3.util.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PolynomialMultiplication {

    public static void multiply(String s1, String s2) {
        long start;
        long finish;
        List<Integer> a1 = new ArrayList<Integer>();
        List<Integer> a2 = new ArrayList<>();
        a1 = Arrays.stream(s1.split("")).map(Integer::valueOf).
                collect(Collectors.toList());
        a2 = Arrays.stream(s2.split("")).map(Integer::valueOf).
                collect(Collectors.toList());
        System.out.println(a1.size());
        start=System.nanoTime();
        List<Complex> fftList1 = fft(a1);
        List<Complex> fftList2 =  fft(a2);
        List<Complex> resultMultiply = multiplyInputs(fftList1,fftList2);
        List<Complex> finalResult = ifft(resultMultiply);
        ArrayList<Integer> result = new ArrayList<>();
        for(int i=0;i<finalResult.size();i++){
            result.add((int) Math.round(finalResult.get(i).getReal()/finalResult.size()));
        }
        finish=System.nanoTime();
        String r=calculateResult(result);
        System.out.println(r);
        System.out.println("time taken for fft multiplication " + TimeUnit.NANOSECONDS.toMillis(finish-start));

    }

    private static List<Complex> multiplyInputs(List<Complex> fftList1, List<Complex> fftList2) {
        for(double i=0;i<fftList1.size();i++){
            fftList1.set((int) i,fftList1.get((int) i).multiply(fftList2.get((int) i)));
        }
        return fftList1;
    }

    private static List<Complex> fft(List<Integer> vector) {
        if(vector.size() == 1){
            List<Complex> base = new ArrayList<Complex>();
            base.add(new Complex(vector.get(0)));
            return base;
        }
        int n = vector.size();
        Complex Wn = new Complex(Math.cos(MathUtils.TWO_PI/n),Math.sin(MathUtils.TWO_PI/n));
        Complex w = new Complex(1);
        ArrayList aEven;
        ArrayList aOdd;
        ArrayList<Complex> resultVector = new ArrayList<Complex>();
        aEven = (ArrayList) IntStream.range(0, vector.size()).filter(i ->  i % 2 == 0).mapToObj(vector::get).collect(Collectors.toList());
        aOdd = (ArrayList) IntStream.range(0, vector.size()).filter(i ->  i % 2 == 1).mapToObj(vector::get).collect(Collectors.toList());
        List aResultEven = fft(aEven);
        List aResultOdd = fft(aOdd);
        int k=0;
        while (k<=(n/2)-1){
            Complex v1 = (Complex) aResultEven.get(k);
            Complex v2 = (Complex) aResultOdd.get(k);
            Complex temp = w.multiply(v2);
            if(resultVector.isEmpty()) {
                resultVector.add(temp.add(v1));
                resultVector.add(v1.subtract(temp));
            }else{
                resultVector.add(k,(v2.multiply(w)).add(v1));
                resultVector.add(v1.subtract(v2.multiply(w)));
            }
            w = Wn.multiply(w);
            k++;
        };
        return resultVector;
    }

    private static List<Complex> ifft(List<Complex> fftresult) {
        if(fftresult.size()==1){
            return fftresult;
        }
        int n = fftresult.size();
        Complex Wn = new Complex(Math.cos(MathUtils.TWO_PI/n),-Math.sin(MathUtils.TWO_PI/n));
        Complex w = new Complex(1);
        ArrayList<Complex> aEven;
        ArrayList<Complex> aOdd;
        ArrayList<Complex> resultVector = new ArrayList<Complex>();
        aEven = (ArrayList<Complex>) IntStream.range(0, fftresult.size()).filter(i ->  i % 2 == 0).mapToObj(fftresult::get).collect(Collectors.toList());
        aOdd = (ArrayList<Complex>) IntStream.range(0, fftresult.size()).filter(i ->  i % 2 == 1).mapToObj(fftresult::get).collect(Collectors.toList());
        List aResultEven = ifft(aEven);
        List aResultOdd = ifft(aOdd);
        int k=0;
        while (k<=(n/2)-1){
            Complex v1 = (Complex) aResultEven.get(k);
            Complex v2 = (Complex) aResultOdd.get(k);
            Complex temp = w.multiply(v2);
            if(resultVector.isEmpty()) {
                resultVector.add(temp.add(v1));
                resultVector.add(v1.subtract(temp));
            }else{
                resultVector.add(k,(v2.multiply(w)).add(v1));
                resultVector.add(v1.subtract(v2.multiply(w)));
            }
            w = Wn.multiply(w);
            k++;
        };
        return resultVector;

    }

    private static String calculateResult(ArrayList<Integer> finalResult) {
        StringBuilder result= new StringBuilder();
        StringUtils.leftPad(result.toString(),finalResult.size());
        int carry=0;
        for(int i=finalResult.size()-1;i>=0;i--) {
            if (finalResult.get(i) != 0) {
                if (result.toString().isEmpty()) {
                    int t = finalResult.get(i) % 10;
                    result.insert(0, t);
                }
                if (i != 0) {
                    int sum = finalResult.get(i) / 10 + finalResult.get(i - 1) % 10 + carry;
                    carry = sum > 9 ? sum/10 : 0;
                    result.insert(0, sum % 10);
                }
            }
            if(i==0){
                int sum= finalResult.get(i)/10+carry;
                result.insert(0,sum);
            }
        }
        return result.toString();
    }
}
