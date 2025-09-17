package w0917;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriteTest01 {
    public static void main(String[] args) {
        FileWriter fw = null;
        String line = null;
        try{
            fw = new FileWriter("D:/FileTest/MyData2.txt");
            line = "땅콩이 귀여워";
            fw.write(line + "\n");

            line = "장군이 귀여워";
            fw.write(line + "\n");

            line = "땅콩이 보고싶어";
            fw.write(line + "\n");

            line = "장군이도 보고싶어";
            fw.write(line + "\n");

        } catch (IOException e){
            System.out.println("FileWriter Generation Error");
        }

        try {
            fw.close();
        } catch (IOException e) {
            System.out.println("FileWriter Close Error");
        }
    }
}
