package vn.com.sonca.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils {
	
	/*
	 * get value of tagName in Element e 
	 */
	public static Document convertToDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	      dbf.setValidating(false);
	      dbf.setIgnoringComments(false);
	      dbf.setIgnoringElementContentWhitespace(false);
	      dbf.setNamespaceAware(true);
	      DocumentBuilder db = null;
	      db = dbf.newDocumentBuilder();
	      db.setEntityResolver(new NullResolver());
//	      db.setErrorHandler( new MyErrorHandler());
	      return db.parse(is);
	}
	public static String getValue(Element e, String tagName) {
		
		String value = "";		
		try {
			NodeList listNode = e.getElementsByTagName(tagName);
			Element elementName = (Element)listNode.item(0);
			NodeList textFNList = elementName.getChildNodes();
			value = ((Node)textFNList.item(0)).getNodeValue();
			value = value.trim();
		} catch (Exception ex) {
			
		}
		return value;
		
	}
	public static Document parse(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	      dbf.setValidating(false);
	      dbf.setIgnoringComments(false);
	      dbf.setIgnoringElementContentWhitespace(true);
	      dbf.setNamespaceAware(true);
	      DocumentBuilder db = null;
	      db = dbf.newDocumentBuilder();
	      db.setEntityResolver(new NullResolver());
	      // db.setErrorHandler( new MyErrorHandler());
	      return db.parse(is);
	}
	public static Document parse(String data) throws ParserConfigurationException, SAXException, IOException {
		  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

	      dbf.setValidating(false);
	      dbf.setIgnoringComments(false);
	      dbf.setIgnoringElementContentWhitespace(true);
	      dbf.setNamespaceAware(true);
	      DocumentBuilder db = null;
	      db = dbf.newDocumentBuilder();
//	      InputSource is = new InputSource();
//	      is.setCharacterStream(new StringReader(data));	      
	      return db.parse(new ByteArrayInputStream(data.getBytes()));
	}
}
class NullResolver implements EntityResolver {
	  public InputSource resolveEntity(String publicId, String systemId) throws SAXException,
	      IOException {
	    return new InputSource(new StringReader(""));
	  }
	}