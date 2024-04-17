package org.glygen.namespacehandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class CelllineXMLParser {
	
	public static void main(String[] args) throws IOException {
		String filename = "original/cellosaurus.txt";
		if (args.length > 0) {
			filename = args[0];
		}
 		
 		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
		    inputStream = new FileInputStream(filename);
		    sc = new Scanner(inputStream, "UTF-8");
		    FileOutputStream fos = new FileOutputStream("namespaces" + File.separator + "cellline.txt");
		    OutputStreamWriter w = new OutputStreamWriter(fos);
		    BufferedWriter bw = new BufferedWriter(w);
		    String accession = null;
		    String identifier = null;
		    boolean start = false;
		    int i=0;
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        if (line.contains("________")) {
		        	start = true;
		        	continue;
		        }
		        if (!start) {
		        	continue;
		        }
		        String[] content = line.split("   ");
		        if (content.length > 1) {
		        	String code = content[0];
		        	String value = content[1];
		        	if (code.trim().equalsIgnoreCase("ID")) {
		        		identifier = value.trim();
		        	} else if (code.trim().equalsIgnoreCase("SY")) {
		        		if (identifier != null && accession != null) {  // they should be initialized by previous rows
			        		// split by ;
			        		String[] synonyms = value.trim().split(";");
			        		for (String syn: synonyms) {
			        			bw.append(syn.trim() + "\t" + identifier + "\t" + 
			        					"https://www.cellosaurus.org/"+ accession + "\n");
			        		}
		        		} else {
		        			System.err.println ("identifier and accession are null for line " + line);
		        		}
		        	} else if (code.trim().equalsIgnoreCase("AC")) {
		        		accession = value.trim();
		        		if (identifier != null) {
		        			bw.append(identifier + "\t" + identifier + "\t" + 
		        					"https://www.cellosaurus.org/"+ accession + "\n");
		        		}
		        	}
		        }
		        i++;
		        if (i % 500 == 0) bw.flush();
		    }
		    bw.close();
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
		
	   /* DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder documentBuilder = null;
	    
	    try {
	        documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    } catch (ParserConfigurationException e) {
	        e.printStackTrace();
	    }

	    Document doc = null;

	    try {
	        doc = documentBuilder.parse(file);
	    } catch (SAXException | IOException e) {
	        e.printStackTrace();
	    }

	    try {
	    	FileOutputStream fos = new FileOutputStream("namespaces" + File.separator + "cellline.txt");
		    OutputStreamWriter w = new OutputStreamWriter(fos);
		    BufferedWriter bw = new BufferedWriter(w);
	        XPathFactory xPathFactory = XPathFactory.newInstance();
	        XPath xPath = xPathFactory.newXPath();
	        XPathExpression expr = xPath.compile("/Cellosaurus/cell-line-list/cell-line");
	        XPathExpression expr2 = xPath.compile("name-list/name");
	        XPathExpression accession = xPath.compile("accession-list/accession[@type = 'primary']");
	        
	        NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
	        for (int i = 0; i < nodeList.getLength(); i++) {
	            Node node = nodeList.item(i);
	            String id = accession.evaluate(node);
	            //System.out.println ("node " + i);
	            NodeList labels = (NodeList) expr2.evaluate(node, XPathConstants.NODESET);
	            //System.out.println ("node labels " + labels.getLength());
	            String label = null;
	            for (int j=0; j < labels.getLength(); j++) {
	            	if (j==0) {
	            		label = labels.item(j).getTextContent().trim();
	            		bw.append(label + "\t" + label + "\thttps://www.cellosaurus.org/" + id + "\n");
	            	} else {
	            		String synonym = labels.item(j).getTextContent().trim();
	            		bw.append(synonym + "\t" + label + "\thttps://www.cellosaurus.org/" + id + "\n");
	            	}
	            }
	            if (i % 100 == 0) bw.flush();
	        }
		    
		    bw.close();
	    } catch (XPathExpressionException | IOException e) {
	        e.printStackTrace();
	    }*/
	}

}
