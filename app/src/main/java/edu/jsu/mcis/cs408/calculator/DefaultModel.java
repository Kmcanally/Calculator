package edu.jsu.mcis.cs408.calculator;

import static edu.jsu.mcis.cs408.calculator.States.*;

import android.util.Log;

import java.math.*;

public class DefaultModel extends AbstractModel {

    public static final String MODEL_TAG = "DefaultModel";

    private static final int CHAR_LIMIT = 12;
    private static final MathContext PRECISION = new MathContext(12);

    private String lhand, rhand, op, oldOut, newOut;
    private States state;


    public void initDefault() {
        lhand = "";
        rhand = "";
        op = "";
        state = CLEAR;
    }

    public void fireChange(States state) {

        switch (state) {
            case CLEAR:
                newOut = lhand;
                Log.i(MODEL_TAG, newOut + " " + oldOut);
                firePropertyChange(DefaultController.ELEMENT_BUTTON_PROPERTY, oldOut,newOut);
                break;
            case LHAND:
                newOut = lhand;
                Log.i(MODEL_TAG, newOut + " " + oldOut);
                Log.i(MODEL_TAG, newOut + " " + oldOut);
                firePropertyChange(DefaultController.ELEMENT_BUTTON_PROPERTY, oldOut,newOut);
                break;
            case RHAND:
                newOut = rhand;
                firePropertyChange(DefaultController.ELEMENT_BUTTON_PROPERTY, oldOut,newOut);
                break;
            case OPERA:
                newOut = lhand + " " + op;
                firePropertyChange(DefaultController.ELEMENT_BUTTON_PROPERTY, oldOut,newOut);
                break;
            case ERROR:
                newOut = lhand;
                firePropertyChange(DefaultController.ELEMENT_BUTTON_PROPERTY, oldOut,newOut);
                break;
            case RESULT:
                newOut = lhand;
                firePropertyChange(DefaultController.ELEMENT_BUTTON_PROPERTY, oldOut,newOut);
                break;
        }

        oldOut = newOut;
    }

    public String calculate(String lhand, String rhand, String op, MathContext pre) {
        BigDecimal num1 = new BigDecimal(lhand);
        BigDecimal num2 = new BigDecimal(rhand);
        BigDecimal result = null;

        if(op.equals("+")) {
            result = num1.add(num2, pre);
        } else if(op.equals("-")) {
            result = num1.subtract(num2, pre);
        } else if(op.equals("×")) {
            result = num1.multiply(num2, pre);
        } else if(op.equals("÷")) {
            if(num1.intValue() == 0) {
                state = ERROR;
                fireChange(state);
                return "ERROR";
            } else {
                result = num1.divide(num2, pre);
            }
        }

        return result.toString();
    }

    public String numAppend(String appended, String num, int charLimit) {
        for(char c: appended.toCharArray()) {
            if(c == '.' || c == '-') {
                charLimit++;
            }
        }

        if(appended.length() < charLimit) {
            StringBuilder sb = new StringBuilder();
            sb.append(appended).append(num);
            appended = sb.toString();
        }
        Log.i(MODEL_TAG, "numAppended" + appended);
        return appended;
    }

    public String symbolAppend(String appended, String sym, int charLimit) {
        boolean hasDecimal = false, isNegative = false;
        for(char c : appended.toCharArray()) {
            if(c == '.') {
                hasDecimal = true;
            }
            if(c == '-') {
                isNegative = true;
            }
        }

        if(sym == "-") {
            if(!isNegative) {
                return sym + appended;
            } else {
                // make unnegative
                return appended.substring(1);
            }
        } else {
            if(!hasDecimal) {
                return appended + sym;
            } else {
                return appended;
            }
        }
    }

    public void setDigit(String digit) {
        if(state == CLEAR){
            state = LHAND;
            lhand = (numAppend(lhand, digit, CHAR_LIMIT));
        }

        else if(state == LHAND){
            lhand = numAppend(lhand, digit, CHAR_LIMIT);
        }

        else if(state == OPERA){
            state = RHAND;
            rhand = numAppend(rhand, digit, CHAR_LIMIT);
        }

        else if(state == RHAND){
            rhand = numAppend(rhand, digit, CHAR_LIMIT);
        }

        else if(state == RESULT){
            lhand = numAppend(lhand, digit, CHAR_LIMIT);
        }
        Log.i(MODEL_TAG, "lhand = " + lhand);
        fireChange(state);

    }

    public void setClear(String digit) {
        initDefault();
        fireChange(state);
    }

    public void setOper(String oper) {
        boolean changeNotFired = true;
        if(state == CLEAR) {
            lhand = "0";
            op = oper;
            state = OPERA;
        } else if(state == LHAND) {
            op = oper;
            state = OPERA;
        } else if(state == OPERA) {
            op = oper;
        } else if(state == RHAND) {
            lhand = calculate(lhand, rhand, op, PRECISION);
            rhand = "";
            op = oper;
            fireChange(LHAND);
            changeNotFired = false;
        } else if(state == RESULT) {
            op = oper;
            rhand = "";
            state = OPERA;
        }

        if(changeNotFired) {
            fireChange(state);
        }
    }

    public void setSquareRoot(String sym) {
        if(state == LHAND) {
            lhand = String.valueOf(Math.sqrt(Double.parseDouble(lhand)));
        } else if(state == RHAND) {
            rhand = String.valueOf(Math.sqrt(Double.parseDouble(rhand)));
        } else if(state == RESULT) {
            lhand = String.valueOf(Math.sqrt(Double.parseDouble(lhand)));
        }
        fireChange(state);
    }

    public void setPercent(String sym) {

        if(state == LHAND) {
            lhand = calculate(lhand, "100", "÷", PRECISION);
        } else if(state == RHAND) {
            rhand = calculate(rhand, "100", "÷", PRECISION);
        } else if(state == RESULT) {
            lhand = calculate(lhand, "100", "÷", PRECISION);
        }
        fireChange(state);
    }

    public void setDecimal(String dec) {
        if(state == CLEAR) {
            state = LHAND;
            lhand = symbolAppend(lhand, "0.", CHAR_LIMIT);
        } else if(state == LHAND) {
            lhand = symbolAppend(lhand, ".", CHAR_LIMIT);
        } else if(state == RHAND) {
            rhand = symbolAppend(rhand, ".", CHAR_LIMIT);
        } else if(state == OPERA) {
            state = RHAND;
            rhand = symbolAppend(rhand, ".", CHAR_LIMIT);
        } else if(state == RESULT) {
            lhand = symbolAppend(lhand, ".", CHAR_LIMIT);
        }
        fireChange(state);
    }

    public void setNegate(String sym) {
        if(state == LHAND) {
            lhand = symbolAppend(lhand, "-", CHAR_LIMIT);
        } else if(state == RHAND) {
            rhand = symbolAppend(rhand, "-", CHAR_LIMIT);
        } else if(state == RESULT) {
            lhand = symbolAppend(lhand, "-", CHAR_LIMIT);
        }

        fireChange(state);
    }

    public void setEquals(String sym) {
        if(state == RHAND) {
            lhand = calculate(lhand, rhand, op, PRECISION);
            state = RESULT;
        } else if(state == OPERA) {
            rhand = lhand;
            lhand = calculate(lhand, rhand, op, PRECISION);
            state = RESULT;
        } else if(state == RESULT) {
            lhand = calculate(lhand, rhand, op, PRECISION);
        }

        fireChange(state);
    }
}
