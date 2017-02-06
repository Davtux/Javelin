//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.02.06 à 04:27:43 PM CET 
//


package fr.unilim.automaton;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "desc",
    "dataOrNodeOrEdgeOrHyperedgeOrLocator"
})
@XmlRootElement(name = "graph")
public class Graph {

    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "edgedefault", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String edgedefault;
    protected String desc;
    @XmlElements({
        @XmlElement(name = "data", type = Data.class),
        @XmlElement(name = "node", type = Node.class),
        @XmlElement(name = "edge", type = Edge.class),
        @XmlElement(name = "hyperedge", type = Hyperedge.class),
        @XmlElement(name = "locator", type = Locator.class)
    })
    protected List<Object> dataOrNodeOrEdgeOrHyperedgeOrLocator;

    /**
     * Obtient la valeur de la propriété id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Définit la valeur de la propriété id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propriété edgedefault.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEdgedefault() {
        return edgedefault;
    }

    /**
     * Définit la valeur de la propriété edgedefault.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEdgedefault(String value) {
        this.edgedefault = value;
    }

    /**
     * Obtient la valeur de la propriété desc.
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
     * Définit la valeur de la propriété desc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDesc(String value) {
        this.desc = value;
    }

    /**
     * Gets the value of the dataOrNodeOrEdgeOrHyperedgeOrLocator property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataOrNodeOrEdgeOrHyperedgeOrLocator property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataOrNodeOrEdgeOrHyperedgeOrLocator().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * {@link Node }
     * {@link Edge }
     * {@link Hyperedge }
     * {@link Locator }
     * 
     * 
     */
    public List<Object> getDataOrNodeOrEdgeOrHyperedgeOrLocator() {
        if (dataOrNodeOrEdgeOrHyperedgeOrLocator == null) {
            dataOrNodeOrEdgeOrHyperedgeOrLocator = new ArrayList<Object>();
        }
        return this.dataOrNodeOrEdgeOrHyperedgeOrLocator;
    }

}
