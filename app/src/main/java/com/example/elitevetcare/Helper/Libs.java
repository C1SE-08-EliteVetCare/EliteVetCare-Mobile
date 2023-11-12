package com.example.elitevetcare.Helper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elitevetcare.Model.Pet;
import com.example.elitevetcare.RecyclerView.RecyclerViewPetListAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Libs {
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Chuyển chuỗi về lowercase và tách thành từng từ
        String[] words = input.toLowerCase().split("\\s");

        // Viết hoa chữ cái đầu của mỗi từ
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }

        // Loại bỏ dấu cách cuối cùng (nếu có)
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }
    public static String HandleString(String inputString){
        String result = "";
        if(TextUtils.isEmpty(inputString))
            return "";
        result = inputString.trim();
        result = result.substring(0, 1).toUpperCase() + result.substring(1).toLowerCase();
        return result;
    }
    public static Future<Bitmap> URLtoBitMap(Pet currentPetPosition) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(new Callable<Bitmap>() {
            @Override
            public Bitmap call() {
                return DownloadImageFromPath(currentPetPosition.getAvatar());
            }
        });
    }
    public static void SetImageFromURL(Pet currentPetPosition, RecyclerViewPetListAdapter.PetListViewHolder currentHolder){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                Bitmap bitmap = Libs.DownloadImageFromPath(currentPetPosition.getAvatar());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        currentHolder.image_avatar.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
    public static void SetImageFromURL(Pet currentPetPosition, ImageFilterView CurrentImage){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                Bitmap bitmap = Libs.DownloadImageFromPath(currentPetPosition.getAvatar());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        CurrentImage.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
    public static void SetImageFromURL(Pet currentPetPosition, ImageView CurrentImage){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                Bitmap bitmap = Libs.DownloadImageFromPath(currentPetPosition.getAvatar());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        CurrentImage.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
    public static Bitmap DownloadImageFromPath(String path){
        InputStream in =null;
        Bitmap bmp=null;
        int responseCode = -1;
        try{
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
            }
            return bmp;
        }
        catch(Exception ex){
            Log.e("Exception",ex.toString());
        }
        return null;
    }
    public static File uriToFile(FragmentActivity CurrentActivity, Uri uri) {
        File outputFile = null;
        try {
            InputStream inputStream = CurrentActivity.getContentResolver().openInputStream(uri);
            outputFile = createImageFile(CurrentActivity); // Hàm tạo một tệp PNG trên thiết bị

            if (outputFile != null) {
                OutputStream outputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile;
    }
    private static File createImageFile(FragmentActivity CurrentActivity) throws IOException {
        // Tạo một tên tệp duy nhất dựa trên thời gian

        String imageFileName = "JPEG_" + 1 + "_";
        File storageDir = CurrentActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }
    private static final String CHARACTERS = "abcdefghijklMNOPQrstuvwxyz0123456789ABCDEFGHIJKLmnopqRSTUVWXYZ";
    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
    public static void Sendmessage(Activity activity, String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     *
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try{
                    task.getResult(ApiException.class);
                }catch (ApiException e){

                }
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                String email = account.getEmail();
                String name = account.getDisplayName();
                if (account != null){
                    Log.d("GoogleRespone", email);
                    Log.d("GoogleRespone", name);
                }
            }
        });
     */
}
