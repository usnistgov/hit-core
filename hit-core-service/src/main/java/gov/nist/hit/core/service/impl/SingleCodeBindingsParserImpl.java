/**
 * This software was developed at the National Institute of Standards and Technology by employees of
 * the Federal Government in the course of their official duties. Pursuant to title 17 Section 105
 * of the United States Code this software is not subject to copyright protection and is in the
 * public domain. This is an experimental system. NIST assumes no responsibility whatsoever for its
 * use by other parties, and makes no guarantees, expressed or implied, about its quality,
 * reliability, or any other characteristic. We would appreciate acknowledgement if the software is
 * used. This software can be redistributed and/or modified freely provided that any derivative
 * works bear some notice that they are derived from it, and any modified versions bear some notice
 * that they have been modified.
 */

package gov.nist.hit.core.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import gov.nist.hit.core.domain.singlecodebindings.ByID;
import gov.nist.hit.core.domain.singlecodebindings.ByName;
import gov.nist.hit.core.domain.singlecodebindings.ByNameOrByID;
import gov.nist.hit.core.domain.singlecodebindings.Context;
import gov.nist.hit.core.domain.singlecodebindings.SimpleBindingLocation;
import gov.nist.hit.core.domain.singlecodebindings.SingleCodeBinding;
import gov.nist.hit.core.domain.singlecodebindings.SingleCodeBindings;
import gov.nist.hit.core.domain.singlecodebindings.ComplexBindingLocation;
import gov.nist.hit.core.service.SingleCodeBindingsParser;

public class SingleCodeBindingsParserImpl implements SingleCodeBindingsParser {

	private TransformerFactory transFactory;
	private Transformer transformer;
	private StringWriter buffer;

	public SingleCodeBindingsParserImpl() {
		super();

		try {
			transFactory = TransformerFactory.newInstance();
			transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	

	private void scbContext(Element elmContext, Context contextObj) {

		if (elmContext != null) {
			NodeList nodes = elmContext.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				if (nodes.item(i).getNodeName().equals("ByName")) {
					ByName byNameObj = new ByName();
					Element elmByName = (Element) nodes.item(i);
					byNameObj.setByName(elmByName.getAttribute("Name"));
					singlecodebindings(elmByName, byNameObj);
					contextObj.getByNameOrByIDs().add(byNameObj);
				} else if (nodes.item(i).getNodeName().equals("ByID")) {
					ByID byIDObj = new ByID();
					Element elmByID = (Element) nodes.item(i);
					byIDObj.setByID(elmByID.getAttribute("ID"));
					singlecodebindings(elmByID, byIDObj);
					contextObj.getByNameOrByIDs().add(byIDObj);
				}
			}
		}
	}

	

	private void singlecodebindings(Element elmByNameOrByID, ByNameOrByID byNameOrByIDObj) {

		// SingleCodeBinding
		NodeList SingleCodeBindingNodes = elmByNameOrByID.getElementsByTagName("SingleCodeBinding");

		for (int i = 0; i < SingleCodeBindingNodes.getLength(); i++) {
			SingleCodeBinding singleCodeBindingObj = new SingleCodeBinding();
			Element elmConstraint = (Element) SingleCodeBindingNodes.item(i);
			singleCodeBindingObj.setSingleCodeBindingId(elmConstraint.getAttribute("ID"));
			singleCodeBindingObj.setSingleCodeBindingTarget(elmConstraint.getAttribute("Target"));
			singleCodeBindingObj.setSingleCodeBindingCode(elmConstraint.getAttribute("Code"));
			singleCodeBindingObj.setSingleCodeBindingCodeSystem(elmConstraint.getAttribute("CodeSystem"));
			singleCodeBindingObj.setBindingLocations(this.convertElementToString(elmConstraint.getElementsByTagName("BindingLocations").item(0)));

			NodeList bindingLocationsNodes = elmConstraint.getElementsByTagName("BindingLocations");
			for (int bl = 0; bl < bindingLocationsNodes.getLength(); bl++) {
				Element bindingLocationEle = (Element) bindingLocationsNodes.item(bl);
				NodeList complexBindingLocationNodes = bindingLocationEle.getElementsByTagName("ComplexBindingLocation");
				for (int cbl = 0; cbl < complexBindingLocationNodes.getLength(); cbl++) {
					Element elmcomplexBindingLocation = (Element) complexBindingLocationNodes.item(cbl);
					singleCodeBindingObj.getBindingLocationList().add(new ComplexBindingLocation(elmcomplexBindingLocation.getAttribute("CodeLocation"),
							elmcomplexBindingLocation.getAttribute("CodeSystemLocation"), elmcomplexBindingLocation.getAttribute("CodeSystemOIDLocation")));
				}

				NodeList simpleBindingLocationNodes = bindingLocationEle.getElementsByTagName("SimpleBindingLocation");
				for (int sbl = 0; sbl < simpleBindingLocationNodes.getLength(); sbl++) {
					Element elmSimpleBindingLocation = (Element) simpleBindingLocationNodes.item(sbl);
					singleCodeBindingObj.getBindingLocationList().add(new SimpleBindingLocation(elmSimpleBindingLocation.getAttribute("CodeLocation")));
				}

			}
			byNameOrByIDObj.addSingleCodeBinding(singleCodeBindingObj);
		}

	}

	private String convertElementToString(Node node) {
		try {
			buffer = new StringWriter();
			transformer.transform(new DOMSource(node), new StreamResult(buffer));
			return buffer.toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Document stringToDom(String xmlSource) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setXIncludeAware(false);
		factory.setNamespaceAware(true);
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xmlSource)));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	

	@Override
	public SingleCodeBindings singleCodeBindings(String xmlvsb) {
		SingleCodeBindings singleCodeBindings = new SingleCodeBindings();
		Document valueSetBindingsContextDoc = this.stringToDom(xmlvsb);

		if (valueSetBindingsContextDoc.getElementsByTagName("SingleCodeBindings") != null) {

			// single code
			Element elmSingleCodeBindings = (Element) valueSetBindingsContextDoc.getElementsByTagName("SingleCodeBindings").item(0);

			if (elmSingleCodeBindings != null) {
				Context datatypeContextObj = new Context();
				Context segmentContextObj = new Context();
				Context groupContextObj = new Context();
				Context messageContextObj = new Context();

				if (elmSingleCodeBindings.getElementsByTagName("Datatype") != null) {
					this.scbContext((Element) elmSingleCodeBindings.getElementsByTagName("Datatype").item(0), datatypeContextObj);
				}

				if (elmSingleCodeBindings.getElementsByTagName("Segment") != null) {
					this.scbContext((Element) elmSingleCodeBindings.getElementsByTagName("Segment").item(0), segmentContextObj);
				}

				if (elmSingleCodeBindings.getElementsByTagName("Group") != null) {
					this.scbContext((Element) elmSingleCodeBindings.getElementsByTagName("Group").item(0), groupContextObj);
				}
				if (elmSingleCodeBindings.getElementsByTagName("Message") != null) {
					this.scbContext((Element) elmSingleCodeBindings.getElementsByTagName("Message").item(0), messageContextObj);
				}

				singleCodeBindings.setDatatypes(datatypeContextObj);
				singleCodeBindings.setSegments(segmentContextObj);
				singleCodeBindings.setGroups(groupContextObj);
				singleCodeBindings.setMessages(messageContextObj);
			}

		}

		return singleCodeBindings;
	}

	
}
