package org.jarbframework.populator.excel.metamodel;

import java.util.Collections;

/**
 * Definition of an Embeddable that is being used as an ElementCollection. Holds its own properties and has its own defined class.
 * It does not contain a tableName however, as this definition can be reused for several ElementCollections.
 * @author Sander Benschop
 *
 * @param <T> type of class being described
 */
public final class EmbeddableElementCollectionDefinition<T> extends Definition {

    /** Entity class being described. */
    private final Class<T> definedClass;

    /**
     * Construct a new {@link EmbeddableElementCollectionDefinition).
     * @param entityClass class being described
     */
    private EmbeddableElementCollectionDefinition(Class<T> entityClass) {
        this.definedClass = entityClass;
    }

    /**
     * Returns the definedClass belonging to classDefinition.
     * @return definedClass instance from domain package
     */
    public Class<T> getDefinedClass() {
        return definedClass;
    }

    /**
     * Start building a new {@link EmbeddableElementCollectionDefinition}.
     * @param <T> type of class being described
     * @param definedClass class being described
     * @return class definition builder
     */
    public static <T> Builder<T> forClass(Class<T> entityClass) {
        return new EmbeddableElementCollectionDefinition.Builder<T>(entityClass);
    }

    public static class Builder<T> extends Definition.Builder<T> {

        private final Class<T> definedClass;

        /**
         * Construct a new {@link Builder}.
         * @param definedClass class being described
         */
        public Builder(Class<T> entityClass) {
            this.definedClass = entityClass;
        }

        /**
         * Construct a new class definition that contains all previously configured attributes.
         * @return new class definition
         */
        public EmbeddableElementCollectionDefinition<T> build() {
            EmbeddableElementCollectionDefinition<T> embeddableElementCollectionDefinition = new EmbeddableElementCollectionDefinition<T>(definedClass);
            embeddableElementCollectionDefinition.setProperties(Collections.unmodifiableSet(getProperties()));
            return embeddableElementCollectionDefinition;
        }

    }

}