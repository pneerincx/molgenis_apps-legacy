import filehandling.generic.MolgenisFileHandler;
import generic.Utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.molgenis.cluster.RScript;
import org.molgenis.framework.db.QueryRule;
import org.molgenis.framework.db.QueryRule.Operator;

import app.servlet.MolgenisServlet;

/**Use seperate servlet because of the custom R script that needs to be added*/
public class RApiServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	public void service( HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		Utils.console("starting RApiServlet");
		OutputStream outs = response.getOutputStream();
		PrintStream out = new PrintStream(new BufferedOutputStream(outs), false, "UTF8"); // 1.4
		Utils.console("URI path: " +request.getRequestURI());
		Utils.console("servlet path: " +request.getServletPath());
		int loc = request.getRequestURI().lastIndexOf(request.getServletPath());
		String filename = request.getRequestURI().substring(loc+request.getServletPath().length());

		Utils.console("filename is now: " + filename);
		
		String s = "";

		
		if (filename.startsWith("/")){
			filename = filename.substring(1);
		}
		// if R file exists, return that
		if (!filename.equals("") && !filename.endsWith(".R")){
			Utils.console("bad request: no R extension");
			s += "you can only load .R files\n";
		} else if (filename.equals(""))
		{
			Utils.console("getting default file");
			String server = "http://" + request.getLocalName() + ":" + request.getLocalPort() + "/"+MolgenisServlet.getMolgenisVariantID();
			String rSource = server + "/api/R/";
			// getRequestURL omits port!
			s +=("#step1: (first time only) install RCurl package from omegahat or bioconductor\n");
			s +=("#source(\"http://bioconductor.org/biocLite.R\")\n");
			s +=("#biocLite(\"RCurl\")\n");
			s +=("\n");
			s +=("#step2: source this file to use the MOLGENIS R interface\n");
			s +=("#source(\"" + rSource + "\")\n");
			s +=("\n");
			s +=("molgenispath <- paste(\"" + rSource + "\")\n");
			s +=("serverpath <- paste(\"" + server + "\")\n");
			s +=("\n");
			s +=("#load autogenerated R interfaces\n");
			s +=("source(\"" + rSource + "source.R\")\n");
			s +=("\n");
			s +=("#load XGAP specific extension to use R/qtl\n");
			s +=("source(\""+rSource+"xgap/R/RqtlTools.R\")\n");
			s +=("\n");
			s +=("#load XGAP specific extension to ease use of the Data <- DataElement structure as matrices\n");
			s +=("source(\""+rSource+"xgap/R/DataMatrix.R\")\n");
			s +=("\n");
			s +=("#load cluster calculation scripts\n");
			
			File[] listing =  new File((this.getClass().getResource("plugins/cluster/R/ClusterJobs/R")).getFile()).listFiles();
			for(File f : listing){
				s +=("source(\""+rSource+"plugins/cluster/R/ClusterJobs/R/"+f.getName()+"\")\n");
				
			}
			s +=("\n");
			
			// quick addition for demo purposes
			s +=("#loading user defined scripts\n");
			MolgenisServlet ms = new MolgenisServlet();
			try
			{
				List<RScript> scripts = ms.getDatabase().find(RScript.class);
				for(RScript script : scripts){
					s +=("source(\""+rSource+"userscripts/"+script.getName()+".R\")\n");
				}
			}
			catch (Exception e)
			{
				throw new IOException(e);
			}
			
			s +=("\n");
			s +=("#connect to the server\n");
			s +=("MOLGENIS.connect(\"" + server + "\")\n");
			
			// quick addition for demo purposes
		}else if(filename.startsWith("userscripts/")){
			MolgenisServlet ms = new MolgenisServlet();
			try
			{
				String name = filename.substring(12, filename.length()-2);
				Utils.console("getting '"+name+".r'");
				QueryRule q = new QueryRule("name", Operator.EQUALS, name);
				RScript script = ms.getDatabase().find(RScript.class, q).get(0);
				MolgenisFileHandler mfh = new MolgenisFileHandler(ms.getDatabase());
				File source = mfh.getFile(script);
				Utils.console("printing file: '"+source.getAbsolutePath()+"'");
				String str = this.printUserScript(source.toURI().toURL(), "", name);
				s +=(str);
			}
			catch (Exception e)
			{
				throw new IOException(e);
			}
			
		}
		else{
			// otherwise return the default R code to source all
			Utils.console("getting specific R file");
			filename = filename.replace(".", "/");
			filename = filename.substring(0, filename.length() - 2) + ".R";
			//map to hard drive, minus path app/servlet
			File root = new File(app.servlet.MolgenisServlet.class.getResource("source.R").getFile()).getParentFile().getParentFile().getParentFile();

			if(filename.equals("source.R"))
			{
				root = new File(root.getAbsolutePath() + "/app/servlet");
			}
			File source = new File(root.getAbsolutePath() + "/" + filename);
			
			//up to root of app	
			Utils.console("trying to load R file: " + filename + " from path " + source);
			if(source.exists()){
				String str = this.printScript(source.toURI().toURL(), "");
				s +=(str);
			}else{
				s +=("File '" + filename + "' not found\n");
			}
			Utils.console("done getting specific R file");
		}
		response.setStatus(HttpServletResponse.SC_OK);		
		response.setContentLength(s.length());
		response.setCharacterEncoding("UTF8");
		response.setContentType("text/plain");
		out.print(s);
		out.flush();
		out.close();
		response.flushBuffer();
		Utils.console("closed & flushed");

		
	}

	private String printScript( URL source, String out ) throws IOException
	{
		Utils.console("reading file to be outputted");
		BufferedReader reader = new BufferedReader(new InputStreamReader(source.openStream()));
		String sourceLine;
		while( (sourceLine = reader.readLine()) != null )
		{
			out += sourceLine + "\n";
		}
		reader.close();
		Utils.console("done reading");
		return out;
	}
	
	private String printUserScript( URL source, String out, String scriptName ) throws IOException
	{
		Utils.console("reading file to be outputted");
		BufferedReader reader = new BufferedReader(new InputStreamReader(source.openStream()));
		out += "run_"+scriptName + " <- function(dbpath, subjob, item, jobid, outname, myanalysisfile, jobparams, investigationname, libraryloc){\n";
		String sourceLine;
		while( (sourceLine = reader.readLine()) != null )
		{
			if(!sourceLine.trim().equals("")){
				if(!sourceLine.trim().endsWith("{") && !sourceLine.trim().endsWith("}")){
					out += "cat(Generate_Statement(\""+sourceLine.replace("\"", "'") + "\"),file=myanalysisfile,append=T)\n";
				}else{
					out += "cat(\""+sourceLine.replace("\"", "'") + "\n\",file=myanalysisfile,append=T)\n";	
				}
			}else{
				Utils.console("Removing empty line");
			}
		}
		out += "}";
		reader.close();
		Utils.console("done reading");
		return out;
	}
}
