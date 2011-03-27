<#macro plugins_protocol_EventViewerPlugin screen>
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

<div id="addeventform">

<div id="animalselect" class="row">
<label for="animal">Target:</label>
<select name="animal" id="animal" class="selectbox" onchange="getMatrix(this);">
	<option value="0">&nbsp;</option>
	<#list screen.targetIdList as targetId>
		<#assign name = screen.getTargetName(targetId)>
		<option value="${targetId}">${name}</option>
	</#list>
</select>
</div>

<div id="matrix">
<!-- This box is filled dynamically by the ViewEventsServlet (Ajax-style) -->
</div>

</div>

<#if screen.success?exists>
  <#if screen.success == 1>
	<p>Event successfully added!</p>
  <#elseif screen.success == -1>
	<p>Oops... something went wrong! Event has not been recorded.</p>
  </#if>
</#if>

<#--<input name="myinput" value="${screen.getMyValue()}">
<input type="submit" value="Change name" onclick="__action.value='do_myaction';return true;"/-->
	
<#--end of your plugin-->	
			</div>
		</div>
	</div>
</form>
</#macro>
