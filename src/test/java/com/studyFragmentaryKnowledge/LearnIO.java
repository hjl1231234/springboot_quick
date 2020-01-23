package com.studyFragmentaryKnowledge;

import org.junit.Test;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class LearnIO {
    public static final String WHERE = "src/where.txt";
    public static final String WHERE_GBK = "src/whereGBK.txt";
    public static final String WHERE_OUT = "src/where_out.txt";


    @Test
    public void testGetBytes() {
        String str1 = "中文";

        byte[] bytes;
        try {
            bytes = str1.getBytes("utf8");
            System.out.println(bytes);
            String str2 = new String(bytes, "UTF-8");
            System.out.println(str2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerialize() throws IOException, ClassNotFoundException {
        A a1 = new A(123, "abc");
        String objectFile = "a1";


        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(objectFile));
        objectOutputStream.writeObject(a1);

        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(objectFile));
        A a2 = (A) objectInputStream.readObject();
        System.out.println(a2);


        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/where.txt"));
//        BufferedReader bufferedReader = new BufferedReader(new FileReader("./src/where.txt"));
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }


        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("src/where.txt"));
        int t = 0;
        while ((t = bufferedInputStream.read()) != -1) {
            System.out.println((char) t);

        }


        //斜杠目录究竟是什么东西？可能是转义 object不向磁盘写可以吗？不行
        A aBar = new A(456, "abc");
        A aDefault = new A(789, "abc");


    }

    @Test
    public void testHexDump() throws IOException {
        final String FILE_NAME = "a1";

        ByteCodes.readFrom(FILE_NAME, 32).show();
    }

    @Test
    public void testInetAddress() throws IOException {
        InetAddress inetAddress = InetAddress.getByName("localhost");
        System.out.println(inetAddress.getHostName() + "  " + inetAddress.getHostAddress());

        URL url = new URL("http://www.baidu.com");

        /* 字节流 */
        InputStream is = url.openStream();

        /* 字符流 */
        InputStreamReader isr = new InputStreamReader(is, "utf-8");

        /* 提供缓存功能 */
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }


    //    ServerSocket serverSocket;
//
//    {
//        try {
//            serverSocket = new ServerSocket(8080);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    Socket clientSocket;
//
//    {
//        try {
//            clientSocket = serverSocket.accept();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//    PrintWriter out;
//
//    {
//        try {
//            out = new PrintWriter(clientSocket.getOutputStream(), true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    String request, response; while((request =in.readLine())!=null)
//
//    {
//        if ("Done".equals(request)) {
//            break;
//        }
//        response = processRequest(request);
//        out.println(response);
//    }


    @Test
    public void testByteArrayInputStream() throws IOException, ClassNotFoundException {
        A aByteArrayInputStream = new A(777, "abc");


        String str = "HELLO    WORLD!";        // 定义一个字符串，全部由大写字母组成
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());    // 内存输入流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    // 内存输出流
        int temp = 0;
        while ((temp = bis.read()) != -1) {
            bos.write(Character.toLowerCase((char) temp));
        }
        bis.close();
        bos.close();
        System.out.println(bos.toString());

    }


}

class A implements Serializable {

    private int x;
    private String y;

    A(int x, String y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "x = " + x + "  " + "y = " + y;
    }
}

class ByteCodes {

    private final static int DEFAULT_ROW_SIZE = 16;

    private List<Byte> byteList = new ArrayList<>();

    private InputStream input;

    private int rowCount;


    private ByteCodes(InputStream input) throws IOException {
        this(input, DEFAULT_ROW_SIZE);
    }

    private ByteCodes(InputStream input, int rowCount) throws IOException {
        this.rowCount = rowCount;
        this.input = input;
        read();
    }

    private void read() throws IOException {
        int b = -1;
        while ((b = input.read()) != -1) {
            byteList.add((byte) b);
        }
    }

    public static ByteCodes readFrom(String filename) throws IOException {
        return readFrom(filename, DEFAULT_ROW_SIZE);
    }

    public static ByteCodes readFrom(InputStream input) throws IOException {
        return new ByteCodes(input);
    }

    public static ByteCodes readFrom(String filename, int rowCount) throws IOException {
        InputStream input = new FileInputStream(filename);
        return new ByteCodes(input, rowCount);
    }

    public static ByteCodes readFrom(InputStream input, int rowCount) throws IOException {
        return new ByteCodes(input, rowCount);
    }

    public ByteCodes show() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("Address   ");
        for (int i = 0; i < rowCount; i++) {
            sb.append(String.format("%02X ", i));
        }
        sb.append(String.format("  |   Dump   %n"));

        int b = -1, rowIndex = 0;
        byte[] rowBytes = new byte[rowCount];
        for (int i = 0; i < byteList.size(); i++) {
            if (i % rowCount == 0) {
                sb.append((i == 0 ? "" : "  |") + new String(rowBytes, 0, rowIndex) + (i == 0 ? "" : "|"));
                sb.append(String.format((i == 0 ? "" : "%n") + "%08X: ", i));
                rowIndex = 0;
            }
            sb.append(String.format("%02X ", byteList.get(i)));

            byte curByte = byteList.get(i);

            if (curByte == '\n' || curByte == '\r' || curByte == '\t') {
                rowBytes[rowIndex++] = '.';
            } else {
                rowBytes[rowIndex++] = byteList.get(i);
            }
        }
        System.out.println(sb);
        return this;
    }

}