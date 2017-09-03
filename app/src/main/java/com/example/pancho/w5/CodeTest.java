package com.example.pancho.w5;

import java.util.Hashtable;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pancho on 9/2/2017.
 */

public class CodeTest {

    public static void main(String[] args) {
        System.out.println(BracketsMatch("({[] })[]"));
        System.out.println(NCopies("catcowcat", "cow", 1));
    }

    public static boolean BracketsMatch(String s){
        Hashtable<String, String> hash = new Hashtable<String, String>();
        hash.put("(",")");
        hash.put("[","]");
        hash.put("{","}");
        hash.put("<",">");

        Stack<String> bracket = new Stack<String>();
        for (int i = 0; i < s.length(); i++) {
            if(hash.containsKey(String.valueOf(s.charAt(i))))
                bracket.push(String.valueOf(s.charAt(i)));
            if(hash.containsValue(String.valueOf(s.charAt(i))))
                if(hash.get(bracket.get(bracket.size()-1)).equals(String.valueOf(s.charAt(i))))
                    bracket.pop();
                else
                    return false;
        }
        return bracket.isEmpty();
    }

    public static boolean NCopies(String s, String sub, int at_least){
        int cont = 0;
        int at_least_cont = 0;
        for (int i = 0; i < s.length(); i++) {
            if(cont == sub.length()){at_least_cont++; cont=0;}
            if (s.charAt(i) == sub.charAt(cont++));
            else cont=0;
        }
        if(cont == sub.length()){at_least_cont++;}
        return at_least_cont >= at_least;
    }

}
