import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClass {
    private int number;
    private String name = "default";
        public MyClass(int number, String name) {
        this.number = number;
        this.name = name;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void printData() {
        System.out.println(number + name);
    }

    public static void main(String[] args) {
        MyClass myClass = null;
        try {
            Class clazz = Class.forName(MyClass.class.getName());
            Constructor[] constructors = clazz.getConstructors();
            for (Constructor constructor : constructors) {
                Class[] paramTypes = constructor.getParameterTypes();
                for (Class paramType : paramTypes) {
                    System.out.print(paramType.getName() + " ");
                }
                System.out.println();
            }
            Class[] params = {int.class, String.class};
            myClass = (MyClass) clazz.getConstructor(params).newInstance(1, "default2");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(myClass);//output created object reflection.MyClass@60e53b93
//        constructorByDefault();
//        invokeMethod();
    }

    private static void constructorByDefault() {
        MyClass myClass = null;
        try {
            Class clazz = Class.forName(MyClass.class.getName());
            myClass = (MyClass) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(myClass);//output created object reflection.MyClass@60e53b93
    }

    private static void invokeMethod() {
        MyClass myClass = new MyClass(1,"test");
        int number = myClass.getNumber();
        String name = null; //?
        printData(myClass); // outout 0default
        try {
            Field field = myClass.getClass().getDeclaredField("name");
            field.setAccessible(true);
            field.set(myClass, "new value");
            name = (String) field.get(myClass);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        printData(myClass);// output 0new value}
    }

    public static void printData(Object myClass) {
        try {
            Method method = myClass.getClass().getDeclaredMethod("printData");
            method.setAccessible(true);
            method.invoke(myClass);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}