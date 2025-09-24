package w0924;

public class ArrayExceptionTest {

    public static void main(String[] args) {

        int[]arr = new int{10,20,30};
        try {
            arr[3] = 40;
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("배열의 인덱스 번호가 범위를 벗어난것 같습니다.");
        }
        for(int i = 0; i<arr.length; i++){
            System.out.println(arr[i]+'\t');
        }
    }
}
