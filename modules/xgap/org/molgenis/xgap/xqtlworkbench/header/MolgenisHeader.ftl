<#--Date:        November 11, 2009
 * Template:	PluginScreenFTLTemplateGen.ftl.ftl
 * generator:   org.molgenis.generators.ui.PluginScreenFTLTemplateGen 3.3.2-testing
 * 
 * THIS FILE IS A TEMPLATE. PLEASE EDIT :-)
-->
<#macro org_molgenis_xgap_xqtlworkbench_header_MolgenisHeader screen>

<div style="height:10px;">&nbsp;</div>

<a href="?__target=main&select=ClusterDemo"><img src="clusterdemo/bg/xqtl_default_banner.png" /></a>

<table style="width: 100%;">
	<tr>
		<td align="right">
			<font style="font-size:14px;">
				<#-->| <a href="api/REST/">JSON api</a> | <a href="api/SOAP/">SOAP api</a> | <a href="api/REST/">REST api</a> | -->
				<a target="_blank" href="http://www.molgenis.org/wiki/xQTL">Help</a> | <a href="generated-doc/fileformat.html">Exchange format</a> | <a href="api/R/">R api</a> | <a href="api/find/">Find api</a> | <a href="api/bash/">Bash api</a> | ${screen.userLogin}
				<#--
				<a href="api/R/"><img src="clusterdemo/icons/r_icon.gif" width="25" height="25"/></a>
				<a href="api/find/"><img src="clusterdemo/icons/txt_icon.png" width="25" height="25"/></a>
				<a href="api/bash/"><img src="clusterdemo/icons/bash.png" width="25" height="25"/></a>
				-->
			</font>
		</td>
	</tr>
</table>


<div style="height:10px;">&nbsp;</div>

	
</#macro>
