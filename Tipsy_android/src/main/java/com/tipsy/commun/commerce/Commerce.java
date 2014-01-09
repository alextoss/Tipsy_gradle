package com.tipsy.commun.commerce;

import android.widget.TextView;

/**
 * Created by valoo on 05/01/14.
 */
public class Commerce {

    public static String prixToString(int prix) {
        if (prix % 100 == 0) {
            return Integer.toString(prix / 100);
        } else
            return Float.toString((float) prix / 100);
    }

    public static String prixToString(int prix, int devise) {
        if (prix % 100 == 0) {
            return Integer.toString(prix / 100) + Devise.getSymbol(devise);
        } else
            return Float.toString((float) prix / 100) + Devise.getSymbol(devise);
    }


    public static int parsePrix(TextView view) {
        return (int) (Float.parseFloat(view.getText().toString()) * 100);
    }


    /* Classe gérant les devises monétaires */
    public static class Devise {
        public static int EURO = 0;
        private static String devises[] = {
                "€"
        };

        public static String getSymbol(int devise) {
            return devises[devise];
        }
    }
}
