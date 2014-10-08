

/* First created by JCasGen Tue Oct 07 19:41:48 EDT 2014 */
package services.uima.types.abnor;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed Oct 08 06:30:23 EDT 2014
 * XML source: D:/workspaces/BICProject/hw2-xingyuy/src/main/resources/UIMA/TypeSystem.xml
 * @generated */
public class TokenizedContent extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(TokenizedContent.class);
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
  protected TokenizedContent() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public TokenizedContent(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public TokenizedContent(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public TokenizedContent(JCas jcas, int begin, int end) {
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
    if (TokenizedContent_Type.featOkTst && ((TokenizedContent_Type)jcasType).casFeat_content == null)
      jcasType.jcas.throwFeatMissing("content", "services.uima.types.abnor.TokenizedContent");
    return jcasType.ll_cas.ll_getStringValue(addr, ((TokenizedContent_Type)jcasType).casFeatCode_content);}
    
  /** setter for content - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setContent(String v) {
    if (TokenizedContent_Type.featOkTst && ((TokenizedContent_Type)jcasType).casFeat_content == null)
      jcasType.jcas.throwFeatMissing("content", "services.uima.types.abnor.TokenizedContent");
    jcasType.ll_cas.ll_setStringValue(addr, ((TokenizedContent_Type)jcasType).casFeatCode_content, v);}    
  }

    