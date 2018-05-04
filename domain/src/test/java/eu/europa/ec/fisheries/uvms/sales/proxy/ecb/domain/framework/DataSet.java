package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataSet {

    String initialData() default "";
    String expectedData() default "";

}