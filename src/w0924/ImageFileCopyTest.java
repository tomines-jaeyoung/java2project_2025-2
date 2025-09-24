package w0924;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageFileCopyTest {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;


        try {
            fis = new FileInputStream("C:/Users/AI-510-157/Pictures/Screenshots/스크린샷 2025-06-04 094527.png");
            fos = new FileOutputStream("D:/FileTest/CopyImageFile1.png");
            int ch;
            while((ch=fis.read()) != -1){
            fos.write(ch);
            }
        }catch(FileNotFoundException e){
            System.out.println("file not found");
    } catch (IOException e){
        System.out.println("Reading file error");
    }
   try{
       fis.close();
       fos.close();
       System.out.println("File copied successfully");

   }catch(IOException e){
       System.out.println("closing file iostream error");
   }


}
}