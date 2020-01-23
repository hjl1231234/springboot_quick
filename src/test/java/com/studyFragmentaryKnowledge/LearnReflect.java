package com.studyFragmentaryKnowledge;

import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class LearnReflect {
    @Test
    public void testCallBack() throws IllegalAccessException, InstantiationException {
        List<Device> Devices = new ArrayList<>();
        List<Class> clazzs = getAllInterfaceAchieveClass(Device.class);
        for (Class clazz : clazzs) {
            Devices.add((Device) clazz.newInstance());
        }
        //打印Class对象
        for (Device d : Devices) {
//            System.out.println("实现类:" + cla.getClass());

        }

        child1 child1=new child1();
        child2 child2=new child2();

        child1.start();
        child1.resume();
        child1.close();
        child2.start();
        child2.resume();
        //假设在另外一个函数中
        child2.close();


        Father father=new Father();
        father.start();
        father.resume();
        father.close();

    }

    @Test
    public void testReflect() throws IllegalAccessException, InstantiationException {

        List<Device> Devices = new ArrayList<>();
        List<Class> clazzs = getAllInterfaceAchieveClass(Device.class);
        for (Class clazz : clazzs) {
            Devices.add((Device) clazz.newInstance());
        }
        //打印Class对象
        for (Device cla : Devices) {
            System.out.println("实现类:" + cla.getClass());
        }
        System.out.println(Devices);

    }

    /**
     * 获取所有接口的实现类
     * 这功能看起来还是很复杂的
     *
     * @return
     */
    public static List<Class> getAllInterfaceAchieveClass(Class clazz) {
        ArrayList<Class> list = new ArrayList<>();
        //判断是否是接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class> allClass = getAllClassByPath(clazz.getPackage().getName());
                /**
                 * 循环判断路径下的所有类是否实现了指定的接口
                 * 并且排除接口类自己
                 */
                for (int i = 0; i < allClass.size(); i++) {

                    //排除抽象类
                    if (Modifier.isAbstract(allClass.get(i).getModifiers())) {
                        continue;
                    }
                    //判断是不是同一个接口
                    if (clazz.isAssignableFrom(allClass.get(i))) {
                        if (!clazz.equals(allClass.get(i))) {
                            list.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("出现异常");
            }
        }
        return list;
    }


    /**
     * 从指定路径下获取所有类
     *
     * @return
     */
    public static ArrayList<Class> getAllClassByPath(String packagename) {
        ArrayList<Class> list = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        String path = packagename.replace('.', '/');
        try {
            ArrayList<File> fileList = new ArrayList<>();
            Enumeration<URL> enumeration = classLoader.getResources(path);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                fileList.add(new File(url.getFile()));
            }
            for (int i = 0; i < fileList.size(); i++) {
                list.addAll(findClass(fileList.get(i), packagename));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 如果file是文件夹，则递归调用findClass方法，或者文件夹下的类
     * 如果file本身是类文件，则加入list中进行保存，并返回
     *
     * @param file
     * @param packagename
     * @return
     */
    private static ArrayList<Class> findClass(File file, String packagename) {
        ArrayList<Class> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        File[] files = file.listFiles();
        for (File file2 : files) {
            if (file2.isDirectory()) {
                assert !file2.getName().contains(".");//添加断言用于判断
                ArrayList<Class> arrayList = findClass(file2, packagename + "." + file2.getName());
                list.addAll(arrayList);
            } else if (file2.getName().endsWith(".class")) {
                try {
                    //保存的类文件不需要后缀.class
                    list.add(Class.forName(packagename + '.' + file2.getName().substring(0,
                            file2.getName().length() - 6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }


/**
 * 不用框架实现接口反射全部实现类
 *
 */


}

interface Device {
    public void start();

    public void resume();

    public void close();
}

class Father implements Device {

    @Override
    public void start() {
        System.out.println("father start");

    }

    @Override
    public void resume() {
        System.out.println("father resume");

    }

    @Override
    public void close() {
        System.out.println("father close");


    }
}





class child1 extends Father {

    @Override
    public void start() {
        super.start();
        System.out.println("子类瞎捷豹start");
    }

    @Override
    public void resume() {
        super.resume();
        System.out.println("子类瞎捷豹resume");

    }

    @Override
    public void close() {
        super.close();
        System.out.println("子类瞎捷豹close");

    }
}
class child2 extends Father {
    @Override
    public void start() {
        super.start();
        System.out.println("子类瞎捷豹start");
    }

    @Override
    public void resume() {
        super.resume();
        System.out.println("子类瞎捷豹resume");

    }

    @Override
    public void close() {
        super.close();
        System.out.println("子类瞎捷豹close");

    }

}
class child3 extends Father {
    @Override
    public void start() {
        super.start();
        System.out.println("子类瞎捷豹start");
    }

    @Override
    public void resume() {
        super.resume();
        System.out.println("子类瞎捷豹resume");

    }

    @Override
    public void close() {
        super.close();
        System.out.println("子类瞎捷豹close");

    }

}