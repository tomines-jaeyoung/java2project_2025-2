package w0827;

import java.util.Calendar;
import java.util.Scanner;

public class rndArray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] strArr = {"돈까스", "비빔밥", "제육볶음", "부대찌개", "김치찌개"};
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        while(true) {
            // 배열의 길이 만큼 곱하면 딱 그 범위만큼 나온다.
            // 뒤에 -1은 0번 인덱스도 포함하기 위함.
            int rnd = (int)(Math.random() * strArr.length) - 1;
            System.out.println(year+"/"+month+"/"+day+" 오늘의 점심 메뉴 추천 "+strArr[rnd]);
            System.out.println("계속 하려면 아무 값이나 입력하세요.");
            String next = sc.nextLine();


            if(next.equals("q")){
                System.out.println("프로그램 종료");
                break;
            }
        }
    }
}
