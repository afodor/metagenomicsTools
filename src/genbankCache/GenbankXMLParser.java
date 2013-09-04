package genbankCache;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import utils.ConfigReader;

public class GenbankXMLParser
{
	/** 
	 * Author:  anthony.fodor@gmail.com    
	 * This code is free software; you can redistribute it and/or
	* modify it under the terms of the GNU General Public License
	* as published by the Free Software Foundation; either version 2
	* of the License, or (at your option) any later version,
	* provided that any use properly credits the author.
	* This program is distributed in the hope that it will be useful,
	* but WITHOUT ANY WARRANTY; without even the implied warranty of
	* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
	* GNU General Public License for more details at http://www.gnu.org * * */
	public static void main(String[] args) throws Exception
	{
		DocumentBuilderFactory dbf = 
			DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		dbf.setValidating(false);
		
		db.setEntityResolver(new EntityResolver() {
	          public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId)
	                 throws SAXException, java.io.IOException
	          {
	            return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
	          }
		});
		
		
		Document document = 
		db.parse(ConfigReader.getGenbankCacheDir() + File.separator + 
				"D37875.xml");
		
		Element element = document.getDocumentElement();	
		//printNodeAndChildren(element, 0);
		
		Holder h = new Holder();
		getFirstGBSeq_Definition(element, h);
		System.out.println(h.returnVal);
		
	}
	
	private static void getFirstGBSeq_Definition( Node node, Holder h )
	{
		NodeList nodeList = node.getChildNodes();
		
		for( int x=0; x < nodeList.getLength() && h.returnVal == null; x++  )
		{
			Node childNode = nodeList.item(x);
			
			if( node.getNodeName().equals("GBSeq_definition") )
			{
				h.returnVal = childNode.getTextContent();
			}
			
			getFirstGBSeq_Definition(childNode, h);
		}		
	}
	
	static void printNodeAndChildren(Node node, int depth)
	{
		for( int x=0; x < depth; x++)
			System.out.print("\t");
		
		System.out.println(node.getNodeName() + "=" + node.getNodeValue());
		
		depth++;
		
		NodeList nodeList = node.getChildNodes();
		
		for( int x=0; x < nodeList.getLength(); x++  )
			printNodeAndChildren(nodeList.item(x), depth);
	}
	
	private static class Holder
	{
		String returnVal = null;
	}
	

	
	

}
