package w0903;

import java.util.Arrays; // Arrays 클래스를 사용하기 위해 추가
import java.util.Collections;
import java.util.Random;

public class RockGame {
    public static void main(String[] args) {
        String[] result = new String[10000];
        String comA, comB;
        String[] strArr = {"가위", "바위", "보"};

        // 1. 승패 횟수를 저장할 변수 선언
        int aWin, bWin, noWin;

        // 게임 실행 로직
        for (int i = 0; i < result.length; i++) {
            Random random = new Random();

            comA = strArr[random.nextInt(strArr.length)];
            comB = strArr[random.nextInt(strArr.length)];

            if (comA.equals("가위")) {
                if (comB.equals("가위")) {
                    result[i] = "없음";
                } else if (comB.equals("바위")) {
                    result[i] = "B";
                } else { // "보"
                    result[i] = "A";
                }
            } else if (comA.equals("바위")) {
                if (comB.equals("가위")) {
                    result[i] = "A";
                } else if (comB.equals("바위")) {
                    result[i] = "없음";
                } else { // "보"
                    result[i] = "B";
                }
            } else { // "보"
                if (comB.equals("가위")) {
                    result[i] = "B";
                } else if (comB.equals("바위")) {
                    result[i] = "A";
                } else { // "보"
                    result[i] = "없음";
                }
            }
        } // for 반복문 종료

        // 2. 10,000번의 게임이 모두 끝난 후, 결과를 한 번만 집계
        aWin = Collections.frequency(Arrays.asList(result), "A");
        bWin = Collections.frequency(Arrays.asList(result), "B");
        noWin = Collections.frequency(Arrays.asList(result), "없음");

        // 3. 최종 결과를 형식에 맞게 한 번만 출력
        System.out.printf("총 %d번의 computer A의 승리 횟수 : %d번\n", result.length, aWin);
        System.out.printf("총 %d번의 computer B의 승리 횟수 : %d번\n", result.length, bWin);
        System.out.printf("총 %d번의 computer 비긴횟수 : %d번\n", result.length, noWin);

    }
}