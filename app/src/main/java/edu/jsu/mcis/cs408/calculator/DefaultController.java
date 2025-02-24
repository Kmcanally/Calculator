package edu.jsu.mcis.cs408.calculator;



public class DefaultController extends AbstractController {
    public static final String ELEMENT_BUTTON_PROPERTY = "Button";
    public static final String ELEMENT_NUM_PROPERTY = "Digit";
    public static final String ELEMENT_OPER_PROPERTY = "Oper";
    public static final String ELEMENT_SQRT_PROPERTY = "SquareRoot";
    public static final String ELEMENT_C_PROPERTY = "Clear";
    public static final String ELEMENT_PERC_PROPERTY = "Percent";
    public static final String ELEMENT_SIGN_PROPERTY = "Negate";
    public static final String ELEMENT_DEC_PROPERTY = "Decimal";
    public static final String ELEMENT_EQLS_PROPERTY = "Equals";

    public void numPress(String newText) {
        setModelProperty(ELEMENT_NUM_PROPERTY, newText);
    }
    public void operPress(String newText) {
        setModelProperty(ELEMENT_OPER_PROPERTY, newText);
    }
    public void sqrtPress(String newText) {
        setModelProperty(ELEMENT_SQRT_PROPERTY, newText);
    }
    public void cPress(String newText) {
        setModelProperty(ELEMENT_C_PROPERTY, newText);
    }
    public void percPress(String newText) {
        setModelProperty(ELEMENT_PERC_PROPERTY, newText);
    }
    public void signPress(String newText) {
        setModelProperty(ELEMENT_SIGN_PROPERTY, newText);
    }
    public void decPress(String newText) {
        setModelProperty(ELEMENT_DEC_PROPERTY, newText);
    }
    public void eqlsPress(String newText) {
        setModelProperty(ELEMENT_EQLS_PROPERTY, newText);
    }

}
