import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * 自定义ClassLoader，读取xlass中的字节，进行解码并加载类
 */
public class HelloXlassClassLoader extends ClassLoader {
  private String path;

  public HelloXlassClassLoader(String path) {
    this.path = path;
  }

  public static void main(String[] args) {
    String filePath = "src\\main\\java\\classloader\\Hello.xlass";
    try {
      //findClass需要类的全限定类名
      Class<?> clazz = new HelloXlassClassLoader(filePath).findClass("Hello");
      Method declaredMethod = clazz.getDeclaredMethod("hello");
      declaredMethod.invoke(clazz.getConstructor().newInstance());
    } catch (ClassNotFoundException | IllegalAccessException |
            InstantiationException | NoSuchMethodException |
            InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected Class<?> findClass(String s) throws ClassNotFoundException {
    byte[] classBytes = new byte[0];
    try {
      classBytes = readXlassBytesFromFileAndDecode();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return defineClass(s, classBytes, 0, classBytes.length);
  }

  /**
   * 从xlass文件中读取字节并进行解码
   * @return
   * @throws IOException
   */
  private byte[] readXlassBytesFromFileAndDecode() throws IOException {
    File sourceFile = new File(this.path);
    FileInputStream fis = new FileInputStream(sourceFile);
    InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.ISO_8859_1);
    BufferedReader reader = new BufferedReader(isr);
    int value;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while ((value = reader.read()) != -1) {
      baos.write(value);
    }
    return decode(baos.toByteArray());
  }

  /**
   * 解码
   * @param xlassBytes
   * @return
   */
  private byte[] decode(byte[] xlassBytes) {
    for (int i = 0; i < xlassBytes.length; ++i) {
      xlassBytes[i] = (byte) (255 - xlassBytes[i]);
    }
    return xlassBytes;
  }
}
