package w0924;

public class ArrayExceptionTest {

    public static void main(String[] args) {

        int[] arr = {10, 20, 30}; // 올바른 배열 초기화 방법

        try {
            arr[3] = 40; // 인덱스 3은 존재하지 않으므로 ArrayIndexOutOfBoundsException 발생
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("배열의 인덱스 번호가 범위를 벗어난 것 같습니다.");
        }

        // 배열의 모든 요소를 출력
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "\t"); // "\t"는 문자열 탭을 의미
        }
    }
}