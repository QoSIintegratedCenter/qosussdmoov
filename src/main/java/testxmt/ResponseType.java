
package testxmt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for responseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="responseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="screen_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="options" type="{}optionsType"/>
 *         &lt;element name="back_link" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="home_link" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="session_op" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="screen_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "responseType", propOrder = {
    "screenType",
    "text",
    "options",
    "backLink",
    "homeLink",
    "sessionOp",
    "screenId"
})
public class ResponseType {

    @XmlElement(name = "screen_type", required = true)
    protected String screenType;
    @XmlElement(required = true)
    protected String text;
    @XmlElement(required = true)
    protected OptionsType options;
    @XmlElement(name = "back_link", required = true)
    protected String backLink;
    @XmlElement(name = "home_link", required = true)
    protected String homeLink;
    @XmlElement(name = "session_op", required = true)
    protected String sessionOp;
    @XmlElement(name = "screen_id", required = true)
    protected String screenId;

    /**
     * Gets the value of the screenType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScreenType() {
        return screenType;
    }

    /**
     * Sets the value of the screenType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScreenType(String value) {
        this.screenType = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link OptionsType }
     *     
     */
    public OptionsType getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionsType }
     *     
     */
    public void setOptions(OptionsType value) {
        this.options = value;
    }

    /**
     * Gets the value of the backLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackLink() {
        return backLink;
    }

    /**
     * Sets the value of the backLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackLink(String value) {
        this.backLink = value;
    }

    /**
     * Gets the value of the homeLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeLink() {
        return homeLink;
    }

    /**
     * Sets the value of the homeLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeLink(String value) {
        this.homeLink = value;
    }

    /**
     * Gets the value of the sessionOp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionOp() {
        return sessionOp;
    }

    /**
     * Sets the value of the sessionOp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionOp(String value) {
        this.sessionOp = value;
    }

    /**
     * Gets the value of the screenId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScreenId() {
        return screenId;
    }

    /**
     * Sets the value of the screenId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScreenId(String value) {
        this.screenId = value;
    }

}
