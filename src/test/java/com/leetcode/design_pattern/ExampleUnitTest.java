package com.leetcode.design_pattern;

import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSingletonOne() {
        SingletonOne singletonOne1 = SingletonOne.getSingletonOne();
        singletonOne1.setName("1");
        SingletonOne singletonOne2 = SingletonOne.getSingletonOne();
        singletonOne2.setName("2");
//测单例
//        System.out.println(singletonOne1.getName() + "  " + singletonOne2.getName());
        assertEquals(true, singletonOne1 == singletonOne2);
        //测反射
        //singletonone类型反射数组 这里没有用到
        SingletonOne[] singletonOneArr = (SingletonOne[]) Array.newInstance(SingletonOne.class, 2);

        try {
            //反射得到类,forname相比于classload直接进行了静态链接步骤,classlaod还需要多进行一次true确认
            Class clazz = Class.forName("com.leetcode.design_pattern.ExampleUnitTest");
            //通过反射类反射拿到方法,然后进行调用.可能出现找不到方法异常和找到方法无法调用异常,不能实例化异常.
            //invoke中的参数是object类型.  class类型实例化后为object类型.
            clazz.getDeclaredMethod("addition_isCorrect").invoke(clazz.newInstance());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
//分别是appclassloader extclassloader bootclassloader(null)
        ClassLoader classLoaderGrandSon = ExampleUnitTest.class.getClassLoader();
        ClassLoader classLoaderSon = classLoaderGrandSon.getParent();
        ClassLoader classLoaderParent = classLoaderSon.getParent();

        try {
            Class clazz2 = classLoaderGrandSon.loadClass("com.leetcode.design_pattern.ExampleUnitTest");
            ExampleUnitTest o1 = (ExampleUnitTest) clazz2.newInstance();
            ExampleUnitTest o2 = o1.getClass().newInstance();
//            System.out.println("显然o1 o2不是同一个对象    " + o1 + "  " + o2);


//            ClassLoader singleOneGrandSon = SingletonOne.class.getClassLoader();
//            Class clazz3 = singleOneGrandSon.loadClass("com.example.servicebestpractice.SingletonOne");
            /**
             * 上面两句和forname等价
             */
            Class clazz3 = Class.forName("com.leetcode.design_pattern.SingletonOne");
            /**
             Object o3_1 = clazz3.newInstance();单例模式无效
             面对private必须用constructor和setaccessible
             *
             */
            Constructor cs = clazz3.getDeclaredConstructor();
            cs.setAccessible(true);
            Object o3_1 = cs.newInstance();
            Constructor cs2 = o3_1.getClass().getDeclaredConstructor();
            cs2.setAccessible(true);
            Object o3_2 = cs2.newInstance();
//            System.out.println("显然o3_1 o3_2不是同一个对象    " + o3_1 + "  " + o3_2);


/**
 * 体会枚举类单例的好例子
 */

//            Class clazz4 = Class.forName("com.example.servicebestpractice.SingleTonThree");
//            Constructor cs3 = clazz4.getDeclaredConstructor();
//            cs3.setAccessible(true);
//            Object o4_1 = cs.newInstance();
//            Constructor cs4 = o3_1.getClass().getDeclaredConstructor();
//            cs4.setAccessible(true);
//            Object o4_2 = cs2.newInstance();
//            System.out.println("显然o4_1 o4_2是同一个对象    " + o4_1 + "  " + o4_2);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("会异常因为这是枚举类根本木有构造器,如果throw exception则try后面也不会执行");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


//        System.out.println(classLoaderGrandSon + "    " + classLoaderSon + "  " + classLoaderParent);
        assertEquals(false, classLoaderGrandSon == classLoaderSon);
//        System.out.println("xxx");

    }

    @Test
    public void testSingletonTwo() {
        SingletonTwo singletonTwo1 = SingletonTwo.getInstance();
        SingletonTwo singletonTwo2 = SingletonTwo.getInstance();
        singletonTwo1.setName("1");
        singletonTwo2.setName("2");
        assertEquals(true, singletonTwo1 == singletonTwo2);


    }

    @Test
    public void testSingletonThree() {
        SingleTonThree singleTonThree1 = SingleTonThree.INSTANCE;
        SingleTonThree singleTonThree2 = SingleTonThree.INSTANCE;
        singleTonThree1.setName("1");
        singleTonThree2.setName("2");
        assertEquals(true, singleTonThree1.getName() == singleTonThree2.getName());
        SingleTonThree[] singleTonThreesArr = SingleTonThree.class.getEnumConstants();
        for (SingleTonThree k : singleTonThreesArr) {
//            System.out.println(k);
        }
    }

    @Test
    public void testAbstractFactory() {
        AbstractFactory abstractFactory1 = new ConcreteFactory1();
        AbstractFactory abstractFactory2 = new ConcreteFactory2();

        ProductA productA1WeNeed = abstractFactory1.createProductA();
        ProductA productA2WeNeed = abstractFactory2.createProductA();

        ProductB productB1WeNeed = abstractFactory1.createProductB();
        ProductB productB2WeNeed = abstractFactory2.createProductB();

    }

    @Test
    public void testStringBuilder() {
    }

    @Test
    public void testPrototype() {
        concretePrototype prototype1 = new concretePrototype("abc");
        concretePrototype prototype2 = (concretePrototype) prototype1.myClone();
        prototype2.filed = "123";
//        System.out.println(prototype1 + "     " + prototype2);
    }

    @Test
    public void testChainOfResponsibility() {
    }

    @Test
    public void testCommand() {
        Light light = new Light();
        Command commandOn = new LightOnCommand(light);
        Command commandOff = new LightOffCommand(light);
        Invoker invoker = new Invoker();
        invoker.setOnCommands(commandOn, 0);
        invoker.setOnCommands(commandOn, 1);
        invoker.setOnCommands(commandOff, 2);
        invoker.setOffCommands(commandOn, 0);
        invoker.setOffCommands(commandOn, 1);
        invoker.setOffCommands(commandOff, 2);

//        invoker.onButtonWasPushed(1);
//        invoker.offButtonWasPushed(1);

    }

    @Test
    public void testStatic() {
        Outer outer = new Outer();
        //测试内部类,静态内部类多些限制条件,外可以访问内,,变量跟随类生命周期,
        Outer.Inner1 inner1 = new Outer.Inner1();
        Outer.Inner2 inner2 = new Outer().new Inner2();
//        inner1.runInner1();
//        inner2.runInner2();
    }


    @Test
    public void testPriorityQueue() {
        PriorityQueue<Recognition> priorityQueue = new PriorityQueue<Recognition>(new Comparator<Recognition>() {
            @Override
            public int compare(Recognition o1, Recognition o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        Recognition recognition1 = new Recognition("1", "b1", Float.valueOf("1"), true);
        Recognition recognition2 = new Recognition("2", "a2", Float.valueOf("2"), true);
        Recognition recognition3 = new Recognition("3", "a3", Float.valueOf("3"), true);
//        System.out.println(priorityQueue.comparator().compare(recognition2, recognition1));
        priorityQueue.add(recognition2);
        priorityQueue.add(recognition2);
        priorityQueue.add(recognition1);
        priorityQueue.add(recognition3);
//        System.out.println(priorityQueue);
//        while (!priorityQueue.isEmpty()) {
//            System.out.print(priorityQueue.poll().toString());
//        }
    }


    @Test
    public void testInterpreter() {
        //构建解析树
        // Literal
        Expression terminal1 = new TerminalExpression("A");
        Expression terminal2 = new TerminalExpression("B");
        Expression terminal3 = new TerminalExpression("C");
        Expression terminal4 = new TerminalExpression("D");
        // B or C
        Expression alternation1 = new OrExpression(terminal2, terminal3);
        // A Or (B C)
        Expression alternation2 = new OrExpression(terminal1, alternation1);
        // D And (A Or (B C))
        Expression alternation3 = new AndExpression(terminal4, alternation2);


        //进行判断
        String context1 = "A D";
        String context2 = "A B";
        String context3 = "B C";

        String context4 = "D B";
        String context5 = "D C";


        System.out.println(alternation3.interpret(context1));
        System.out.println(alternation3.interpret(context2));
        System.out.println(alternation3.interpret(context3));

        System.out.println(alternation3.interpret(context4));
        System.out.println(alternation3.interpret(context5));

        StringTokenizer stringTokenizer = new StringTokenizer(context1);

        while (stringTokenizer.hasMoreTokens()) {
            String temp = stringTokenizer.nextToken();
            if (temp.equals(null))
                System.out.println(temp);
        }
    }

    @Test
    public void testIterator() {
        //这里存储元素是int,如果有其他类型元素也可以。
        Aggregate concreteAggregate = new ConcreteAggregate();
        Iterator iterator = concreteAggregate.createIterator();
        while (iterator.hasNext())
            System.out.println(iterator.next());
    }

    @Test
    public void testMediator() {
        Alarm alarm = new Alarm();
        CoffeePot coffeePot = new CoffeePot();
        Calender calender = new Calender();
        Sprinkler sprinkler = new Sprinkler();

        Mediator mediator = new ConcreteMediator(alarm, coffeePot, calender, sprinkler);

//        alarm.doAlarm();
//        alarm.onEvent(mediator);
        //上面的做法不是足够的
        Colleague colleague = new Alarm();
        colleague.onEvent(mediator);

        colleague = new Sprinkler();
        colleague.onEvent(mediator);

    }


    @Test
    public void testMemento() {
        Calculator calculator = new CalculatorImp();
        PreviousCalculationToCareTaker memento;

        //保存memento
        memento = calculator.backupLastCalculation();
        //回复memento
        calculator.restorePreviousCalculation(memento);
        System.out.println("result memo :" + calculator.getCalculationResult());

        calculator.setFirstNumber(10);
        calculator.setSecondNumber(100);
        System.out.println("result :" + calculator.getCalculationResult());

        //保存memento
        memento = calculator.backupLastCalculation();
        //存储memento


        calculator.setSecondNumber(-1);
        calculator.setFirstNumber(-10);
        System.out.println("result :" + calculator.getCalculationResult());
        calculator.restorePreviousCalculation(memento);
        System.out.println("result memo :" + calculator.getCalculationResult());


    }
}


/**
 * hjl
 * 单例模式1
 */

class Recognition {
    private final String id;
    private final String title;
    private final boolean quant;
    private final Float confidence;

    public Recognition(
            final String id, final String title, final Float confidence, final boolean quant) {
        this.id = id;
        this.title = title;
        this.confidence = confidence;
        this.quant = quant;

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Float getConfidence() {
        return confidence;
    }

    @Override
    public String toString() {
        String resultString = "";
        if (id != null) {
            resultString += "[" + id + "] ";
        }

        if (title != null) {
            resultString += title + " ";
        }

        if (confidence != null) {
            resultString += String.format("(%.1f%%) ", confidence * 100.0f);
        }

        return resultString.trim();
    }
}


class SingletonOne {
    private static volatile SingletonOne singletonOne;

    public static void setSingletonOne(SingletonOne singletonOne) {
        SingletonOne.singletonOne = singletonOne;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private SingletonOne() {
    }

    //方便用静态方式调用
    public static SingletonOne getSingletonOne() {
        //双重加锁法
        if (singletonOne == null) {
            synchronized (SingletonOne.class) {
                if (singletonOne == null) {
                    singletonOne = new SingletonOne();
                }
            }

        }
        return singletonOne;
    }

}

/**
 * 单例模式2
 */
class SingletonTwo {
    private SingletonTwo() {
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //静态内部类法
    private static class SingletonHolder {
        private static SingletonTwo INSTANCE = new SingletonTwo();
    }

    //方便用静态方式调用
    public static SingletonTwo getInstance() {
        return SingletonHolder.INSTANCE;
    }

}

/**
 * 单例模式3
 * 序列化后仍然保证单例,其他必须用transient字段才行
 */
enum SingleTonThree {
    INSTANCE;
    private String name = "什么都没有";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

/**
 * 抽象工厂
 * xpath.xpathfactory ioc util.calendar
 */
abstract class ProductA {
}

abstract class ProductB {
}

class ProductA1 extends ProductA {
}

class ProductA2 extends ProductA {
}

class ProductB1 extends ProductB {
}

class ProductB2 extends ProductB {
}

abstract class AbstractFactory {
    abstract ProductA createProductA();

    abstract ProductB createProductB();
}

class ConcreteFactory1 extends AbstractFactory {
    //A1 B1为一家工厂生产 a2 b2为另一家工厂生产
    @Override
    ProductA createProductA() {
        return new ProductA1();
    }

    @Override
    ProductB createProductB() {
        return new ProductB1();
    }
}

class ConcreteFactory2 extends AbstractFactory {

    @Override
    ProductA createProductA() {
        return new ProductA2();
    }

    @Override
    ProductB createProductB() {
        return new ProductB2();
    }
}
/**
 * 建造者 核心是return this
 * nio.bytebuffer lang.stringbuild lang.stringbuffer
 */

/**
 * 原型模式 多例模式
 * lang.object.clone
 */
abstract class Prototype {

    abstract Prototype myClone();
}

class concretePrototype extends Prototype {
    public String filed;

    public concretePrototype(String filed) {
        this.filed = filed;
    }

    @Override
    Prototype myClone() {
        return new concretePrototype(filed);
    }

    @Override
    public String toString() {
        return "concretePrototype{" +
                "filed='" + filed + '\'' +
                '}';
    }
}

/**
 * 责任链模式  父类变量protected可以被继承      * 未完成
 * servlet.filter
 */
abstract class Handler {
    private Handler successor;

    public Handler(Handler successor) {
        this.successor = successor;
    }

    public abstract void handleRequest(Request request);
}

class Request {
    private RequestType type;
    private String name;

    public Request(RequestType type, String name) {
        this.type = type;
        this.name = name;
    }

    public RequestType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}

enum RequestType {
    TYPE1, TYPE2;
}

class ConcreteHandler1 extends Handler {

    public ConcreteHandler1(Handler successor) {
        super(successor);
    }

    @Override
    public void handleRequest(Request request) {
        if (request.getType().equals(RequestType.TYPE1))
            System.out.println("this is type1");

    }
}
/**
 *
 */

/**
 * 命令模式
 * swing.Action hystrix lang.runnable
 */
interface Command {
    void execute();
}

class LightOnCommand implements Command {
    //开灯命令
    Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}

class LightOffCommand implements Command {
    Light light;

    @Override
    public void execute() {
        light.turnOff();
    }

    public LightOffCommand(Light light) {
        this.light = light;
    }
}

class Light {
    public void turnOn() {
        System.out.println("turnOn");
    }

    public void turnOff() {
        System.out.println("turnOff");
    }
}

class Invoker {
    //遥控
    private Command[] onCommands;
    private Command[] offCommands;
    //数组大小,但放这里有什么用呢?
    private final int soltNum = 7;

    //传入两个参数完成命令存储
    public void setOnCommands(Command onCommands, int soltNum) {
        this.onCommands[soltNum] = onCommands;
    }

    public void setOffCommands(Command offCommands, int soltNum) {
        this.offCommands[soltNum] = offCommands;
    }

    public Invoker() {
        //初始化两个命令数组
        this.onCommands = new Command[soltNum];
        this.offCommands = new Command[soltNum];
    }

    //具体命令动作调用对应命令数组的执行
    public void onButtonWasPushed(int soltNum) {
        onCommands[soltNum].execute();
    }

    public void offButtonWasPushed(int soltNum) {
        offCommands[soltNum].execute();
    }
}


/**
 * 备忘录模式 和命令模式结合可变为可撤销命令的功能
 * java.io.Serializable
 */


/**
 * 解释器模式
 * java.util.Pattern
 * java.text.Normalizer
 * All subclasses of java.text.Format
 * javax.el.ELResolver
 * <p>
 * 以下是一个规则检验器实现，具有 and 和 or 规则，通过规则可以构建一颗解析树，用来检验一个文本是否满足解析树定义的规则。
 * <p>
 * 例如一颗解析树为 D And (A Or (B C))，文本 "D A" 满足该解析树定义的规则。
 * <p>
 * 不知道这个例子搞什么杰宝。
 */
abstract class Expression {
    public abstract boolean interpret(String str);

}

class TerminalExpression extends Expression {
    private String literal = null;

    public TerminalExpression(String literal) {
        this.literal = literal;
    }

    @Override
    public boolean interpret(String str) {
        //终止符都能判断吗？从未见过
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            String test = st.nextToken();
            if (literal.equals(test)) {
                System.out.println("    " + test);
                return true;
            }
        }
        return false;
    }
}

class AndExpression extends Expression {
    private Expression expression1;
    private Expression expression2;

    public AndExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public boolean interpret(String str) {
        return expression1.interpret(str) && expression2.interpret(str);
    }
}

class OrExpression extends Expression {
    private Expression expression1;
    private Expression expression2;

    public OrExpression(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public boolean interpret(String str) {
        return expression1.interpret(str) || expression2.interpret(str);
    }
}

/**
 * Iterator
 * java.util.Iterator
 * java.util.Enumeration
 * <p>
 * 提供一种顺序访问聚合对象元素的方法，并且不暴露聚合对象的内部表示。
 */
interface Aggregate<T> {
    Iterator createIterator();
}

class ConcreteAggregate implements Aggregate {
    private Integer[] items;

    public ConcreteAggregate(Integer[] items) {
        this.items = items;
    }

    public ConcreteAggregate() {
        //预先保存10个元素
//        items = new Integer[10];
        items = new Integer[10];

        for (int i = 0; i < items.length; i++) {
            items[i] = i;
        }
    }


    @Override
    public Iterator createIterator() {
        return new ConcreteIterator(items);
    }
}


interface Iterator<T> {
    T next();

    boolean hasNext();

}

class ConcreteIterator<T> implements Iterator {
    private T[] items;
    private int position = 0;

    public ConcreteIterator(T[] items, int position) {
        this.items = items;
        this.position = position;
    }

    public ConcreteIterator(T[] items) {
        this.items = items;
    }

    @Override
    public Object next() {
        //返回元素并移至下一个
        return items[position++];
    }

    @Override
    public boolean hasNext() {
        return position < items.length;
    }


}

/**
 * Mediator中介者模式
 * All scheduleXXX() methods of java.util.Timer
 * java.util.concurrent.Executor#execute()
 * submit() and invokeXXX() methods of java.util.concurrent.ExecutorService
 * scheduleXXX() methods of java.util.concurrent.ScheduledExecutorService
 * java.lang.reflect.Method#invoke()
 */

abstract class Mediator {
    public abstract void doEvent(String eventType);
}


abstract class Colleague {
    public abstract void onEvent(Mediator mediator);
}

class Alarm extends Colleague {

    @Override
    public void onEvent(Mediator mediator) {
        //相当于至传递一个message
        mediator.doEvent("Alarm");
    }

    public void doAlarm() {
        System.out.println("doAlarm!");
    }
}

class CoffeePot extends Colleague {

    @Override
    public void onEvent(Mediator mediator) {
        mediator.doEvent("CoffeePot");

    }

    public void doCoffeePot() {
        System.out.println("doCoffeePot!");
    }
}

class Calender extends Colleague {

    @Override
    public void onEvent(Mediator mediator) {
        mediator.doEvent("Calender");

    }

    public void doCalender() {
        System.out.println("doCalender!");
    }
}

class Sprinkler extends Colleague {

    @Override
    public void onEvent(Mediator mediator) {
        mediator.doEvent("Sprinkler");

    }

    public void doSprinkler() {
        System.out.println("doSprinkler!");
    }
}

class ConcreteMediator extends Mediator {
    private Alarm alarm;
    private CoffeePot coffeePot;
    private Calender calender;
    private Sprinkler sprinkler;

    public ConcreteMediator(Alarm alarm, CoffeePot coffeePot, Calender calender, Sprinkler sprinkler) {
        this.alarm = alarm;
        this.coffeePot = coffeePot;
        this.calender = calender;
        this.sprinkler = sprinkler;
    }

    @Override
    public void doEvent(String eventType) {

        switch (eventType) {
            case "Alarm":
                alarm.doAlarm();
                break;
            case "CoffeePot":
                coffeePot.doCoffeePot();
                break;
            case "Calender":
                calender.doCalender();
                break;
            case "Sprinkler":
                sprinkler.doSprinkler();
                break;
        }
    }
}

/**
 * memento备忘录模式
 * 带存储功能计算器
 * java.io.Serializable
 */

/**
 * Memento Interface to Originator
 * <p>
 * This interface allows the originator to restore its state
 */
interface PreviousCalculationToOriginator {
    int getFirstNumber();

    int getSecondNumber();
}

/**
 * Memento interface to CalculatorOperator (Caretaker)
 */
interface PreviousCalculationToCareTaker {
    // no operations permitted for the caretaker
}

class PreviousCalculationImp implements PreviousCalculationToOriginator, PreviousCalculationToCareTaker {
    private int firstNumber;
    private int secondNumber;

    public PreviousCalculationImp(int firstNumber, int secondNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
    }

    @Override
    public int getFirstNumber() {
        return firstNumber;
    }

    @Override
    public int getSecondNumber() {
        return secondNumber;
    }
}

interface Calculator {
    // Create Memento
    PreviousCalculationToCareTaker backupLastCalculation();

    // setMemento
    void restorePreviousCalculation(PreviousCalculationToCareTaker memento);

    int getCalculationResult();

    void setFirstNumber(int firstNumber);

    void setSecondNumber(int secondNumber);

}

class CalculatorImp implements Calculator {
    private int firstNumber;
    private int secondNumber;

    @Override
    public PreviousCalculationToCareTaker backupLastCalculation() {
        return new PreviousCalculationImp(firstNumber, secondNumber);
    }

    @Override
    public void restorePreviousCalculation(PreviousCalculationToCareTaker memento) {
        //我擦，还有这种强转方式？不如一开始写成PreviousCalculationToOriginator类，搞这么啰嗦
        this.firstNumber = ((PreviousCalculationToOriginator) memento).getFirstNumber();
        this.secondNumber = ((PreviousCalculationToOriginator) memento).getSecondNumber();

    }

    @Override
    public int getCalculationResult() {
        return firstNumber + secondNumber;
    }

    @Override
    public void setFirstNumber(int firstNumber) {
        this.firstNumber = firstNumber;

    }

    @Override
    public void setSecondNumber(int secondNumber) {
        this.secondNumber = secondNumber;
    }
}

/**
 * 观察者（Observer）
 * java.util.Observer
 * java.util.EventListener
 * javax.servlet.http.HttpSessionBindingListener
 * RxJava
 *
 */

interface Subject{
    public void registerObserve();
    public void removeObserve();
    public void notifyObervers();
}
//class ConcreteSubject implements Subject{
//    private List<Observer> observers;
//    private
//
//    @Override
//    public void registerObserve() {
//
//    }
//
//    @Override
//    public void removeObserve() {
//
//    }
//
//    @Override
//    public void notifyObervers() {
//
//    }
//}

interface Observer{
public void update();

}
class ConcreteObserver implements Observer{


    @Override
    public void update() {

    }
}


/***
 * hjl
 */

class Outer {

    static int num = 999;

    void runo() {
        System.out.println(num);
    }

    static class Inner1 {
        int num1 = 101;

        void runInner1() {
            //普通,静态内部类和js闭包用法类似,但其实有差别,
            // 差别在于js中,外封闭域中没有像Java中还要区别变量存在于实例变量(包含类变量),还是局部变量.
            // 如果是实例变量则和js闭包一样,外封闭域和内封闭域共享共享同一个变量,在堆上.如果是局部变量,则外封闭域和内封闭域不共享共享同一个变量.
            //即外封闭域的方法中的局部变量和内封闭域匿名内部类中的局部变量在不同的栈中.
            //如果内外封闭域中变量不一致则违反闭包原则,因此设为final不可变,使得内外封闭域变量一致
            num1 = Outer.num;
            System.out.println(num1 + "   ");

        }

    }

    class Inner2 {
        int inner2 = 201;
        Dog dog = new Dog();

        void runInner2() {
            //好玩的事情,变量放在方法外不需要final和方法内需要final
            //但利用数组仍然可以改变对象
            final int[] inner2Local = {2001};
            final Dog dogLocal = new Dog();
            final Dog[] dogLocalArr = {new Dog(), new Dog()};

            System.out.println(inner2);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    System.out.println(inner2 + " " + inner2Local[0] + "  " + inner2Local
                            + "  " + dog.name + " " + dogLocal.name + "  " + dogLocal + "   " + dogLocalArr[0]);
                    inner2++;
                    inner2Local[0]++;
                    dog.name = "cat";
                    dogLocal.name = "cat";
                    dogLocalArr[0] = new Dog();

                }
            }).start();

            try {
                //最好玩的事情出现了,有延时和没有延时完全是不同的结果,inner2被改变需要一定的时间
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(inner2 + " " + inner2Local[0] + "  " + inner2Local
                    + "  " + dog.name + " " + dogLocal.name + "  " + dogLocal + "   " + dogLocalArr[0]
                    + "开启thread后    ");
        }
    }

    class Dog {
        public String name = "dog";
    }
}

