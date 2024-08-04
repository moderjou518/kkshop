package com.hms.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.hms.entity.Actions.Action;

public class testJaxb {

	/**
	 * @param args
	 * @throws JAXBException 
	 */
	public static void main(String[] args) throws JAXBException {
		// TODO Auto-generated method stub
		test();

	}
	
	public static void test0() throws JAXBException{
		
		JAXBContext jc = JAXBContext.newInstance(Actions.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Actions actions = (Actions) unmarshaller.unmarshal(new File("ROOT/_ref/actions.xml"));

        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(actions, System.out);
                
        List acts = ((Actions) actions).getAction();
        
        for(int i=0; i < acts.size(); i++){
        	Action a = (Action) acts.get(i);
        	System.out.println(a.getActionName());
        }
	}
	
	
	
	
	public static void test(){
		
		try {
		    // create a JAXBContext capable of handling classes generated into
		    // the com.abhi.xml.jaxb.generated package
		    JAXBContext jc = JAXBContext.newInstance("com.hms.entity.ObjectFactory" );

		    // create an Unmarshaller
		    Unmarshaller u = jc.createUnmarshaller();

		    // unmarshal a FosterHome instance document into a tree of Java content
		    // objects composed of classes from the com.abhi.xml.jaxb.generated 
		    // package.
		    JAXBElement<?> fhElement =(JAXBElement<?>)u.unmarshal
		    (new FileInputStream("ROOT/_ref/actions.xml"));
		    Actions FH = (Actions)fhElement.getValue();
		    for(int i=0; i < FH.action.size(); i++){
		    	Action act = FH.action.get(i);
		    	System.out.println(act.actionID);
		    }
		         // so on ..you can get all elements based on generated objects

		} catch( JAXBException je ) {
		    je.printStackTrace();
		} catch( IOException ioe ) {
		    ioe.printStackTrace();
		}
	}

}
