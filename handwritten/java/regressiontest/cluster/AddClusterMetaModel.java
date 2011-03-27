package regressiontest.cluster;

import org.molgenis.cluster.Analysis;
import org.molgenis.cluster.DataName;
import org.molgenis.cluster.DataSet;
import org.molgenis.cluster.DataValue;
import org.molgenis.cluster.ParameterName;
import org.molgenis.cluster.ParameterSet;
import org.molgenis.cluster.ParameterValue;
import org.molgenis.data.Data;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;
import org.molgenis.organization.Investigation;

import app.JDBCDatabase;

public class AddClusterMetaModel
{
	
	public static void main(String[] args) throws Exception
	{
		JDBCDatabase db = new JDBCDatabase("xgap.properties");
		
		Investigation inv = db.find(Investigation.class, new QueryRule("name", Operator.EQUALS, "ClusterDemo")).get(0);
		QueryRule investigationId = new QueryRule("investigation", Operator.EQUALS, inv.getId());
		QueryRule genoName = new QueryRule("name", Operator.EQUALS, "genotypes");
		QueryRule phenoName = new QueryRule("name", Operator.EQUALS, "metaboliteexpression");
		Data genoData = db.find(Data.class, investigationId, genoName).get(0);
		Data phenoData = db.find(Data.class, investigationId, phenoName).get(0);
		
		
		new AddClusterMetaModel(genoData, phenoData, db);

	}

	public AddClusterMetaModel(Data genoData, Data phenoData, JDBCDatabase db) throws Exception
	{

		db.beginTx();

		try
		{

			// QTL
			DataSet rqtlData = new DataSet();
			rqtlData.setName("Rqtl_data");
			db.add(rqtlData);
			
			DataName genotypes = new DataName();
			genotypes.setName("genotypes");
			genotypes.setDataSet(rqtlData);
			db.add(genotypes);

			DataName phenotypes = new DataName();
			phenotypes.setName("phenotypes");
			phenotypes.setDataSet(rqtlData);
			db.add(phenotypes);

			DataValue refToGenoData = new DataValue();
			refToGenoData.setName("Fu_Genotypes");
			refToGenoData.setValue(genoData);
			refToGenoData.setDataName(genotypes);
			db.add(refToGenoData);

			DataValue refToPhenoData = new DataValue();
			refToPhenoData.setName("Fu_LCMS_data");
			refToPhenoData.setValue(phenoData);
			refToPhenoData.setDataName(phenotypes);
			db.add(refToPhenoData);

			ParameterSet rqtlParams = new ParameterSet();
			rqtlParams.setName("Rqtl_params");
			db.add(rqtlParams);

			ParameterName map = new ParameterName();
			map.setName("map");
			map.setParameterSet(rqtlParams);
			db.add(map);

			ParameterName method = new ParameterName();
			method.setName("method");
			method.setParameterSet(rqtlParams);
			db.add(method);

			ParameterName model = new ParameterName();
			model.setName("model");
			model.setParameterSet(rqtlParams);
			db.add(model);
			
			ParameterName stepsize = new ParameterName();
			stepsize.setName("stepsize");
			stepsize.setParameterSet(rqtlParams);
			db.add(stepsize);
				
			ParameterValue scanall = new ParameterValue();
			scanall.setName("Scanone");
			scanall.setValue("scanone");
			scanall.setParameterName(map);
			db.add(scanall);
			
//			ParameterValue cimall = new ParameterValue();
//			cimall.setName("Scanall_using_CIM");
//			cimall.setValue("cimall");
//			cimall.setParameterName(map);
//			db.add(cimall);
//			
//			ParameterValue mqmall = new ParameterValue();
//			mqmall.setName("Scan_using_MQM");
//			mqmall.setValue("mqmscan");
//			mqmall.setParameterName(map);
//			db.add(mqmall);
			
			ParameterValue hk = new ParameterValue();
			hk.setName("Haley_Knott");
			hk.setValue("hk");
			hk.setParameterName(method);
			db.add(hk);
			
			ParameterValue normal = new ParameterValue();
			normal.setName("Normal_distribution");
			normal.setValue("normal");
			normal.setParameterName(model);
			db.add(normal);
			
			ParameterValue stepsize1 = new ParameterValue();
			stepsize1.setName("Markers");
			stepsize1.setValue("0");
			stepsize1.setParameterName(stepsize);
			db.add(stepsize1);
			
			ParameterValue stepsize2 = new ParameterValue();
			stepsize2.setName("Two_cM");
			stepsize2.setValue("2");
			stepsize2.setParameterName(stepsize);
			db.add(stepsize2);
			
			Analysis rqtlScan = new Analysis();
			rqtlScan.setName("Rqtl_analysis");
			rqtlScan.setDataSet(rqtlData);
			rqtlScan.setParameterSet(rqtlParams);
			rqtlScan.setTargetFunctionName("QTL");
			db.add(rqtlScan);
			
			
			
			//MINJOB
			ParameterSet minjobParams = new ParameterSet();
			minjobParams.setName("Min_params");
			db.add(minjobParams);

			ParameterName minName = new ParameterName();
			minName.setName("message");
			minName.setParameterSet(minjobParams);
			db.add(minName);
				
			ParameterValue minVal = new ParameterValue();
			minVal.setName("Klaus");
			minVal.setValue("Hello Klaus");
			minVal.setParameterName(minName);
			db.add(minVal);
			
			ParameterValue minVal2 = new ParameterValue();
			minVal2.setName("Ritsert");
			minVal2.setValue("Hello Ritsert");
			minVal2.setParameterName(minName);
			db.add(minVal2);
			
			ParameterValue minVal3 = new ParameterValue();
			minVal3.setName("Brian");
			minVal3.setValue("Hello Brian");
			minVal3.setParameterName(minName);
			db.add(minVal3);
			
			ParameterValue minVal4 = new ParameterValue();
			minVal4.setName("Andrew");
			minVal4.setValue("Hello Andrew");
			minVal4.setParameterName(minName);
			db.add(minVal4);
			
			DataSet emptyDataSet = new DataSet();
			emptyDataSet.setName("Empty");
			db.add(emptyDataSet);
			
			Analysis minjob = new Analysis();
			minjob.setName("Hello_analysis");
			minjob.setDataSet(emptyDataSet);
			minjob.setParameterSet(minjobParams);
			minjob.setTargetFunctionName("MIN");
			db.add(minjob);
			
			
			/*
			// PERMUTATION
			ParameterSet permParams = new ParameterSet();
			permParams.setName("Rqtl_perm_params");
			db.add(permParams);
			
			ParameterName mapp = new ParameterName();
			mapp.setName("map");
			mapp.setParameterSet(permParams);
			db.add(mapp);

			ParameterName methodp = new ParameterName();
			methodp.setName("method");
			methodp.setParameterSet(permParams);
			db.add(methodp);

			ParameterName modelp = new ParameterName();
			modelp.setName("model");
			modelp.setParameterSet(permParams);
			db.add(modelp);
			
			ParameterName nperms = new ParameterName();
			nperms.setName("nperms");
			nperms.setParameterSet(permParams);
			db.add(nperms);
			
			ParameterValue scanonep = new ParameterValue();
			scanonep.setName("Scanone");
			scanonep.setValue("scanone");
			scanonep.setParameterName(mapp);
			db.add(scanonep);
			
			ParameterValue permp = new ParameterValue();
			permp.setName("Permutation");
			permp.setValue("permutation");
			permp.setParameterName(methodp);
			db.add(permp);
			
			ParameterValue simp = new ParameterValue();
			simp.setName("Simulation");
			simp.setValue("simulation");
			simp.setParameterName(methodp);
			db.add(simp);
			
			ParameterValue normalp = new ParameterValue();
			normalp.setName("Normal_distribution");
			normalp.setValue("normal");
			normalp.setParameterName(modelp);
			db.add(normalp);
			
			ParameterValue nperm1 = new ParameterValue();
			nperm1.setName("Test");
			nperm1.setValue("10");
			nperm1.setParameterName(nperms);
			db.add(nperm1);
			
			ParameterValue nperm2 = new ParameterValue();
			nperm2.setName("Low");
			nperm2.setValue("100");
			nperm2.setParameterName(nperms);
			db.add(nperm2);
			
			ParameterValue nperm3 = new ParameterValue();
			nperm3.setName("High");
			nperm3.setValue("1000");
			nperm3.setParameterName(nperms);
			db.add(nperm3);
			
			// create 'Analysis' object for PERMUTATION
			Analysis permute = new Analysis();
			permute.setName("Rqtl_permutation_analysis");
			permute.setDataSet(rqtlData);
			permute.setParameterSet(permParams);
			permute.setTargetFunctionName("PERM");
			db.add(permute);
			
			
			
			// QUALITY CONTROL
			ParameterSet qcParams = new ParameterSet();
			qcParams.setName("QC_params");
			db.add(qcParams);
			
			ParameterName qcparam = new ParameterName();
			qcparam.setName("nperms");
			qcparam.setParameterSet(qcParams);
			db.add(qcparam);
			
			ParameterValue qc1 = new ParameterValue();
			qc1.setName("Low");
			qc1.setValue("1");
			qc1.setParameterName(qcparam);
			db.add(qc1);
			
			ParameterValue qc2 = new ParameterValue();
			qc2.setName("Normal");
			qc2.setValue("1,5");
			qc2.setParameterName(qcparam);
			db.add(qc2);
			
			ParameterValue qc3 = new ParameterValue();
			qc3.setName("High");
			qc3.setValue("2");
			qc3.setParameterName(qcparam);
			db.add(qc3);
			
			Analysis qc = new Analysis();
			qc.setName("QualityControl");
			qc.setDataSet(rqtlData);
			qc.setParameterSet(qcParams);
			qc.setTargetFunctionName("QC");
			db.add(qc);
			
			
			
			// PLOT
			DataSet rqtlDataHax = new DataSet();
			rqtlDataHax.setName("Plot_data");
			db.add(rqtlDataHax);
			
			DataName genotypesHax = new DataName();
			genotypesHax.setName("genotypes");
			genotypesHax.setDataSet(rqtlDataHax);
			db.add(genotypesHax);

			DataName phenotypesHax = new DataName();
			phenotypesHax.setName("phenotypes");
			phenotypesHax.setDataSet(rqtlDataHax);
			db.add(phenotypesHax);

			DataValue refToGenoDataHax = new DataValue();
			refToGenoDataHax.setName("Fu_Genotypes");
			refToGenoDataHax.setValue(genoData);
			refToGenoDataHax.setDataName(genotypesHax);
			db.add(refToGenoDataHax);

			DataValue refToPhenoDataHax = new DataValue();
			refToPhenoDataHax.setName("Fu_LCMS_data");
			refToPhenoDataHax.setValue(phenoData);
			refToPhenoDataHax.setDataName(phenotypesHax);
			db.add(refToPhenoDataHax);
			
			DataName blaatHax = new DataName();
			blaatHax.setName("map");
			blaatHax.setDataSet(rqtlDataHax);
			db.add(blaatHax);
			
			ParameterSet bogusParams = new ParameterSet();
			bogusParams.setName("Plot_params");
			db.add(bogusParams);
			
			Analysis plot = new Analysis();
			plot.setName("PlotQTL");
			plot.setDataSet(rqtlDataHax);
			plot.setParameterSet(bogusParams);
			plot.setTargetFunctionName("PLOT");
			db.add(plot);
			*/

			// commit
			db.commitTx();

		}
		catch (Exception e)
		{
			db.rollbackTx();
			throw(e);
		}
	}
}
