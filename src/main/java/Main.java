import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        start(MainTest.class);
    }

    public static void start(Class c) {
        Map<Method, Integer> unsortMap = getMethodIntegerMap(c);
        LinkedHashMap<Method, Integer> sortedMap = sortByValueJava8Stream(unsortMap);
        runMethods(c, sortedMap);
    }

    /**
     * Получаем неотсортированный хэш-мап,
     * где ключ - это метод, а значение - приоритет
     * @param c класс, где обрабатываются аннотации
     * @return хэш-мап с методами и их приоритетами
     */
    private static Map<Method, Integer> getMethodIntegerMap(Class c) {
        int countBefor = 0;
        int countAfter = 0;
        Method[] declaredMethods = c.getDeclaredMethods();
        // sort by value
        Map<Method, Integer> unsortMap = new HashMap<>();

        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(BeforeSuite.class)) {
                countBefor++;
                if (countBefor > 1) {
                    throw new RuntimeException("countBefor >1");
                }
                unsortMap.put(declaredMethod, 0);
            }
            if (declaredMethod.isAnnotationPresent(Test.class)) {
                Test annotation = declaredMethod.getAnnotation(Test.class);
                unsortMap.put(declaredMethod, annotation.priority());
            }
            if (declaredMethod.isAnnotationPresent(AfterSuite.class)) {
                countAfter++;
                if (countAfter > 1) {
                    throw new RuntimeException("countAfter >1");
                }
                unsortMap.put(declaredMethod, 11);
            }

        }
        return unsortMap;
    }

    private static void runMethods(Class c, LinkedHashMap<Method, Integer> sortedMap) {
        for (Map.Entry<Method, Integer> entry : sortedMap.entrySet()) {
            Method method = entry.getKey();
            Integer value = entry.getValue();
            try {
                method.invoke(c.newInstance());
                System.out.println("key = " + method + " value = " + value);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private static LinkedHashMap<Method, Integer> sortByValueJava8Stream(Map<Method, Integer> unSortedMap) {
        LinkedHashMap<Method, Integer> sortedMap = new LinkedHashMap<>();
        unSortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
        return sortedMap;
    }
}
