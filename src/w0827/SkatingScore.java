package w0827;

import java.util.Scanner;

public class SkatingScore {
    public static void main(String[] args) {
        // 심사위원 5명의 점수를 저장할 배열
        int[] scores = new int[5];

        // 사용자 입력을 받기 위한 Scanner 객체 생성
        Scanner scanner = new Scanner(System.in);

        System.out.println("피겨스케이팅 심사위원 5명의 점수를 입력하세요. (10점 만점)");

        // 반복문을 통해 5명의 심사위원 점수를 입력받아 배열에 저장
        for (int i = 0; i < scores.length; i++) {
            System.out.print((i + 1) + "번째 심사위원 점수: ");
            scores[i] = scanner.nextInt();
        }

        // 점수 합계를 계산할 변수
        int sum = 0;

        // 배열에 저장된 점수들의 합계 계산
        for (int score : scores) {
            sum += score;
        }

        // 평균 점수 계산 (정수형 나누기 오류를 방지하기 위해 double로 형변환)
        double average = (double) sum / scores.length;

        System.out.println("\n--- 심사 결과 ---");
        System.out.println("김연아 선수 경기 끝났습니다~~~ 짝짝짝");

        // 입력받은 점수 출력
        for (int i = 0; i < scores.length; i++) {
            System.out.println("평가 점수 ==> " + scores[i]);
        }

        // 심사위원 평균 점수 출력
        System.out.printf("심사위원 평균 점수 : %.2f\n", average);

        // Scanner 객체 닫기
        scanner.close();
    }
}