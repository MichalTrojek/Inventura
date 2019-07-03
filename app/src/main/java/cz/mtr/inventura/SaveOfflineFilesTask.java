package cz.mtr.inventura;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.mtr.inventura.listView.Item;

public class SaveOfflineFilesTask extends AsyncTask<String, Integer, Void> {


    private Context mContext;
    private ArrayList<Item> mItems = new ArrayList<>();


    public SaveOfflineFilesTask(Context c, ArrayList<Item> items) {
        this.mContext = c;
        this.mItems = items;

    }

    @Override
    protected Void doInBackground(String... voids) {
        try {
            createOfflineFiles(mItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void v) {
        Toast.makeText(mContext, "Data uložena", Toast.LENGTH_LONG).show();
    }


    //středníkem oddělené hodnoty EAN, LOKACE, POČET, DATUM
    private void createOfflineFiles(ArrayList<Item> items) throws IOException {
        try {
            if (isExternalStorageWritable()) {
                String filename = "INV_" + returnDate() + ".csv";
                File file = new File(Environment.getExternalStorageDirectory(), filename);
                FileOutputStream fileOut = new FileOutputStream(file);
                FileWriter csvWriter = new FileWriter(fileOut.getFD());
                for (Item i : items) {
                    csvWriter.append(i.getEan()).append(";").append(i.getLocation()).append(";").append(i.getAmount() + "").append(";").append(currentDate()).append("\n");
                }
                csvWriter.flush();
                csvWriter.close();
                fileOut.getFD().sync();
                fileOut.close();
                MediaScannerConnection.scanFile(
                        mContext,
                        new String[]{file.getAbsolutePath()}, // "file" was created with "new File(...)"
                        null,
                        null);
            } else {
                System.out.println("external storage is not writable");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String currentDate() {
        DateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        Date date = new Date();
        return sdf.format(date);
    }




    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private String returnDate() {
        DateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HHMMSS");
        Date date = new Date();
        System.out.println(sdf.format(date));
        return sdf.format(date).toString();
    }

}
