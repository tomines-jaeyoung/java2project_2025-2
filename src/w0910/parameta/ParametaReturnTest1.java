package w0910.parameta;

import w0910.Calc1;

import java.util.Scanner;

public class ParametaReturnTest1 {

    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
        System.out.println("=== 2개의 정수의 합계를 구해드립니다. ===");
        System.out.print("★ 정수1 입력 :");
        int num1 = sc.nextInt();
        System.out.print("★ 정수12 입력 :");
        int num2 = sc.nextInt();
        Calc1 calc1 = new Calc1();
    int result = calc1.plus(num1, num2);
        System.out.println("덧셈결과 =" + result);

        sc.close();
    }
}
