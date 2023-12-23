package com.example.elitevetcare.Helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.FragmentActivity;

import com.example.elitevetcare.Model.CurrentData.CurrentLocation;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Clinic;
import com.example.elitevetcare.Model.ObjectModel.Pet;
import com.example.elitevetcare.R;
import com.example.elitevetcare.Adapter.RecyclerViewAdapter.RecyclerViewPetListAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Libs {
    public static String AgeToBirthYear(String age){
        String result = "";
        result = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(age));
        return result;
    }
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

    /**
     *  Đây là hàm chuyển đổi Bitmap
     */
    public static Bitmap uriToBitmap(Uri uri, Context mContext) {
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
    public static File BitmapToFile(File CacheDir, Bitmap bitmap){
        File imageFile = new File(CacheDir, "image.png");
        try {
            if (!CacheDir.exists()) {
                CacheDir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // Lưu dưới dạng PNG
            fos.flush();
            fos.close();

            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  Đây là hàm tự tạo ký tự gồm 8 chữ cái
     */
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
    public static String calculateDateDifference(Date startDate, Date endDate) {
        long timeDiff = endDate.getTime() - startDate.getTime();

        // Chuyển đổi thành giây
        long seconds = timeDiff / 1000;

        if (seconds >= 7 * 24 * 60 * 60) {
            // Khoảng cách hơn 7 ngày, trả về ngày xa hơn
            return formatDate(endDate, "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        } else if (seconds >= 24 * 60 * 60) {
            // Khoảng cách dưới 7 ngày, trả về số ngày
            long days = seconds / (24 * 60 * 60);
            return days + " ngày";
        } else if (seconds >= 60 * 60) {
            // Khoảng cách dưới 1 ngày, trả về số giờ
            long hours = seconds / (60 * 60);
            return hours + " giờ";
        } else if (seconds >= 60) {
            // Khoảng cách dưới 1 giờ, trả về số phút
            long minutes = seconds / 60;
            return minutes + " phút";
        } else {
            // Dưới 1 phút, trả về số giây
            return seconds + " giây";
        }
    }
    private static String formatDate(Date date, String pattern, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        return sdf.format(date);
    }
    public static void SetImageFromURL(String URL, RoundedImageView imageFilterButton){
        if(URL == null || URL == ""){
            if (CurrentUser.getCurrentUser().getRole().getId() == 3)
                imageFilterButton.setImageResource(R.drawable.ic_non_avatar);
            if(CurrentUser.getCurrentUser().getRole().getId() == 2)
                imageFilterButton.setImageResource(R.drawable.image_nonavatar_clinic);
            return;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                Bitmap bitmap = Libs.DownloadImageFromPath(URL);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        imageFilterButton.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
    public static void SetImageFromURL(String URL, ImageFilterButton imageFilterButton){
        if(URL == null || URL == ""){
            if (CurrentUser.getCurrentUser().getRole().getId() == 3)
                imageFilterButton.setImageResource(R.drawable.ic_non_avatar);
            if(CurrentUser.getCurrentUser().getRole().getId() == 2)
                imageFilterButton.setImageResource(R.drawable.image_nonavatar_clinic);
            return;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                Bitmap bitmap = Libs.DownloadImageFromPath(URL);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        imageFilterButton.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
    public static void SetImageFromURL(String URL, ImageFilterView imageFilterButton){
        if(URL == null || URL == ""){
            if (CurrentUser.getCurrentUser().getRole().getId() == 3)
                imageFilterButton.setImageResource(R.drawable.ic_non_avatar);
            if(CurrentUser.getCurrentUser().getRole().getId() == 2)
                imageFilterButton.setImageResource(R.drawable.image_nonavatar_clinic);
            return;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                Bitmap bitmap = Libs.DownloadImageFromPath(URL);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        imageFilterButton.setImageBitmap(bitmap);
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
    public static void SetImageFromURL(String URL, ImageView imageFilterButton){
        if(URL == null || URL == ""){
            if (CurrentUser.getCurrentUser().getRole().getId() == 3)
                imageFilterButton.setImageResource(R.drawable.ic_non_avatar);
            if(CurrentUser.getCurrentUser().getRole().getId() == 2)
                imageFilterButton.setImageResource(R.drawable.image_nonavatar_clinic);
            return;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background work here
                Bitmap bitmap = Libs.DownloadImageFromPath(URL);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                        imageFilterButton.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
    private static Bitmap DownloadImageFromPath(String path){
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

    /**
     *  Đây là hàm chuyển đổi URI thành FILE để lưu trữ
     */
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

    /**
     *  Đây là hàm tự tạo ký tự gồm 8 chữ cái
     */
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

    /**
     *  Đây là hàm gửi Thông báo Toast
     */
    public static void Sendmessage(Activity activity, String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *  Đây là hàm lấy khoản cách giữa người dùng đến các clinic
     */
    public static Location GetLocatioFromAdress(Geocoder geocoder, String QuerryAddress){
        try{
            List<Address> addresses = geocoder.getFromLocationName(QuerryAddress, 1);
            if(addresses !=null && addresses.size() > 0){
                double destinationLatitude = addresses.get(0).getLatitude();
                double destinationLongitude = addresses.get(0).getLongitude();

                Location destinationLocation = new Location("Destination");
                destinationLocation.setLatitude(destinationLatitude);
                destinationLocation.setLongitude(destinationLongitude);
                return destinationLocation;
            }
        }catch (IOException e){

        }
        return null;
    }

    private static ArrayList<Clinic> GetHomeClinicGeocoder(ArrayList<Clinic> list, Geocoder geocoder ) {
        ArrayList<Clinic> result = list;

        for(int i = 0; i < result.size(); i++){
            if(result.get(i).getDistance() == 0){
                String QuerryLocation = result.get(i).getStreetAddress() + "," + result.get(i).getCity();
                Location location = Libs.GetLocatioFromAdress(geocoder,QuerryLocation);
                float distance = CurrentLocation.getInstance().getCurrentLocation().distanceTo(location);
                result.get(i).setDistance(distance);
            }
        }

        Collections.sort(result, new DistanceComparator());

        return result;
    }
    public static Future<ArrayList<Clinic>> GetHomeClinic(ArrayList<Clinic> list, Geocoder geocoder ) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor.submit(new Callable<ArrayList<Clinic>>() {
            @Override
            public ArrayList<Clinic> call() throws Exception {
                return GetHomeClinicGeocoder(list,geocoder);
            }
        });
    }
    static class DistanceComparator implements Comparator<Clinic> {
        @Override
        public int compare(Clinic o1, Clinic o2) {
            return Float.compare(o1.getDistance(), o2.getDistance());
        }
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
