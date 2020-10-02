package com.company.annotation1;
import java.lang.annotation.*;
import java.lang.reflect.Method;

//1. Создать аннотацию, которая принимает параметры для метода. Написать код, который вызовет метод, помеченный этой аннотацией, и передаст параметры аннотации в вызываемый метод.
//@Test(a=2, b=5)
//public void test(int a, int b) {...}
     interface Test {
        int a();
        int b();
    }

    class Sum {
        @Test(a = 3, b = 5)
        public static void test (int a, int b) {
            System.out.println("a + b = " + (a + b));
        }
    }
public class Task1 {
         public static void main (String[] args) {
try {
    Class<?> cls = Sum.class;
    Method method = cls.getMethod("test", int.class, int.class);
    if (method.isAnnotationPresent((Class<? extends Annotation>) Test.class)) {

        Test mad = (Test) method.getAnnotation(Test.class);
        method.invoke(null, mad.a(), mad.b());

    }
} catch (Exception e) {
    System.out.println(e.toString());
        }
    }
}
