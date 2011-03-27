package plugins.ngs.workflow;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.molgenis.framework.db.DatabaseException;
import org.molgenis.ngs.NgsSample;
import org.molgenis.ngs.Project;
import org.molgenis.pheno.ObservableFeature;
import org.molgenis.pheno.ObservedValue;
import org.molgenis.protocol.Protocol;
import org.molgenis.protocol.WorkflowElement;

import commonservice.CommonService;

public class WorkflowModel {


    private List<NgsSample> samples = new ArrayList<NgsSample>();
    private List<ObservableFeature> features = new ArrayList<ObservableFeature>();
    private List<Project> projects = new ArrayList<Project>();
    private List<Protocol> protocols = new ArrayList<Protocol>();
    private List<ObservedValue> valuesBySample = new ArrayList<ObservedValue>();
    private  Set<WorkflowElement> workflowElements = new LinkedHashSet<WorkflowElement>();
    private Protocol currentProtocol;
    private CommonService cq;
    private NgsSample sample = new NgsSample();

    private boolean projectSpecific = false;
    private String projectName = "";
    private String action = "init";
    
    public void setCommonQueries(CommonService cq) {
	this.cq = cq;
    }
    
    public CommonService getCommonQueries() {
	return cq;
    }
    
    public void setWorkflowElements(Set<WorkflowElement> set) {
	this.workflowElements = set;
    }
    
    public Set<WorkflowElement> getWorkflowElements() {
	return workflowElements;
    }

    
    public List<ObservedValue> getValuesBySample() throws DatabaseException, ParseException {
	return valuesBySample;
    }
    
    public void setValuesBySample(int sampleId, List<ObservableFeature> features) throws DatabaseException, ParseException {
	valuesBySample = cq.getObservedValueBySampleAndFeatures(sampleId, features);
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getAction() {
	return action;
    }

    public void setSamples(List<NgsSample> allSamples) {
	this.samples = allSamples;
    }

    public List<NgsSample> getSamples() {
	return samples;
    }

    public void setProjects(List<Project> projects) {
	this.projects = projects;
    }

    public List<Project> getProjects() {
	return projects;
    }

    public void setSample(NgsSample sample) {
	this.sample = sample;
    }

    public NgsSample getSample() {
	return sample;
    }

    public void setFeatures(List<ObservableFeature> features) {
	this.features = features;
    }

    public List<ObservableFeature> getFeatures() {
	return features;
    }

    public void setProtocols(List<Protocol> protocols) {
	this.protocols = protocols;
    }

    public List<Protocol> getProtocols() {
	return protocols;
    }

    public void setCurrentProtocol(Protocol currentProtocol) {
	this.currentProtocol = currentProtocol;
    }

    public Protocol getCurrentProtocol() {
	return currentProtocol;
    }

    public void setProjectSpecific(boolean projectSpecific) {
	this.projectSpecific = projectSpecific;
    }

    public boolean isProjectSpecific() {
	return projectSpecific;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public String getProjectName() {
	return projectName;
    }


}


