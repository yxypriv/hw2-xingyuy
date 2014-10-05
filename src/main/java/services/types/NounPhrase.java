

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
public class NounPhrase extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(NounPhrase.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected NounPhrase() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public NounPhrase(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public NounPhrase(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public NounPhrase(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
  //*--------------*
  //* Feature: content

  /** getter for content - gets 
   * @generated
   * @return value of the feature 
   */
  public String getContent() {
    if (NounPhrase_Type.featOkTst && ((NounPhrase_Type)jcasType).casFeat_content == null)
      jcasType.jcas.throwFeatMissing("content", "services.types.NounPhrase");
    return jcasType.ll_cas.ll_getStringValue(addr, ((NounPhrase_Type)jcasType).casFeatCode_content);}
    
  /** setter for content - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setContent(String v) {
    if (NounPhrase_Type.featOkTst && ((NounPhrase_Type)jcasType).casFeat_content == null)
      jcasType.jcas.throwFeatMissing("content", "services.types.NounPhrase");
    jcasType.ll_cas.ll_setStringValue(addr, ((NounPhrase_Type)jcasType).casFeatCode_content, v);}    
  }

    