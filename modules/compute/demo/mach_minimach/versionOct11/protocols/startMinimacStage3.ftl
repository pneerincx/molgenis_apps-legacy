#MOLGENIS walltime=96:00:00 nodes=1 cores=1 mem=4



getFile ${expandWorksheetJar}

getFile ${concattedChunkWorksheet}

inputs "${concattedChunkWorksheet}"
alloutputsexist \
"${projectImputationJobsDirTarGz}" \
${projectImputationJobsDir}/check_for_submission.txt




#Call compute to generate phasing jobs
module load jdk/${javaversion}

mkdir -p ${projectImputationJobsDir}

# Execute MOLGENIS/compute to create job scripts.
sh ${McDir}/molgenis_compute.sh \
-worksheet=${concattedChunkWorksheet} \
-parameters=${McParameters} \
-workflow=${McProtocols}/../workflowMinimacStage3.csv \
-protocols=${McProtocols}/ \
-templates=${McTemplates}/ \
-scripts=${projectImputationJobsDir}/ \
-id=${McId}


#Get return code from last program call
returnCode=$?


if [ $returnCode -eq 0 ]
then
	
	echo -e "\nJob generation succesful!\n\n"

	<#if autostart == "TRUE">
		cd ${projectImputationJobsDir}
		sh submit.sh
	
		touch ${projectImputationJobsDir}/check_for_submission.txt

	<#elseif autostart == "FALSE">
	
		echo "No autostart selected"
		
		echo "You can submit your jobs using the following command:"
		echo "cd ${projectImputationJobsDir}"
		echo "sh submit.sh"
		
		
	</#if>


	tar czf ${projectImputationJobsDirTarGz} ${projectImputationJobsDir}
	putFile ${projectImputationJobsDirTarGz}
	
else
	
	echo -e "\nNon zero return code not making files final. Existing temp files are kept for debuging purposes\n\n"
	#Return non zero return code
	exit 1

fi


