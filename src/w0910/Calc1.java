package w0910;
import java.util.List;

public class Calc1 {

    public int plus(int n1, int n2) {
        int result = n1 + n2;
        return result;
    }

    public int plus(int n1, int n2, int n3) {
        int result = n1 + n2 + n3;
        return result;
    }

    // 이 부분을 추가하세요.
    public int plus(int[] numbers) {
        int result = 0;
        for (int number : numbers) {
            result += number;
        }
        return result;
    }
    public int plus(List<Integer> numbers) {
        int reselt = 0;
        for (int number : numbers) {
            reselt += number;
        }
        return reselt;
    }

}