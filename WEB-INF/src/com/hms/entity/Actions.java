//
// ���ɮ׬O�� JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 �Ҳ��� 
// �аѾ\ <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �@�����s�sĶ�ӷ����n, �惡�ɮשҰ�������קﳣ�N�|��. 
// ���ͮɶ�: 2019.06.22 �� 05:18:00 PM CST 
//


package com.hms.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="action" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="actionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="actionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="pageName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="desc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "action"
})
@XmlRootElement(name = "actions")
public class Actions {

    protected List<Actions.Action> action;

    /**
     * Gets the value of the action property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the action property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Actions.Action }
     * 
     * 
     */
    public List<Actions.Action> getAction() {
        if (action == null) {
            action = new ArrayList<Actions.Action>();
        }
        return this.action;
    }


    /**
     * <p>anonymous complex type �� Java ���O.
     * 
     * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="actionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="actionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="pageName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="desc" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "actionID",
        "actionName",
        "className",
        "pageName",
        "desc"
    })
    public static class Action {

        @XmlElement(required = true)
        protected String actionID;
        @XmlElement(required = true)
        protected String actionName;
        @XmlElement(required = true)
        protected String className;
        @XmlElement(required = true)
        protected String pageName;
        @XmlElement(required = true)
        protected String desc;

        /**
         * ���o actionID �S�ʪ���.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActionID() {
            return actionID;
        }

        /**
         * �]�w actionID �S�ʪ���.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActionID(String value) {
            this.actionID = value;
        }

        /**
         * ���o actionName �S�ʪ���.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActionName() {
            return actionName;
        }

        /**
         * �]�w actionName �S�ʪ���.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActionName(String value) {
            this.actionName = value;
        }

        /**
         * ���o className �S�ʪ���.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getClassName() {
            return className;
        }

        /**
         * �]�w className �S�ʪ���.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setClassName(String value) {
            this.className = value;
        }

        /**
         * ���o pageName �S�ʪ���.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPageName() {
            return pageName;
        }

        /**
         * �]�w pageName �S�ʪ���.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPageName(String value) {
            this.pageName = value;
        }

        /**
         * ���o desc �S�ʪ���.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDesc() {
            return desc;
        }

        /**
         * �]�w desc �S�ʪ���.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDesc(String value) {
            this.desc = value;
        }

    }

}
