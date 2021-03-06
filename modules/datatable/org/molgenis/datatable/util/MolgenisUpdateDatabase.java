package org.molgenis.datatable.util;

import java.util.ArrayList;
import java.util.List;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.pheno.Individual;
import org.molgenis.pheno.ObservedValue;

public class MolgenisUpdateDatabase
{

	public MolgenisUpdateDatabase()
	{
		// TODO Auto-generated constructor stub
	}

	public void UpdateDatabase(Database db, String targetID, String value, String measurement, Integer protappID)
			throws DatabaseException
	{

		List<QueryRule> filterRules = new ArrayList<QueryRule>();
		filterRules.add(new QueryRule(ObservedValue.FEATURE_NAME, Operator.EQUALS, measurement));
		filterRules.add(new QueryRule(ObservedValue.TARGET_NAME, Operator.EQUALS, targetID));

		if (db.find(ObservedValue.class, new QueryRule(filterRules)).size() > 0)
		{

			ObservedValue oldValue = db.find(ObservedValue.class, new QueryRule(filterRules)).get(0);

			oldValue.setValue(value);

			db.update(oldValue);

		}
		else
		{
			if ("".equals(value) || value == null)
			{

			}
			else
			{

				Individual indv = db.find(Individual.class, new QueryRule(Individual.NAME, Operator.EQUALS, targetID))
						.get(0);

				ObservedValue ob = new ObservedValue();
				ob.setFeature_Name(measurement);
				ob.setTarget_Name(targetID);
				ob.setValue(value);
				ob.setProtocolApplication_Id(protappID);
				ob.setInvestigation(indv.getInvestigation_Id());
				db.add(ob);
			}

		}
	}
}
