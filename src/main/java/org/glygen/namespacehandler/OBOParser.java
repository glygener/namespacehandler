package org.glygen.namespacehandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.json.*;

/**
 * processes all json files found in the given folder (command line argument) to be used for type ahead functionality
 * and saves the corresponding result text files in "namespaces" folder
 * 
 * @author sena
 *
 */
public class OBOParser {
	
	public static void main(String[] args) {
		String folder = args.length > 0 ? "args[0]" : "original";
		File namespaceFolder = new File (folder);
        String[] namespaceFiles = namespaceFolder.list();
        boolean namespaceMatch = false;
        if (namespaceFiles != null) {
	        for (String filename: namespaceFiles) {
	        	if (filename.endsWith("json")) {
	        		if (filename.startsWith("go-")) {
	        			namespaceMatch = true;
	        		} else {
	        			namespaceMatch = false;
	        		}
	        		StringBuffer namespaceOut = new StringBuffer();
	        		String jsonString;
					try {
						jsonString = FileUtils.readFileToString(
								new File(namespaceFolder + File.separator + filename), StandardCharsets.UTF_8);
						JSONObject obj = new JSONObject(jsonString);
		        		JSONArray arr = obj.getJSONArray("graphs"); 
		        		int count = 0;
		        		int deprecated = 0;
		        		int totalCount = 0;
		        		for (int i = 0; i < arr.length(); i++) {
		        		    JSONObject graph = arr.getJSONObject(i);
		        		    JSONArray nodes = graph.getJSONArray("nodes"); 
		        		    totalCount = nodes.length();
		        		    for (int j=0; j < nodes.length(); j++) {
		        		    	boolean skip = false;
		        		    	JSONObject node = nodes.getJSONObject(j);
		        		    	String id = node.getString("id");   //URI
		        		    	if (node.has("lbl")) {
			        		    	String label = node.getString("lbl");
			        		    	if (node.has("type")) {
			        		    		String type = node.getString("type");
			        		    		if (type.equalsIgnoreCase("class")) {
			        		    			if (node.has("meta") && node.getJSONObject("meta").has("deprecated")) {
				        		    			deprecated++;
				        		    		}
			        		    			if (namespaceMatch) {
					        		    		if (node.has("meta") && node.getJSONObject("meta").has("basicPropertyValues")) {
					        		    			JSONArray basicPropertyValues = node.getJSONObject("meta").getJSONArray("basicPropertyValues");
					        		    			if (basicPropertyValues != null) {
							        		    		for (int k=0; k < basicPropertyValues.length(); k++) {
							        		    			JSONObject prop = basicPropertyValues.getJSONObject(k);
							        		    			String pred = prop.getString("pred");
							        		    			if (pred.contains("hasOBONamespace")) {
							        		    				String val = prop.getString("val");
						        		    					if (!val.equalsIgnoreCase("cellular_component")) {
						        		    						// skip this one
						        		    						skip = true;
						        		    						break;
						        		    					} 
							        		    			}
							        		    		}
					        		    			}
					        		    		}
					        		    		if (skip) continue;
					        		    	}
			        		    			
					        		    	namespaceOut.append(label + "\t" + label + "\t" + id + "\n");
					        		    	if (node.has("meta") && node.getJSONObject("meta").has("synonyms")) {
						        		    	JSONArray synonyms = node.getJSONObject("meta").getJSONArray("synonyms");
						        		    	if (synonyms != null) {
						        		    		for (int k=0; k < synonyms.length(); k++) {
						        		    			JSONObject synonym = synonyms.getJSONObject(k);
						        		    			String pred = synonym.getString("pred");
						        		    			//if (pred.equalsIgnoreCase("hasExactSynonym")) {
						        		    			if (pred.contains("Synonym")) {
						        		    				String syn = synonym.getString("val");
						        		    				namespaceOut.append(syn + "\t" + label + "\t" + id + "\n");
						        		    			}
						        		    		}
						        		    	}
					        		    	}
			        		    			
			        		    		} else {
			        		    			count++;
			        		    			System.out.println ("found non-class node " + type);
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
		        		
		        		System.out.println ("Deprecated count for " + filename + ": " + deprecated + " out of " + totalCount);
		        		System.out.println ("Non-class count for " + filename + ": " + count);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
        }
	}

}
