package org.glygen.namespacehandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CelllineXMLParser {
	
	public static void main(String[] args) {
		String filename = "original/cellosaurus.xml";
		if (args.length > 0) {
			filename = args[0];
		}
 		File file = new File(filename);
	    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
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
	    }
	}

}
