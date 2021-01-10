import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Properties;

class Injector<T> {
    private Properties prop;
    Injector() throws IOException {
        prop = new Properties();
        prop.load(new FileInputStream(new File("src/properties.properties")));
    }
    T inject(T object) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class aClass;
        Class className = object.getClass();
        Field[] fields = className.getDeclaredFields();
        for (Field field: fields) {
            Annotation an = field.getAnnotation(AutoInjectable.class);
            if (an != null) {
                String[] type = ((field.getType()).toString()).split(" ");
                String implClassName = prop.getProperty(type[1], null);
                if (implClassName != null) {
                    aClass = Class.forName(implClassName);
                    field.setAccessible(true);
                    field.set(object, aClass.newInstance());
                }
            }
        }
        return object;
    }
}
