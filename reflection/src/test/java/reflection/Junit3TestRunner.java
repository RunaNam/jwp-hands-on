package reflection;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class Junit3TestRunner {

    @Test
    void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;

        Junit3Test clazzObj = clazz.getConstructor().newInstance();

        // TODO Junit3Test에서 test로 시작하는 메소드 실행
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if(method.getName().startsWith("test")){
                method.invoke(clazzObj);
            }
        }
    }
}
