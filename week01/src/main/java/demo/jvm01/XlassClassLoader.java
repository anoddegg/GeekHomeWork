package demo.jvm01;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 2.（必做）自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。
 */
public class XlassClassLoader extends ClassLoader {

    private static String className = "Hello";
    private static String methodName = "hello";

    public static void main(String[] args) {


        try {
            //加载类
            Class<?> clazz = new XlassClassLoader().findClass(className);
            Object o = clazz.newInstance();

            //调用hello方法
            Method method = clazz.getMethod(methodName);
            method.invoke(o);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = null;
        try {
            bytes = getBytesFromFile("src/main/resources/"+methodName+".xlass");
        } catch (IOException e) {
            e.printStackTrace();
        }
        decode(bytes);

        return defineClass(name, bytes, 0, bytes.length);
    }

    /**
     * x = 255-x
     *
     * @param bytes
     */
    private void decode(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
    }


    /**
     * 读取文件, 返回字节数组
     *
     * @param filePath 文件路径
     * @return 文件的字节数组
     * @throws IOException
     */
    public static byte[] getBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException(filePath);
        }
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            return null;
        }

        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        byte[] bytes;
        try {
            // 利用FileInputStream读取文件
            fis = new FileInputStream(file);
            // 利用ByteArrayOutputStream将FileInputStream中的文件数据读出来
            bos = new ByteArrayOutputStream((int) fileSize);

            byte[] b = new byte[1024];
            int len;
            while ((len = fis.read(b)) != -1) {
                bos.write(b, 0, len);
            }

            bytes = bos.toByteArray();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }

        return bytes;
    }
}
