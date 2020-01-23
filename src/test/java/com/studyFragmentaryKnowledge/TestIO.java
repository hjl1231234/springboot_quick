package com.studyFragmentaryKnowledge;

import java.io.*;

/**
 * 对于scanner之类的总结，终于清楚版
 * <p>
 * 通常判断中是hasxxx后面通常用nextxxx来处理。如hasnext配合next hasnextline配合nextline hasnextint配合nextint
 * 其中hasnextline判断是否有模式串，即使没有非空字符也不会停。所以不好用。hasnext判断没有非空字符后便停止。
 * <p>
 * 所谓空字符,包括,\n,0X0A,空格,制表符\t 等等后缀。
 * <p>
 * hasnext中提供传入模式字符串提供正则匹配功能，但是有重大缺陷。如，*匹配应该允许匹配0个的情况，但是该方法中直接将其等价于+。
 * 而却，如果是空字符（包含转义字符），则根本不起作用，应该是自己将其过滤掉了。
 * <p>
 * <p>
 * <p>
 * io输入流(例如fileinputstrem)的地址格式也是非常奇怪，相对路径竟然以.开始而不是/开始。
 */


public class TestIO {

    public static void main(String[] args) throws IOException {


//        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
//        FileOutputStream fileOutputStream = new FileOutputStream(LearnIO.WHERE_OUT);

//        FileInputStream fileInputStream = new FileInputStream(LearnIO.WHERE_GBK);
        FileInputStream fileInputStream = new FileInputStream(LearnIO.WHERE_GBK);


//        FileReader fileReader = new FileReader(LearnIO.WHERE_GBK);
//        FileWriter fileWriter = new FileWriter(LearnIO.WHERE_OUT);


//        BufferedReader bufferedReader=new BufferedReader(new FileReader(LearnIO.WHERE_GBK));
//        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(LearnIO.WHERE_OUT));

        InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(LearnIO.WHERE_GBK),"gbk");
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(new FileOutputStream(LearnIO.WHERE_OUT),"utf8");


        int str = 0;
        byte[] bytes = new byte[256];

        try {
            while ((str = inputStreamReader.read()) != -1) {
//                System.out.printf((char)str+" ");
//                fileOutputStream.write((char)str);
//                fileWriter.write((char) str);
//                fileWriter.flush();

                outputStreamWriter.write(str);
                outputStreamWriter.flush();
                System.out.println(str);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str);


        /**
         * bufferedreader inputstreamreader 练习
         */
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        String line = "";
//        while ((line = bufferedReader.readLine()) != null) {
//            System.out.println(line);
//        }

        /**
         * scanner练习
         */
//        Scanner scanner=new Scanner(System.in);
//        String line="";
//        while(scanner.hasNextLine()){
//            line=scanner.nextLine()+'\n';
//        }
//
//        System.out.println(line);


//        Scanner sc = new Scanner(System.in);
//        while (!sc.hasNext("n+")) {
//            System.out.println(sc.next());
//        }


/**
 * 重定向练习,file
 */
//        try {
//            FileInputStream fileInputStream = new FileInputStream("src/where.txt");
//            //到底应该是在fileoutputstream追加还是在printstream追加？就是在fileoutputstream中加
//            PrintStream printStream = new PrintStream(new FileOutputStream("src/where_out.txt", true));
//
//            System.setIn(fileInputStream);
//            Scanner scanner = new Scanner(System.in);
//            scanner.useDelimiter("\n");
//            while (scanner.hasNext()) {
//                System.out.println(scanner.next());
//            }
//            //如果setout比循环先开始执行，那么接下来所有内容都会被输入到setout中。
//            System.setOut(printStream);
//            FileOutputStream fileOutputStream=new FileOutputStream("src/where_out.txt",true);
//            fileOutputStream.write("追加在结尾".getBytes());
//
//
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}
