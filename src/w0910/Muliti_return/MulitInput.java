package w0910.Muliti_return;
import java.util.Scanner;
public class MulitInput {

    public int[] inputScores(){
        int[] scores = new int[3];
        Scanner scanner = new Scanner(System.in);

        for(int i=0;i<3;i++){
            System.out.printf("정수" + (i+1) + "입력: ");
            scores[i] = scanner.nextInt();
        }
        scanner.close();
        return scores;
    }
}
