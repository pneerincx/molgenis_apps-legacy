/* Date:        November 9, 2010
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.3
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package plugins.system;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.db.DatabaseException;
import org.molgenis.framework.db.Query;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.framework.ui.ScreenMessage;
import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.pheno.ObservationTarget;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.protocol.ProtocolApplication;
import org.molgenis.util.Tuple;

import commonservice.CommonService;

public class CascadingDeleteAnimalsPlugin extends PluginModel
{
	private static final long serialVersionUID = -366762636959036651L;
	private CommonService ct = CommonService.getInstance();
	private List<Integer> targetIdList;
	private Map<Integer, String> targetMap;

	public CascadingDeleteAnimalsPlugin(String name, ScreenModel parent)
	{
		super(name, parent);
	}
	
	public String getCustomHtmlHeaders()
    {
        return "<link rel=\"stylesheet\" style=\"text/css\" href=\"res/css/animaldb.css\">\n";
    }

	@Override
	public String getViewName()
	{
		return "plugins_system_CascadingDeleteAnimalsPlugin";
	}

	@Override
	public String getViewTemplate()
	{
		return "plugins/system/CascadingDeleteAnimalsPlugin.ftl";
	}
	
	// Target related methods:
	public List<Integer> getTargetIdList()
	{
		return targetIdList;
	}

	public void setTargetIdList(List<Integer> targetIdList)
	{
		this.targetIdList = targetIdList;
	}
	
	public String getTargetName(Integer id) {
		if (targetMap.get(id) != null) {
			return targetMap.get(id);
		} else {
			return id.toString();
		}
	}

	public void removeValues(Database db, CommonService ct, int targetId, List<ProtocolApplication> protocolApplicationList) throws DatabaseException, ParseException, IOException {
		// Values related to the target itself
		Query<ObservedValue> q = db.query(ObservedValue.class);
		q.addRules(new QueryRule(ObservedValue.TARGET, Operator.EQUALS, targetId));
		List<ObservedValue> valueList = q.find();
		for (ObservedValue value : valueList) {
			int paId = value.getProtocolApplication_Id();
			ProtocolApplication pa = ct.getProtocolApplicationById(paId);
			if (!protocolApplicationList.contains(pa)) {
				protocolApplicationList.add(pa);
			}
		}
		db.remove(valueList);
		// Values in which the target is linked to
		q = db.query(ObservedValue.class);
		q.addRules(new QueryRule(ObservedValue.RELATION, Operator.EQUALS, targetId));
		valueList = q.find();
		for (ObservedValue value : valueList) {
			int paId = value.getProtocolApplication_Id();
			ProtocolApplication pa = ct.getProtocolApplicationById(paId);
			if (!protocolApplicationList.contains(pa)) {
				protocolApplicationList.add(pa);
			}
		}
		db.remove(valueList);
	}

	@Override
	public void handleRequest(Database db, Tuple request)
	{
		try
		{
			String action = request.getString("__action");
			List<ProtocolApplication> protocolApplicationList = new ArrayList<ProtocolApplication>();
			
			if (action.equals("remove")) {
				List<?> targetsIdsAsObjectsList = request.getList("target");
				for (Object targetIdAsObject : targetsIdsAsObjectsList) {
					// Animal ID
					String targetIdString = (String)targetIdAsObject;
					targetIdString = targetIdString.replace(".", "");
					targetIdString = targetIdString.replace(",", "");
					int targetId = Integer.parseInt(targetIdString);
					removeValues(db, ct, targetId, protocolApplicationList);
					ObservationTarget target = ct.getObservationTargetById(targetId);
					db.remove(target);
				}
				for (ProtocolApplication pa : protocolApplicationList) {
					Query<ObservedValue> q = db.query(ObservedValue.class);
					q.addRules(new QueryRule(ObservedValue.PROTOCOLAPPLICATION, Operator.EQUALS, pa.getId()));
					List<ObservedValue> valueList = q.find();
					if (valueList.size() == 0) {
						// No values left in this application, so safe to remove
						db.remove(pa);
					}
				}
				
				this.getMessages().clear();
				this.getMessages().add(new ScreenMessage("Targets, values and protocol applications successfully removed", true));
			}
		
			if (action.equals("removeAllAnimals"))
			{
				List<Integer> animalIdList = ct.getAllObservationTargetIds("Individual", false);
				List<ObservationTarget> allAnimalList = ct.getObservationTargets(animalIdList);
				for (ObservationTarget animal : allAnimalList) {
					removeValues(db, ct, animal.getId(), protocolApplicationList);
					db.remove(animal);
				}
				db.remove(protocolApplicationList);
				
				this.getMessages().clear();
				this.getMessages().add(new ScreenMessage("All animals successfully removed", true));
			}
		} catch(Exception e) {
			this.getMessages().clear();
			if (e.getMessage() != null) {
				this.getMessages().add(new ScreenMessage("Error - targets not or partly removed", false));
			}
			e.printStackTrace();
		}
	}

	@Override
	public void reload(Database db)
	{
		ct.setDatabase(db);

		try
		{
			this.setTargetIdList(ct.getAllObservationTargetIds(null, false));
			this.targetMap = ct.getObservationTargetNames(this.targetIdList);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage() != null) {
				this.getMessages().add(new ScreenMessage(e.getMessage(), false));
			}
		}
	}
	
	@Override
	public boolean isVisible()
	{
		//you can use this to hide this plugin, e.g. based on user rights.
		try {
			if (this.getLogin().isAuthenticated()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// Authenticated but no user name, so probably debug user
			return true;
		}
	}
}
