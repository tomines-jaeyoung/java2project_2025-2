package w0910.lotto;

import java.util.Random;

public class RandomNumber {
    public int randomNum(int max){
        int num=0;
        Random rand = new Random();
   num = rand.nextInt(max) +1; //1-45까지의 숫자


   return num;


    }
}
