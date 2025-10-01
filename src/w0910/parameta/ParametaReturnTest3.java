package w0910.parameta;

import java.util.ArrayList;

public class ParametaReturnTest3 {

    public static void main(String[] args) {
        int[] numbers = {1,3,5,7,9,11};
        Calc1 calc = new Calc1();

    int result = calc.plus(numbers);
        System.out.println("배열에 저장된 정수 값들의 합계; " + result);
  ArrayList<Integer> list = new ArrayList<Integer>();
  list.add(10);
  list.add(22);
  list.add(33);
  list.add(11);

  result =calc.plus(list);

        System.out.println("리스트에 추가된 정수들의 합계: " +result);




    }
}
