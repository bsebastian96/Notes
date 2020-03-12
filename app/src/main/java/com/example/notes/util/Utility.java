package com.example.notes.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
    This class will generate a timestamp in the format we want so we don't have to hardcode it.
    Methods in this class are static so they can be used without an object reference.  They can
    be used by just calling the class name.
 */
public class Utility {

     public static String getCurrentTimestamp() {
         try {
             SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
             return dateFormat.format(new Date());
         } catch (Exception e) {
            return null;
         }
     }

     public static String getMonthFromNumber(String monthNumber) {
         switch (monthNumber) {
             case "01" :{
                 return "JAN";
             }
             case "02" :{
                 return "FEB";
             }
             case "03" :{
                 return "MAR";
             }
             case "04" :{
                 return "APR";
             }
             case "05" :{
                 return "MAY";
             }
             case "06" :{
                 return "JUN";
             }
             case "07" :{
                 return "JUL";
             }
             case "08" :{
                 return "AUG";
             }
             case "09" :{
                 return "SEP";
             }
             case "10" :{
                 return "OCT";
             }
             case "11" :{
                 return "NOV";
             }
             case "12" :{
                 return "DEC";
             }
             default :{
                 return "Error";
             }
         }
     }
}
