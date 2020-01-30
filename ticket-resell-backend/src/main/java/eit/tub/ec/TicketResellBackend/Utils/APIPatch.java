package eit.tub.ec.TicketResellBackend.Utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation partly copied from https://codippa.com/skip-null-properties-spring-beanutils/
 */

public class APIPatch {
    private static String[] ignoredProperties = {"id", "eventId", "ownerId", "ethUri", "ethId", "sellContractId"};

    public static void merge(Object source, Object destination){
        BeanUtils.copyProperties(source, destination, getNullPropertyNames(source));
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        emptyNames.addAll(Arrays.asList(ignoredProperties));

        return emptyNames.toArray(new String[0]);
    }
}
