package w0924;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class SecureFileTest {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        FileWriter fw = null;
        String line = "";
        String secureOutStr = "";
        try{
            while(true){
                System.out.println("Enter Message: ");
                line = sc.nextLine();
                if(line.equals(""))
                    break;

                for(int i = 0; i<line.length(); i++){
                    int code = (int)line.charAt(i);
                    code += 100;
                    secureOutStr += (char)code;
                }
            fw.write(secureOutStr+"\n");
            }


        }catch(IOException e){
            System.out.println("File Write Error");
        }

        try{
            fw.close();
            System.out.println("Generated Secure File");
        }catch(IOException e){
            System.out.println("File Write Error");
        }
    }
}

