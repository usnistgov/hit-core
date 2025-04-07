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
import java.util.List;

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

import gov.nist.hit.core.domain.coconstraints.ByMessage;
import gov.nist.hit.core.domain.coconstraints.CellsType;
import gov.nist.hit.core.domain.coconstraints.CoConstraint;
import gov.nist.hit.core.domain.coconstraints.CoConstraintContext;
import gov.nist.hit.core.domain.coconstraints.CoConstraintGroup;
import gov.nist.hit.core.domain.coconstraints.CoConstraintGroupId;
import gov.nist.hit.core.domain.coconstraints.Code;
import gov.nist.hit.core.domain.coconstraints.Condition;
import gov.nist.hit.core.domain.coconstraints.ConditionalTable;
import gov.nist.hit.core.domain.coconstraints.Context;
import gov.nist.hit.core.domain.coconstraints.IdPath;
import gov.nist.hit.core.domain.coconstraints.PlainText;
import gov.nist.hit.core.domain.coconstraints.Segment;
import gov.nist.hit.core.domain.coconstraints.SimpleTable;
import gov.nist.hit.core.domain.coconstraints.ValueSet;
import gov.nist.hit.core.domain.valuesetbindings.Binding;
import gov.nist.hit.core.domain.valuesetbindings.ComplexBindingLocation;
import gov.nist.hit.core.domain.valuesetbindings.SimpleBindingLocation;
import gov.nist.hit.core.domain.valuesetbindings.ValueSetBinding;
import gov.nist.hit.core.service.CoConstraintsParser;

public class CoConstraintsParserImpl implements CoConstraintsParser {

	private TransformerFactory transFactory;
	private Transformer transformer;
	private StringWriter buffer;

	private List<String> externalVSIdentifiers;

	public void setExternalVSIdentifiers(List<String> externalVSIdentifiers) {
		this.externalVSIdentifiers = externalVSIdentifiers;
	}

	public CoConstraintsParserImpl() {
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
	public CoConstraintContext coConstraints(String xmlCoConstraints) {
		CoConstraintContext coConstraintContext = null;
		if (xmlCoConstraints != null) {
			Document coConstraintContextDoc = this.stringToDom(xmlCoConstraints);
			if (coConstraintContextDoc != null && coConstraintContextDoc.getElementsByTagName("CoConstraintContext") != null) {
				Element coConstraintContextElement = (Element) coConstraintContextDoc.getElementsByTagName("CoConstraintContext").item(0);
				coConstraintContext = new CoConstraintContext(coConstraintContextElement.getAttribute("ID"));
				coConstraintContext.setByMessages(getByMessages(coConstraintContextElement));
			}						
		}

		return coConstraintContext;
	}

	private List<ByMessage> getByMessages(Element coConstraintContextElement) {
		List<ByMessage> list = new ArrayList<ByMessage>();
		if (coConstraintContextElement != null && coConstraintContextElement.getElementsByTagName("ByMessage") != null) {
			NodeList byMessagesNodes = coConstraintContextElement.getElementsByTagName("ByMessage");
			for (int i = 0; i < byMessagesNodes.getLength(); i++) {
				Element byMessageElement = (Element) byMessagesNodes.item(i);
				ByMessage byMessage = new ByMessage(byMessageElement.getAttribute("ID"));
				byMessage.setContexts(getContexts(byMessageElement));
				list.add(byMessage);
			}
		}
		return list;
	}

	private List<Context> getContexts(Element byMessageElement) {
		List<Context> list = new ArrayList<Context>();
		if (byMessageElement != null && byMessageElement.getElementsByTagName("Context") != null) {
			NodeList contextNodes = byMessageElement.getElementsByTagName("Context");
			for (int i = 0; i < contextNodes.getLength(); i++) {
				Element contextElement = (Element) contextNodes.item(i);
				Context context = new Context(contextElement.getAttribute("Name"), contextElement.getAttribute("Path"));
				context.setSegments(getSegments(contextElement));
				list.add(context);
			}
		}
		return list;
	}

	private List<Segment> getSegments(Element contextElement) {
		List<Segment> list = new ArrayList<Segment>();
		if (contextElement != null && contextElement.getElementsByTagName("Segment") != null) {
			NodeList segmentNodes = contextElement.getElementsByTagName("Segment");
			for (int i = 0; i < segmentNodes.getLength(); i++) {
				Element segmentElement = (Element) segmentNodes.item(i);
				Segment segment = new Segment(segmentElement.getAttribute("Name"), segmentElement.getAttribute("Path"));
				// check for simple and conditional look and parse for both but only one should
				// be present at a time
				segment.setConditionalTables(getConditionalTables(segmentElement));
				segment.setSimpleTables(getSimpleTables(segmentElement));
				list.add(segment);
			}
		}
		return list;
	}

	
	private List<SimpleTable> getSimpleTables(Element segmentElement) {
		List<SimpleTable> list = new ArrayList<SimpleTable>();
		if (segmentElement != null && segmentElement.getElementsByTagName("SimpleTable") != null) {
			NodeList simpleTableNodes = segmentElement.getElementsByTagName("SimpleTable");
			for (int i = 0; i < simpleTableNodes.getLength(); i++) {
				Element simpleTableElement = (Element) simpleTableNodes.item(i);
				SimpleTable simpleTable = new SimpleTable();
				// get CoConstraintGroupId
				if (simpleTableElement != null && simpleTableElement.getElementsByTagName("CoConstraintGroupId") != null) {
					Element coConstraintGroupIdElement = (Element) simpleTableElement.getElementsByTagName("CoConstraintGroupId").item(0);
					if (coConstraintGroupIdElement != null && coConstraintGroupIdElement.getElementsByTagName("IdPath") != null) {
						Element idPathElement = (Element) coConstraintGroupIdElement.getElementsByTagName("IdPath").item(0);
						IdPath idPath = new IdPath();
						idPath.setName(idPathElement.getAttribute("Name"));
						idPath.setPath(idPathElement.getAttribute("Path"));
						idPath.setPriority(idPathElement.getAttribute("Priority"));
						CoConstraintGroupId coCoConstraintGroupId = new CoConstraintGroupId(idPath);
						simpleTable.setCoConstraintGroupId(coCoConstraintGroupId);
					}
				}				
				// coConstraints and CoConstraintGroup				
				simpleTable.setCoConstraints(getCoConstraints(simpleTableElement));
				simpleTable.setCoConstraintGroups(getCoConstraintGroups(simpleTableElement));
				list.add(simpleTable);
			}
		}
		return list;
	}
	
	private List<ConditionalTable> getConditionalTables(Element segmentElement) {
		List<ConditionalTable> list = new ArrayList<ConditionalTable>();
		if (segmentElement != null && segmentElement.getElementsByTagName("ConditionalTable") != null) {
			NodeList conditionalTableNodes = segmentElement.getElementsByTagName("ConditionalTable");
			for (int i = 0; i < conditionalTableNodes.getLength(); i++) {
				Element conditionalTableElement = (Element) conditionalTableNodes.item(i);
				ConditionalTable conditionalTable = new ConditionalTable();
				// get CoConstraintGroupId
				if (conditionalTableElement != null && conditionalTableElement.getElementsByTagName("CoConstraintGroupId") != null) {
					Element coConstraintGroupIdElement = (Element) conditionalTableElement.getElementsByTagName("CoConstraintGroupId").item(0);
					if (coConstraintGroupIdElement !=null && coConstraintGroupIdElement.getElementsByTagName("IdPath") != null) {
						Element idPathElement = (Element) coConstraintGroupIdElement.getElementsByTagName("IdPath").item(0);
						IdPath idPath = new IdPath();
						idPath.setName(idPathElement.getAttribute("Name"));
						idPath.setPath(idPathElement.getAttribute("Path"));
						idPath.setPriority(idPathElement.getAttribute("Priority"));
						CoConstraintGroupId coCoConstraintGroupId = new CoConstraintGroupId(idPath);
						conditionalTable.setCoConstraintGroupId(coCoConstraintGroupId);
					}
				}
				// get Condition
				if (conditionalTableElement != null &&conditionalTableElement.getElementsByTagName("Condition") != null) {
					Element conditionElement = (Element) conditionalTableElement.getElementsByTagName("Condition").item(0);
					Condition condition = new Condition();
					if (conditionElement != null && conditionElement.getElementsByTagName("Assertion") != null) {
						condition.setAssertion(conditionElement.getElementsByTagName("Assertion").item(0).getTextContent());
					}
					if (conditionElement != null && conditionElement.getElementsByTagName("Description") != null) {
						condition.setDescription(conditionElement.getElementsByTagName("Description").item(0).getTextContent());
					}
					conditionalTable.setCondition(condition);
				}
				// coConstraints and CoConstraintGroup				
				conditionalTable.setCoConstraints(getCoConstraints(conditionalTableElement));
				conditionalTable.setCoConstraintGroups(getCoConstraintGroups(conditionalTableElement));
				list.add(conditionalTable);
			}
		}
		return list;
	}

	private List<CoConstraint> getCoConstraints(Element conditionalTableElement) {
		List<CoConstraint> list = new ArrayList<CoConstraint>();
		if (conditionalTableElement != null && conditionalTableElement.getElementsByTagName("CoConstraint") != null) {
			NodeList coConstraintNodes = conditionalTableElement.getElementsByTagName("CoConstraint");
			for (int i = 0; i < coConstraintNodes.getLength(); i++) {
				Element coConstraintElement = (Element) coConstraintNodes.item(i);
				CoConstraint coConstraint = new CoConstraint();
				coConstraint.setMin(coConstraintElement.getAttribute("Min"));
				coConstraint.setMax(coConstraintElement.getAttribute("Max"));
				coConstraint.setUsage(coConstraintElement.getAttribute("Usage"));

				if (coConstraintElement != null && coConstraintElement.getElementsByTagName("Selectors") != null) {
					Element selectorsElement = (Element) coConstraintElement.getElementsByTagName("Selectors").item(0);
					coConstraint.setSelectors(getCellsType(selectorsElement));
				}
				if (coConstraintElement != null && coConstraintElement.getElementsByTagName("Constraints") != null) {
					Element constraintsElement = (Element) coConstraintElement.getElementsByTagName("Constraints").item(0);
					coConstraint.setConstraints(getCellsType(constraintsElement));
				}

				list.add(coConstraint);
			}
		}
		return list;
	}
	
	private CoConstraint getPrimaryCoConstraint(Element element) {
		CoConstraint primary = new CoConstraint();
		if (element != null && element.getElementsByTagName("Primary") != null) {
			NodeList coConstraintNodes = element.getElementsByTagName("Primary");
			Element coConstraintElement = (Element) coConstraintNodes.item(0);	
				
			CoConstraint coConstraint = new CoConstraint();
			coConstraint.setMin(coConstraintElement.getAttribute("Min"));
			coConstraint.setMax(coConstraintElement.getAttribute("Max"));
			coConstraint.setUsage(coConstraintElement.getAttribute("Usage"));

			if (coConstraintElement !=null && coConstraintElement.getElementsByTagName("Selectors") != null) {
				Element selectorsElement = (Element) coConstraintElement.getElementsByTagName("Selectors").item(0);
				coConstraint.setSelectors(getCellsType(selectorsElement));
			}
			if (coConstraintElement !=null && coConstraintElement.getElementsByTagName("Constraints") != null) {
				Element constraintsElement = (Element) coConstraintElement.getElementsByTagName("Constraints").item(0);
				coConstraint.setConstraints(getCellsType(constraintsElement));
			}				
		
		}
		return primary;
	}
	
	private List<CoConstraintGroup> getCoConstraintGroups(Element conditionalTableElement) {
		List<CoConstraintGroup> list = new ArrayList<CoConstraintGroup>();
		if (conditionalTableElement != null && conditionalTableElement.getElementsByTagName("CoConstraintGroup") != null) {
			NodeList coConstraintGroupNodes = conditionalTableElement.getElementsByTagName("CoConstraintGroup");
			for (int i = 0; i < coConstraintGroupNodes.getLength(); i++) {
				Element coConstraintGroupElement = (Element) coConstraintGroupNodes.item(i);
				CoConstraintGroup coConstraintGroup = new CoConstraintGroup();
				
				if (coConstraintGroupElement != null && coConstraintGroupElement.getElementsByTagName("Primary") != null) {
					Element selectorsElement = (Element) coConstraintGroupElement.getElementsByTagName("Primary").item(0);
					coConstraintGroup.setPrimary(null);
				}
				
				coConstraintGroup.setCoConstraints(getCoConstraints(coConstraintGroupElement));
				coConstraintGroup.setPrimary(getPrimaryCoConstraint(coConstraintGroupElement));
				
				coConstraintGroup.setMin(coConstraintGroupElement.getAttribute("Min"));
				coConstraintGroup.setMax(coConstraintGroupElement.getAttribute("Max"));
				coConstraintGroup.setUsage(coConstraintGroupElement.getAttribute("Usage"));
				coConstraintGroup.setName(coConstraintGroupElement.getAttribute("Name"));
								
				list.add(coConstraintGroup);
			}
		}
		return list;
	}

	private List<CellsType> getCellsType(Element element) {
		List<CellsType> list = new ArrayList<CellsType>();
		if (element != null && element.getElementsByTagName("Code") != null) {
			NodeList codeNodes = element.getElementsByTagName("Code");
			for (int i = 0; i < codeNodes.getLength(); i++) {
				Element codeElement = (Element) codeNodes.item(i);
				Code code = new Code();
				code.setName(codeElement.getAttribute("Name"));
				code.setPath(codeElement.getAttribute("Path"));
				code.setCode(codeElement.getAttribute("Code"));
				code.setCodeSystem(codeElement.getAttribute("CodeSystem"));
				list.add(code);
			}
		}
		if (element != null && element.getElementsByTagName("PlainText") != null) {
			NodeList plainTextNodes = element.getElementsByTagName("PlainText");
			for (int i = 0; i < plainTextNodes.getLength(); i++) {
				Element codeElement = (Element) plainTextNodes.item(i);
				PlainText plainText = new PlainText();
				plainText.setName(codeElement.getAttribute("Name"));
				plainText.setPath(codeElement.getAttribute("Path"));
				plainText.setValue(codeElement.getAttribute("Value"));
				list.add(plainText);
			}
		}
		if (element != null && element.getElementsByTagName("ValueSet") != null) {
			NodeList valueSetNodes = element.getElementsByTagName("ValueSet");
			for (int i = 0; i < valueSetNodes.getLength(); i++) {
				Element valueSetElement = (Element) valueSetNodes.item(i);
				ValueSet valueSet = new ValueSet();
				valueSet.setName(valueSetElement.getAttribute("Name"));
				valueSet.setPath(valueSetElement.getAttribute("Path"));
				valueSet.addValueSetBinding(getValueSetBinding(valueSetElement));
				list.add(valueSet);
			}
		}

		return list;
	}

	private ValueSetBinding getValueSetBinding(Element valueSetElement) {
			ValueSetBinding valueSetBindingObj = null;
			NodeList valueSetBindingNodes = valueSetElement.getElementsByTagName("ValueSetBinding");
			Element elmConstraint = (Element) valueSetBindingNodes.item(0);
			if (elmConstraint != null) {
				valueSetBindingObj = new ValueSetBinding();
				
				valueSetBindingObj.setValueSetBindingId(elmConstraint.getAttribute("ID"));
				valueSetBindingObj.setValueSetBindingTarget(elmConstraint.getAttribute("Target"));
				valueSetBindingObj.setBindingStrength(elmConstraint.getAttribute("BindingStrength"));
				valueSetBindingObj.setBindingLocations(this.convertElementToString(elmConstraint.getElementsByTagName("BindingLocations").item(0)));
				valueSetBindingObj.setBindings(this.convertElementToString(elmConstraint.getElementsByTagName("Bindings").item(0)));

				NodeList bindingLocationsNodes = elmConstraint.getElementsByTagName("BindingLocations");
				for (int bl = 0; bl < bindingLocationsNodes.getLength(); bl++) {
					Element bindingLocationEle = (Element) bindingLocationsNodes.item(bl);
					NodeList complexBindingLocationNodes = bindingLocationEle.getElementsByTagName("ComplexBindingLocation");
					for (int cbl = 0; cbl < complexBindingLocationNodes.getLength(); cbl++) {
						Element elmcomplexBindingLocation = (Element) complexBindingLocationNodes.item(cbl);
						valueSetBindingObj.getBindingLocationList().add(new ComplexBindingLocation(elmcomplexBindingLocation.getAttribute("CodeLocation"),
								elmcomplexBindingLocation.getAttribute("CodeSystemLocation"), elmcomplexBindingLocation.getAttribute("CodeSystemOIDLocation")));
					}

					NodeList simpleBindingLocationNodes = bindingLocationEle.getElementsByTagName("SimpleBindingLocation");
					for (int sbl = 0; sbl < simpleBindingLocationNodes.getLength(); sbl++) {
						Element elmSimpleBindingLocation = (Element) simpleBindingLocationNodes.item(sbl);
						valueSetBindingObj.getBindingLocationList().add(new SimpleBindingLocation(elmSimpleBindingLocation.getAttribute("CodeLocation")));
					}

				}
				NodeList bindingsNodes = elmConstraint.getElementsByTagName("Bindings");
				for (int b = 0; b < bindingsNodes.getLength(); b++) {
					Element bindingEle = (Element) bindingsNodes.item(b);
					NodeList bindingNodes = bindingEle.getElementsByTagName("Binding");
					for (int bn = 0; bn < bindingNodes.getLength(); bn++) {
						Element binding = (Element) bindingNodes.item(bn);
						// check if binding identifier refers to an external vs.
						if (externalVSIdentifiers.contains(binding.getAttribute("BindingIdentifier"))) {
							valueSetBindingObj.getBindingList().add(new Binding(binding.getAttribute("BindingIdentifier"), true));
						} else {
							valueSetBindingObj.getBindingList().add(new Binding(binding.getAttribute("BindingIdentifier"), false));
						}
					}

				}
			}
			
			return valueSetBindingObj;
		
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
