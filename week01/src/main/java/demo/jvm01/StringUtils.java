package demo.jvm01;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StringUtils {

    private static final char[] LOWERHEXES = "0123456789abcdef".toCharArray();
    // private static final char[] UPPERHEXES = "0123456789ABCDEF".toCharArray();

    /**
     * 检查字符串中圆括号是否配对, 即 "("和")"的数量是否相同
     *
     * @param s
     * @return
     */
    public static boolean isIntegralParentheses(String s) {
        String str1 = "(";
        String str2 = ")";

        int num1 = (s.length() - s.replace(str1, "").length()) / str1.length();
        int num2 = (s.length() - s.replace(str2, "").length()) / str2.length();

        return num1 == num2;
    }

    /**
     * 检查"("和")"之间是否对应
     *
     * @param s
     * @return
     */
    public static boolean isMatch(String s) {
        // 定义左右括号的对应关系
        Map<Character, Character> bracket = new HashMap<>();
        bracket.put(')', '(');
        // bracket.put(']','[');
        // bracket.put('}','{');
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            // 先转换成字符
            Character temp = s.charAt(i);
            // 是否为左括号
            if (bracket.containsValue(temp)) {
                stack.push(temp);
                // 是否为右括号
            } else if (bracket.containsKey(temp)) {
                if (stack.isEmpty()) {
                    return false;
                }
                // 若左右括号匹配
                if (stack.peek() == bracket.get(temp)) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 获取字节数组信息
     *
     * @param bytes 字节数组
     */
    public static String getBytesInfo(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        if (bytes == null) {
            return "Null";
        } else if (bytes.length == 0) {
            return "Empty Byte Array!";
        }

        //sb.append("\n字节数组长度: ").append(bytes.length);
        //sb.append("\n字节数组(可Copy): \n").append(Arrays.toString(bytes));
        //sb.append("\n字节数组(HexString): \n[");
        for (byte temp : bytes) {
            sb.append(String.format("%02x", temp));
        }
        //sb.append("]");

        return sb.toString();
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
            int len = -1;
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

//    /**
//     * 使用FastJson对象转换为JSON串
//     *
//     * @param obj
//     * @return
//     */
//    public static String toJSONString(Object obj) {
//        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
//    }

    /**
     * 字节数组转换为16进制小写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        StringBuilder hex = new StringBuilder();

        for (byte b : bytes) {
            hex.append(LOWERHEXES[(b >> 4) & 0x0F]);
            hex.append(LOWERHEXES[b & 0x0F]).append(" ");
        }

        return hex.toString();
    }

    /**
     * 16进制字符串转换为对应的字节数组
     */
    public static byte[] hexString2Bytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length / 2];   // 如果 hex 中的字符不是偶数个, 则忽略最后一个

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
        }

        return bytes;
    }

    /**
     * 整数转字节数组
     *
     * @param value ..
     * @return ..
     */
    public static byte[] int2Bytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * @param b ..
     * @return ..
     */
    public static int bytes2Int(byte[] b) {
        return ((b[0] & 0xff) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | b[3] & 0xff);
    }

    public static String getPathName(String src) {
        if (src.equals("/"))
            return src;
        return src.substring(src.lastIndexOf("/") + 1);
    }

    /**
     * 把完整的principal名字分割成字符串数组
     *
     * @param fullName 完整的principal名字
     * @return
     */
    public static String[] splitKerberosName(String fullName) {
        return fullName.split("[/@]");
    }

    /**
     * 使用ByteBuffer连接字节数组
     *
     * @param byte1
     * @param byte2
     * @return
     */
    public static byte[] joinByteArray0(byte[] byte1, byte[] byte2) {
        return ByteBuffer.allocate(byte1.length + byte2.length)
                .put(byte1)
                .put(byte2)
                .array();
    }

    /**
     * 使用ByteBuffer拆分字节数组
     *
     * @param input
     */
    public static void splitByteArray0(byte[] input) {
        ByteBuffer bb = ByteBuffer.wrap(input);

        byte[] cipher = new byte[8];
        byte[] nonce = new byte[4];
        byte[] extra = new byte[2];
        bb.get(cipher, 0, cipher.length);
        bb.get(nonce, 0, nonce.length);
        bb.get(extra, 0, extra.length);
    }

    /**
     * 使用System.arraycopy连接字节数组
     *
     * @param byte1
     * @param byte2
     * @return
     */
    public static byte[] joinByteArray(byte[] byte1, byte[] byte2) {
        byte[] result = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, result, 0, byte1.length);
        System.arraycopy(byte2, 0, result, byte1.length, byte2.length);

        return result;
    }

    /**
     * 使用System.arraycopy拆分字节数组
     *
     * @param input
     */
    public static byte[] subByteArray(byte[] input, int off) {
        byte[] result = new byte[input.length - off];
        System.arraycopy(input, off, result, 0, result.length);

        return result;
    }

    /**
     * 将byte数组以无符号十进制整数的形式输出，常用的Arrays.toString(byte[])会出现负数，有点烦
     *
     * @param bytes ..
     * @return ..
     */
    public static String printUnsignedBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            if (b < 0) {
                sb.append(256 + b).append(" ");
            } else {
                sb.append(b).append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * 打印byte数组内容，并跳过控制字符（ascii十进制值小于31和第127）。用这个方法打印出的字符应该能复制
     * byte数组是引用传递，别把原来的数组污染了
     * 不直接转每个byte的原因是可能会有多个byte才能拼出含义的字符（汉字）
     *
     * @param bytes ...
     * @return ...
     */
    public static String printBytesNoControlCharacter(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        byte[] tmp = bytes.clone();
        // 直接把不能打印的byte置0
        for (int i = 0; i < tmp.length; i++) {
            if ((0 <= tmp[i] && tmp[i] < 32) || tmp[i] == 127) {
                tmp[i] = 32;// 32是空格
            }
        }
//        LOG.info(StringUtils.bytes2HexString(tmp));
        return new String(tmp);
    }

    /**
     * 将byte数组转换成char数组
     *
     * @param bytes
     * @return
     */
    public static char[] bytes2Chars(byte[] bytes) {
        Charset c = StandardCharsets.UTF_8;
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        char[] result = c.decode(bb).array();
        // TODO 将bb中的数组全部覆盖掉
        String a = null;
        return result;
    }

    /**
     * 以?切分字符串
     *
     * @return ..
     */
    public static String[] splitByQuestionMark(String str) {
        ArrayList<String> list = new ArrayList<>();
        int start = 0;
        while (str.contains("?")) {
            int index = str.indexOf("?");
            String sub = str.substring(start, index);
            list.add(sub);
            str = str.substring(index + 1);
        }
        list.add(str);
        return list.toArray(new String[0]);
    }
}
