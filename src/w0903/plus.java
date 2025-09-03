package w0903;

import java.util.Scanner;

public class plus {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        System.out.print("첫 번째 정수를 입력하세요: ");
        int num1 = scanner.nextInt();
        System.out.print("연산자를 입력하세요: ");
        char operator = scanner.next().charAt(0);
        System.out.print("두 번째 정수를 입력하세요: ");
        int num2 = scanner.nextInt();
        int result = calc(num1, num2, operator);
        if(operator == '+' || operator == '-' || operator == '*' || operator == '/' ) {
            System.out.println("정답 : " + num1 + operator + num2 + " = " + result);
        }
        else {
            System.out.println("잘못된 연산자입니다.");
        }

        scanner.close();
    }

    public static int calc(int v1, int v2, char op) {
        int result = 0;
        switch (op) {
            case '+':
                result = v1 + v2;
                break;
            case '-':
                result = v1 - v2;
                break;
            case '*':
                result = v1 * v2;
                break;
            case '/':
                if (v2 == 0) {
                    System.out.println("0으로 나눌수없다");
                    result = 0;
                } else {
                    result = v1 / v2;
                }
                break;
            default:
                result = 0;
                break;
        }

        return result;
    }
}