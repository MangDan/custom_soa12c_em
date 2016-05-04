package oracle.bpm.workspace.client.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlUtility {
	public static Element loadDocument(File location) {
		Document doc = null;
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
			doc = parser.parse(location);
			Element root = doc.getDocumentElement();
			root.normalize();

			return root;
		} catch (SAXParseException err) {
			System.err.println(
					"URLMappingsXmlDAO ** Parsing error, line " + err.getLineNumber() + ", uri " + err.getSystemId());
			System.err.println("URLMappingsXmlDAO error: " + err.getMessage());
		} catch (SAXException e) {
			System.err.println("URLMappingsXmlDAO error: " + e);
		} catch (MalformedURLException mfx) {
			System.err.println("URLMappingsXmlDAO error: " + mfx);
		} catch (IOException e) {
			System.err.println("URLMappingsXmlDAO error: " + e);
		} catch (Exception pce) {
			System.err.println("URLMappingsXmlDAO error: " + pce);
		}
		return null;
	}

	public static Element loadDocument(String value, String type) {
		Document doc = null;
		InputSource xmlInp = null;
		try {
			if (type.equals("location")) {
				URL url = new URL(value);
				xmlInp = new InputSource(url.openStream());
			} else {
				xmlInp = new InputSource(new StringReader(value));
			}
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
			doc = parser.parse(xmlInp);
			Element root = doc.getDocumentElement();
			root.normalize();
			return root;
		} catch (SAXParseException err) {
			System.err.println(
					"URLMappingsXmlDAO ** Parsing error, line " + err.getLineNumber() + ", uri " + err.getSystemId());
			System.err.println("URLMappingsXmlDAO error: " + err.getMessage());
		} catch (SAXException e) {
			System.err.println("URLMappingsXmlDAO error: " + e);
		} catch (MalformedURLException mfx) {
			System.err.println("URLMappingsXmlDAO error: " + mfx);
		} catch (IOException e) {
			System.err.println("URLMappingsXmlDAO error: " + e);
		} catch (Exception pce) {
			System.err.println("URLMappingsXmlDAO error: " + pce);
		}
		return null;
	}

	public static Element loadDocument(Reader target) {
		Document doc = null;
		try {
			InputSource xmlInp = new InputSource(target);

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = docBuilderFactory.newDocumentBuilder();
			doc = parser.parse(xmlInp);
			Element root = doc.getDocumentElement();
			root.normalize();
			return root;
		} catch (SAXParseException err) {
			System.err.println(
					"URLMappingsXmlDAO ** Parsing error, line " + err.getLineNumber() + ", uri " + err.getSystemId());
			System.err.println("URLMappingsXmlDAO error: " + err.getMessage());
		} catch (SAXException e) {
			System.err.println("URLMappingsXmlDAO error: " + e);
		} catch (MalformedURLException mfx) {
			System.err.println("URLMappingsXmlDAO error: " + mfx);
		} catch (IOException e) {
			System.err.println("URLMappingsXmlDAO error: " + e);
		} catch (Exception pce) {
			System.err.println("URLMappingsXmlDAO error: " + pce);
		}
		return null;
	}

	public static String getTagValue(Element root, String tagName) {
		String returnString = "";
		NodeList list = root.getElementsByTagName(tagName);
		for (int loop = 0; loop < list.getLength(); loop++) {
			Node node = list.item(loop);
			if (node != null) {
				Node child = node.getFirstChild();
				if ((child != null) && (child.getNodeValue() != null)) {
					return child.getNodeValue();
				}
			}
		}
		return returnString;
	}

	public static String getSubTagValue(Element root, String tagName, String subTagName) {
		String returnString = "";
		NodeList list = root.getElementsByTagName(tagName);
		for (int loop = 0; loop < list.getLength(); loop++) {
			Node node = list.item(loop);
			if (node != null) {
				NodeList children = node.getChildNodes();
				for (int innerLoop = 0; innerLoop < children.getLength(); innerLoop++) {
					Node child = children.item(innerLoop);
					if ((child != null) && (child.getNodeName() != null) && (child.getNodeName().equals(subTagName))) {
						Node grandChild = child.getFirstChild();
						if (grandChild.getNodeValue() != null) {
							return grandChild.getNodeValue();
						}
					}
				}
			}
		}
		return returnString;
	}

	public static String getSubTagAttribute(Element root, String tagName, String subTagName, String attribute) {
		String returnString = "";
		NodeList list = root.getElementsByTagName(tagName);
		for (int loop = 0; loop < list.getLength(); loop++) {
			Node node = list.item(loop);
			if (node != null) {
				NodeList children = node.getChildNodes();
				for (int innerLoop = 0; innerLoop < children.getLength(); innerLoop++) {
					Node child = children.item(innerLoop);
					if ((child != null) && (child.getNodeName() != null) && (child.getNodeName().equals(subTagName))
							&& ((child instanceof Element))) {
						return ((Element) child).getAttribute(attribute);
					}
				}
			}
		}
		return returnString;
	}

	private static String getSubTagValue(Node node, String subTagName) {
		String returnString = "";
		if (node != null) {
			NodeList children = node.getChildNodes();
			for (int innerLoop = 0; innerLoop < children.getLength(); innerLoop++) {
				Node child = children.item(innerLoop);
				if ((child != null) && (child.getNodeName() != null) && (child.getNodeName().equals(subTagName))) {
					Node grandChild = child.getFirstChild();
					if (grandChild.getNodeValue() != null) {
						return grandChild.getNodeValue();
					}
				}
			}
		}
		return returnString;
	}

	public static void nodeSubInsert(Element root, String table, String queryType, String queryValue) {
		Node find = getTag(root, table);
		Document doc = find.getOwnerDocument();

		Element type = doc.createElement(queryType);

		Text value = doc.createTextNode(queryValue);

		type.appendChild(value);
		find.appendChild(type);

		print(doc);
	}

	public static void nodeSubUpdate(Element root, String table, String queryType, String queryValue) {
		Node find = getTag(root, table);
		Document doc = find.getOwnerDocument();

		NodeList nl = find.getChildNodes();
		int numnodes = nl.getLength();

		System.out.println("length : " + numnodes);

		Node replNode = null;
		for (int i = 0; i < numnodes; i++) {
			Node n = nl.item(i);
			String name = n.getNodeName();
			if (name.equals(queryType)) {
				replNode = n;
				System.out.println("Finding Node : " + replNode.getNodeName());

				break;
			}
		}
		Element type = doc.createElement(queryType);
		Text value = doc.createTextNode(queryValue);

		type.appendChild(value);

		find.replaceChild(type, replNode);

		print(doc);
	}

	public static void nodeSubDelete(Element root, String table, String queryType) {
		Node find = getTag(root, table);
		Document doc = find.getOwnerDocument();

		NodeList nl = find.getChildNodes();
		int numnodes = nl.getLength();

		System.out.println("length : " + numnodes);

		Node deleteNode = null;
		for (int i = 0; i < numnodes; i++) {
			Node n = nl.item(i);
			String name = n.getNodeName();
			if (name.equals(queryType)) {
				deleteNode = n;
				System.out.println("Finding Node : " + deleteNode.getNodeName());

				break;
			}
		}
		find.removeChild(deleteNode);

		print(doc);
	}

	public static Node getTag(Element root, String tagName) {
		NodeList list = root.getElementsByTagName(tagName);
		for (int loop = 0; loop < list.getLength(); loop++) {
			Node node = list.item(loop);
			System.out.println("nodeName : " + node.getNodeName() + ":::::" + node.getNodeType());
			if (node.getNodeName().equals(tagName)) {
				return node;
			}
		}
		return null;
	}

	public static void print(Document doc) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty("encoding", "utf-8");

			transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(System.out, "utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void write(Element root, String xmlPath) {
		try {
			Document doc = root.getOwnerDocument();
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty("encoding", "utf-8");

			FileWriter fw = new FileWriter(xmlPath);

			transformer.transform(new DOMSource(doc), new StreamResult(fw));

			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String transText(Node doc) throws Exception {
		TransformerFactory transfactory = TransformerFactory.newInstance();
		Transformer transformer = transfactory.newTransformer();
		transformer.setOutputProperty("omit-xml-declaration", "yes");
		transformer.setOutputProperty("indent", "yes");

		Source source = new DOMSource(doc);
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		transformer.transform(source, result);
		return sw.toString();
	}

	public static NodeList xPath(Node context, String xpath) throws Exception {
		Templates txp = TrAXPath(xpath);
		return selectNodeList(context, txp.newTransformer());
	}

	public static Node trance(Node context, String xpath) throws Exception {
		Templates txp = TrAXPath(xpath);
		return trance(context, txp.newTransformer());
	}

	public static Templates TrAXPath(String xpath) throws TransformerConfigurationException {
		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		sb.append("<xsl:stylesheet version=\"1.0\" ");
		sb.append(" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">");
		sb.append("<xsl:output method=\"xml\" indent=\"yes\"/>");
		sb.append("<xsl:template match=\"" + xpath + "\">");
		sb.append("<xsl:copy-of select=\".\"/>");
		sb.append("</xsl:template>");
		sb.append("<xsl:template match=\"*|@*|text()\">");
		sb.append("<xsl:apply-templates />");
		sb.append("</xsl:template>");
		sb.append("</xsl:stylesheet>");

		TransformerFactory tf = TransformerFactory.newInstance();
		String stylesheet = sb.toString();
		Reader r = new StringReader(stylesheet);
		StreamSource ssrc = new StreamSource(r);

		return tf.newTemplates(ssrc);
	}

	public static NodeList selectNodeList(Node context, Transformer t) throws Exception {
		DocumentFragment df = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
				.createDocumentFragment();

		Result result = new DOMResult(df);

		t.transform(new DOMSource(context), result);

		return df.getChildNodes();
	}

	public static Node trance(Node context, Transformer t) throws Exception {
		DocumentFragment df = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
				.createDocumentFragment();

		Result result = new DOMResult(df);

		t.transform(new DOMSource(context), result);

		return df;
	}

	public static Object transformXmlNodesIntoMap(NodeList nodes) {
		Map<String, Object> nodeMap = new HashMap();
		if ((nodes.getLength() == 1) && (nodes.item(0).getNodeType() == 3)) {
			return nodes.item(0).getFirstChild().getNodeValue();
		}
		for (int nodeIdx = 0; nodeIdx < nodes.getLength(); nodeIdx++) {
			Node node = nodes.item(nodeIdx);
			NodeList subNodes = node.getChildNodes();
			if (node.getNodeType() != 3) {
				nodeMap.put(node.getNodeName(), transformXmlNodesIntoMap(subNodes));
			}
		}
		return nodeMap;
	}

	public static Object transformXmlNodesIntoMap(Node node) {
		Map<String, Object> nodeMap = new HashMap();

		NodeList subNodes = node.getChildNodes();

		NamedNodeMap nodeAttrs = node.getAttributes();
		for (int nodeAttrIdx = 0; nodeAttrIdx < nodeAttrs.getLength(); nodeAttrIdx++) {
			Node attrNode = nodeAttrs.item(nodeAttrIdx);

			nodeMap.put("@" + attrNode.getNodeName(), attrNode.getFirstChild().getNodeValue());
		}
		if (nodeAttrs.getLength() == 0) {
			if (subNodes.getLength() == 0) {
				return "";
			}
			if ((subNodes.getLength() == 1) && (subNodes.item(0).getNodeType() == 3)) {
				return subNodes.item(0).getFirstChild().getNodeValue();
			}
		}
		for (int subNodeIdx = 0; subNodeIdx < subNodes.getLength(); subNodeIdx++) {
			Node subNode = subNodes.item(subNodeIdx);
			if (subNode.getNodeType() == 3) {
				nodeMap.put(subNode.getNodeName(), subNode.getFirstChild().getNodeValue());
			} else if (nodeMap.containsKey(subNode.getNodeName())) {
				Object subObject = nodeMap.get(subNode.getNodeName());
				if ((subObject instanceof List)) {
					((List) subObject).add(transformXmlNodesIntoMap(subNode));
				} else {
					List<Object> subObjectList = new ArrayList();
					subObjectList.add(subObject);
					subObjectList.add(transformXmlNodesIntoMap(subNode));
					nodeMap.put(subNode.getNodeName(), subObjectList);
				}
			} else {
				nodeMap.put(subNode.getNodeName(), transformXmlNodesIntoMap(subNode));
			}
		}
		return nodeMap;
	}
}
