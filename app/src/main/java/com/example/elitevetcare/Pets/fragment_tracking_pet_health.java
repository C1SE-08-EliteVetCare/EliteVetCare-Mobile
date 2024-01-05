package com.example.elitevetcare.Pets;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elitevetcare.Activity.ContentView;
import com.example.elitevetcare.Helper.ProgressHelper;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Helper.HelperCallingAPI;
import com.example.elitevetcare.Helper.Libs;
import com.example.elitevetcare.Model.ViewModel.PetViewModel;
import com.example.elitevetcare.Model.ObjectModel.PetCondition;
import com.example.elitevetcare.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class fragment_tracking_pet_health extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public fragment_tracking_pet_health() {

    }

    public static fragment_tracking_pet_health newInstance(String param1, String param2) {
        fragment_tracking_pet_health fragment = new fragment_tracking_pet_health();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    PetViewModel Current;
    ArrayList<PetCondition> DataCondition;
    int role = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Current = new ViewModelProvider(requireActivity()).get(PetViewModel.class);
        DataCondition = Current.getPetCondition().getValue();
        role = CurrentUser.getCurrentUser().getRole().getId();

    }
    private LineChart chart1;
    private LineChart chart2;
    ArrayList<String> DateCondition = null;
    AppCompatEditText edt_advance, edt_medicine,edt_food;
    AppCompatButton btn_Action;
    TextView txt_dateUpdate, txt_food_status, txt_expression_status, txt_defecation_status;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tracking_pet_health, container, false);

        SetID(root);
        InitContent();
        SetEvent();
        InitData();
        // Thiết lập dữ liệu cho biểu đồ 1
        SetDataChart(chart1,getActivity().getString(R.string.portion_pet));
        // Thiết lập dữ liệu cho biểu đồ 2
        SetDataChart(chart2,getActivity().getString(R.string.weight_pet));
        if(ProgressHelper.isDialogVisible())
            ProgressHelper.dismissDialog();
        // Inflate the layout for this fragment
        return root;
    }

    private void SetEvent() {
        if(role == 3){
            btn_Action.setText("Thêm Lời Khuyên");
            if(DataCondition.get(0).getVetAdvice() != null && DataCondition.get(0).getVetAdvice() != ""){
                EventUpdated();
                return;
            }
            btn_Action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Advice = Libs.HandleString(edt_advance.getText().toString());
                    String Medicine = Libs.HandleString(edt_medicine.getText().toString());
                    String food = Libs.HandleString(edt_food.getText().toString());
                    if (Advice == null || Advice == "" || Medicine == null || Medicine == "" || food == null || food == "")
                        return;
                    RequestBody body = new FormBody.Builder()
                            .add("vetAdvice", edt_advance.getText().toString())
                            .add("recommendedMedicines", edt_medicine.getText().toString())
                            .add("recommendedMeal", edt_food.getText().toString())
                            .build();
                    HelperCallingAPI.CallingAPI_PathParams_Put(HelperCallingAPI.UPDATE_ADVICE_PATH,
                            String.valueOf(Current.getCurrentPetTreatment().getPet().getId()), body, new HelperCallingAPI.MyCallback() {
                                @Override
                                public void onResponse(Response response) {
                                    int responseStatus = response.code();
                                    JSONObject data = null;
                                    if(responseStatus == 200){
                                        try {
                                            data = new JSONObject(response.body().string());
                                            JSONObject finalData = data;
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        EventUpdated();
                                                        Toast.makeText(getContext(), finalData.getString("message"), Toast.LENGTH_SHORT).show();
                                                        ((ContentView)getActivity()).setfragment(R.layout.fragment_tracking_pet_health);
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            });
                                        } catch (JSONException | IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(IOException e) {

                                }
                            });
                }
            });
        } else if (role == 2) {
            btn_Action.setText("Cập Nhập Tình Trạng");
            btn_Action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int TodayAdvance = 0;
                    Log.d("LocalDateDataConditon",LocalDate.parse(DataCondition.get(TodayAdvance).getDateUpdate()) +" : " +LocalDate.now() );
                    if (LocalDate.parse(DataCondition.get(TodayAdvance).getDateUpdate()).isEqual(LocalDate.now()) ){
                        Toast.makeText(getContext(),"Hôm nay đã câp nhập không cần phải cập nhập lại !!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ((ContentView)getActivity()).setfragment(R.layout.fragment_update_pet_condition);
                }
            });
        }
    }

    private void EventUpdated() {
        btn_Action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Đã Cập nhập cho hôm nay", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitData() {
        DateCondition = new ArrayList<>();
        DateCondition.add("0");
        for (int i = DataCondition.size()-1; i >= 0 ; i--){
            DateCondition.add(DataCondition.get(i).getDateUpdate());
        }
    }

    private void InitContent() {
        int TodayAdvance = 0;

        txt_dateUpdate.setText("Chi Tiết Thông số (" + DataCondition.get(TodayAdvance).getDateUpdate()  + ") :");
        txt_food_status.setText( DataCondition.get(TodayAdvance).getMeal() == null ? "Data" : DataCondition.get(TodayAdvance).getMeal() );
        txt_expression_status.setText(DataCondition.get(TodayAdvance).getManifestation() == null ? "Data" : DataCondition.get(TodayAdvance).getManifestation());
        txt_defecation_status.setText( (DataCondition.get(TodayAdvance).getConditionOfDefecation() == null ? "Data" : DataCondition.get(TodayAdvance).getConditionOfDefecation()));
        if (role == 2){
            SetEdtEnable(edt_advance,false);
            SetEdtEnable(edt_medicine,false);
            SetEdtEnable(edt_food,false);
            SetTextEdt(edt_advance,DataCondition.get(TodayAdvance).getVetAdvice());
            SetTextEdt(edt_medicine,DataCondition.get(TodayAdvance).getRecommendedMedicines());
            SetTextEdt(edt_food,DataCondition.get(TodayAdvance).getRecommendedMeal());
        } else if (role == 3) {
            SetEdtEnable(edt_advance,true);
            SetEdtEnable(edt_medicine,true);
            SetEdtEnable(edt_food,true);
            SetTextEdt(edt_advance,DataCondition.get(TodayAdvance).getVetAdvice());
            SetTextEdt(edt_medicine,DataCondition.get(TodayAdvance).getRecommendedMedicines());
            SetTextEdt(edt_food,DataCondition.get(TodayAdvance).getRecommendedMeal());
        }
    }

    private void SetID(View root) {
        chart1 = root.findViewById(R.id.chart1);
        chart2 = root.findViewById(R.id.chart2);
        edt_advance = root.findViewById(R.id.edt_advance_tracking);
        edt_medicine = root.findViewById(R.id.edt_medicine_pet_tracking);
        edt_food = root.findViewById(R.id.edt_food_pet_tracking);
        btn_Action = root.findViewById(R.id.btn_Action_tracking);
        txt_dateUpdate = root.findViewById(R.id.txt_last_date_update);
        txt_food_status = root.findViewById(R.id.txt_food_status);
        txt_expression_status = root.findViewById(R.id.txt_expression_status);
        txt_defecation_status = root.findViewById(R.id.txt_defecation_status);
    }

    private void SetTextEdt(AppCompatEditText edt_current, String content) {
        if((content == null && role == 2)|| (content == "" && role == 2))
            content = "Bác Sĩ Chưa Đánh Giá";
        else if((content == null && role == 3)|| (content == "" && role == 3)){
            content = "";
            edt_current.setText(content);
            return;
        }
        edt_current.setText(content);
        edt_current.setTextColor(getActivity().getColor(R.color.red));
    }
    private void SetEdtEnable(AppCompatEditText edt_current, boolean Status) {
        if (Status == false)
            edt_current.setBackgroundResource(R.drawable.custom_box_advance);
        int TodayAdvance = 0;
        if(DataCondition.get(TodayAdvance).getVetAdvice() != null && DataCondition.get(TodayAdvance).getVetAdvice() != ""){
            Status = false;
        }

        edt_current.setFocusableInTouchMode(Status);
        edt_current.setClickable(Status);
        edt_current.setFocusable(Status);
    }

    private void SetDataChart(LineChart chart, String keyWordData ) {
        String title = "";
        int ColorLine = -1;
        if (getActivity().getString(R.string.portion_pet) == keyWordData) {
            title = "Đồ Ăn Tiêu Thụ";
            ColorLine = R.color.green;
        }
        if (getActivity().getString(R.string.weight_pet) == keyWordData) {
            title = "Cân Nặng";
            ColorLine = R.color.orange;
        }
        ArrayList<Double> data = GetListData(keyWordData);
        LineDataSet dataSet = createDataSet(data,ColorLine,title);
        LineData lineData = new LineData(dataSet);
//        LineDataSet dataSet3 = createDataSet(RandomData(),R.color.green,"Cân Nặng Tiêu Chuẩn");
//        LineData lineData2 = new LineData(dataSet2,dataSet3);
        configureChart(chart);
        chart.setData(lineData);
    }

    private ArrayList<Double> GetListData(String keyWordData) {
        ArrayList<Double> Data = new ArrayList<>();
        Random random = new Random();

        for (int i = DataCondition.size()- 1; i >= 0; i--) {
            if (getActivity().getString(R.string.portion_pet) == keyWordData) {
                double value = DataCondition.get(i).getPortion();
                Data.add(value);
                Log.d("Data", String.valueOf(DataCondition.get(i).getPortion()));
            }
            if (getActivity().getString(R.string.weight_pet) == keyWordData) {
                Data.add(DataCondition.get(i).getWeight());
                Log.d("Data", String.valueOf(DataCondition.get(i).getWeight()));
            }
        }
        return Data;
    }

    private ArrayList<Integer> RandomData() {
        ArrayList<Integer> Data = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int randomValue = random.nextInt(100 - 20 + 1) + 10;
            Data.add(randomValue);
        }
        return Data;
    }

    private LineDataSet createDataSet(ArrayList<Double> List, int Color, String LabelTitle) {
        // Tạo danh sách dữ liệu mẫu
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0,List.get(0).floatValue()));
        for (int i = 0; i < List.size(); i++) {
            entries.add(new Entry(i+1,List.get(i).floatValue()));
        }

        // Tạo và cấu hình dữ liệu của biểu đồ
        LineDataSet dataSet = new LineDataSet(entries, LabelTitle);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawCircles(true);
        dataSet.setColor(ContextCompat.getColor(getContext(),Color));
        dataSet.setDrawValues(false);
        dataSet.setCubicIntensity(1);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        return dataSet;
    }
    private void configureChart(LineChart chart) {
        // Cấu hình thông tin biểu đồ
        // no description text
        chart.getDescription().setEnabled(false);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setMinimumWidth(DataCondition.size() * 100);
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);

        YAxis RightAxis = chart.getAxisRight();
        RightAxis.setEnabled(false);

        YAxis LeftAxis = chart.getAxisRight();
        LeftAxis.setAxisMinimum(0);
        LeftAxis.setStartAtZero(true);
        LeftAxis.setGranularity(1f);
        LeftAxis.setDrawGridLines(false);

        XAxis BottomAxis = chart.getXAxis();
        BottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        BottomAxis.setAxisMinimum(0f);
        BottomAxis.setGranularity(1f);
        BottomAxis.setLabelCount(5);
        BottomAxis.setValueFormatter(new IndexAxisValueFormatter(DateCondition));
        BottomAxis.setAxisMaximum(10f);
        BottomAxis.setLabelRotationAngle(0);
        BottomAxis.setDrawAxisLine(true);
        BottomAxis.setDrawGridLines(false);

        chart.invalidate();
    }

    class DateAxisValueFormatter extends ValueFormatter {
        private final String[] dates;

        DateAxisValueFormatter(String[] dates) {
            this.dates = dates;
        }

        @Override
        public String getFormattedValue(float value) {
            int index = (int) value;
            if (index >= 0 && index < dates.length) {
                return dates[index];
            }
            return "";
        }
    }
}
