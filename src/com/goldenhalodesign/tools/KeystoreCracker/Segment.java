package com.goldenhalodesign.tools.KeystoreCracker;
public class Segment {
    public String[] values;
    public boolean optional;

    public Segment(String[] values) {
        this(values, false);
    }

    public Segment(String[] values, boolean optional) {
        this.values = values;
        this.optional = optional;
    }

    private String getValuesString() {
        String retVal = "";
        for (int i = 0; i < values.length; i++) {
            retVal += values[i];
            if (i + 1 < values.length) {
                retVal += ", ";
            }
        }

        return retVal;
    }

    public String toString() {
        return "Values = " + getValuesString() + "\nOptional = " + optional;
    }
}