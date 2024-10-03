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

import gov.nist.hit.core.domain.valuesetbindings.Binding;
import gov.nist.hit.core.domain.valuesetbindings.ByID;
import gov.nist.hit.core.domain.valuesetbindings.ByName;
import gov.nist.hit.core.domain.valuesetbindings.ByNameOrByID;
import gov.nist.hit.core.domain.valuesetbindings.ComplexBindingLocation;
import gov.nist.hit.core.domain.valuesetbindings.Context;
import gov.nist.hit.core.domain.valuesetbindings.SimpleBindingLocation;
import gov.nist.hit.core.domain.valuesetbindings.ValueSetBinding;
import gov.nist.hit.core.domain.valuesetbindings.ValueSetBindings;
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




  private void context(Element elmContext, Context contextObj, List<String> externalVSIdentifiers) {
	  
    if (elmContext != null) {
      NodeList nodes = elmContext.getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        if (nodes.item(i).getNodeName().equals("ByName")) {
          ByName byNameObj = new ByName();
          Element elmByName = (Element) nodes.item(i);
          byNameObj.setByName(elmByName.getAttribute("Name"));
          valuesetbindings(elmByName, byNameObj,externalVSIdentifiers);
          contextObj.getByNameOrByIDs().add(byNameObj);
        } else if (nodes.item(i).getNodeName().equals("ByID")) {
          ByID byIDObj = new ByID();
          Element elmByID = (Element) nodes.item(i);
          byIDObj.setByID(elmByID.getAttribute("ID"));
          valuesetbindings(elmByID, byIDObj,externalVSIdentifiers);
          contextObj.getByNameOrByIDs().add(byIDObj);
        }
      }
    }
  }

  private void valuesetbindings(Element elmByNameOrByID, ByNameOrByID byNameOrByIDObj, List<String> externalVSIdentifiers) {
	//ValueSetBinding
    NodeList valueSetBindingNodes = elmByNameOrByID.getElementsByTagName("ValueSetBinding");
    for (int i = 0; i < valueSetBindingNodes.getLength(); i++) {
      ValueSetBinding valueSetBindingObj = new ValueSetBinding();
      Element elmConstraint = (Element) valueSetBindingNodes.item(i);
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
    		  valueSetBindingObj.getBindingLocationList().add(
    				  new ComplexBindingLocation(
    						  elmcomplexBindingLocation.getAttribute("CodeLocation"),
    						  elmcomplexBindingLocation.getAttribute("CodeSystemLocation"),
    						  elmcomplexBindingLocation.getAttribute("CodeSystemOIDLocation")
    						  ));
    	  }
    	  
    	  NodeList simpleBindingLocationNodes = bindingLocationEle.getElementsByTagName("SimpleBindingLocation");
    	  for (int sbl = 0; sbl < simpleBindingLocationNodes.getLength(); sbl++) {
    		  Element elmSimpleBindingLocation = (Element) simpleBindingLocationNodes.item(sbl);
    		  valueSetBindingObj.getBindingLocationList().add(
    				  new SimpleBindingLocation(
    						  elmSimpleBindingLocation.getAttribute("CodeLocation")
    						  ));
    	  }
    	      	         
      }   
      NodeList bindingsNodes = elmConstraint.getElementsByTagName("Bindings"); 
      for (int b = 0; b < bindingsNodes.getLength(); b++) {
    	  Element bindingEle = (Element) bindingsNodes.item(b);
    	  NodeList bindingNodes = bindingEle.getElementsByTagName("Binding");
    	  for (int bn = 0; bn < bindingNodes.getLength(); bn++) {
    		  Element binding = (Element) bindingNodes.item(bn);    	
    		  //check if binding identifier refers to an external vs.
    		  if(externalVSIdentifiers.contains(binding.getAttribute("BindingIdentifier"))) {
    			  valueSetBindingObj.getBindingList().add(new Binding(binding.getAttribute("BindingIdentifier"),true));
    		  }else {
    			  valueSetBindingObj.getBindingList().add(new Binding(binding.getAttribute("BindingIdentifier"),false));
    		  }
    	  }
    	  
      } 
         
      
      byNameOrByIDObj.getValueSetBindings().add(valueSetBindingObj);
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
public ValueSetBindings valueSetBindings(String xmlvsb, String vsXML) {
	ValueSetBindings valueSetBindings = new ValueSetBindings();
    if (xmlvsb != null) {
      Document valueSetBindingsContextDoc = this.stringToDom(xmlvsb);
      List<String> externalVSIdentifiers = new ArrayList<String>();
      if (vsXML != null) {
    	  Document valueSetDoc = this.stringToDom(vsXML);
    	  //list all external vs to identify them in vs bindings
    	  if (valueSetDoc.getElementsByTagName("ExternalValueSetDefinitions") != null && valueSetDoc.getElementsByTagName("ExternalValueSetDefinitions").getLength() > 0) {
    		  Element extVSDefs =  (Element) valueSetDoc.getElementsByTagName("ExternalValueSetDefinitions").item(0);
    		  NodeList valueSetDefNodes = extVSDefs.getElementsByTagName("ValueSetDefinition");
    		    for (int i = 0; i < valueSetDefNodes.getLength(); i++) {
    		    	Element vsdef = (Element) valueSetDefNodes.item(i);
    		    	externalVSIdentifiers.add(vsdef.getAttribute("BindingIdentifier"));
    		    }
    	  }
    		    
    	  
    	  
      }
      if (valueSetBindingsContextDoc.getElementsByTagName("ValueSetBindings") != null) {
        Element elmValueSetBindings =  (Element) valueSetBindingsContextDoc.getElementsByTagName("ValueSetBindings").item(0);
        if (elmValueSetBindings != null) {

          Context datatypeContextObj = new Context();
          Context segmentContextObj = new Context();     
          Context groupContextObj = new Context();     
          Context messageContextObj = new Context();     

          if (elmValueSetBindings.getElementsByTagName("Datatype") != null) {
            this.context((Element) elmValueSetBindings.getElementsByTagName("Datatype").item(0), datatypeContextObj,externalVSIdentifiers);
          }

          if (elmValueSetBindings.getElementsByTagName("Segment") != null) {
            this.context((Element) elmValueSetBindings.getElementsByTagName("Segment").item(0), segmentContextObj,externalVSIdentifiers);
          }
          
          if (elmValueSetBindings.getElementsByTagName("Group") != null) {
              this.context((Element) elmValueSetBindings.getElementsByTagName("Group").item(0), groupContextObj,externalVSIdentifiers);
          }
          if (elmValueSetBindings.getElementsByTagName("Message") != null) {
              this.context((Element) elmValueSetBindings.getElementsByTagName("Message").item(0), messageContextObj,externalVSIdentifiers);
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
