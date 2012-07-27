<form method="post" enctype="multipart/form-data" name="${model.name}" action="">
	<!--needed in every form: to redirect the request to the right screen-->
	<input type="hidden" name="__target" value="${model.name}">
	<!--needed in every form: to define the action. This can be set by the submit button-->
	<input type="hidden" name="__action">
	<input type='submit' id='back' value='Return' onclick="__action.value='back'" />
</form>



<!-- normally you make one big form for the whole plugin-->
<form method="post" enctype="multipart/form-data" name="${model.name}" action="">
	<!--needed in every form: to redirect the request to the right screen-->
	<input type="hidden" name="__target" value="${model.name}">
	<!--needed in every form: to define the action. This can be set by the submit button-->
	<input type="hidden" name="__action">
<!-- this shows a title and border -->
	<div class="formscreen">
		<div class="form_header" id="${model.getName()}">
			${model.label}
		</div>
		
		<#--optional: mechanism to show messages-->
		<#list model.getMessages() as message>
			<#if message.success>	
		<p class="successmessage">${message.text}</p>
			<#else>
		<p class="errormessage">${message.text}</p>
			</#if>
		</#list>
	
		<div class="screenbody">
			<div class="screenpadding">	
	
			<#if model.state == "BEGINNING">
				<div id="askingQuestions">
					Which question do you want to ask?<br />
					<input type="radio" id="Q1" name="questions" value="questionOne"  /> Search for expression of gene(s) in subsets of samples<br />
					<input type="radio" id="Q2" name="questions" value="questionTwo"  /> Make comparisons: what are significantly differentially expressed genes between group A and group B <br />
					<input type="radio" id="Q3" name="questions" value="questionThree" /> Make comparisons: is gene X differentially expressed in group A versus B <br />
					<input type="radio" id="Q4" name="questions" value="questionFour" checked/> Convert between probes and genes <br />
				</div>
				<div id="submit">
					<input type='submit' id='submitInfo' value='Submit' onclick="__action.value='submitInformation'" />
				</div>
			
			<#elseif model.state == "QUESTION1">
	
			<#--begin your plugin-->	
			<br/>Search for expression of gene(s) in subsets of samples.<br/><br/>
				<div id="geneExpression">
					Choose the type of gene expression:<br />
					<input type="radio" id="geneExpRaw" name="geneExp" value="expDataRaw" /> Raw expression<br />
					<input type="radio" id="geneExpLog" name="geneExp" value="expDataLog2Quan" checked /> Quantile normalized & log2 transformed expression<br />
				</div>
	
				<div id="geneList">
					<br />Supply the gene(s) you want to select (one per line):<br />
					<textarea rows="10" cols="51" name="geneText"value="genes"></textarea>
				</div>
		
				<div id="groupSelection">
					<br/>Make a selection of the groups you are interested in. Hold down the Ctrl (windows) / Command (Mac) button to select multiple groups.<br />
					<select multiple="multiple" name="sampleGroups" id="sGroups" style="margin-right:10px">
											<option value="none" selected="selected">no selection made</option>
					<#list model.names as samplenames>
						<option value="${samplenames}">${samplenames}</option>
					</#list>
					</select>
				</div>
		
				<div id="submit">
					<input type='submit' id='submitInfo' value='Submit' onclick="__action.value='submitInfoQ1'" />
				</div>
			<#--end of your plugin-->	
			</div>
		</div>
	</div>
</form>

<#elseif model.state == "QUESTION1_RESULT">

			<#--matrix sub-->
			<#assign screen = model.getController().get("QuestionsSub")/>
			<#include screen.getViewTemplate()>
      		<#assign templateSource = "<@"+screen.getViewName() + " screen/>">
      		<#assign inlineTemplate = templateSource?interpret>
      		<@inlineTemplate screen />  
      		
      		
<#elseif model.state == "QUESTION2">
	<br/>Make comparisons: what are significantly differentially expressed genes between group A and group B. <br/><br/>
	<div id="geneExpression">
		Choose the type of gene expression:<br />
		<input type="radio" id="geneExpRaw" name="geneExp" value="expDataRaw" /> Raw expression<br />
		<input type="radio" id="geneExpLog" name="geneExp" value="expDataLog2Quan" checked /> Quantile normalized & log2 transformed expression<br />
	</div>

	<div id="sampleCombiningMethod">
		<br/> Choose the method which you want to use to combine the samples within each group:<br/>
		<!--input type="radio" id="sampleCombineNone" name="sampleCombine" value="sampleCombineNo" /> Do not combine the samples<br /-->
		<input type="radio" id="sampleCombineMean" name="sampleCombine" value="sampleCombineMean" checked/> Mean<br />
		<input type="radio" id="sampleCombineMedian" name="sampleCombine" value="sampleCombineMed" /> Median<br />
		<!--input type="radio" id="sampleCombineHighest" name="sampleCombine" value="sampleCombineHigh" /> Highest<br />
		<input type="radio" id="sampleCombineLowest" name="sampleCombine" value="sampleCombineLow" /> Lowest<br /-->
	</div>

	<!--div id="probeCombiningMethod">
		<br/> Choose the method which you want to use to combine the probes (if more than one probe is annotating a gene):<br/>
		<input type="radio" id="probeCombineNone" name="probeCombine" value="probeCombineNo" /> Do not combine the probes<br />
		<input type="radio" id="probeCombinemean" name="probeCombine" value="probeCombineMean" /> Mean<br />
		<input type="radio" id="probeCombineMedian" name="probeCombine" value="probeCombineMed" /> Median<br />
		<input type="radio" id="probeCombineHighest" name="probeCombine" value="probeCombineHigh" /> Highest<br />
		<input type="radio" id="probeCombineLowest" name="probeCombine" value="probeCombineLow" /> Lowest<br />
	</div-->

	<div id="groupSelection">
		<br/>Make a selection of the groups you are interested in. Hold down the Ctrl (windows) / Command (Mac) button to select multiple groups.<br />
		<select multiple="multiple" name="sampleGroups" id="sGroups" style="margin-right:10px">
			<option value="none" selected="selected">no selection made</option>
			<#list model.names as samplenames>
				<option value="${samplenames}">${samplenames}</option>
			</#list>
		</select>
	</div>
	
	<div>
	<br/>What is the significance cutoff you want to use? (Gene Y must be minimal x times higher or lower than gene Z to be signifficant (Non log2 values))<br/>
	<textarea rows="2" cols="5" name="signifCutoff" value="cutoff">1.5</textarea>
	</div>

	<div id="submit">
		<input type='submit' id='submitInfo' value='Submit' onclick="__action.value='submitInfoQ2'" />
	</div>

<#elseif model.state == "QUESTION3">
This question hasn't been implemented yet. Try again later

<#elseif model.state == "QUESTION4">
	You can convert between probes and genes here.
	<div id="convertGenesProbes">
		Choose the input:
		<input type="radio" id="convertGenesToProbes" name="convertGP" value="convertGenes" /> Genes<br />
		<input type="radio" id="convertProbesToGenes" name="convertGP" value="convertProbes" /> Probes<br />
	</div>
	
	<div id="geneList">
		<br />Supply the probes/genes you want to convert (one per line):<br />
		<textarea rows="10" cols="51" name="gpText" value="convertThese"></textarea>
	</div>
	
	
<#else>
UNKNOWN STATE ${model.state}
</#if>