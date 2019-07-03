package cz.mtr.inventura.model;

import android.content.Context;

public class Model {

    private static final String TAG = Model.class.getSimpleName();
    private static volatile Model INSTANCE;
    private Context mContext;


    public Model() {

    }


    public static Model getInstance() {
        if (INSTANCE == null) {
            synchronized (Model.class) {
                if (INSTANCE == null) {
                    return new Model();
                }
            }
        }
        return INSTANCE;
    }

    public void setContext(Context context) {
        mContext = context;

    }


}
