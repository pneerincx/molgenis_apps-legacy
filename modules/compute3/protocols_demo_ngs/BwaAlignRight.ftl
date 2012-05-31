#
# =====================================================
# $Id: BwaAlignRight.ftl 10962 2012-02-21 09:59:42Z mdijkstra $
# $URL: http://www.molgenis.org/svn/molgenis_apps/trunk/modules/compute/protocols/BwaAlignRight.ftl $
# $LastChangedDate: 2012-02-21 10:59:42 +0100 (Tue, 21 Feb 2012) $
# $LastChangedRevision: 10962 $
# $LastChangedBy: mdijkstra $
# =====================================================
#

<#if seqType == "PE">
#MOLGENIS walltime=15:00:00 nodes=1 cores=4 mem=6

#INPUTS
#OUTPUTS
#LOGS log
#EXES
#TARGETS

inputs "${indexfile}" 
inputs "${rightbarcodefqgz}"
alloutputsexist "${rightbwaout}"

mkdir -p "${intermediatedir}"

${bwaalignjar} aln \
${indexfile} \
${rightbarcodefqgz} \
-t ${bwaaligncores} \
-f ${rightbwaout}
</#if>