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
}