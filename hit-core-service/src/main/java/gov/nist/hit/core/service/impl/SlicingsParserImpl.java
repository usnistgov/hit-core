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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import gov.nist.hit.core.domain.slicings.AssertionSlice;
import gov.nist.hit.core.domain.slicings.AssertionSlicing;
import gov.nist.hit.core.domain.slicings.FieldSlicing;
import gov.nist.hit.core.domain.slicings.FieldSlicingContext;
import gov.nist.hit.core.domain.slicings.OccurrenceSlice;
import gov.nist.hit.core.domain.slicings.OccurrenceSlicing;
import gov.nist.hit.core.domain.slicings.ProfileSlicing;
import gov.nist.hit.core.domain.slicings.SegmentSlicing;
import gov.nist.hit.core.domain.slicings.SegmentSlicingGroupContext;
import gov.nist.hit.core.domain.slicings.SegmentSlicingMessageContext;
import gov.nist.hit.core.service.SlicingsParser;

public class SlicingsParserImpl implements SlicingsParser {

	private TransformerFactory transFactory;
	private Transformer transformer;
	private StringWriter buffer;
	private Set<String> segmentReferences = new HashSet<String>();
	private Set<String> dataTypeReferences  = new HashSet<String>();

	
	
	public SlicingsParserImpl() {
		super();

		try {
			transFactory = TransformerFactory.newInstance();
			transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ProfileSlicing slicings(String xmlSlicings) {
		ProfileSlicing profileSlicing = null;
		segmentReferences = new HashSet<String>();
		dataTypeReferences  = new HashSet<String>();
		
		if (xmlSlicings != null) {
			Document profileSlicingDoc = this.stringToDom(xmlSlicings);
			if (profileSlicingDoc != null && profileSlicingDoc.getElementsByTagName("ProfileSlicing") != null) {
				Element profileSlicingElement = (Element) profileSlicingDoc.getElementsByTagName("ProfileSlicing").item(0);
				profileSlicing = new ProfileSlicing(profileSlicingElement.getAttribute("ID"));
				profileSlicing.setSegmentSlicing(getSegmentSlicing(profileSlicingElement));
				profileSlicing.setFieldSlicing(getFieldSlicing(profileSlicingElement));
			}			
			profileSlicing.setSegmentReferences(segmentReferences);
			profileSlicing.setDataTypeReferences(dataTypeReferences);
		}
		
		return profileSlicing;
	}

	private SegmentSlicing getSegmentSlicing(Element profileSlicingElement) {
		SegmentSlicing segmentSlicing = null;
		if (profileSlicingElement != null && profileSlicingElement.getElementsByTagName("SegmentSlicing") != null) {
			NodeList segmentSlicingNodes = profileSlicingElement.getElementsByTagName("SegmentSlicing");
			Element segmentSlicingElement = (Element) segmentSlicingNodes.item(0);
			if(segmentSlicingElement != null) {
				segmentSlicing = new SegmentSlicing();
				segmentSlicing.setMessages(getMessages(segmentSlicingElement));
			}
			
			
		}
		return segmentSlicing;
	}
	
	private FieldSlicing getFieldSlicing(Element profileSlicingElement) {
		FieldSlicing fieldSlicing = null;
		if (profileSlicingElement != null && profileSlicingElement.getElementsByTagName("FieldSlicing") != null) {
			NodeList fieldSlicingNodes = profileSlicingElement.getElementsByTagName("FieldSlicing");
			Element fieldSlicingElement = (Element) fieldSlicingNodes.item(0);
			if(fieldSlicingElement != null) {
				fieldSlicing = new FieldSlicing();
				fieldSlicing.setSegmentContexts(getSegmentContexts(fieldSlicingElement));
			}
		}
		return fieldSlicing;
	}

	
	private List<SegmentSlicingMessageContext> getMessages(Element segmentSlicingElement) {
		List<SegmentSlicingMessageContext> list = new ArrayList<SegmentSlicingMessageContext>();
		if (segmentSlicingElement != null && segmentSlicingElement.getElementsByTagName("Message") != null) {
			NodeList messageNodes = segmentSlicingElement.getElementsByTagName("Message");
			for (int i = 0; i < messageNodes.getLength(); i++) {
				Element messageElement = (Element) messageNodes.item(i);
				SegmentSlicingMessageContext segmentSlicingMessageContext = new SegmentSlicingMessageContext(messageElement.getAttribute("ID"));
				segmentSlicingMessageContext.setGroupContexts(getSegmentSlicingGroupContext(messageElement));
				list.add(segmentSlicingMessageContext);
			}
		}
		return list;
	}
	
	private List<FieldSlicingContext> getSegmentContexts(Element fieldSlicingElement) {
		List<FieldSlicingContext> list = new ArrayList<FieldSlicingContext>();
		if (fieldSlicingElement != null && fieldSlicingElement.getElementsByTagName("SegmentContext") != null) {
			NodeList segmentContextgNodes = fieldSlicingElement.getElementsByTagName("SegmentContext");
			for (int i = 0; i < segmentContextgNodes.getLength(); i++) {
				Element segmentContextElement = (Element) segmentContextgNodes.item(i);
				FieldSlicingContext fieldSlicingContext = new FieldSlicingContext(segmentContextElement.getAttribute("ID"));
				fieldSlicingContext.setAssertionSlicing(getAssertionSlicing(segmentContextElement,"segment"));
				fieldSlicingContext.setOccurrenceSlicing(getOccurrenceSlicing(segmentContextElement,"segment"));
				list.add(fieldSlicingContext);
			}
		}
		return list;
	}


	private List<SegmentSlicingGroupContext> getSegmentSlicingGroupContext(Element messageElement) {
		List<SegmentSlicingGroupContext> list = new ArrayList<SegmentSlicingGroupContext>();
		if (messageElement != null && messageElement.getElementsByTagName("GroupContext") != null) {
			NodeList segmentSlicingGroupContextNodes = messageElement.getElementsByTagName("GroupContext");
			for (int i = 0; i < segmentSlicingGroupContextNodes.getLength(); i++) {
				Element segmentSlicingGroupContextElement = (Element) segmentSlicingGroupContextNodes.item(i);
				SegmentSlicingGroupContext segmentSlicingGroupContext = new SegmentSlicingGroupContext(segmentSlicingGroupContextElement.getAttribute("ID"));				
				segmentSlicingGroupContext.setAssertionSlicing(getAssertionSlicing(segmentSlicingGroupContextElement,"group"));
				segmentSlicingGroupContext.setOccurrenceSlicing(getOccurrenceSlicing(segmentSlicingGroupContextElement,"group"));

				list.add(segmentSlicingGroupContext);
			}
		}
		return list;
	}
	
	private List<AssertionSlicing> getAssertionSlicing(Element element, String context) {
		List<AssertionSlicing> list = new ArrayList<AssertionSlicing>();
		if (element != null && element.getElementsByTagName("AssertionSlicing") != null) {
			NodeList assertionSlicingNodes = element.getElementsByTagName("AssertionSlicing");
			for (int i = 0; i < assertionSlicingNodes.getLength(); i++) {
				Element assertionSlicingElement = (Element) assertionSlicingNodes.item(i);
				AssertionSlicing assertionSlicing = new AssertionSlicing(Integer.parseInt(assertionSlicingElement.getAttribute("Position")));				
				assertionSlicing.setSlices(getAssertionSlices(assertionSlicingElement,context));
				list.add(assertionSlicing);
			}
		}
		return list;
	}

	
	private List<AssertionSlice> getAssertionSlices(Element element, String context) {
		List<AssertionSlice> list = new ArrayList<AssertionSlice>();
		if (element != null && element.getElementsByTagName("Slice") != null) {
			NodeList assertionSliceNodes = element.getElementsByTagName("Slice");
			for (int i = 0; i < assertionSliceNodes.getLength(); i++) {
				Element assertionSliceElement = (Element) assertionSliceNodes.item(i);
				AssertionSlice assertionSlice = new AssertionSlice(assertionSliceElement.getAttribute("Ref"));				
				try {
					assertionSlice.setAssertion(getInnerXml((Element)assertionSliceElement.getElementsByTagName("Assertion").item(0)));
				} catch (Exception e) {
					e.printStackTrace();
				}
				assertionSlice.setDescription(assertionSliceElement.getElementsByTagName("Description").item(0).getTextContent());
				if (context.equalsIgnoreCase("group")) {
					segmentReferences.add(assertionSlice.getRef());
				}else if (context.equalsIgnoreCase("segment")) {
					dataTypeReferences.add(assertionSlice.getRef());
				}
				list.add(assertionSlice);
			}
		}
		return list;
	}
	
	private List<OccurrenceSlicing> getOccurrenceSlicing(Element element, String context) {
		List<OccurrenceSlicing> list = new ArrayList<OccurrenceSlicing>();
		if (element != null && element.getElementsByTagName("OccurrenceSlicing") != null) {
			NodeList occurrenceSlicingNodes = element.getElementsByTagName("OccurrenceSlicing");
			for (int i = 0; i < occurrenceSlicingNodes.getLength(); i++) {
				Element occurrenceSlicingElement = (Element) occurrenceSlicingNodes.item(i);
				OccurrenceSlicing occurrenceSlicing = new OccurrenceSlicing(Integer.parseInt(occurrenceSlicingElement.getAttribute("Position")));				
				occurrenceSlicing.setSlices(getOccurrenceSlices(occurrenceSlicingElement,context));
				list.add(occurrenceSlicing);
			}
		}
		return list;
	}

	
	private List<OccurrenceSlice> getOccurrenceSlices(Element element, String context) {
		List<OccurrenceSlice> list = new ArrayList<OccurrenceSlice>();
		if (element != null && element.getElementsByTagName("Slice") != null) {
			NodeList occurrenceSliceNodes = element.getElementsByTagName("Slice");
			for (int i = 0; i < occurrenceSliceNodes.getLength(); i++) {
				Element assertionSliceElement = (Element) occurrenceSliceNodes.item(i);
				OccurrenceSlice occurrenceSlice = new OccurrenceSlice();				
				occurrenceSlice.setOccurrence(Integer.parseInt(assertionSliceElement.getAttribute("Occurrence")));
				occurrenceSlice.setRef(assertionSliceElement.getAttribute("Ref"));
				list.add(occurrenceSlice);
				if (context.equalsIgnoreCase("group")) {
					segmentReferences.add(occurrenceSlice.getRef());
				}else if (context.equalsIgnoreCase("segment")) {
					dataTypeReferences.add(occurrenceSlice.getRef());
				}
			}
		}
		return list;
	}
	
	

	
	private String getInnerXml(Element element) throws Exception {
	    StringBuilder sb = new StringBuilder();
	    NodeList children = element.getChildNodes();
	    for (int i = 0; i < children.getLength(); i++) {
	        Node child = children.item(i);
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.setOutputProperty(OutputKeys.INDENT, "no");
	        StringWriter writer = new StringWriter();
	        transformer.transform(new DOMSource(child), new StreamResult(writer));
	        sb.append(writer.toString());
	    }
	    return sb.toString();
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
}
