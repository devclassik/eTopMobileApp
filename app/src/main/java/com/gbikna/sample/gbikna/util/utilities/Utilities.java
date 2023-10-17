package com.gbikna.sample.gbikna.util.utilities;

import android.content.Context;
import android.os.Build;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utilities {
    private static final String TAG = Utilities.class.getSimpleName();

    public static String etisalet = "[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 1-day (100MB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 1-day (100MB)\",\n" +
            "                        \"Amount\":\"100\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 100MB+100MB social for N100 valid for 1day\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (1.5GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (1.5GB)\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 1.5GB for N1000 valid for 30days\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (40GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (40GB)\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 40GB for N10000 valid for 30days\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (2GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (2GB)\",\n" +
            "                        \"Amount\":\"1200\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 2GB for N1200 valid for 30days\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 1-day (300MB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 1-day (300MB)\",\n" +
            "                        \"Amount\":\"150\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 300MB + 300secs (200MB All time + 100MB Night)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 7-days (7GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 7-days (7GB)\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 7GB+100MB social for N1500 valid for 7days\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (75GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (75GB)\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 75GB for N15,000 valid for 30days. \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 1-day (650MB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 1-day (650MB)\",\n" +
            "                        \"Amount\":\"200\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 650MB for N200 valid for 1day\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (4.5GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (4.5GB)\",\n" +
            "                        \"Amount\":\"2000\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 4.5GB for N2000 valid for 30days\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (125GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (125GB)\",\n" +
            "                        \"Amount\":\"20000\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 1250GB for N20,000 valid for 30days. \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9mobile 1-day (1GB)\",\n" +
            "                        \"ItemName\":\"9mobile 1-day (1GB)\",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"this data plan gives  1GB+100MB Social for N300 valid for 1day\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (12GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (12GB)\",\n" +
            "                        \"Amount\":\"3000\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 12GB for N3,000 valid for 30days. \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (11GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (11GB)\",\n" +
            "                        \"Amount\":\"4000\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 11GB for N4000 valid for 30days\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 1-day (50MB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 1-day (50MB) \",\n" +
            "                        \"Amount\":\"50\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 50MB for N50 valid for 1day\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 3-day (2GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 3-day (2GB)\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 2GB+ 100MB Social for N500 valid for 3days\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9MOBILE 30-days (15GB)\",\n" +
            "                        \"ItemName\":\"9MOBILE 30-days (15GB)\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"This Data plan gives 15GB for N5000 valid for 30days\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     }\n" +
            "                  ]";

    public static String airtel = "[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"99\",\n" +
            "                        \"ItemName\":\"100MB for N100 valid for 1day.\",\n" +
            "                        \"Amount\":\"100\",\n" +
            "                        \"ItemDesc\":\"100MB for N100 valid for 1day. \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"999\",\n" +
            "                        \"ItemName\":\"1.5GB for N1,000 valid for 30days.\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"1.5GB for N1,000 valid for 30days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9999\",\n" +
            "                        \"ItemName\":\"40GB for N10,000 valid for 30 days.\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"40GB for N10,000 valid for 30 days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"99999.02\",\n" +
            "                        \"ItemName\":\"1TB for N100,000 valid for 365days.\",\n" +
            "                        \"Amount\":\"100000\",\n" +
            "                        \"ItemDesc\":\"1TB for N100,000 valid for 365days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"1199\",\n" +
            "                        \"ItemName\":\"2GB for N1,200 valid for 30days.\",\n" +
            "                        \"Amount\":\"1200\",\n" +
            "                        \"ItemDesc\":\"2GB for N1,200 valid for 30days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"1499.01\",\n" +
            "                        \"ItemName\":\"3GB for N1,500 valid for 30 days.\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"3GB for N1,500 valid for 30 days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"1499.03\",\n" +
            "                        \"ItemName\":\"6GB for N1500valid for 7days.\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"6GB for N1500 valid for 7days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"14999\",\n" +
            "                        \"ItemName\":\"75GB for N15,000 valid for 30days.\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"75GB for N15,000valid for 30days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"199.03\",\n" +
            "                        \"ItemName\":\"200MB for N 200 valid for 3days.\",\n" +
            "                        \"Amount\":\"200\",\n" +
            "                        \"ItemDesc\":\"200MB for N 200 valid for 3days. \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"1999\",\n" +
            "                        \"ItemName\":\"4.5GB for N2,000 valid for 30 days.\",\n" +
            "                        \"Amount\":\"2000\",\n" +
            "                        \"ItemDesc\":\"4.5GB for N2,000 valid for 30 days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"19999.02\",\n" +
            "                        \"ItemName\":\"120GB for N20,000 valid for 30days.\",\n" +
            "                        \"Amount\":\"20000\",\n" +
            "                        \"ItemDesc\":\"120GB for N20,000 valid for 30days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"2499.01\",\n" +
            "                        \"ItemName\":\"6GB for N2,500 valid for 30 days.\",\n" +
            "                        \"Amount\":\"2500\",\n" +
            "                        \"ItemDesc\":\"6GB for N2,500 valid for 30 days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"299.02\",\n" +
            "                        \"ItemName\":\"350MB for N300 valid for 7days.\",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"350MB for N300 valid for 7days. \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"299.03\",\n" +
            "                        \"ItemName\":\"1GB for N300 valid for 1day.\",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"1GB for N300 valid for 1day.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"2999.02\",\n" +
            "                        \"ItemName\":\"10GB for N3,000 valid for 30 days.\",\n" +
            "                        \"Amount\":\"3000\",\n" +
            "                        \"ItemDesc\":\"10GB for N3,000 valid for 30 days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"29999.02\",\n" +
            "                        \"ItemName\":\"240GB for N30,000 valid for 30days.\",\n" +
            "                        \"Amount\":\"30000\",\n" +
            "                        \"ItemDesc\":\"240GB for N30,000 valid for 30days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"35999.02\",\n" +
            "                        \"ItemName\":\"280GB for N36,000 valid for 30days.\",\n" +
            "                        \"Amount\":\"36000\",\n" +
            "                        \"ItemDesc\":\"280GB for N36,000 valid for 30days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"3999.01\",\n" +
            "                        \"ItemName\":\" 11GB for N4,000 valid for 30 days.\",\n" +
            "                        \"Amount\":\"4000\",\n" +
            "                        \"ItemDesc\":\" 11GB for N4,000 valid for 30 days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"49.99\",\n" +
            "                        \"ItemName\":\"40MB for N50 valid for 1day\",\n" +
            "                        \"Amount\":\"50\",\n" +
            "                        \"ItemDesc\":\"40MB for N50 valid for 1day\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"499\",\n" +
            "                        \"ItemName\":\"750MB for N500 valid for 14days. \",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"750MB for N500 valid for 14days. \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"499.03\",\n" +
            "                        \"ItemName\":\"2GB for N500 valid for 1day.\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"2GB for N500 valid for 1day.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"4999\",\n" +
            "                        \"ItemName\":\"20GB for N5,000 valid for 30 days.\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"20GB for N5,000 valid for 30 days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"49999.02\",\n" +
            "                        \"ItemName\":\"400GB for N50,000 valid for 90days.\",\n" +
            "                        \"Amount\":\"50000\",\n" +
            "                        \"ItemDesc\":\"400GB for N50,000 valid for 90days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"59999.02\",\n" +
            "                        \"ItemName\":\" 500GB for N60,000 valid for 120days.\",\n" +
            "                        \"Amount\":\"60000\",\n" +
            "                        \"ItemDesc\":\" 500GB for N60,000 valid for 120days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"7999.02\",\n" +
            "                        \"ItemName\":\"30GB for N8,000 valid for 30 days.\",\n" +
            "                        \"Amount\":\"8000\",\n" +
            "                        \"ItemDesc\":\"30GB for N8,000 valid for 30 days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     }\n" +
            "                  ]";

    public static String glo = "\"[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-21\",\n" +
            "                        \"ItemName\":\"GLO N100=150 MB 1DAY\",\n" +
            "                        \"Amount\":\"100\",\n" +
            "                        \"ItemDesc\":\"N100=150 MB 1DAY\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-31\",\n" +
            "                        \"ItemName\":\"NIGHT N100 =1GB 5 DAYS\",\n" +
            "                        \"Amount\":\"100\",\n" +
            "                        \"ItemDesc\":\"N100 =1GB 5 DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-2\",\n" +
            "                        \"ItemName\":\"GLO N1000=2.9GB 30DAYS\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"N1000=2.9GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-10\",\n" +
            "                        \"ItemName\":\"GLO N10000=50GB 30DAYS\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"N10000=50GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-69\",\n" +
            "                        \"ItemName\":\"GLOMEGA N100000 AUTO 1TB 365DAYS\",\n" +
            "                        \"Amount\":\"100000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N100000 AUTO 1TB 365DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-439\",\n" +
            "                        \"ItemName\":\"GLOMEGA N100000 ONEOFF 1TB 365DAYS\",\n" +
            "                        \"Amount\":\"100000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N100000 ONEOFF 1TB 365DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-16\",\n" +
            "                        \"ItemName\":\"GLO N1500 =4.1GB 30DAYS\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"N1500 =4.1GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-24\",\n" +
            "                        \"ItemName\":\"SPECIAL N1500 =7GB 7DAYS\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"N1500 =7GB 7DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-11\",\n" +
            "                        \"ItemName\":\"GLO N15000=93GB 30DAYS\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"N15000=93GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-20\",\n" +
            "                        \"ItemName\":\"GLO N18000=119GB 30DAYS\",\n" +
            "                        \"Amount\":\"18000\",\n" +
            "                        \"ItemDesc\":\"N18000=119GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-28\",\n" +
            "                        \"ItemName\":\"GLO N200=350MB 2DAYS\",\n" +
            "                        \"Amount\":\"200\",\n" +
            "                        \"ItemDesc\":\"N200=350MB 2DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-37\",\n" +
            "                        \"ItemName\":\"SUNDAY N200 =1.25GB 1DAY\",\n" +
            "                        \"Amount\":\"200\",\n" +
            "                        \"ItemDesc\":\"N200= 1.25GB 1DAY\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-25\",\n" +
            "                        \"ItemName\":\"GLO N2000=5.8GB 30DAYS\",\n" +
            "                        \"Amount\":\"2000\",\n" +
            "                        \"ItemDesc\":\"N2000=5.8GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-33\",\n" +
            "                        \"ItemName\":\"GLO N20000=138GB 30DAYS\",\n" +
            "                        \"Amount\":\"20000\",\n" +
            "                        \"ItemDesc\":\"N20000=138GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-15\",\n" +
            "                        \"ItemName\":\"NIGHT N25 =250MB 1 DAY\",\n" +
            "                        \"Amount\":\"25\",\n" +
            "                        \"ItemDesc\":\"N25 =250MB 1 DAY\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-19\",\n" +
            "                        \"ItemName\":\"GLO N2500=7.7GB 30DAYS\",\n" +
            "                        \"Amount\":\"2500\",\n" +
            "                        \"ItemDesc\":\"N2500=7.7GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-23\",\n" +
            "                        \"ItemName\":\"GLO N3000=10GB 30DAYS\",\n" +
            "                        \"Amount\":\"3000\",\n" +
            "                        \"ItemDesc\":\"N3000=10GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-64\",\n" +
            "                        \"ItemName\":\"GLOMEGA N30000 AUTO 225GB 30DAYS\",\n" +
            "                        \"Amount\":\"30000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N30000 AUTO 225GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-434\",\n" +
            "                        \"ItemName\":\"GLOMEGA N30000 ONEOFF 225GB 30DAYS\",\n" +
            "                        \"Amount\":\"30000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N30000 ONEOFF 225GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-65\",\n" +
            "                        \"ItemName\":\"GLOMEGA N36000 AUTO 300GB 30DAYS\",\n" +
            "                        \"Amount\":\"36000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N36000 AUTO 300GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-435\",\n" +
            "                        \"ItemName\":\"GLOMEGA N36000 ONEOFF 300GB 30DAYS\",\n" +
            "                        \"Amount\":\"36000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N36000 ONEOFF 300GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-12\",\n" +
            "                        \"ItemName\":\"GLO N4000=13.25GB 30DAYS\",\n" +
            "                        \"Amount\":\"4000\",\n" +
            "                        \"ItemDesc\":\"N4000=13.25GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-18\",\n" +
            "                        \"ItemName\":\"GLO N50=50 MB 1DAY\",\n" +
            "                        \"Amount\":\"50\",\n" +
            "                        \"ItemDesc\":\"N50=50 MB 1DAY\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-30\",\n" +
            "                        \"ItemName\":\"NIGHT N50 =500MB 1 DAY\",\n" +
            "                        \"Amount\":\"50\",\n" +
            "                        \"ItemDesc\":\"N50 =500MB 1 DAY\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-27\",\n" +
            "                        \"ItemName\":\"GLO N500=1.35GB 14DAYS\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"N500=1.35GB 14DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-5\",\n" +
            "                        \"ItemName\":\"GLO N5000=18.25GB 30DAYS\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"N5000=18.25GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-66\",\n" +
            "                        \"ItemName\":\"GLOMEGA N50000 AUTO 425GB 90DAYS\",\n" +
            "                        \"Amount\":\"50000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N50000 AUTO 425GB 90DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-436\",\n" +
            "                        \"ItemName\":\"GLOMEGA N50000 ONEOFF 425GB 90DAYS\",\n" +
            "                        \"Amount\":\"50000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N50000 ONEOFF 425GB 90DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-67\",\n" +
            "                        \"ItemName\":\"GLOMEGA N60000 AUTO 525GB 120DAYS\",\n" +
            "                        \"Amount\":\"60000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N60000 AUTO 525GB 120DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-437\",\n" +
            "                        \"ItemName\":\"GLOMEGA N60000 ONEOFF 525GB 120DAYS\",\n" +
            "                        \"Amount\":\"60000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N60000 ONEOFF 525GB 120DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-68\",\n" +
            "                        \"ItemName\":\"GLOMEGA N75000 AUTO 675GB 120DAYS\",\n" +
            "                        \"Amount\":\"75000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N75000 AUTO 675GB 120DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-438\",\n" +
            "                        \"ItemName\":\"GLOMEGA N75000 ONEOFF  675GB 120DAYS\",\n" +
            "                        \"Amount\":\"75000\",\n" +
            "                        \"ItemDesc\":\"GLOMEGA N75000 ONEOFF  675GB 120DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"DATA-4\",\n" +
            "                        \"ItemName\":\"GLO N8000=29.5GB 30DAYS\",\n" +
            "                        \"Amount\":\"8000\",\n" +
            "                        \"ItemDesc\":\"N8000=29.5GB 30DAYS\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     }\n" +
            "                  ]";

    public static String mtn = "[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N100 for 100MB Daily\",\n" +
            "                        \"Amount\":\"100\",\n" +
            "                        \"ItemDesc\":\"MTN N100 for 100MB Daily\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN  N1000 for   1.5GB 1-Month Mobile\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"MTN N1000 for   1.5GB 1-Month Mobile\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk Monthly Bundle for N1000\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"Xtratalk Monthly Bundle for N1000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 3GB Monthly Bundle for N1000\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"Xtradata 3GB Monthly Bundle for N1000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 40GB Monthly RACT_NG_Data_198\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"MTN 40GB Monthly RACT_NG_Data_198\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk Monthly Bundle for N10000\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"Xtratalk Monthly Bundle for N10000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 60GB Monthly Bundle for N10000\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"Xtradata 60GB Monthly Bundle for N10000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"12\",\n" +
            "                        \"ItemName\":\"MTN N10000 for 25GB SME Monthly Plan\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"MTN N10000 for 25GB SME Monthly Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N100,000 for  325GB (6Months)\",\n" +
            "                        \"Amount\":\"100000\",\n" +
            "                        \"ItemDesc\":\"MTN N100,000 for  325GB (6Months)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"12\",\n" +
            "                        \"ItemName\":\"MTN N100000 FOR 360GB SME 3-Months Plan\",\n" +
            "                        \"Amount\":\"100000\",\n" +
            "                        \"ItemDesc\":\"MTN N100000 FOR 360GB SME 3-Months Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 100000 FOR 1TB YEARLY\",\n" +
            "                        \"Amount\":\"100000\",\n" +
            "                        \"ItemDesc\":\"MTN 100000 FOR 1TB YEARLY\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N1200 for 2GB  Monthly\",\n" +
            "                        \"Amount\":\"1200\",\n" +
            "                        \"ItemDesc\":\"MTN N1200 for 2GB  Monthly\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N120000 FOR 400GB YEARLY PLAN\",\n" +
            "                        \"Amount\":\"120000\",\n" +
            "                        \"ItemDesc\":\"MTN N120000 FOR 400GB YEARLY PLAN\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"12\",\n" +
            "                        \"ItemName\":\"MTN 135000 35GB SME Monthly\",\n" +
            "                        \"Amount\":\"135000\",\n" +
            "                        \"ItemDesc\":\"MTN 135000 35GB SME Monthly\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N1500 FOR 6GB Weekly Plan\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"MTN N1500 FOR 6GB Weekly Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"36\",\n" +
            "                        \"ItemName\":\"MTN 3GB MonthlyRACT_NG_Data_211\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"MTN 3GB MonthlyRACT_NG_Data_211\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 75GB Monthly RACT_NG_Data_278\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"MTN 75GB Monthly RACT_NG_Data_278\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk Monthly Bundle for N15000\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"Xtratalk Monthly Bundle for N15000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 100GB Monthly Bundle for N15000\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"Xtradata 100GB Monthly Bundle for N15000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N200 for  200MB 3-Day Plan\",\n" +
            "                        \"Amount\":\"200\",\n" +
            "                        \"ItemDesc\":\"MTN N200 for 200MB 3-Day Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 400MB 200 3days Bundle\",\n" +
            "                        \"Amount\":\"200\",\n" +
            "                        \"ItemDesc\":\"Xtradata 400MB 200 3days Bundle\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk 200 3days Bundle\",\n" +
            "                        \"Amount\":\"200\",\n" +
            "                        \"ItemDesc\":\"Xtratalk 200 3days Bundle\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N2000for 4.5GB 1-Month All Day \",\n" +
            "                        \"Amount\":\"2000\",\n" +
            "                        \"ItemDesc\":\"MTN N2000 for 4.5GB 1-Month All Day \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk Monthly Bundle for N2000\",\n" +
            "                        \"Amount\":\"2000\",\n" +
            "                        \"ItemDesc\":\"Xtratalk Monthly Bundle for N2000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 9GB Monthly Bundle for N2000\",\n" +
            "                        \"Amount\":\"2000\",\n" +
            "                        \"ItemDesc\":\"Xtradata 9GB Monthly Bundle for N2000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 75GB 2-Month Plan RACT_NG_Data_199\",\n" +
            "                        \"Amount\":\"20000\",\n" +
            "                        \"ItemDesc\":\"MTN 75GB 2-Month Plan RACT_NG_Data_199\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"36\",\n" +
            "                        \"ItemName\":\"MTN 110GB Monthly Plan RACT_NG_Data_746\",\n" +
            "                        \"Amount\":\"20000\",\n" +
            "                        \"ItemDesc\":\"MTN 110GB Monthly Plan RACT_NG_Data_746\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk Monthly Bundle for N20000\",\n" +
            "                        \"Amount\":\"20000\",\n" +
            "                        \"ItemDesc\":\"Xtratalk Monthly Bundle for N20000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 140GB Monthly Bundle for N20000\",\n" +
            "                        \"Amount\":\"20000\",\n" +
            "                        \"ItemDesc\":\"Xtradata 140GB Monthly Bundle for N20000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N2500 FOR 6GB Monthly Plan\",\n" +
            "                        \"Amount\":\"2500\",\n" +
            "                        \"ItemDesc\":\"MTN N2500 FOR 6GB Monthly Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"12\",\n" +
            "                        \"ItemName\":\"MTN 250000 FOR 1TB SME 3-Months Plan \",\n" +
            "                        \"Amount\":\"250000\",\n" +
            "                        \"ItemDesc\":\"MTN 250000 FOR 1TB SME 3-Months Plan \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 2.5TB  yearly Plan RACT_NG_Data_203\",\n" +
            "                        \"Amount\":\"250000\",\n" +
            "                        \"ItemDesc\":\"MTN  2.5TB  yearly Plan RACT_NG_Data_203\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N300 for 350MB weekly  \",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"MTN N300 for 350MB weekly  \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk Weekly Bundle for N300\",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"Xtratalk Weekly Bundle for N300\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 700MB Weekly Bundle for 300\",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"Xtradata 700MB Weekly Bundle for 300\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"18\",\n" +
            "                        \"ItemName\":\"MTN N300 for 1GB DAILY\",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"MTN N300 for 1GB DAILY\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N3000 FOR 10GB MONTHLY PLAN\",\n" +
            "                        \"Amount\":\"3000\",\n" +
            "                        \"ItemDesc\":\"MTN N3000 FOR 10GB MONTHLY PLAN\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"36\",\n" +
            "                        \"ItemName\":\"MTN 11GB  MONTHLY PLAN RACT_NG_Data_1593\",\n" +
            "                        \"Amount\":\"3000\",\n" +
            "                        \"ItemDesc\":\"MTN 11GB  MONTHLY PLAN RACT_NG_Data_1593\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 120GB 2-Month Plan RACT_NG_Data_210\",\n" +
            "                        \"Amount\":\"30000\",\n" +
            "                        \"ItemDesc\":\"MTN 120GB 2-Month Plan RACT_NG_Data_210\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 3500 for 12GB Monthly\",\n" +
            "                        \"Amount\":\"3500\",\n" +
            "                        \"ItemDesc\":\"MTN 3500 for 12GB Monthly \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"36\",\n" +
            "                        \"ItemName\":\"MTN 13GB  MONTHLY PLAN RACT_NG_Data_1594\",\n" +
            "                        \"Amount\":\"3500\",\n" +
            "                        \"ItemDesc\":\"MTN 13GB  MONTHLY PLAN RACT_NG_Data_1594\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N450,000 for  1500GB (1Yr)\",\n" +
            "                        \"Amount\":\"450000\",\n" +
            "                        \"ItemDesc\":\"MTN N450,000 for  1500GB (1Yr)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"132\",\n" +
            "                        \"ItemName\":\"MTN 50MB 2Go Weekly Plan.Valid for 7Days.\",\n" +
            "                        \"Amount\":\"50\",\n" +
            "                        \"ItemDesc\":\"MTN 50MB 2Go Weekly Plan.Valid for 7Days.\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N500 for 750MB 2-Week Plan\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"MTN N500 for 750MB 2-Week Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"20\",\n" +
            "                        \"ItemName\":\"MTN 500 for 2.5GB 2-Day Plan\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"MTN 500 for 2.5GB 2-Day Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"19\",\n" +
            "                        \"ItemName\":\"MTN 500 for 1GB Weekly Plan\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"MTN 500 for 1GB Weekly Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk Weekly Bundle for N500\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"Xtratalk Weekly Bundle for N500\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 1.47GB Weeks Bundle for N500\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"Xtradata 1.47GB Weeks Bundle for N500\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 5000 for 20GB Monthly\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"MTN 5000 for 20GB Monthly \",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"24\",\n" +
            "                        \"ItemName\":\"Xtratalk Monthly Bundle for N15000\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"Xtratalk Monthly Bundle for N15000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"25\",\n" +
            "                        \"ItemName\":\"Xtradata 30GB Monthly Bundle for N5000\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"Xtradata 30GB Monthly Bundle for N5000\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"36\",\n" +
            "                        \"ItemName\":\"MTN 22GB  MONTHLY PLAN RACT_NG_Data_1595\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"MTN 22GB  MONTHLY PLAN RACT_NG_Data_1595\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 150GB 3-Month Plan RACT_NG_Data_200\",\n" +
            "                        \"Amount\":\"50000\",\n" +
            "                        \"ItemDesc\":\"MTN 150GB 3-Month Plan RACT_NG_Data_200\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"12\",\n" +
            "                        \"ItemName\":\"MTN N50000 for 165GB SME 2-Months Plan\",\n" +
            "                        \"Amount\":\"50000\",\n" +
            "                        \"ItemDesc\":\"MTN N50000 for 165GB SME 2-Months Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"36\",\n" +
            "                        \"ItemName\":\"MTN for 25GB Monthly PlanRACT_NG_Data_212\",\n" +
            "                        \"Amount\":\"6000\",\n" +
            "                        \"ItemDesc\":\"MTN  25GB Monthly PlanRACT_NG_Data_212\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN 27GB  MONTHLY PLAN RACT_NG_Data_1596\",\n" +
            "                        \"Amount\":\"6000\",\n" +
            "                        \"ItemDesc\":\"MTN 27GB  MONTHLY PLAN RACT_NG_Data_1596\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"9\",\n" +
            "                        \"ItemName\":\"MTN N75000 for 250GB 3-Month Plan\",\n" +
            "                        \"Amount\":\"75000\",\n" +
            "                        \"ItemDesc\":\"MTN N75000 for 250GB 3-Month Plan\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     }\n" +
            "                  ]";

    public static String spectranet = "[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PIN_PURCHASE2\",\n" +
            "                        \"ItemName\":\"N1,000 PIN PURCHASE\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"N1,000 PIN PURCHASE\",\n" +
            "                        \"ItemOthers\":\"N1,000 PIN PURCHASE\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PIN_PURCHASE6\",\n" +
            "                        \"ItemName\":\"N10,000 PIN PURCHASE\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"N10,000 PIN PURCHASE\",\n" +
            "                        \"ItemOthers\":\"N10,000 PIN PURCHASE\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PIN_PURCHASE3\",\n" +
            "                        \"ItemName\":\"N2,000 PIN PURCHASE\",\n" +
            "                        \"Amount\":\"2000\",\n" +
            "                        \"ItemDesc\":\"N2,000 PIN PURCHASE\",\n" +
            "                        \"ItemOthers\":\"N2,000 PIN PURCHASE\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PIN_PURCHASE1\",\n" +
            "                        \"ItemName\":\"N500 PIN PURCHASE\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"N500 PIN PURCHASE\",\n" +
            "                        \"ItemOthers\":\"N500 PIN PURCHASE\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PIN_PURCHASE4\",\n" +
            "                        \"ItemName\":\"N5,000 PIN PURCHASE\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"N5,000 PIN PURCHASE\",\n" +
            "                        \"ItemOthers\":\"N5,000 PIN PURCHASE\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PIN_PURCHASE5\",\n" +
            "                        \"ItemName\":\"N7,000 PIN PURCHASE\",\n" +
            "                        \"Amount\":\"7000\",\n" +
            "                        \"ItemDesc\":\"N7,000 PIN PURCHASE\",\n" +
            "                        \"ItemOthers\":\"N7,000 PIN PURCHASE\"\n" +
            "                     }\n" +
            "                  ]";

    public static String dstv = "[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"ASIADDE36\",\n" +
            "                        \"ItemName\":\"DStv Asian Add-on Bouquet E36\",\n" +
            "                        \"Amount\":\"7100\",\n" +
            "                        \"ItemDesc\":\"DStv Asian Add-on Bouquet E36\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"ASIAE36\",\n" +
            "                        \"ItemName\":\"Asian\",\n" +
            "                        \"Amount\":\"7100\",\n" +
            "                        \"ItemDesc\":\"Asian\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPE36\",\n" +
            "                        \"ItemName\":\"Compact\",\n" +
            "                        \"Amount\":\"9000\",\n" +
            "                        \"ItemDesc\":\"Compact\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPE36,ASIADDE36\",\n" +
            "                        \"ItemName\":\"Compact + Asia\",\n" +
            "                        \"Amount\":\"16100\",\n" +
            "                        \"ItemDesc\":\"Compact + Asia\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPE36,ASIADDE36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Compact + Asia + Xtraview\",\n" +
            "                        \"Amount\":\"19000\",\n" +
            "                        \"ItemDesc\":\"Compact + Asia + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPE36,FRN15E36\",\n" +
            "                        \"ItemName\":\"Compact + French Plus\",\n" +
            "                        \"Amount\":\"18300\",\n" +
            "                        \"ItemDesc\":\"Compact + French Plus\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPE36,FRN7E36\",\n" +
            "                        \"ItemName\":\"Compact + French Touch\",\n" +
            "                        \"Amount\":\"10200\",\n" +
            "                        \"ItemDesc\":\"Compact + French Touch\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPE36,FRN7E36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Compact + French Touch + Xtraview\",\n" +
            "                        \"Amount\":\"12700\",\n" +
            "                        \"ItemDesc\":\"Compact + French Touch + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPE36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Compact + Xtraview\",\n" +
            "                        \"Amount\":\"11900\",\n" +
            "                        \"ItemDesc\":\"Compact + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPLE36\",\n" +
            "                        \"ItemName\":\"Compact Plus\",\n" +
            "                        \"Amount\":\"14250\",\n" +
            "                        \"ItemDesc\":\"Compact Plus\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPLE36,ASIADDE36\",\n" +
            "                        \"ItemName\":\"CompactPlus + Asia\",\n" +
            "                        \"Amount\":\"21350\",\n" +
            "                        \"ItemDesc\":\"CompactPlus + Asia\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPLE36,ASIADDE36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"CompactPlus + Asia + Xtraview\",\n" +
            "                        \"Amount\":\"24250\",\n" +
            "                        \"ItemDesc\":\"CompactPlus + Asia + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPLE36,FRN15E36\",\n" +
            "                        \"ItemName\":\"CompactPlus + French Plus\",\n" +
            "                        \"Amount\":\"23550\",\n" +
            "                        \"ItemDesc\":\"CompactPlus + French Plus\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPLE36,FRN15E36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"CompactPlus + French Plus + Xtraview\",\n" +
            "                        \"Amount\":\"26450\",\n" +
            "                        \"ItemDesc\":\"CompactPlus + French Plus + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPLE36,FRN7E36\",\n" +
            "                        \"ItemName\":\"CompactPlus + French Touch\",\n" +
            "                        \"Amount\":\"16900\",\n" +
            "                        \"ItemDesc\":\"CompactPlus + French Touch\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"COMPLE36_17150\",\n" +
            "                        \"ItemName\":\"CompactPlus + Xtraview\",\n" +
            "                        \"Amount\":\"17150\",\n" +
            "                        \"ItemDesc\":\"CompactPlus + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"FRN11E36\",\n" +
            "                        \"ItemName\":\"French 11 Bouquet E36\",\n" +
            "                        \"Amount\":\"4100\",\n" +
            "                        \"ItemDesc\":\"French 11 Bouquet E36\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"FRN15E36\",\n" +
            "                        \"ItemName\":\"DStv French Plus Add-on Bouquet E36\",\n" +
            "                        \"Amount\":\"9300\",\n" +
            "                        \"ItemDesc\":\"DStv French Plus Add-on Bouquet E36\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"FRN7E36\",\n" +
            "                        \"ItemName\":\"DStv French Touch Add-on Bouquet E36\",\n" +
            "                        \"Amount\":\"2650\",\n" +
            "                        \"ItemDesc\":\"DStv French Touch Add-on Bouquet E36\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"HDPVRE36\",\n" +
            "                        \"ItemName\":\"DStv HDPVR Access Service E36\",\n" +
            "                        \"Amount\":\"2900\",\n" +
            "                        \"ItemDesc\":\"DStv HDPVR Access Service E36\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"HDPVRE36\",\n" +
            "                        \"ItemName\":\"Xtraview Access\",\n" +
            "                        \"Amount\":\"2500\",\n" +
            "                        \"ItemDesc\":\"Xtraview Access\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NLTESE36\",\n" +
            "                        \"ItemName\":\"Padi\",\n" +
            "                        \"Amount\":\"2150\",\n" +
            "                        \"ItemDesc\":\"Padi\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NLTESE36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Padi + Xtraview\",\n" +
            "                        \"Amount\":\"5050\",\n" +
            "                        \"ItemDesc\":\"Padi + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NNJ1E36\",\n" +
            "                        \"ItemName\":\"Yanga\",\n" +
            "                        \"Amount\":\"2950\",\n" +
            "                        \"ItemDesc\":\"Yanga\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NNJ1E36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Yanga + Xtraview\",\n" +
            "                        \"Amount\":\"5850\",\n" +
            "                        \"ItemDesc\":\"Yanga + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NNJ2E36\",\n" +
            "                        \"ItemName\":\"Confam\",\n" +
            "                        \"Amount\":\"5300\",\n" +
            "                        \"ItemDesc\":\"Confam\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NNJ2E36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Confam + Xtraview\",\n" +
            "                        \"Amount\":\"8200\",\n" +
            "                        \"ItemDesc\":\"Confam + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PRWASIE36\",\n" +
            "                        \"ItemName\":\"PremiumAsia\",\n" +
            "                        \"Amount\":\"23500\",\n" +
            "                        \"ItemDesc\":\"PremiumAsia\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PRWASIE36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Premiumasia + Xtraview\",\n" +
            "                        \"Amount\":\"26400\",\n" +
            "                        \"ItemDesc\":\"Premiumasia + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PRWE36\",\n" +
            "                        \"ItemName\":\"Premium\",\n" +
            "                        \"Amount\":\"21000\",\n" +
            "                        \"ItemDesc\":\"Premium\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PRWE36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Premium + Xtraview\",\n" +
            "                        \"Amount\":\"23900\",\n" +
            "                        \"ItemDesc\":\"Premium + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PRWFRNSE36\",\n" +
            "                        \"ItemName\":\"Premium + French\",\n" +
            "                        \"Amount\":\"29300\",\n" +
            "                        \"ItemDesc\":\"Premium + French\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"PRWFRNSE36,HDPVRE36\",\n" +
            "                        \"ItemName\":\"Premium + French + Xtraview\",\n" +
            "                        \"Amount\":\"32200\",\n" +
            "                        \"ItemDesc\":\"Premium + French + Xtraview\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     }\n" +
            "                  ]";

    public static String gotv = "[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"GOTVNJ1\",\n" +
            "                        \"ItemName\":\"GOtv Jinja Bouquet (N1900)\",\n" +
            "                        \"Amount\":\"1900\",\n" +
            "                        \"ItemDesc\":\"GOtv Jinja Bouquet\",\n" +
            "                        \"ItemOthers\":\"1\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"GOLITE_2400\",\n" +
            "                        \"ItemName\":\"GOtv Smallie - quarterly (3 months)(N2400)\",\n" +
            "                        \"Amount\":\"2400\",\n" +
            "                        \"ItemDesc\":\"GOtv Smallie - quarterly (3 months)\",\n" +
            "                        \"ItemOthers\":\"3\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"GOTVNJ2\",\n" +
            "                        \"ItemName\":\"GOtv Jolli Bouquet (N2800)\",\n" +
            "                        \"Amount\":\"2800\",\n" +
            "                        \"ItemDesc\":\"GOtv Jolli Bouquet\",\n" +
            "                        \"ItemOthers\":\"1\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"GOTVMAX\",\n" +
            "                        \"ItemName\":\"GOtv Max (N4150)\",\n" +
            "                        \"Amount\":\"4150\",\n" +
            "                        \"ItemDesc\":\"GOtv Max\",\n" +
            "                        \"ItemOthers\":\"1\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"GOTVSUPA\",\n" +
            "                        \"ItemName\":\"GOtv Supa (5500)\",\n" +
            "                        \"Amount\":\"5500\",\n" +
            "                        \"ItemDesc\":\"GOtv Supa\",\n" +
            "                        \"ItemOthers\":\"1\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"GOLITE_7000\",\n" +
            "                        \"ItemName\":\"GOtv Smallie - yearly (N7000)\",\n" +
            "                        \"Amount\":\"7000\",\n" +
            "                        \"ItemDesc\":\"GOtv Smallie - yearly\",\n" +
            "                        \"ItemOthers\":\"12\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"GOLITE\",\n" +
            "                        \"ItemName\":\"GOtv Smallie - monthly (N900)\",\n" +
            "                        \"Amount\":\"900\",\n" +
            "                        \"ItemDesc\":\"GOtv Smallie - monthly\",\n" +
            "                        \"ItemOthers\":\"1\"\n" +
            "                     }\n" +
            "                  ]";

    public static String disco = "[\n" +
            "   {\n" +
            "      \"service_type\":\"ABJ_POSTPAID\",\n" +
            "      \"shortname\":\"ABUJA POSTPAID\",\n" +
            "      \"biller_id\":1104,\n" +
            "      \"product_id\":1104,\n" +
            "      \"name\":\"ABUJA POSTPAID\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"ABJ_PREPAID\",\n" +
            "      \"shortname\":\"ABUJA PREPAID\",\n" +
            "      \"biller_id\":1106,\n" +
            "      \"product_id\":1106,\n" +
            "      \"name\":\"ABUJA PREPAID\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"EKEDC_POSTPAID\",\n" +
            "      \"shortname\":\"EKEDC POSTPAID\",\n" +
            "      \"biller_id\":1170,\n" +
            "      \"product_id\":1170,\n" +
            "      \"name\":\"EKEDC POSTPAID\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"EKEDC_PREPAID\",\n" +
            "      \"shortname\":\"EKEDC PREPAID\",\n" +
            "      \"biller_id\":1171,\n" +
            "      \"product_id\":1171,\n" +
            "      \"name\":\"EKEDC PREPAID\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"ekdc prepaid\",\n" +
            "      \"shortname\":\"EKO Electric Prepaid\",\n" +
            "      \"biller_id\":63,\n" +
            "      \"product_id\":63,\n" +
            "      \"name\":\"EKO Electric Prepaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"EEDC\",\n" +
            "      \"shortname\":\"Enugu Electricity\",\n" +
            "      \"biller_id\":1182,\n" +
            "      \"product_id\":1182,\n" +
            "      \"name\":\"Enugu Electricity\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"IBEDC_F\",\n" +
            "      \"shortname\":\"Ibadan Electric New\",\n" +
            "      \"biller_id\":1169,\n" +
            "      \"product_id\":1169,\n" +
            "      \"name\":\"Ibadan Electric New\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"iedc postpaid\",\n" +
            "      \"shortname\":\"Ikeja Electric PostPaid\",\n" +
            "      \"biller_id\":41,\n" +
            "      \"product_id\":41,\n" +
            "      \"name\":\"Ikeja Electric PostPaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"iedc\",\n" +
            "      \"shortname\":\"Ikeja Electric Prepaid\",\n" +
            "      \"biller_id\":42,\n" +
            "      \"product_id\":42,\n" +
            "      \"name\":\"Ikeja Electric Prepaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"JOS_POSTPAID\",\n" +
            "      \"shortname\":\"Jos Electric Postpaid\",\n" +
            "      \"biller_id\":1111,\n" +
            "      \"product_id\":1111,\n" +
            "      \"name\":\"Jos Electric Postpaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"JOS_PREPAID\",\n" +
            "      \"shortname\":\"Jos Electric Prepaid\",\n" +
            "      \"biller_id\":1114,\n" +
            "      \"product_id\":1114,\n" +
            "      \"name\":\"Jos Electric Prepaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"KADUNA_POSTPAID\",\n" +
            "      \"shortname\":\"Kaduna Electric Postpaid\",\n" +
            "      \"biller_id\":1107,\n" +
            "      \"product_id\":1107,\n" +
            "      \"name\":\"Kaduna Electric Postpaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"KADUNA_PREPAID\",\n" +
            "      \"shortname\":\"Kaduna Electric Prepaid\",\n" +
            "      \"biller_id\":1108,\n" +
            "      \"product_id\":1108,\n" +
            "      \"name\":\"Kaduna Electric Prepaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"KEDCOPostpaid\",\n" +
            "      \"shortname\":\"KEDCO Postpaid\",\n" +
            "      \"biller_id\":1167,\n" +
            "      \"product_id\":1167,\n" +
            "      \"name\":\"KEDCO Postpaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"KEDCOPrepaid\",\n" +
            "      \"shortname\":\"KEDCO Prepaid\",\n" +
            "      \"biller_id\":1168,\n" +
            "      \"product_id\":1168,\n" +
            "      \"name\":\"KEDCO Prepaid\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"service_type\":\"PHEDDIR2\",\n" +
            "      \"shortname\":\"PHED2\",\n" +
            "      \"biller_id\":1116,\n" +
            "      \"product_id\":1116,\n" +
            "      \"name\":\"PHED2\"\n" +
            "   }\n" +
            "]";

    public static String smile = "[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"606\",\n" +
            "                        \"ItemName\":\"1.5GB Bigga  (#1,000.00)\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"1.5GB Bigga  (#1,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"627\",\n" +
            "                        \"ItemName\":\"2GB Flexi-Weekly  (#1,000.00)\",\n" +
            "                        \"Amount\":\"1000\",\n" +
            "                        \"ItemDesc\":\"2GB Flexi-Weekly  (#1,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"616\",\n" +
            "                        \"ItemName\":\"40GB Bigga Plan (#10,000.00)\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"40GB Bigga Plan (#10,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"629\",\n" +
            "                        \"ItemName\":\"Unlimited-Lite (#10,000)\",\n" +
            "                        \"Amount\":\"10000\",\n" +
            "                        \"ItemDesc\":\"Unlimited-Lite (#10,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"413\",\n" +
            "                        \"ItemName\":\"2GB MidNite (#1,020.00)\",\n" +
            "                        \"Amount\":\"1020\",\n" +
            "                        \"ItemDesc\":\"2GB MidNite (#1,020.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"748\",\n" +
            "                        \"ItemName\":\"SmileVoice ONLY 135 (#1,150.00)\",\n" +
            "                        \"Amount\":\"1150\",\n" +
            "                        \"ItemDesc\":\"SmileVoice ONLY 135 (#1,150.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"607\",\n" +
            "                        \"ItemName\":\"2GB Bigga (#1,200.00)\",\n" +
            "                        \"Amount\":\"1200\",\n" +
            "                        \"ItemDesc\":\"2GB Bigga (#1,200.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"605\",\n" +
            "                        \"ItemName\":\"400GB-Anytime (#120,000)\",\n" +
            "                        \"Amount\":\"120000\",\n" +
            "                        \"ItemDesc\":\"400GB-Anytime (#120,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"617\",\n" +
            "                        \"ItemName\":\"60GB Bigga (#13,500)\",\n" +
            "                        \"Amount\":\"13500\",\n" +
            "                        \"ItemDesc\":\"60GB Bigga (#13,500)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"608\",\n" +
            "                        \"ItemName\":\"3GB Bigga (#1,500.00)\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"3GB Bigga (#1,500.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"628\",\n" +
            "                        \"ItemName\":\"6GB FlexiWeekly  (#1,500.00)\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"6GB FlexiWeekly  (#1,500.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"618\",\n" +
            "                        \"ItemName\":\"75GB Bigga (#15,000)\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"75GB Bigga (#15,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"621\",\n" +
            "                        \"ItemName\":\"50GB Bumpa-Value (#15000)\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"50GB Bumpa-Value (#15000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"630\",\n" +
            "                        \"ItemName\":\"Unlimited-Essential (#15,000)\",\n" +
            "                        \"Amount\":\"15000\",\n" +
            "                        \"ItemDesc\":\"Unlimited-Essential (#15,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"414\",\n" +
            "                        \"ItemName\":\"3GB MidNite  (#1,530.00)\",\n" +
            "                        \"Amount\":\"1530\",\n" +
            "                        \"ItemDesc\":\"3GB MidNite  (#1,530.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"415\",\n" +
            "                        \"ItemName\":\"3GB Weekend ONLY  (#1,530.00)\",\n" +
            "                        \"Amount\":\"1530\",\n" +
            "                        \"ItemDesc\":\"3GB Weekend ONLY  (#1,530.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"602\",\n" +
            "                        \"ItemName\":\"35GB-Anytime (#16,000)\",\n" +
            "                        \"Amount\":\"16000\",\n" +
            "                        \"ItemDesc\":\"35GB-Anytime (#16,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"750\",\n" +
            "                        \"ItemName\":\"SmileVoice ONLY 150 (#1,650.00)\",\n" +
            "                        \"Amount\":\"1650\",\n" +
            "                        \"ItemDesc\":\"SmileVoice ONLY 150 (#1,650.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"619\",\n" +
            "                        \"ItemName\":\"100GB Bigga (#18,000)\",\n" +
            "                        \"Amount\":\"18000\",\n" +
            "                        \"ItemDesc\":\"100GB Bigga (#18,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"620\",\n" +
            "                        \"ItemName\":\"5GB Bigga  (#2,000.00)\",\n" +
            "                        \"Amount\":\"2000\",\n" +
            "                        \"ItemDesc\":\"5GB Bigga  (#2,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"655\",\n" +
            "                        \"ItemName\":\"UnlimitedPremium (#20,000)\",\n" +
            "                        \"Amount\":\"20000\",\n" +
            "                        \"ItemDesc\":\"UnlimitedPremium (#20,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"752\",\n" +
            "                        \"ItemName\":\"SmileVoice ONLY 175 (#2,200.00)\",\n" +
            "                        \"Amount\":\"2200\",\n" +
            "                        \"ItemDesc\":\"SmileVoice ONLY 175 (#2,200.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"583\",\n" +
            "                        \"ItemName\":\"UnlimitedPlatinum (#24,000)\",\n" +
            "                        \"Amount\":\"24000\",\n" +
            "                        \"ItemDesc\":\"UnlimitedPlatinum (#24,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"609\",\n" +
            "                        \"ItemName\":\"6.5GB Bigga  (#2,500.00)\",\n" +
            "                        \"Amount\":\"2500\",\n" +
            "                        \"ItemDesc\":\"6.5GB Bigga  (#2,500.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"624\",\n" +
            "                        \"ItemName\":\"1GB Flexi (#300.00)\",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"1GB Flexi (#300.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"610\",\n" +
            "                        \"ItemName\":\"8GB Bigga  (#3,000.00)\",\n" +
            "                        \"Amount\":\"3000\",\n" +
            "                        \"ItemDesc\":\"8GB Bigga  (#3,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"622\",\n" +
            "                        \"ItemName\":\"80GB Bumpa-Value (#30,000)\",\n" +
            "                        \"Amount\":\"30000\",\n" +
            "                        \"ItemDesc\":\"80GB Bumpa-Value (#30,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"749\",\n" +
            "                        \"ItemName\":\"SmileVoice ONLY 430  (#3,500.00)\",\n" +
            "                        \"Amount\":\"3500\",\n" +
            "                        \"ItemDesc\":\"SmileVoice ONLY 430  (#3,500.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"611\",\n" +
            "                        \"ItemName\":\"10GB Bigga (#3,500.00)\",\n" +
            "                        \"Amount\":\"3500\",\n" +
            "                        \"ItemDesc\":\"10GB Bigga (#3,500.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"603\",\n" +
            "                        \"ItemName\":\"90GB-Anytime (#36,000)\",\n" +
            "                        \"Amount\":\"36000\",\n" +
            "                        \"ItemDesc\":\"90GB-Anytime (#36,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"612\",\n" +
            "                        \"ItemName\":\"12GB Bigga  (#4,000.00)\",\n" +
            "                        \"Amount\":\"4000\",\n" +
            "                        \"ItemDesc\":\"12GB Bigga  (#4,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"623\",\n" +
            "                        \"ItemName\":\"100GB Bumpa-Value (#40,000)\",\n" +
            "                        \"Amount\":\"40000\",\n" +
            "                        \"ItemDesc\":\"100GB Bumpa-Value (#40,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"751\",\n" +
            "                        \"ItemName\":\"SmileVoice ONLY 450 (#4,450.00)\",\n" +
            "                        \"Amount\":\"4450\",\n" +
            "                        \"ItemDesc\":\"SmileVoice ONLY 450 (#4,450.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"625\",\n" +
            "                        \"ItemName\":\"2.5GB Flexi  (#500.00)\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"2.5GB Flexi  (#500.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"626\",\n" +
            "                        \"ItemName\":\"1GB Flexi Weekly (#500.00)\",\n" +
            "                        \"Amount\":\"500\",\n" +
            "                        \"ItemDesc\":\"1GB Flexi Weekly (#500.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"613\",\n" +
            "                        \"ItemName\":\"15GB Bigga  (#5,000.00)\",\n" +
            "                        \"Amount\":\"5000\",\n" +
            "                        \"ItemDesc\":\"15GB Bigga  (#5,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"753\",\n" +
            "                        \"ItemName\":\"SmileVoice ONLY 500 (#5,500.00)\",\n" +
            "                        \"Amount\":\"5500\",\n" +
            "                        \"ItemDesc\":\"SmileVoice ONLY 500 (#5,500.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"747\",\n" +
            "                        \"ItemName\":\"SmileVoice ONLY 65 (#575.00)\",\n" +
            "                        \"Amount\":\"575\",\n" +
            "                        \"ItemDesc\":\"SmileVoice ONLY 65 (#575.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"614\",\n" +
            "                        \"ItemName\":\"20GB Bigga (#6,000.00)\",\n" +
            "                        \"Amount\":\"6000\",\n" +
            "                        \"ItemDesc\":\"20GB Bigga (#6,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"604\",\n" +
            "                        \"ItemName\":\"200GB-Anytime (#70,000)\",\n" +
            "                        \"Amount\":\"70000\",\n" +
            "                        \"ItemDesc\":\"200GB-Anytime (#70,000)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"601\",\n" +
            "                        \"ItemName\":\"15GB-Anytime  (#8,000.00)\",\n" +
            "                        \"Amount\":\"8000\",\n" +
            "                        \"ItemDesc\":\"15GB-Anytime  (#8,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"615\",\n" +
            "                        \"ItemName\":\"30GB Bigga (#8,000.00)\",\n" +
            "                        \"Amount\":\"8000\",\n" +
            "                        \"ItemDesc\":\"30GB Bigga (#8,000.00)\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     }\n" +
            "                  ]";

    public static String startimes = "[\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SMART SOLAR - ONE MONTH\",\n" +
            "                        \"ItemName\":\"SMART SOLAR - ONE MONTH\",\n" +
            "                        \"Amount\":\"10500\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SUPER SOLAR - ONE MONTH\",\n" +
            "                        \"ItemName\":\"SUPER SOLAR - ONE MONTH\",\n" +
            "                        \"Amount\":\"11400\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"CLASSIC- ONE WEEK\",\n" +
            "                        \"ItemName\":\"DTT CLASSIC- ONE WEEK\",\n" +
            "                        \"Amount\":\"1200\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"UNIQUE - ONE WEEK\",\n" +
            "                        \"ItemName\":\"UNIQUE - ONE WEEK\",\n" +
            "                        \"Amount\":\"1300\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SUPER- ONE WEEK\",\n" +
            "                        \"ItemName\":\"DTH SUPER- ONE WEEK\",\n" +
            "                        \"Amount\":\"1500\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"BASIC - ONE DAY\",\n" +
            "                        \"ItemName\":\"DTT BASIC - ONE DAY\",\n" +
            "                        \"Amount\":\"160\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NOVA - TWO MONTH\",\n" +
            "                        \"ItemName\":\"DTT NOVA - TWO MONTH\",\n" +
            "                        \"Amount\":\"1800\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NOVA - TWO MONTH\",\n" +
            "                        \"ItemName\":\"DTH NOVA - TWO MONTH\",\n" +
            "                        \"Amount\":\"1800\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"BASIC - ONE MONTH\",\n" +
            "                        \"ItemName\":\"DTT BASIC - ONE MONTH\",\n" +
            "                        \"Amount\":\"1850\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SMART- ONE DAY\",\n" +
            "                        \"ItemName\":\"DTH SMART- ONE DAY\",\n" +
            "                        \"Amount\":\"200\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"UNIQUE - ONE DAY\",\n" +
            "                        \"ItemName\":\"UNIQUE - ONE DAY\",\n" +
            "                        \"Amount\":\"240\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SMART- ONE MONTH\",\n" +
            "                        \"ItemName\":\"DTH SMART- ONE MONTH\",\n" +
            "                        \"Amount\":\"2600\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"CLASSIC- ONE MONTH\",\n" +
            "                        \"ItemName\":\"DTT CLASSIC- ONE MONTH\",\n" +
            "                        \"Amount\":\"2750\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NOVA - ONE WEEK\",\n" +
            "                        \"ItemName\":\"DTT NOVA - ONE WEEK\",\n" +
            "                        \"Amount\":\"300\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"CLASSIC- ONE DAY\",\n" +
            "                        \"ItemName\":\"DTT CLASSIC- ONE DAY\",\n" +
            "                        \"Amount\":\"320\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"BASIC - TWO MONTH\",\n" +
            "                        \"ItemName\":\"DTT BASIC - TWO  MONTH\",\n" +
            "                        \"Amount\":\"3700\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"UNIQUE - ONE MONTH\",\n" +
            "                        \"ItemName\":\"UNIQUE - ONE MONTH\",\n" +
            "                        \"Amount\":\"3800\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SUPER- ONE DAY\",\n" +
            "                        \"ItemName\":\"DTH SUPER- ONE DAY\",\n" +
            "                        \"Amount\":\"400\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"FRENCH -ONE MONTH\",\n" +
            "                        \"ItemName\":\"DTH FRENCH -ONE MONTH\",\n" +
            "                        \"Amount\":\"4500\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SUPER- ONE MONTH\",\n" +
            "                        \"ItemName\":\"DTH SUPER- ONE MONTH\",\n" +
            "                        \"Amount\":\"4900\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SMART - TWO MONTH\",\n" +
            "                        \"ItemName\":\"DTH SMART - TWO MONTH\",\n" +
            "                        \"Amount\":\"5200\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"CLASSIC - TWO MONTH\",\n" +
            "                        \"ItemName\":\"DTT CLASSIC - TWO MONTH\",\n" +
            "                        \"Amount\":\"5500\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"BASIC - ONE WEEK\",\n" +
            "                        \"ItemName\":\"DTT BASIC - ONE WEEK\",\n" +
            "                        \"Amount\":\"600\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SMART- ONE WEEK\",\n" +
            "                        \"ItemName\":\"DTH SMART- ONE WEEK\",\n" +
            "                        \"Amount\":\"700\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"CHINESE- ONE MONTH\",\n" +
            "                        \"ItemName\":\"DTH CHINESE- ONE MONTH\",\n" +
            "                        \"Amount\":\"7800\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NOVA - ONE DAY\",\n" +
            "                        \"ItemName\":\"DTT NOVA - ONE DAY\",\n" +
            "                        \"Amount\":\"90\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NOVA - ONE MONTH\",\n" +
            "                        \"ItemName\":\"DTT NOVA - ONE MONTH\",\n" +
            "                        \"Amount\":\"900\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"SUPER - TWO MONTH\",\n" +
            "                        \"ItemName\":\"DTH SUPER - TWO MONTH\",\n" +
            "                        \"Amount\":\"9800\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     },\n" +
            "                     {\n" +
            "                        \"ItemType\":\"NOVA SOLAR - ONE MONTH\",\n" +
            "                        \"ItemName\":\"NOVA SOLAR - ONE MONTH\",\n" +
            "                        \"Amount\":\"9900\",\n" +
            "                        \"ItemDesc\":\"-\",\n" +
            "                        \"ItemOthers\":\"-\"\n" +
            "                     }\n" +
            "                  ]";

    public static String getWholeAmount(String amount)
    {
        int i = amount.indexOf(".");
        if(i < 1)
        {
            Log.i(TAG, amount);
            return amount;
        }else
        {
            Log.i(TAG, "AMOUNT EXTRACTED B: " + amount.substring(0, i));
            return amount.substring(0, i);
        }
    }

    public static byte[] StrToHexByte(String str) {
        if (str == null)
            return null;
        else if (str.length() < 2)
            return null;
        else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public static String getField4(String amountStr) {
        Log.i(TAG, "begin amount: " + amountStr);
        int index = amountStr.indexOf(".");
        if (amountStr.substring(index + 1, amountStr.length()).length() < 2) {
            amountStr = amountStr + "0";
        }
        amountStr = amountStr.replace(".", "");
        int amtlen = amountStr.length();
        StringBuilder amtBuilder = new StringBuilder();
        if (amtlen < 12) {
            for (int i = 0; i < (12 - amtlen); i++) {
                amtBuilder.append("0");
            }
        }
        amtBuilder.append(amountStr);
        amountStr = amtBuilder.toString();
        Log.i(TAG, "begin amount: " + amountStr);
        return amountStr;
    }

    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (byte element : b) {
            String hex = Integer.toHexString(element & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static String getSerialNumber() {
        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;

            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        return serialNumber;
    }

    /*
    public static String getSerialNumber() {
        try {
            IAidlSys system = MyApplication.getINSTANCE().getDevice().getSysHandler();
            String sn = system.getSn();
            Log.i(TAG, "SN: " + sn);
            Log.i(TAG, "Serial: " + Build.SERIAL);
            return sn;
        } catch (RemoteException e) {
            AppLog.d(TAG, e.getMessage());
            return "";
        }
    }
    */

    public static void writeStringAsFile(final String fileContents, String fileName, Context context) {
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName), false);
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFileAsString(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void logISOMsg(ISO8583 msg, String[] storage)
    {
        Log.i(TAG, "----ISO MESSAGE-----");
        try {
            for (int i = 0; i < 129; i++)
            {
                try
                {
                    String log = new String(msg.getBit(i));
                    if (log != null)
                    {
                        Log.i(TAG, "Field " + i + " : " + log);
                        storage[i] = log;
                    }
                }catch (Exception e)
                {
                    //Do nothing about it
                }
            }
        } finally {
            Log.i(TAG, "--------------------");
        }

    }

    public static String maskedPan(String cardNo)
    {
        if(cardNo == null)
            return "*****";
        int cardLength = cardNo.length();
        String firCardNo = cardNo.substring(0,6);
        String lastCardNo = cardNo.substring(cardLength - 4);
        String mid = "******";
        return (firCardNo + mid + lastCardNo);
    }

    public static String getDATEYYYYMMDD(String date)
    {
        if(date == null)
            return "*****";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String format = String.valueOf(year) + date;
        return format;
    }

    public static void logISOMsgMute(ISO8583 msg, String[] storage)
    {
        //Log.i(TAG, "----ISO STORAGE-----");
        try {
            for (int i = 0; i < 129; i++)
            {
                try
                {
                    String log = new String(msg.getBit(i));
                    if (log != null)
                    {
                        storage[i] = log;
                    }
                }catch (Exception e)
                {
                    //Do nothing about it
                }
            }
        } finally {
            //Log.i(TAG, "--------------------");
        }

    }


    public static byte[] getCustomPacketHeader(byte[] isobyte) {
        String cs = String.format("%04X", ISO8583Util.byteArrayAdd(isobyte, null).length);
        byte[] bcdlen = BCDASCII.hexStringToBytes(cs);
        Log.i(TAG, "Length 2: " + bcdlen.length);
        return ISO8583Util.byteArrayAdd(bcdlen, ISO8583Util.byteArrayAdd(null, null, null), isobyte);
    }

    public static String getCurrentTime()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
