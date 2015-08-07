package com.cablelabs.vcpe.common;

/**
 * Created by steve on 5/25/15.
 */
public class Dbg {

    private static final String TAB = "    ";


    static public void p( String s) { p( 0, "", s);}
    static public void p( int tabs, String s) { p( tabs, "", s);}
    static public void p( int tabs, String pre, String s){
        System.out.println(tab(tabs) + pre + s);
    }

    static private String tab (int tabs) {
        String t = new String();
        for ( int i = 0; i < tabs; i++ ) {
            t = t + TAB;
        }
        return t;
    }
}
