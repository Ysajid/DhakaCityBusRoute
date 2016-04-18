package com.greentech.dhakacitybusroute.SqliteHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


class Utils {

    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final String TAG = SQLiteAssetHelper.class.getSimpleName();
    private static ProgressDialog mDialog;

    public static List<String> splitSqlScript(String script, char delim) {
        List<String> statements = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean inLiteral = false;
        char[] content = script.toCharArray();
        for (int i = 0; i < script.length(); i++) {
            if (content[i] == '"') {
                inLiteral = !inLiteral;
            }
            if (content[i] == delim && !inLiteral) {
                if (sb.length() > 0) {
                    statements.add(sb.toString().trim());
                    sb = new StringBuilder();
                }
            } else {
                sb.append(content[i]);
            }
        }
        if (sb.length() > 0) {
            statements.add(sb.toString().trim());
        }
        return statements;
    }

    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs, Context context) throws IOException {
        byte[] buffer = new byte[4096];
        int length;
        int progress = 0;
        int totalsize = in.available();
        while ((length = in.read(buffer)) > 0) {
            outs.write(buffer, 0, length);
            progress += length;
//            publishprogress(progress, totalsize, context);
        }
        outs.flush();
        outs.close();
        in.close();
//        if (mDialog!=null)
//            mDialog.dismiss();
    }

    public static void publishprogress(final int mProgress, final int mTotal, final Context context) {
        final String message = "Preparing files...";
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() { // This thread runs in the UI
                    @Override
                    public void run() {
                        if (mDialog == null) {
                            mDialog = new ProgressDialog(context);
                            mDialog.setMessage(message);
                            mDialog.setCancelable(false);
                            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mDialog.show();
                            mDialog.setMax(mTotal);
                        }
                        mDialog.setProgress(mProgress);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    public static ZipInputStream getFileFromZip(InputStream zipFileStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFileStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            Log.w(TAG, "extracting file: '" + ze.getName() + "'...");
            return zis;
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) {
        return new Scanner(is).useDelimiter("\\A").next();
    }

}
