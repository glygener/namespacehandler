package org.glygen.namespacehandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.json.*;

public class OBOParser {
	
	public static void main(String[] args) {
		File namespaceFolder = new File ("namespaces");
        String[] namespaceFiles = namespaceFolder.list();
        if (namespaceFiles != null) {
	        for (String filename: namespaceFiles) {
	        	if (filename.endsWith("json")) {
	        		StringBuffer namespaceOut = new StringBuffer();
	        		String jsonString;
					try {
						jsonString = FileUtils.readFileToString(new File("original" + File.separator + filename), StandardCharsets.UTF_8);
						JSONObject obj = new JSONObject(jsonString);
		        		JSONArray arr = obj.getJSONArray("graphs"); 
		        		for (int i = 0; i < arr.length(); i++)
		        		{
		        		    JSONObject graph = arr.getJSONObject(i);
		        		    JSONArray nodes = graph.getJSONArray("nodes"); 
		        		    for (int j=0; j < nodes.length(); j++) {
		        		    	JSONObject node = nodes.getJSONObject(j);
		        		    	String id = node.getString("id");   //URI
		        		    	if (node.has("lbl")) {
			        		    	String label = node.getString("lbl");
			        		    	if (node.has("type")) {
			        		    		String type = node.getString("type");
			        		    		if (type.equalsIgnoreCase("class")) {
					        		    	namespaceOut.append(label + "\t" + label + "\t" + id + "\n");
					        		    	if (node.has("meta") && node.getJSONObject("meta").has("synonyms")) {
						        		    	JSONArray synonyms = node.getJSONObject("meta").getJSONArray("synonyms");
						        		    	if (synonyms != null) {
						        		    		for (int k=0; k < synonyms.length(); k++) {
						        		    			JSONObject synonym = synonyms.getJSONObject(k);
						        		    			String pred = synonym.getString("pred");
						        		    			if (pred.equalsIgnoreCase("hasExactSynonym")) {
						        		    				String syn = synonym.getString("val");
						        		    				namespaceOut.append(syn + "\t" + label + "\t" + id + "\n");
						        		    			}
						        		    		}
						        		    	}
					        		    	}
			        		    		}
			        		    	}
		        		    	}
		        		    } 
		        		}
		        		
		        		//write to file
		        		FileWriter fWriter = new FileWriter("namespaces" + File.separator + 
		        				filename.substring(0, filename.lastIndexOf(".")) + ".txt");
		        		fWriter.append(namespaceOut.toString());
		        		fWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
        }
	}

}
