package w0827;
import java.util.Arrays;
import java.util.Collections;

public class SortArrayTest {
    public static void main(String[] args) {
        int[] arr = {77,88,99,11,22,33,44,99,66,55};
        String[] strArr = {"인공지능", "소프트웨어", "한국", "대학", "폴리텍", "하이테크", "1학년", "2학년"};

    Arrays.sort(arr);

        for(int num :arr) {
            System.out.printf(num + "\t");


        }
        System.out.println();

        Arrays.sort(strArr, Collections.reverseOrder());

        for(String str :strArr) {
            System.out.printf(str+"\t");
        }
    }
}
