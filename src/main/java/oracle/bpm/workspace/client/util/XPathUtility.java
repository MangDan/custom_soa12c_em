package oracle.bpm.workspace.client.util;

import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import oracle.bpel.services.common.util.XMLUtil;
import oracle.fabric.common.xml.xpath.SimpleNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XPathUtility {
	public static void setNode(Element payload, Map namespacemap, String xpath, Node appendNode) throws Exception {
		NodeList nodes = null;
		try {
			nodes = selectNodes(XMLUtil.getDocumentElement(payload), xpath, namespacemap);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		if (nodes.getLength() > 0) {
			Element currentEle = (Element) nodes.item(0);

			currentEle.getParentNode().replaceChild(appendNode, currentEle);
		}
	}

	public static void setNodeValue(Element payload, Map namespacemap, String xpath, String value) throws Exception {
		Node node = null;
		NodeList nodes = null;
		NodeList children = null;
		int length = 0;
		try {
			nodes = selectNodes(XMLUtil.getDocumentElement(payload), xpath, namespacemap);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		node = nodes.item(0);
		if (nodes.getLength() > 0) {
			children = node.getChildNodes();
			length = children.getLength();
			if (length == 1) {
				Node textNode = children.item(0);
				String oldValue = textNode.getNodeValue();
				value = value == null ? "" : value;
				oldValue = oldValue == null ? "" : oldValue;
				if (!oldValue.equals(value)) {
					textNode.setNodeValue(value);
				}
			} else if ((length == 0) && (value != null) && (!value.equals(""))) {
				Node valueNode = node.getOwnerDocument().createTextNode(value);
				node.appendChild(valueNode);
			}
		}
	}

	public static NodeList selectNodes(Element pElement, String xpathExpression, Map prefixNamespaceMapping)
			throws Exception {
		XPath xPath = createXPath(prefixNamespaceMapping);
		Document doc = null;
		if ((pElement instanceof Document)) {
			doc = (Document) pElement;
		} else {
			doc = pElement != null ? pElement.getOwnerDocument() : null;
		}
		NodeList value = (NodeList) xPath.evaluate(xpathExpression, doc, XPathConstants.NODESET);
		return value;
	}

	public static String getNodeValue(NodeList nodes) {
		Node node = nodes.item(0);
		String value = null;
		if (nodes.getLength() > 0) {
			NodeList children = node.getChildNodes();
			int length = children.getLength();
			if (length == 1) {
				Node textNode = children.item(0);
				value = textNode.getNodeValue();
			}
		}
		return value;
	}

	private static XPath createXPath(Map namespaceMapping) {
		XPath xpath = null;
		NamespaceContext nc = null;
		if (namespaceMapping != null) {
			if (namespaceMapping.containsKey("")) {
				namespaceMapping.remove("");
			}
			nc = new SimpleNamespaceContext(namespaceMapping);
		}
		xpath = getXPath(nc);
		return xpath;
	}

	public static XPath getXPath(NamespaceContext nsContext) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		if (nsContext != null) {
			xpath.setNamespaceContext(nsContext);
		}
		return xpath;
	}
}
