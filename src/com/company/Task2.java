package com.company;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.FileWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//2. Написать класс TextContainer, который содержит в себе строку. С помощью механизма аннотаций указать
//1) в какой файл должен сохраниться текст 2) метод, который выполнит сохранение. Написать класс Saver,
//который сохранит поле класса TextContainer в указанный файл.

//@SaveTo(path=“c:\\file.txt”)
//class Container {
//    String text = “...”;
//    @Saver
//    public void save(..) {...}
//}


public class Task2 {
    public static <TextContainer> void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    TextContainer container = new TextContainer();
    Class<?> cls = container.getClass();

    SaveTo annSaveTo = cls.getAnnotation(SaveTo.class);

    Method[] methods = cls.getDeclaredMethods();
    for (Method method : methods) {
        if (method.isAnnotationPresent(Saver.class)) {
            method.invoke(container,annSaveTo.path());
            }
        }
    }


@Retention(value = RetentionPolicy.RUNTIME)
@interface SaveTo {
    String path();
}

@Retention(value = RetentionPolicy.RUNTIME)
@interface Saver {
}

@SaveTo(path = "d:\\file.txt")
public static final String TEXT = "Some text ...";

@Saver
public static void save (String path) {
    try (FileWriter writer = new FileWriter(path)) {
        writer.write(TEXT);
        System.out.println("All save ");
    } catch (IOException ex) {
        System.out.println("ERROR " + ex.getMessage());
            }
        }
    }
