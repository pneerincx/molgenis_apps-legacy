package org.molgenis.xgap.other.xqtlworkbench_lifelines;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.jpa.JpaUtil;

import app.DatabaseFactory;

public class XWBLLUpdateDatabaseJPA
{

	/**
	 * @param args
	 * @throws DatabaseException 
	 */
	public static void main(String[] args) throws DatabaseException
	{
		JpaUtil.dropAndCreateTables(DatabaseFactory.create());
	}

}
