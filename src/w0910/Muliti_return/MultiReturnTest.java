package w0910.Muliti_return;

import w0910.Calc1;

public class MultiReturnTest {

    public static void main(String[] args) {
        MulitInput input = new MulitInput();
        int [] scores = input.inputScores();
        Calc1 calc1 = new Calc1();
        calc1.plus(scores);
        int result= calc1.plus(scores);
        System.out.println(scores.length + "개 정수의 합계: "+ result);


    }}
