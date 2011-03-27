<#macro plugins_fillanimaldb_FillDatabasePlugin screen>
<!-- normally you make one big form for the whole plugin-->
<form method="post" enctype="multipart/form-data" name="${screen.name}">
	<!--needed in every form: to redirect the request to the right screen-->
	<input type="hidden" name="__target" value="${screen.name}"" />
	<!--needed in every form: to define the action. This can be set by the submit button-->
	<input type="hidden" name="__action" />
	
<!-- this shows a title and border -->
	<div class="formscreen">
		<div class="form_header" id="${screen.getName()}">
		${screen.label}
		</div>
		
		<#--optional: mechanism to show messages-->
		<#list screen.getMessages() as message>
			<#if message.success>
		<p class="successmessage">${message.text}</p>
			<#else>
		<p class="errormessage">${message.text}</p>
			</#if>
		</#list>
		
		<div class="screenbody">
			<div class="screenpadding">	
<#--begin your plugin-->	

<h1>Load database tables from the old version of AnimalDB</h1>
<em>Caution: this might interfere with existing database items!</em>

<div id="animaltablediv" class="row">
	<label for="animaltable">Animal table CSV export file:</label>
	<input type="file" name="animaltable" id="animaltable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type='submit' class='addbutton' value='Load animals from old version' onclick="__action.value='loadAnimals'" />
</div>

<div id="locationtablediv" class="row">
	<label for="locationtable">Location table CSV export file:</label>
	<input type="file" name="locationtable" id="locationtable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type='submit' class='addbutton' value='Load locations from old version' onclick="__action.value='loadLocations'" />
</div>

<div id="littertablediv" class="row">
	<label for="littertable">Litter table CSV export file:</label>
	<input type="file" name="littertable" id="littertable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type='submit' class='addbutton' value='Load litters from old version' onclick="__action.value='loadLitters'" />
</div>

<div>
    <br />
    <hr />
    <br />
</div>

<div id="experimenttablediv" class="row">
	<label for="experimenttable">DEC subproject (experiment) table CSV export file:</label>
	<input type="file" name="experimenttable" id="experimenttable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type='submit' class='addbutton' value='Load DEC subprojects (experiments) from old version' onclick="__action.value='loadExperiments'" />
</div>

<div id="decapplicationtablediv" class="row">
	<label for="decapplicationtable">DEC project (application) table CSV export file:</label>
	<input type="file" name="decapplicationtable" id="decapplicationtable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type='submit' class='addbutton' value='Load DEC projects (applications) from old version' onclick="__action.value='loadDECApplications'" />
</div>

<div id="experimentanimaltablediv" class="row">
	<label for="experimentanimaltable">Animals in DEC subprojects table CSV export file:</label>
	<input type="file" name="experimentanimaltable" id="experimentanimaltable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type='submit' class='addbutton' value='Load animals in DEC subprojects from old version' onclick="__action.value='loadAnimalsInExperiments'" />
</div>

<div>
    <br />
    <hr />
    <br />
</div>

<div id="loadpresetsdiv" class="row">
	<label for="presettable">Presets table CSV export file:</label>
	<input type="file" name="presettable" id="presettable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type='submit' class='addbutton' value='Convert presets from old version to targetgroups' onclick="__action.value='loadPresets'" />
</div>

<div id="loadpresetanimalssdiv" class="row">
    <label for="presetanimaltable">Presetanimals table CSV export file:</label>
    <input type="file" name="presetanimaltable" id="presetanimaltable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type='submit' class='addbutton' value='Add the preset animals from the old version to targetgroups' onclick="__action.value='loadPresetAnimals'" />
</div>

<div>
    <br />
    <hr />
    <br />
</div>

<div id="loadeventsdiv" class="row">
	<label for="eventtable">Events table CSV export file:</label>
	<input type="file" name="eventtable" id="eventtable" class="textbox" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type='submit' class='addbutton' value='Load events from old version' onclick="__action.value='loadEvents'" />
</div>
	
<#--end of your plugin-->	
			</div>
		</div>
	</div>
</form>
</#macro>
