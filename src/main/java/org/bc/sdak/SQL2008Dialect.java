package org.bc.sdak;

import org.hibernate.dialect.SQLServer2008Dialect;

public class SQL2008Dialect extends SQLServer2008Dialect{
	public SQL2008Dialect() {  
        super();  
        registerHibernateType(-16, "string");
        registerHibernateType(-9, "string");
    }  
}
