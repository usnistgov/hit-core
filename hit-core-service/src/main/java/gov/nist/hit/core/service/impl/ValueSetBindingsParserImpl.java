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

import gov.nist.hit.core.domain.Usage;
import gov.nist.hit.core.domain.valuesetbindings.ByID;
import gov.nist.hit.core.domain.valuesetbindings.ByName;
import gov.nist.hit.core.domain.valuesetbindings.ByNameOrByID;
import gov.nist.hit.core.domain.valuesetbindings.ValueSetBindings;
import gov.nist.hit.core.domain.valuesetbindings.Context;
import gov.nist.hit.core.domain.valuesetbindings.ValueSetBinding;
import gov.nist.hit.core.service.ValueSetBindingsParser;

public class ValueSetBindingsParserImpl implements ValueSetBindingsParser {

	private TransformerFactory transFactory;
	private Transformer transformer; 
	private StringWriter buffer;
	
  public ValueSetBindingsParserImpl(){
		super();
		 
	     try {
	    	 transFactory = TransformerFactory.newInstance();
			transformer = transFactory.newTransformer();
		     transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}	
	}




  private void context(Element elmContext, Context contextObj) {
	  
    if (elmContext != null) {
      NodeList nodes = elmContext.getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        if (nodes.item(i).getNodeName().equals("ByName")) {
          ByName byNameObj = new ByName();
          Element elmByName = (Element) nodes.item(i);
          byNameObj.setByName(elmByName.getAttribute("Name"));
          valuesetbindings(elmByName, byNameObj);
          contextObj.getByNameOrByIDs().add(byNameObj);
        } else if (nodes.item(i).getNodeName().equals("ByID")) {
          ByID byIDObj = new ByID();
          Element elmByID = (Element) nodes.item(i);
          byIDObj.setByID(elmByID.getAttribute("ID"));
          valuesetbindings(elmByID, byIDObj);
          contextObj.getByNameOrByIDs().add(byIDObj);
        }
      }
    }
  }

  private void valuesetbindings(Element elmByNameOrByID, ByNameOrByID byNameOrByIDObj) {
	//ValueSetBinding
    NodeList constraintNodes = elmByNameOrByID.getElementsByTagName("ValueSetBinding");
    for (int i = 0; i < constraintNodes.getLength(); i++) {
      ValueSetBinding valueSetBindingObj = new ValueSetBinding();
      Element elmConstraint = (Element) constraintNodes.item(i);
      valueSetBindingObj.setValueSetBindingId(elmConstraint.getAttribute("ID"));      
      valueSetBindingObj.setValueSetBindingTarget(elmConstraint.getAttribute("Target"));
      valueSetBindingObj.setBindingStrength(elmConstraint.getAttribute("BindingStrength"));        
      valueSetBindingObj.setBindingLocations(this.convertElementToString(elmConstraint.getElementsByTagName("BindingLocations").item(0)));
      valueSetBindingObj.setBindings(this.convertElementToString(elmConstraint.getElementsByTagName("Bindings").item(0)));
      
      
      byNameOrByIDObj.getConformanceStatements().add(valueSetBindingObj);
    }
// SingleCodeBinding
//    NodeList predicateNodes = elmByNameOrByID.getElementsByTagName("SingleCodeBinding");
//
//    for (int i = 0; i < predicateNodes.getLength(); i++) {
//      Predicate predicateObj = new Predicate();
//      Element elmPredicate = (Element) predicateNodes.item(i);
//
//      predicateObj.setConstraintId(elmPredicate.getAttribute("ID"));
//      predicateObj.setConstraintTarget(elmPredicate.getAttribute("Target"));
//      predicateObj.setTrueUsage(Usage.fromValue(elmPredicate.getAttribute("TrueUsage")));
//      predicateObj.setFalseUsage(Usage.fromValue(elmPredicate.getAttribute("FalseUsage")));
//      NodeList descriptionNodes = elmPredicate.getElementsByTagName("Description");
//      if (descriptionNodes != null && descriptionNodes.getLength() == 1) {
//        predicateObj.setDescription(descriptionNodes.item(0).getTextContent());
//      }
//      predicateObj.setAssertion(this.convertElementToString(elmPredicate.getElementsByTagName(
//          "Condition").item(0)));
//      byNameOrByIDObj.getPredicates().add(predicateObj);
//    }

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
public ValueSetBindings datatypes(String xmlvsb) {
	ValueSetBindings valueSetBindings = new ValueSetBindings();
    if (xmlvsb != null) {
      Document valueSetBindingsContextDoc = this.stringToDom(xmlvsb);
      if (valueSetBindingsContextDoc.getElementsByTagName("ValueSetBindings") != null) {
        Element elmValueSetBindings =  (Element) valueSetBindingsContextDoc.getElementsByTagName("ValueSetBindings").item(0);
        if (elmValueSetBindings != null) {

          Context datatypeContextObj = new Context();
          Context segmentContextObj = new Context();     
          Context groupContextObj = new Context();     
          Context messageContextObj = new Context();     

          if (elmValueSetBindings.getElementsByTagName("Datatype") != null) {
            this.context((Element) elmValueSetBindings.getElementsByTagName("Datatype").item(0), datatypeContextObj);
          }

          if (elmValueSetBindings.getElementsByTagName("Segment") != null) {
            this.context((Element) elmValueSetBindings.getElementsByTagName("Segment").item(0), segmentContextObj);
          }
          
          if (elmValueSetBindings.getElementsByTagName("Group") != null) {
              this.context((Element) elmValueSetBindings.getElementsByTagName("Group").item(0), groupContextObj);
          }
          if (elmValueSetBindings.getElementsByTagName("Message") != null) {
              this.context((Element) elmValueSetBindings.getElementsByTagName("Message").item(0), messageContextObj);
          }        
          
          valueSetBindings.setDatatypes(datatypeContextObj);
          valueSetBindings.setSegments(segmentContextObj);
          valueSetBindings.setGroups(groupContextObj);
          valueSetBindings.setMessages(messageContextObj);
        }
      }
    }
    return valueSetBindings;
}

@Override
public ValueSetBindings segments(String xmlvsb) {
	// TODO Auto-generated method stub
	return null;
}
}
