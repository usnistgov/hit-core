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

import gov.nist.hit.core.domain.Usage;
import gov.nist.hit.core.domain.constraints.ByID;
import gov.nist.hit.core.domain.constraints.ByName;
import gov.nist.hit.core.domain.constraints.ByNameOrByID;
import gov.nist.hit.core.domain.constraints.ConformanceStatement;
import gov.nist.hit.core.domain.constraints.Constraints;
import gov.nist.hit.core.domain.constraints.Context;
import gov.nist.hit.core.domain.constraints.Predicate;
import gov.nist.hit.core.domain.constraints.Reference;
import gov.nist.hit.core.service.ConstraintsParser;

public class ConstraintsParserImpl implements ConstraintsParser {

	private TransformerFactory transFactory;
	private Transformer transformer; 
	private StringWriter buffer;
	
  public ConstraintsParserImpl(){
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
  public Constraints confStatements(String xmlConstraints) {
    Constraints constraints = new Constraints();
    if (xmlConstraints != null) {
      Document conformanceContextDoc = this.stringToDom(xmlConstraints);
      if (conformanceContextDoc.getElementsByTagName("Constraints") != null) {
        Element elmConstraints =  (Element) conformanceContextDoc.getElementsByTagName("Constraints").item(0);
        if (elmConstraints != null) {

          Context datatypeContextObj = new Context();
          Context segmentContextObj = new Context();
          Context groupContextObj = new Context();
          Context messageContextObj = new Context();

          if (elmConstraints.getElementsByTagName("Datatype") != null) {
            this.context((Element) elmConstraints.getElementsByTagName("Datatype").item(0),
                datatypeContextObj);
          }

          if (elmConstraints.getElementsByTagName("Segment") != null) {
            this.context((Element) elmConstraints.getElementsByTagName("Segment").item(0),
                segmentContextObj);
          }

          if (elmConstraints.getElementsByTagName("Group") != null) {
            this.context((Element) elmConstraints.getElementsByTagName("Group").item(0),
                groupContextObj);
          }

          if (elmConstraints.getElementsByTagName("Message") != null) {
            this.context((Element) elmConstraints.getElementsByTagName("Message").item(0),
                messageContextObj);
          }
          constraints.setMessages(messageContextObj);
          constraints.setDatatypes(datatypeContextObj);
          constraints.setSegments(segmentContextObj);
          constraints.setGroups(groupContextObj);
        }
      }
    }
    return constraints;
  }

  @Override
  public Constraints predicates(String xmlConstraints) {
    Constraints constraints = new Constraints();
    if (xmlConstraints != null) {
      Document conformanceContextDoc = this.stringToDom(xmlConstraints);
      if (conformanceContextDoc.getElementsByTagName("Predicates") != null) {
        Element elmConstraints =
            (Element) conformanceContextDoc.getElementsByTagName("Predicates").item(0);
        if (elmConstraints != null) {
          Context datatypeContextObj = new Context();
          Context segmentContextObj = new Context();
          Context groupContextObj = new Context();
          Context messageContextObj = new Context();

          if (elmConstraints.getElementsByTagName("Datatype") != null) {
            this.context((Element) elmConstraints.getElementsByTagName("Datatype").item(0),
                datatypeContextObj);
          }

          if (elmConstraints.getElementsByTagName("Segment") != null) {
            this.context((Element) elmConstraints.getElementsByTagName("Segment").item(0),
                segmentContextObj);
          }
          if (elmConstraints.getElementsByTagName("Group") != null) {
            this.context((Element) elmConstraints.getElementsByTagName("Group").item(0),
                groupContextObj);
          }

          if (elmConstraints.getElementsByTagName("Message") != null) {
            this.context((Element) elmConstraints.getElementsByTagName("Message").item(0),
                messageContextObj);
          }

          constraints.setMessages(messageContextObj);
          constraints.setDatatypes(datatypeContextObj);
          constraints.setSegments(segmentContextObj);
          constraints.setGroups(groupContextObj);
        }
      }
    }
    return constraints;
  }

  private void context(Element elmContext, Context contextObj) {
	  
    if (elmContext != null) {
      NodeList nodes = elmContext.getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        if (nodes.item(i).getNodeName().equals("ByName")) {
          ByName byNameObj = new ByName();
          Element elmByName = (Element) nodes.item(i);
          byNameObj.setByName(elmByName.getAttribute("Name"));
          constraints(elmByName, byNameObj);
          contextObj.getByNameOrByIDs().add(byNameObj);
        } else if (nodes.item(i).getNodeName().equals("ByID")) {
          ByID byIDObj = new ByID();
          Element elmByID = (Element) nodes.item(i);
          byIDObj.setByID(elmByID.getAttribute("ID"));
          constraints(elmByID, byIDObj);
          contextObj.getByNameOrByIDs().add(byIDObj);
        }
      }
    }
  }
  
  private List<Element> listElementsWithPath(Element rootElement) {
      List<Element> list = new ArrayList<Element>();
      NodeList nodeList = rootElement.getElementsByTagName("*"); // Get all descendant elements

      for (int i = 0; i < nodeList.getLength(); i++) {
          Node node = nodeList.item(i);
          if (node.getNodeType() == Node.ELEMENT_NODE) {
              Element element = (Element) node;
              if (element.hasAttribute("Path")) {
            	  list.add(element);
              }
          }
      }
      return list;
  }

  private void constraints(Element elmByNameOrByID, ByNameOrByID byNameOrByIDObj) {
    NodeList constraintNodes = elmByNameOrByID.getElementsByTagName("Constraint");
    for (int i = 0; i < constraintNodes.getLength(); i++) {
      ConformanceStatement constraintObj = new ConformanceStatement();
      Element elmConstraint = (Element) constraintNodes.item(i);
      constraintObj.setConstraintId(elmConstraint.getAttribute("ID"));
      
      if (elmConstraint.hasAttribute("Target")) {
          constraintObj.setConstraintTarget(elmConstraint.getAttribute("Target"));
      }else {
    	  List<Element> pathElementList = listElementsWithPath((Element)elmConstraint.getElementsByTagName("Assertion").item(0));
    	  //if simple expression use path for target, else path is context "."	
			if (pathElementList.size() > 1) {
				constraintObj.setConstraintTarget(".");
			} else if (pathElementList.size() == 1) {
				constraintObj.setConstraintTarget(pathElementList.get(0).getAttribute("Path"));
			}    	  
      }
      
      NodeList referenceNodes = elmConstraint.getElementsByTagName("Reference");
      if (referenceNodes != null && referenceNodes.getLength() == 1) {
    	  Reference ref = new Reference();
    	  Element elmReference= (Element) referenceNodes.item(0);
    	  ref.setChapter(elmReference.getAttribute("Chapter"));
    	  ref.setSection(elmReference.getAttribute("Section"));
    	  ref.setPage(elmReference.getAttribute("Page"));
    	  ref.setUrl(elmReference.getAttribute("URL"));
    	  ref.setSource(elmReference.getAttribute("Source"));
    	  ref.setGeneratedBy(elmReference.getAttribute("GeneratedBy"));
    	  ref.setReferencePath(elmReference.getAttribute("ReferencePath"));
    	  ref.setTestDataCategorization(elmReference.getAttribute("TestDataCategorization"));   
    	  constraintObj.setReference(ref);
      }
      
      NodeList descriptionNodes = elmConstraint.getElementsByTagName("Description");
      if (descriptionNodes != null && descriptionNodes.getLength() == 1) {
        constraintObj.setDescription(descriptionNodes.item(0).getTextContent());
      }

      constraintObj.setAssertion(this.convertElementToString(elmConstraint.getElementsByTagName(
          "Assertion").item(0)));
      
      
      byNameOrByIDObj.getConformanceStatements().add(constraintObj);
    }

    NodeList predicateNodes = elmByNameOrByID.getElementsByTagName("Predicate");

    for (int i = 0; i < predicateNodes.getLength(); i++) {
      Predicate predicateObj = new Predicate();
      Element elmPredicate = (Element) predicateNodes.item(i);

      predicateObj.setConstraintId(elmPredicate.getAttribute("ID"));
      predicateObj.setConstraintTarget(elmPredicate.getAttribute("Target"));
      predicateObj.setTrueUsage(Usage.fromValue(elmPredicate.getAttribute("TrueUsage")));
      predicateObj.setFalseUsage(Usage.fromValue(elmPredicate.getAttribute("FalseUsage")));
      NodeList descriptionNodes = elmPredicate.getElementsByTagName("Description");
      if (descriptionNodes != null && descriptionNodes.getLength() == 1) {
        predicateObj.setDescription(descriptionNodes.item(0).getTextContent());
      }
      predicateObj.setAssertion(this.convertElementToString(elmPredicate.getElementsByTagName(
          "Condition").item(0)));
      byNameOrByIDObj.getPredicates().add(predicateObj);
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
}
