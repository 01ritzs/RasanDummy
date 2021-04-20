package com.du.de.rasandummy.util;


import android.text.SpannableStringBuilder;

import com.du.de.rasandummy.RoomDatabase.Product;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductUtil {

    public static List<Product> sort(List<Product> productList, String searchKey) {
        if (searchKey == null || searchKey.trim().length() <= 0) {
            return productList;
        }
        List<Product> sortedList = new ArrayList<>();
        for (Product item : productList) {
            if (item.getName().toLowerCase().contains(searchKey.toLowerCase())) {
                sortedList.add(item);
            }
        }
        return sortedList;
    }


    public static String getListToShare(Map<Product, Integer> mapProduct) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(Constants.APP_URL);
        sb.append(Constants.DIVIDER);
        sb.append(getTodayDate() + Constants.NEW_LINE);
        sb.append(Constants.DIVIDER);
        sb.append(Constants.HEADER);
        sb.append(Constants.DIVIDER);
        for (Map.Entry<Product, Integer> entry : mapProduct.entrySet()) {
            sb.append(entry.getValue() + " - - - - " + entry.getKey().getName() + Constants.NEW_LINE);
        }
        sb.append(Constants.DIVIDER);
        return sb.toString();
    }

    public static String getTodayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
