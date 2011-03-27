/* Date:        November 11, 2009
 * Template:	PluginScreenJavaTemplateGen.java.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenJavaTemplateGen 3.3.2-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
 */

package plugins.cluster.demo.xqtleuratransheader;

import org.molgenis.framework.db.Database;
import org.molgenis.framework.ui.ScreenModel;
import org.molgenis.framework.ui.PluginModel;
import org.molgenis.util.Tuple;

/**
 * A simple plugin to create the header of the MOLGENIS application. This
 * includes the header logo as well as the top level menu items for
 * documentation, services etc (replaces the hardcoded header).
 * 
 * @author Morris Swertz
 */
public class MolgenisHeader extends PluginModel
{
	public MolgenisHeader(String name, ScreenModel parent)
	{
		super(name, parent);
	}

	@Override
	public String getViewName()
	{
		return "plugins_cluster_demo_xqtleuratransheader_MolgenisHeader";
	}

	@Override
	public String getViewTemplate()
	{
		return "plugins/cluster/demo/xqtleuratransheader/MolgenisHeader.ftl";
	}

	@Override
	public void handleRequest(Database db, Tuple request)
	{
		//static
	}

	@Override
	public void reload(Database db)
	{
		//static
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}
	
	@Override
	public String getCustomHtmlHeaders()
	{
		return "<link rel=\"stylesheet\" style=\"text/css\" href=\"clusterdemo/xqtleuratranscolors.css\">" + "\n" +
			   "<link rel=\"stylesheet\" style=\"text/css\" href=\"clusterdemo/main_override.css\">" ;
	}
}
