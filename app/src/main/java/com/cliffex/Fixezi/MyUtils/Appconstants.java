package com.cliffex.Fixezi.MyUtils;

import java.util.ArrayList;

/**
 * Created by technorizen8 on 16/2/16.
 */
public class Appconstants {

    public static String IMAGE_BASE511 = "";
    public static String state = "", street = "", housenoo = "";
    public static String buisinessnameeS = "", tradingnameS = "", buisness_owner_name = "",
            buisness_address = "", tradeofiicenoS = "", tradeemailS = "", companywebsiteS = "",
            trademobileee = "",RELATION_STATUS="",RELATION_ID;
    public static String servicelocation = "", afterhours = "", selecttrade = "", doyouwork = "";
    public static String PREVIOUSTRADE_SELECTED = "";
    public static String PROBLEM_SELECTED = "";
    public static String TIME_SELECTED = "";
    public static String IMAGE_BASE64 = "";
    public static String Charge = "";
    public static String IMAGE_BASE642 = "";
    public static String radius = "";
    public static String IMAGE_BASE6423 = "";
    public static String IsCallout = "";
    public static String tradeId = "";
    public static String Radius = "";
    public static int position;
    public static String liabilityIn = "";
    public static String DAYS = "";
    public static String JobId = "";

    public static String Category = "";
    public static String ServiceLocation = "";
    public static String Abn = "";

    public static String Latitude;
    public static String Longitude;

    public static double lat;
    public static double lon;

    public static String problemId = "";

    public static String DATE_SELECTED = "";
    public static String JOB_REQUEST = "";
    public static String PERSON_ON_SITE = "", WHICH_TYPE_ADDRESS = "", SITE_ADDRESS = "", SITE_HOME_NUMBER = "", SITE_MOBILE_NUMBER = "", RELATION_TYPE = "", Different_Address = "";

    public static String R_ID = "", My_RID;
    public static String TradesmanUsername = "", TradesmanPassword = "", TradesmanConfirmPassword = "";

    public static String TradesmanBusinessName = "", TradesmanOfficeNumber = "", TradesmanMobileNumber = "", CompanyDetail = "";

    public static String timeFlexi = "", dateFlexi = "";

    public static String AdminType = "";
    public static String afterHourGetData = "";
    public static String timeFlexibleValue = "";
    public static String companyProfileRatio = "";
    public static String companyDesRatio = "";

    public static boolean checkValue = false;

    public static ArrayList<String> postCode;


    // In App Billing
    public static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnzi69UnFdXfJhUy0bkPwvGVufik3eA+DxDM28w1UyU4z8rTREykzrUM9TrGGnjhQpfI9qN7K6zIMR2VoaxF7P5GLiiWoqGU5LGkqINItQZoKohZJ5qpa9LJj0zWzXJZRf6tDlNCSoFLb5Xqz+caEjO8o8lEOxuvr3M6gX45RQ27nhlN1vnZPZlZNPDAN9F61FoKFpzf2a+I8jnWHDGZ7kKjVRMY31JS7clJE5/4lz/MrgzQ8Fa5j0vgZJ9elJZL+bSc2R6bRdhqq6zgZhOfvrK3507NfNo9t8bFLuKZ85mSnzvgX5zhVkvK78m6BS4vi283Umr2tOzIfWfesrCfgDQIDAQAB";
    /*public static final String SKU_ECONOMY = "economy_plan";
    public static final String SKU_FUll = "full_plan";
    public static final String SKU_PAY_PER_JOB= "pay_per_job";
    */
    public static final String SKU_ECONOMY = "economy_plan_demo";
    public static final String SKU_FUll = "full_plan_demo";
    public static final String SKU_PAY_PER_JOB = "pay_per_job_demo";

    public static final String SKU_3_ID = "3_id_demo";
    public static final String SKU_6_ID = "6_id_demo";
    public static final String SKU_9_ID = "9_id_demo";


    public static String IsQuoteSelected = "";

    public final static boolean isValidEmail(CharSequence target) {

        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

}

