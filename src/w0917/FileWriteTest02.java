package w0917;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileWriteTest02 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FileWriter fw = null;
        String line = null;
        try{
            fw = new FileWriter("D:/FileTest/MyData3.txt");

            while(true){
                System.out.printf("좋아하는 가수는 ? : ");
                line = sc.nextLine();
                if(line.equalsIgnoreCase("q"))
                    break;
                fw.write(line+ "\n");


            }


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
