package w0917;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReadTest01 {
    public static void main(String[] args) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("D:/FileTest/MyData1.txt"));
            String line = null;
            while (true)
            {
                line =br.readLine();
                if(line == null)
                    break;
                System.out.println(line);
            }
        } catch(FileNotFoundException e) {
            System.out.println("현재경로에서 파일을 찾을 수 없습니다.");
            return;
        }catch (IOException e){
            System.out.println("한 줄씩 읽는 동안 문제가 발생했습니다.");
        }



        try {
            br.close();
        }catch(IOException e){
            System.out.println("BufferedReader를 닫는데 문제가 발생했습니다.");
        }
        }
}