package w0827;
import java.util.Arrays;
public class CloneArrayTest {
    public static void main(String[] args) {

        String[] originArr1 = {"김치찌개", "된장찌개",};
        String[] originArr2 = {"참치", "옥돔"};
        System.out.println(Arrays.toString(originArr1));
        System.out.println(Arrays.toString(originArr2));

        String[] OriginArr = {"다니엘", "민지", "해인", "해린"};
        String[] CloneArr = OriginArr.clone();

        CloneArr[0] = "장원영";
        CloneArr[2] = "리즈";

        System.out.println(Arrays.toString(OriginArr));
        System.out.println(Arrays.toString(CloneArr));
    }
}
