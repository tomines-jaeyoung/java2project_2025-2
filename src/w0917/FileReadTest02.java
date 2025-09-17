package w0917;

import java.io.*;

public class FileReadTest02 {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = null;
        String line = null;
        int linenum = 1;
        try{
            br = new BufferedReader(new FileReader("D:/FileTest/MyData1.txt"));
            while ((line = br.readLine()) != null) {
                System.out.printf("%d: %s\n", linenum++, line);
            }
//            while (true) {
//                line = br.readLine();
//                if(line==null)
//                    break;
//                sb.append(linenum + "." + line + "\n");
//                linenum++;
//            }
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다");
        } catch (IOException e) {
            System.out.println("한 줄씩 읽어오는데 문제가 발생했습니다");
        }

        sb.reverse();
        System.out.println(sb); //toString이 자동으로 실행되서 생략 가능

        try {
            br.close();
        } catch (IOException e) {
            System.out.println("파일을 닫는데 오류가 발생했습니다");
        }
    }
}
