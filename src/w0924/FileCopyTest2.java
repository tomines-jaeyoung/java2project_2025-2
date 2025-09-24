package w0924;
import java.io.*;

public class FileCopyTest2 {

    public static void copyFile() throws IOException {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;

        fr = new FileReader("D:/FileTest/MyData1.txt");
        br = new BufferedReader(fr);
        fw = new FileWriter("D:/FileTest/CopyFile1.txt");
        String line;

        while ((line = br.readLine()) != null) {
            fw.write(line + "\n");
        }

        br.close();
        fw.close();

        System.out.println("복사가 완료되었습니다.");
    }

    public static void main(String[] args) {
        try {
            copyFile();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}