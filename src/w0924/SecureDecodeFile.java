package q0924;

import java.io.*;

public class SecureDecodeFile
{
    public static void main(String[] args)
    {
        FileReader fr = null;           //파일 읽어오는 도구
        BufferedReader br=null;         //파일 한 줄씩 읽게 해줌
        FileWriter fw = null;           //파일 쓰는 도구
        String read1;                   //파일 읽을 때 저장


        try {
            fr=new FileReader("C:/JavaProjects/secure1.txt");
            br=new BufferedReader(fr);
            fw=new FileWriter("C:/JavaProjects/secureDecode.txt");

            while((read1=br.readLine())!=null)       //null일때까지 실행. 즉 작성된 내용이 다 읽히면 끝남
            {
                System.out.println("읽은 줄: "+read1);
                String read2="";                   //파일 쓸 때 사용
                for(int i=0; i<read1.length(); i++)
                {
                    int code=(int)read1.charAt(i);
                    code-=100;
                    read2+=(char)code;
                }
                fw.write(read2+"\n");        //fw가 가르키는 Decode.txt에 작성
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileReader Fail");
        } catch (IOException e) {
            System.out.println("FileWriter Fail");
        }

        try
        {
            fw.close();
            fr.close();
            System.out.println("Text Decoding Success");
        } catch (IOException e) {
            System.out.println("fw,fr Closing Error");
        }
    }
}
//파일 읽어오기 - C:/JavaProjects/secure1.txt
//해석하기 - 문자>숫자>문자
//파일 출력하기 - C:/JavaProjects/result.txt