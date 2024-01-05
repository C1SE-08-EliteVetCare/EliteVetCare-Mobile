package com.example.elitevetcare.Appointment;

import static com.example.elitevetcare.Adapter.CalendarUtils.selectedDate;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.elitevetcare.Adapter.CalendarUtils;
import com.example.elitevetcare.Adapter.Event;
import com.example.elitevetcare.Adapter.HourAdapter;
import com.example.elitevetcare.Adapter.HourEven;
import com.example.elitevetcare.Model.CurrentData.CurrentUser;
import com.example.elitevetcare.Model.ObjectModel.Appointment;
import com.example.elitevetcare.Model.ViewModel.AppointmentViewModel;
import com.example.elitevetcare.R;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_calendar_view#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_calendar_view extends Fragment  {

    private TextView monthDayText;
    private TextView dayOfWeekTV;

    private Button nextDayAction;
    private Button previousDayAction;
    private ListView hourListView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_calendar_view() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_calendar_view.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_calendar_view newInstance(String param1, String param2) {
        fragment_calendar_view fragment = new fragment_calendar_view();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    AppointmentViewModel appointmentViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(CalendarUtils.selectedDate == null)
            CalendarUtils.selectedDate = LocalDate.now();
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(AppointmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_calendar_view, container, false);

        initWidgets(root);
        setDayView();
        return root;

    }

    private void initWidgets(View root) {

        monthDayText = root.findViewById(R.id.monthDayText);
        dayOfWeekTV = root.findViewById(R.id.dayOfWeekTV);
        hourListView = root.findViewById(R.id.hourListView);
        nextDayAction = root.findViewById(R.id.nextDayAction);
        nextDayAction = root.findViewById(R.id.nextDayAction);
        previousDayAction = root.findViewById(R.id.previousDayAction);

        nextDayAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
                setDayView();
            }
        });

        previousDayAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
                setDayView();
            }
        });
    }

    private void setDayView()
    {
        monthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    private void setHourAdapter()
    {
        HourAdapter hourAdapter = new HourAdapter(getActivity().getApplicationContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
        hourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<HourEven> list = new ArrayList<>();
                list = hourEventList();
                int index = i;
                if ( list.get(index).getEvents().size() <= 0){
                    return;
                }
                Appointment appointment = list.get(index).getEvents().get(0).getAppointment();
                openShowInfoDialog(Gravity.CENTER, appointment);


            }
        });
    }

    private void openShowInfoDialog( int gravity , Appointment appointment){

        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_schedule);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        // fill name
        if(CurrentUser.getCurrentUser().getRole().getId() == 3){
            ((TextView)dialog.findViewById(R.id.txt_ten)).setText(appointment.getPetOwner().getFullName());

        }else if(CurrentUser.getCurrentUser().getRole().getId() == 2){
            ((TextView)dialog.findViewById(R.id.txt_ten)).setText(appointment.getVetAppointment().getFullName());

        }



        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

       // window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setBackgroundDrawableResource(R.drawable.background_transparent);

        WindowManager.LayoutParams windownAttributes = window.getAttributes();
        windownAttributes.gravity = gravity;

        window.setAttributes(windownAttributes);


        dialog.show();

    }
    private ArrayList<HourEven> hourEventList()
    {

        ArrayList<HourEven> list = new ArrayList<>();

        for(int hour = 7; hour < 23; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            LocalTime time2 = LocalTime.of(hour, 30);
            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, time);
            ArrayList<Event> events2 = Event.eventsForDateAndTime(selectedDate, time2);
            HourEven hourEvent = new HourEven(time, events);
            HourEven hourEvent2 = new HourEven(time2, events2);
            list.add(hourEvent);
            list.add(hourEvent2);
        }

        return list;
    }

}