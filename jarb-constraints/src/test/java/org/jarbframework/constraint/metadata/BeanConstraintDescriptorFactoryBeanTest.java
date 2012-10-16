package org.jarbframework.constraint.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jarbframework.constraint.metadata.database.DatabaseConstraintRepository;
import org.jarbframework.constraint.metadata.domain.Wine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class BeanConstraintDescriptorFactoryBeanTest {
    
    private BeanConstraintDescriptorFactoryBean factoryBean;

    @Autowired
    private DatabaseConstraintRepository databaseConstraintRepository;

    @Before
    public void setUp() throws Exception {
        factoryBean = new BeanConstraintDescriptorFactoryBean();
        factoryBean.setDatabaseConstraintRepository(databaseConstraintRepository);
    }

    /**
     * Ensure that the generated descriptor can access database and JSR constraint information.
     */
    @Test
    public void testGeneratedObject() throws Exception {
        BeanConstraintDescriptor descriptor = factoryBean.getObject();
        BeanConstraintDescription<Wine> wineDescription = descriptor.describe(Wine.class);
        PropertyConstraintDescription nameDescription = wineDescription.getPropertyDescription("name");
        assertEquals(String.class, nameDescription.getJavaType()); // Retrieved by introspection
        assertTrue(nameDescription.isRequired()); // Retrieved from database
        assertEquals(Integer.valueOf(6), nameDescription.getMinimumLength()); // Retrieved from @Length
        assertEquals(Integer.valueOf(6), nameDescription.getMaximumLength()); // Merged @Length and database
    }

}
