<#macro plugins_header_BbmriHeader screen>
<#--Date:        November 11, 2009
 * Template:	PluginScreenFTLTemplateGen.ftl.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenFTLTemplateGen 3.3.2-testing

-->
<div id="header">	
	<p><a href="http://www.bbmri.nl/">
		<img src="res/img/bbmri.png" height="70px" align="left" style="vertical-align: bottom;"/> </a>
		&nbsp;${application.getLabel()}
	</p>
</div>
<div align="right" style="color: maroon; font: 12px Arial;margin-right: 10px;">
   		${screen.setUserLogin()}
   		${screen.getUserLogin()}
     <!-- <form>  <input type="submit" value="Logout" onclick="__action.value='doLogout';return true;"/><br /><br /><br /> </form> --> 		
   | <!--<a href="about.html">About</a>  | <a href="generated-doc/objectmodel.html">Object model</a>  |--> 
     <a href="generated-doc/fileformat.html">Exchange format</a> | 
     <a href="api/R">R-project API</a> | 
     <a href="api/find">HTTP API</a> | 
     <a href="api/soap?wsdl">Web Services API</a></div>
</#macro>

