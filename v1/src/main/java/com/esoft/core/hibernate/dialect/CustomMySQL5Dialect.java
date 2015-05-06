package com.esoft.core.hibernate.dialect;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class CustomMySQL5Dialect extends MySQL5InnoDBDialect {

	@Override
	public String getTableTypeString() {
		return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
	}
}
