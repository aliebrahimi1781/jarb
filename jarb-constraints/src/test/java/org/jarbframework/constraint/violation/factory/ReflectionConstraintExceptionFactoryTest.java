package org.jarbframework.constraint.violation.factory;

import static org.jarbframework.constraint.violation.DatabaseConstraintViolation.violaton;
import static org.jarbframework.constraint.violation.DatabaseConstraintType.UNIQUE_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.jarbframework.constraint.violation.DatabaseConstraintViolation;
import org.jarbframework.constraint.violation.domain.UsernameAlreadyExistsException;
import org.jarbframework.constraint.violation.factory.DatabaseConstraintExceptionFactory;
import org.jarbframework.constraint.violation.factory.ReflectionConstraintExceptionFactory;
import org.junit.Test;

public class ReflectionConstraintExceptionFactoryTest {

    /**
     * Custom exception classes should be initialized using the best available constructor.
     * The best constructor has the most supported parameter types.
     */
    @Test
    public void testInstantiateWithBestConstructor() {
        DatabaseConstraintExceptionFactory factory = new ReflectionConstraintExceptionFactory(UsernameAlreadyExistsException.class);
        DatabaseConstraintViolation violation = violaton(UNIQUE_KEY).constraint("uk_cars_license_number").build();
        final Throwable cause = new SQLException("Database exception 'uk_cars_license_number' violated !");
        Throwable exception = factory.createException(violation, cause);
        // Ensure we created an instance of the correct type
        assertTrue(exception instanceof UsernameAlreadyExistsException);
        // Ensure the correct constructor was invoked (violation + cause)
        assertEquals("Unique key 'uk_cars_license_number' was violated.", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(factory, ((UsernameAlreadyExistsException) exception).getExceptionFactory());
    }

    /**
     * Even third party exception classes can be used, as long as they have a supported constructor.
     */
    @Test
    public void testInstantiateThirdPartyException() {
        DatabaseConstraintExceptionFactory factory = new ReflectionConstraintExceptionFactory(IllegalStateException.class);
        DatabaseConstraintViolation violation = violaton(UNIQUE_KEY).constraint("uk_cars_license_number").build();
        final Throwable cause = new SQLException("Database exception 'uk_cars_license_number' violated !");
        Throwable exception = factory.createException(violation, cause);
        // The only supported constructor is (Throwable)
        assertTrue(exception instanceof IllegalStateException);
        assertEquals(cause, exception.getCause());
    }

    /**
     * No argument constructors are also supported, but only used if there are no other supported constructors.
     */
    @Test
    public void testInstantiateNullary() {
        DatabaseConstraintExceptionFactory factory = new ReflectionConstraintExceptionFactory(ExceptionWithOnlyNullaryConstructor.class);
        DatabaseConstraintViolation violation = violaton(UNIQUE_KEY).build();
        final Throwable cause = new SQLException("Database exception 'uk_cars_license_number' violated !");
        Throwable exception = factory.createException(violation, cause);
        assertTrue(exception instanceof ExceptionWithOnlyNullaryConstructor);
    }

    /**
     * Whenever an exception has no supported constructors at all, we throw a runtime exception.
     */
    @Test
    public void testNoMatchingConstructor() {
        try {
            new ReflectionConstraintExceptionFactory(ExceptionWithUnsupportedConstructor.class);
            fail("Expected an illegal state exception, as no supported constructor could be found!");
        } catch (IllegalStateException e) {
            assertEquals("Could not find a supported constructor in 'ExceptionWithUnsupportedConstructor'.", e.getMessage());
        }
    }

    /**
     * Unsupported constructors cannot be used.
     */
    @Test
    public void testUnsupportedConstructor() throws SecurityException, NoSuchMethodException {
        Constructor<? extends Throwable> unsupportedConstructor = ExceptionWithUnsupportedConstructor.class.getConstructor(BigDecimal.class);
        try {
            new ReflectionConstraintExceptionFactory(unsupportedConstructor);
            fail("Expected an illegal argument exception, as an unsupported constructor was provided!");
        } catch (IllegalStateException e) {
            assertEquals("Constructor contains unsupported parameter types", e.getMessage());
        }
    }

    // Exception with a no-arg as only supported constructor
    public static class ExceptionWithOnlyNullaryConstructor extends RuntimeException {
        private static final long serialVersionUID = -8084276175092538738L;

        public ExceptionWithOnlyNullaryConstructor() {
            super();
        }
    }

    // Exception with unsupported constructor, cannot be created
    public static class ExceptionWithUnsupportedConstructor extends RuntimeException {
        private static final long serialVersionUID = -8084276175092538738L;

        public ExceptionWithUnsupportedConstructor(BigDecimal arg) {
            super();
        }
    }

}