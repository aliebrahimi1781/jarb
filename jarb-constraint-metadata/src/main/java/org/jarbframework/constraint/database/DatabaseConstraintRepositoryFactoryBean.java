/*
 * (C) 2011 Nidera (www.nidera.com). All rights reserved.
 */
package org.jarbframework.constraint.database;

import javax.sql.DataSource;

import org.jarbframework.constraint.database.column.CachingColumnMetadataRepository;
import org.jarbframework.constraint.database.column.JdbcColumnMetadataProvider;
import org.jarbframework.utils.orm.SchemaMapper;
import org.jarbframework.utils.orm.jpa.JpaHibernateSchemaMapper;
import org.jarbframework.utils.spring.SingletonFactoryBean;
import org.springframework.util.Assert;

/**
 * DatabaseConstraintRepositoryFactoryBean
 *
 * @author Jeroen van Schagen
 * @date Sep 6, 2011
 */
public class DatabaseConstraintRepositoryFactoryBean extends SingletonFactoryBean<DatabaseConstraintRepository> {
    private SchemaMapper schemaMapper = new JpaHibernateSchemaMapper();
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setSchemaMapper(SchemaMapper schemaMapper) {
        this.schemaMapper = schemaMapper;
    }

    @Override
    protected DatabaseConstraintRepository createObject() throws Exception {
        Assert.notNull(dataSource, "Data source cannot be null");
        DatabaseConstraintRepositoryImpl databaseConstraintRepository = new DatabaseConstraintRepositoryImpl();
        databaseConstraintRepository.setColumnMetadataRepository(new CachingColumnMetadataRepository(new JdbcColumnMetadataProvider(dataSource)));
        databaseConstraintRepository.setSchemaMapper(schemaMapper);
        return databaseConstraintRepository;
    }

}