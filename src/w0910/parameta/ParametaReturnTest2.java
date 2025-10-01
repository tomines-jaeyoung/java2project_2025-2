
package w0910.parameta;

public class ParametaReturnTest2 {
    public static void main(String[] args) {

        Calc1 calc1 = new Calc1();
        int result = calc1.plus(100, 200, 300);
        System.out.println("3개 정수의 합계 : " + result);




    }
public int plus(int[] numbers) {
        int result = 0;
        for(int number : numbers) {
            result += number;
        }
        return result;
}
    //숫자값 3개에 값을 배열에다가 저장해서 사용




}
