package lucas0204.todolist.Utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyValue;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class TodolistUtils {
    public static void copyNonNullValues(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = wrapper.getPropertyDescriptors();

        Set<String> emptyProperties = new HashSet<>();

        for (PropertyDescriptor pd: propertyDescriptors) {
            var value = wrapper.getPropertyValue(pd.getName());
            if (value == null) {
                emptyProperties.add(pd.getName());
            }
        }

        String[] result = new String[emptyProperties.size()];
        return emptyProperties.toArray(result);
    }
}
