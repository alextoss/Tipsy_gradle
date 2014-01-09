package com.tipsy.commun.billetterie;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

/**
 * Created by vquefele on 06/01/14.
 */
public class ParametresBillet extends StackMobModel implements Parcelable {
    private int nbMax = 0;

    public interface ParamBillet {
        public int getNbMax();

        public void setNbMax(int nbMax);
    }

    public ParametresBillet() {
        super(ParametresBillet.class);
    }

    public int getNbMax() {
        return nbMax;
    }

    public void setNbMax(int nbMax) {
        this.nbMax = nbMax;
    }

    // Nom du schema dans Stackmob
    public static String overrideSchemaName() {
        return "billet";
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeInt(nbMax);
    }

    public ParametresBillet(Parcel in) {
        super(ParametresBillet.class);
        setID(in.readString());
        nbMax = in.readInt();
    }
}
