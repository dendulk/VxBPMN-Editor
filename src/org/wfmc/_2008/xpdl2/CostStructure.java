//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.05 at 01:56:55 PM CEST 
//


package org.wfmc._2008.xpdl2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.wfmc.org/2008/XPDL2.1}ResourceCosts" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FixedCost" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "resourceCosts",
    "fixedCost"
})
@XmlRootElement(name = "CostStructure")
public class CostStructure {

    @XmlElement(name = "ResourceCosts")
    protected List<ResourceCosts> resourceCosts;
    @XmlElement(name = "FixedCost")
    protected BigInteger fixedCost;

    /**
     * Gets the value of the resourceCosts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceCosts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceCosts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceCosts }
     * 
     * 
     */
    public List<ResourceCosts> getResourceCosts() {
        if (resourceCosts == null) {
            resourceCosts = new ArrayList<ResourceCosts>();
        }
        return this.resourceCosts;
    }

    /**
     * Gets the value of the fixedCost property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFixedCost() {
        return fixedCost;
    }

    /**
     * Sets the value of the fixedCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFixedCost(BigInteger value) {
        this.fixedCost = value;
    }

}
