package w0910.lotto;

import java.util.ArrayList;
import java.util.Arrays;

public class LottoDrawwithLabel
{



    public static void main(String[] args)
  {
        System.out.println("** 로또 추첨을 시작합니다 **");
        ArrayList<Integer> numArr = new ArrayList<>();
        int randNum =0;
        RandomNumber rn =new RandomNumber();

   reFindNum:
   while(true)
     {
       randNum = rn.randomNum(45);
       for(int num: numArr)
       {
           if(randNum == num)
             continue reFindNum;


       }
        numArr.add(randNum);

        if(numArr.size()==6)
            break;


     }
Object numArr2 = numArr.toArray();
    Arrays.sort(numArr.toArray());
      System.out.println("=======  이번 주 로또 번호 ======== ");
      System.out.println(numArr.toString());
  }

}
