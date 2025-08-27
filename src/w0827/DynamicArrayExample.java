package w0827; // 패키지 선언 추가

import java.util.Arrays;

public class DynamicArrayExample {

    public static void main(String[] args) {
        // 행의 크기가 3인 2차원 배열을 선언
        int[][] dynamicArray = new int[3][];

        // 각 행에 원하는 크기의 배열을 생성하여 할당
        dynamicArray[0] = new int[]{1, 2};
        dynamicArray[1] = new int[]{3, 4, 5};
        dynamicArray[2] = new int[]{6};

        // 생성된 동적 배열의 내용을 확인하기 위해 출력
        System.out.println("동적 배열의 내용:");
        for (int i = 0; i < dynamicArray.length; i++) {
            System.out.println("[" + i + "]행: " + Arrays.toString(dynamicArray[i]));
        }
    }
}