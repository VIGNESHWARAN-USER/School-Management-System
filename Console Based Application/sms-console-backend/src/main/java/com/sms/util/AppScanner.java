package com.sms.util;
//Author: Vigneshwaran M 
import java.util.Scanner;
//Creating the scanner object only once for the entire application 
public class AppScanner {
    private static final Scanner sc = new Scanner(System.in);
//Returning the scanner object
    public static Scanner get() {
        return sc;
    }
}
