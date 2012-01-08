package org.jarbframework.constraint;

import static org.jarbframework.utils.Asserts.hasText;
import static org.jarbframework.utils.Asserts.notNull;
import static org.jarbframework.utils.bean.BeanAnnotationScanner.fieldOrGetter;

import java.lang.annotation.Annotation;

/**
 * Enhances the property with a type whenever a specific
 * annotation is present inside the property declaration.
 *
 * @author Jeroen van Schagen
 * @date Sep 7, 2011
 */
public class AnnotationPropertyTypeEnhancer implements PropertyConstraintEnhancer {
    private final Class<? extends Annotation> annotationType;
    private final String type;

    public AnnotationPropertyTypeEnhancer(Class<? extends Annotation> annotationType, String type) {
        this.annotationType = notNull(annotationType, "Annotation type cannot be null");
        this.type = hasText(type, "Type needs to have text");
    }

    @Override
    public PropertyConstraintDescription enhance(PropertyConstraintDescription description) {
        if (fieldOrGetter().hasAnnotation(description.toPropertyReference(), annotationType)) {
            description.addType(type);
        }
        return description;
    }

}
