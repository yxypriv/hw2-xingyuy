/* First created by JCasGen Tue Sep 23 06:28:45 EDT 2014 */
package services.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/** 
 * Updated by JCasGen Wed Sep 24 13:41:55 EDT 2014
 * XML source: C:/workspaces/uima/hw1/src/main/resources/types.xml
 * @generated */
public class DocumentKey extends Annotation {
	/**
	 * @generated
	 * @ordered
	 */
	@SuppressWarnings("hiding")
	public final static int typeIndexID = JCasRegistry.register(DocumentKey.class);
	/**
	 * @generated
	 * @ordered
	 */
	@SuppressWarnings("hiding")
	public final static int type = typeIndexID;

	/**
	 * @generated
	 * @return index of the type
	 */
	@Override
	public int getTypeIndexID() {return typeIndexID;}
 
	/**
	 * Never called. Disable default constructor
	 * 
	 * @generated
	 */
	protected DocumentKey() {/* intentionally empty block */}
    
	/**
	 * Internal - constructor used by generator
	 * 
	 * @generated
	 * @param addr
	 *            low level Feature Structure reference
	 * @param type
	 *            the type of this Feature Structure
	 */
	public DocumentKey(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
	/**
	 * @generated
	 * @param jcas
	 *            JCas to which this Feature Structure belongs
	 */
	public DocumentKey(JCas jcas) {
    super(jcas);
    readObject();   
  } 

	/**
	 * @generated
	 * @param jcas
	 *            JCas to which this Feature Structure belongs
	 * @param begin
	 *            offset to the begin spot in the SofA
	 * @param end
	 *            offset to the end spot in the SofA
	 */
	public DocumentKey(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

	/**
	 * <!-- begin-user-doc --> Write your own initialization here <!--
	 * end-user-doc -->
	 * 
	 * @param jcas
	 *
	 * @generated modifiable
	 */
	private void readObject() {
	}
  //*--------------*
  //* Feature: key

  /** getter for key - gets 
   * @generated
   * @return value of the feature 
   */
  public String getKey() {
    if (DocumentKey_Type.featOkTst && ((DocumentKey_Type)jcasType).casFeat_key == null)
      jcasType.jcas.throwFeatMissing("key", "services.types.DocumentKey");
    return jcasType.ll_cas.ll_getStringValue(addr, ((DocumentKey_Type)jcasType).casFeatCode_key);}
    
  /** setter for key - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setKey(String v) {
    if (DocumentKey_Type.featOkTst && ((DocumentKey_Type)jcasType).casFeat_key == null)
      jcasType.jcas.throwFeatMissing("key", "services.types.DocumentKey");
    jcasType.ll_cas.ll_setStringValue(addr, ((DocumentKey_Type)jcasType).casFeatCode_key, v);}    
  }
