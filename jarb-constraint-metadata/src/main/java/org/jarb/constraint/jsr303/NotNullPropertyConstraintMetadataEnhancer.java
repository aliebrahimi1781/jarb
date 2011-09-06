package org.jarb.constraint.jsr303;

import javax.validation.constraints.NotNull;

import org.jarb.constraint.PropertyConstraintMetadata;
import org.jarb.constraint.PropertyConstraintMetadataEnhancer;

/**
 * Whenever a property is annotated as @NotNull , it is required.
 * 
 * @author Jeroen van Schagen
 * @since 31-05-2011
 */
public class NotNullPropertyConstraintMetadataEnhancer implements PropertyConstraintMetadataEnhancer {

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> PropertyConstraintMetadata<T> enhance(PropertyConstraintMetadata<T> propertyMetadata) {
        if (ConstraintAnnotationScanner.isPropertyAnnotated(propertyMetadata.getPropertyReference(), NotNull.class)) {
            propertyMetadata.setRequired(true);
        }
        return propertyMetadata;
    }

}
