package reflection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.util.Properties;

public class TestReflection {
    public static void main(String[] args) {
        Properties properties = new Properties();
        FileInputStream fis;
        try {
            fis = new FileInputStream("src/main/java/reflection/service.properties");
            properties.load(fis);
            String className = properties.getProperty("clazz");
            String classMethod = properties.getProperty("method");

            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getConstructor();
            Object service = constructor.newInstance();

//            Method m = clazz.getMethod(classMethod, new Class[]{int.class, long.class});
            Method m = clazz.getMethod(classMethod, int.class, long.class);
            m.invoke(service, 4, 5l);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("can't load fis");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("好不到类");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.out.println("好不到方法");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
