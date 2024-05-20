package org.glygen.namespacehandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NcbiTaxonomyParser {
	
	public static void main(String[] args) throws IOException {
		String folder = "original";
		if (args.length > 0) {
			folder = args[0];
		}
		
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
		    inputStream = new FileInputStream(folder + File.separator + "names.dmp");
		    sc = new Scanner(inputStream, "UTF-8");
		    FileOutputStream fos = new FileOutputStream("namespaces" + File.separator + "species.txt");
		    OutputStreamWriter w = new OutputStreamWriter(fos);
		    BufferedWriter bw = new BufferedWriter(w);
		    String scientificName = "root";
		    String scientificId = "1";
		    List<String> excludedTypes = new ArrayList<>();
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        String[] values = line.split("\\t\\|");
		        if (values.length > 3) {
		        	String type = values[3].trim();
		        	String id = values[0].trim();
		        	String name = values[1].trim();
		        	//String uniqueName = values[2].trim();
		        	if (type.equalsIgnoreCase("scientific name")) {
		        		if (scientificName != null) {
			        		bw.append(scientificName + "\t" + scientificName +
			        				"\t" + "https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id="+scientificId + "\n");
		        		}
		        		scientificId = id;
		        		//scientificName = (uniqueName != null && !uniqueName.isBlank() ? uniqueName : name);
		        		scientificName = name;
		        		/*if (uniqueName != null && !uniqueName.isBlank()) {
		        			System.out.println ("unique name found " + name + "->" + uniqueName);
		        		}*/
		        	} else if (type.equalsIgnoreCase("synonym") || type.equalsIgnoreCase("equivalent name") 
		        			|| type.toLowerCase().contains("common name")
		        			|| type.equalsIgnoreCase("blast name") || type.toLowerCase().contains("acronym")){
		        		if (scientificName != null) {
		        			bw.append(name + "\t" + scientificName + "\t" + 
		        					"https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?id="+scientificId + "\n");
		        		}
		        	} else {
		        		if (!excludedTypes.contains(type)) {
		        			excludedTypes.add(type);
		        		}
		        	}
		        }   
		    }
		    
		    bw.close();
		    for (String t: excludedTypes) {
		    	System.out.println ("excluded type:" + t);
		    }
		    if (sc.ioException() != null) {
		        sc.ioException().printStackTrace();
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
		    if (inputStream != null) {
		        inputStream.close();
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
		
	}

}
