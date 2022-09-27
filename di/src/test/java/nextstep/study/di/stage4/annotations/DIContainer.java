package nextstep.study.di.stage4.annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;

/**
 * 스프링의 BeanFactory, ApplicationContext에 해당되는 클래스
 */
class DIContainer {

    private final Set<Object> beans;

    private DIContainer(final Set<Class<?>> classes) {
        this.beans = createBeans(classes);
        this.beans.forEach(this::setFields);
    }

    public static DIContainer createContainerForPackage(final String rootPackageName) {
        Set<Class<?>> targetClasses = getTargetClasses(rootPackageName);
        return new DIContainer(targetClasses);
    }

    private static Set<Class<?>> getTargetClasses(String rootPackageName) {
        Set<Class<?>> classes = new HashSet<>();
        Reflections reflections = new Reflections(rootPackageName);
        classes.addAll(reflections.getTypesAnnotatedWith(Service.class));
        classes.addAll(reflections.getTypesAnnotatedWith(Repository.class));

        return classes;
    }

    private Set<Object> createBeans(Set<Class<?>> classes) {
        return classes.stream()
            .map(this::getObject)
            .collect(Collectors.toUnmodifiableSet());
    }

    private Object getObject(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("빈 생성 중 에러가 발생하였습니다.");
        }
    }

    private void setFields(Object bean) {
        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            setField(bean, field);
        }
    }

    private void setField(Object bean, Field field) {
        try {
            if (field.isAnnotationPresent(Inject.class)) {
                field.set(bean, getBean(field.getType()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(final Class<T> clazz) {
        return (T)beans.stream()
            .filter(bean -> clazz.isInstance(bean))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("빈을 찾을 수 없습니다."));

    }
}
