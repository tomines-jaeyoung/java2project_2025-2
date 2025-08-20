package w0820;

public class Array2Test {
    public static void main(String[] args) {

        int[][] arr1 = new int[2][3];
        int cnt = 1;

        // 1. arr1 배열에 값 할당 (1부터 6까지)
        System.out.println("--- arr1 배열 값 할당 ---");
        for (int i = 0; i < arr1.length; i++) { // arr1.length는 행의 개수 (2)
            for (int j = 0; j < arr1[i].length; j++) { // arr1[i].length는 각 행의 열 개수 (3)
                arr1[i][j] = cnt++;
            }
        }

        // 2. arr1 배열 출력
        System.out.println("--- arr1 배열 출력 ---");
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr1[i].length; j++) {
                System.out.print(arr1[i][j] + "\t");
            }
            System.out.println(); // 한 행 출력이 끝나면 줄바꿈
        }
        System.out.println(); // 배열 간 구분을 위한 빈 줄 추가

        // 3. arr2 배열 선언 및 초기화
        int[][] arr2 = {{1, 3, 5}, {7, 8, 9}};

        // 4. arr2 배열 출력
        System.out.println("--- arr2 배열 출력 ---");
        for (int i = 0; i < arr2.length; i++) {
            for (int j = 0; j < arr2[i].length; j++) {
                System.out.print(arr2[j][i] + "\t");
            }
            System.out.println();
        }
    }
}