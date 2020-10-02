package com.company;
import java.io.InvalidObjectException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.io.*;
import java.io.Serializable;


//3. Написать код, который сериализирует и десериализирует в/из файла все значения полей класса, которые
//        отмечены аннотацией @Save.
// сначяла постарайтесь решить сами, еслы не выйдет - решение по задаче выложено, скопируйте и разберытесь

//            TestConteiner testConteiner = new TestConteiner();
//            Class<?> classClass = testConteiner.getClass();
//
//            if (!classClass.isAnnotationPresent(SaveTo.class)) {
//                System.out.println("Class is not annotated");
//            } else {
//                Method[] methods = classClass.getMethods();
//                for (Method method : methods) {
//                    if (method.isAnnotationPresent(Saver.class)) {
//                        SaveTo saveTo = classClass.getAnnotation(SaveTo.class);
//                        method.invoke(testConteiner, testConteiner.text, saveTo.PATH());
//                    } else {
//                        System.out.println("method is not annotated");
//                    }
//                }
//            }
//        }
//
//
//---------------------------
//
//    @Inherited
//    @Retention(value = RetentionPolicy.RUNTIME)
//    public @interface SaveTo {
//        String PATH(); // = "/home/roman/ROMA/JAVA/ProgKievUa/HW1 Refl 13.10/Task2/file.txt";
//    }
//-----------------
//
//    @Target(value = ElementType.METHOD)
//    @Retention(value = RetentionPolicy.RUNTIME)
//    public @interface Saver {
//    }
//----------------
//
//    @SaveTo(PATH = "/home/roman/ROMA/JAVA................................./file.txt")
//    public class TestConteiner {
//        String text = "text from textContainer";
//
//        @Saver
//        public void save(String text1, String path) throws IOException {
//            FileWriter w = new FileWriter(path);
//            try {
//                w.write(text1);
//            } finally {
//                w.close();
//            }
//        }
//    }
//}

public class Task3 {
    public static void main(String[] args) throws IllegalAccessException {
        final Class<?> cls = Test.class;
        Test test = new Test();
        StringBuilder sb = new StringBuilder();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Save.class)) {
                if (Modifier.isPrivate(field.getModifiers())) {
                    field.setAccessible(true);
                    sb.append("private ");
                }
                if (Modifier.isPublic(field.getModifiers())) {
                    sb.append("public ");
                }

                if (!field.getType().equals(String.class)) {
                    sb.append(field.getType()).append(" ").append(field.getName()).append("= ");
                } else {
                    sb.append("String ").append(field.getName()).append("= ");
                }

                if (field.getType().equals(int.class)) {
                    sb.append(field.getInt(test));
                }
                if (field.getType().equals(long.class)) {
                    sb.append(field.getLong(test));
                }
                if (field.getType().equals(double.class)) {
                    sb.append(field.getLong(test));
                }
                if (field.getType().equals(String.class)) {
                    sb.append(field.get(test).toString());
                }
                sb.append("; \n");
            }
        }


        System.out.println(sb.toString());
        String file = "D:\\file.txt";
        Serializator sz = new Serializator();
        boolean b = sz.serealization(sb.file);

        StringBuilder deser = null;
        try {
            deser = sz.deserialization(file);
        } catch (InvalidObjectException e) {
            e.printStackTrace();
        }
        System.out.println("Deserialized file \n" + sb.toString());
    }

    @Retention(value = RetentionPolicy.RUNTIME)
    @Target(value = ElementType.FIELD)
    public @interface Save {
    }


    public static class Serializator {
        public boolean serialization(StringBuilder sb, String fileName) {
            boolean flag = false;
            File f = new File(fileName);
            ObjectOutputStream ostream = null;
            try {
                FileOutputStream fos = new FileOutputStream(f);
                if (fos != null) {
                    ostream = new ObjectOutputStream(fos);
                    ostream.writeObject(sb);
                    flag = true;
                }
            } catch (FileNotFoundException e) {
                System.err.println("Файл не может быть создан: " + e);
            } catch (NotSerializableException e) {
                System.err.println("Класс не поддерживает сериализацию " + e);
            } catch (IOException e) {
                System.err.println(e);
            } finally {
                try {
                    if (ostream != null) {
                        ostream.close();
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка закрытия потока");
                }
            }
            return flag;
        }

        public StringBuilder desearialization(String fileName) throws InvalidObjectException {
            File fr = new File(fileName);
            ObjectOutputStream istream = null;
            try {
                FileInputStream fis = new FileInputStream(fr);
                istream = new ObjectInputStream(fis);

                StringBuilder sb = (StringBuilder) istream.readObject();
                return sb;
            } catch (ClassNotFoundException ce) {
                System.err.println("Класс не существует: " + ce);
            } catch (FileNotFoundException e) {
                System.err.println("Файл для десерилазации не существует: " + e);
            } catch (InvalidClassException ioe) {
                System.err.println("Несовпадение версий классов: " + ioe);
            } catch (IOException ioe) {
                System.err.println("Общая I/O ошибка: " + ioe);
            } finally {
                try {
                    if (istream != null) {
                        istream.close();
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка закрытия потока: ");
                }
            }
            throw new InvalidObjectException("Объект не восстановлен");
        }
    }

    public static class Test implements Serializable {
        @Save
        public String text = "Какой-то текст";

        @Save
        public int a = 15;
        int b = 25;

        @Save
        private long c = 50000;

        @Save
        public double d = 1.234;
        char e = 'Q';

        public Test(int a, int b, long c, double d, char e, String text) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.text = text;
        }

        public Test() {

        }
    }
}









