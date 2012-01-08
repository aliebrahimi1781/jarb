package org.jarbframework.constraint.database;

import static org.jarbframework.utils.bean.BeanAnnotationScanner.fieldOrGetter;

import org.jarbframework.constraint.PropertyConstraintDescription;
import org.jarbframework.constraint.PropertyConstraintEnhancer;

/**
 * Properties annotated with @DatabaseGenerated are never set to required
 * as they will be generated by the database on demand.
 * 
 * @author Jeroen van Schagen
 * @since 6 September 2011
 */
public class DatabaseGeneratedPropertyConstraintEnhancer implements PropertyConstraintEnhancer {

    @Override
    public PropertyConstraintDescription enhance(PropertyConstraintDescription description) {
        if (fieldOrGetter().hasAnnotation(description.toPropertyReference(), DatabaseGenerated.class)) {
            description.setRequired(false);
        }
        return description;
    }

}
