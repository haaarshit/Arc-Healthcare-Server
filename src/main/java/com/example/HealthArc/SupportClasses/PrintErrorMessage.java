package com.example.HealthArc.SupportClasses;

import java.util.Arrays;

public class PrintErrorMessage {
    public  PrintErrorMessage(Exception e){
        System.out.println("Error message "+e.getMessage());
//        System.out.println("Error cause "+e.getCause().toString());
//        System.out.println("Error stack trace "+Arrays.toString(e.getStackTrace()));
    }
}
