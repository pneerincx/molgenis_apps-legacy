#MOLGENIS walltime=48:00:00 nodes=1 cores=1 mem=4
#INPUTS aaa,/target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.dat,/target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.gz,/target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.snps
#OUTPUTS /target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.results
#EXES /target/gpfs2/gcc/tools/minimac-beta-2012.8.15/minimac
#LOGS log
#TARGETS project,referencePanel,chr

#FOREACH project,referencePanel,chr

inputs "aaa"
inputs "/target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.gz"
inputs "/target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.dat"
inputs "/target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.snps"
alloutputsexist "/target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.results"

#Minimac requires a file listing markers in the haplotype file. This file can be easily 
#generated by extracting the second column from the .dat file. 

cut -f 2 -d " " /target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.dat > /target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.snps

#Example
#cut -f 2 -d " " examples/sample.dat > examples/sample.snps

#The imputation step
/target/gpfs2/gcc/tools/minimac-beta-2012.8.15/minimac --vcfReference --refHaps aaa --haps /target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.gz --snps /target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.snps --rounds 5 --states 200 --prefix /target/gpfs2/gcc/tmp/processing/demo/proj001//preparedStudy//chr20.results


#Example:
#./minimac --vcfReference --refHaps /target/gpfs2/gcc/resources/ImputationReferenceSets/GIANT.phase1_release_v3.20101123.snps_indels_svs.genotypes.refpanel.ALL/chr10.phase1_release_v3.20101123.snps_indels_svs.genotypes.refpanel.ALL.vcf.gz --haps mach1.out.gz --snps examples/sample.snps --rounds 5 --states 200 --prefix results



