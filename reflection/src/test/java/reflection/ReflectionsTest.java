package reflection;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.Controller;
import annotation.Repository;
import annotation.Service;

class ReflectionsTest {

    private static final Logger log = LoggerFactory.getLogger(ReflectionsTest.class);

    @Test
    void showAnnotationClass() throws Exception {
        Reflections reflections = new Reflections("examples");

        for (Class<?> aClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            log.info(aClass.getName());
        }
        for (Class<?> aClass : reflections.getTypesAnnotatedWith(Service.class)) {
            log.info(aClass.getName());
        }
        for (Class<?> aClass : reflections.getTypesAnnotatedWith(Repository.class)) {
            log.info(aClass.getName());
        }

        // TODO 클래스 레벨에 @Controller, @Service, @Repository 애노테이션이 설정되어 모든 클래스 찾아 로그로 출력한다.
    }
}
