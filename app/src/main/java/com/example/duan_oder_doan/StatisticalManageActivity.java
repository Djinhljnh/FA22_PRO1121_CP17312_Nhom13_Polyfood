package com.example.duan_oder_doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duan_oder_doan.model.HoaDonChiTietAdmin;
import com.example.duan_oder_doan.model.SanPham;
import com.example.duan_oder_doan.model.TheLoai;
import com.example.duan_oder_doan.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class StatisticalManageActivity extends AppCompatActivity {

    private TextView tvUser;
    private TextView tvCategory;
    private TextView tvFood;
    private TextView tvDate;
    private TextView tvSumoder;
    private TextView tvSummoney;

    private List<User> userList;
    private List<TheLoai> theLoaiList;
    private List<SanPham> sanPhamList;
    private List<HoaDonChiTietAdmin> hoaDonChiTietAdminList;

    private int sum1, sum2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_manage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvUser = (TextView) findViewById(R.id.tv_user);
        tvCategory = (TextView) findViewById(R.id.tv_category);
        tvFood = (TextView) findViewById(R.id.tv_food);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvSumoder = (TextView) findViewById(R.id.tv_sumoder);
        tvSummoney = (TextView) findViewById(R.id.tv_summoney);

        tvDate.setOnClickListener(v ->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar mCalendar = Calendar.getInstance();
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, month);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String selectedDate = DateFormat.getDateInstance().format(mCalendar.getTime());
                    tvDate.setText(selectedDate);
                }
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
            );
            dialog.show();
        });

        findViewById(R.id.btn_check).setOnClickListener(v ->{
            sum2 = 0;
            sum1 = 0;
            for (HoaDonChiTietAdmin hoaDonChiTietAdmin: hoaDonChiTietAdminList) {
                if (hoaDonChiTietAdmin.getDate().toLowerCase().contains(tvDate.getText().toString().toLowerCase())) {
                    sum1 = hoaDonChiTietAdminList.size();
                    sum2 = sum2+ Integer.parseInt(hoaDonChiTietAdmin.getSum_Price());
                }
            }

            tvSumoder.setText(String.valueOf(sum1));
            tvSummoney.setText("$ "+(String.valueOf(sum2)));

        });

        userList = new ArrayList<>();
        hoaDonChiTietAdminList =  new ArrayList<>();
        theLoaiList =  new ArrayList<>();
        sanPhamList =  new ArrayList<>();
        getList();
    }

    private void getList(){
        userList.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                tvUser.setText(""+userList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticalManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        theLoaiList.clear();
        DatabaseReference reference1 = database.getReference("Categories");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TheLoai theLoai = dataSnapshot.getValue(TheLoai.class);
                    theLoaiList.add(theLoai);
                }
                tvCategory.setText(""+theLoaiList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticalManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        sanPhamList.clear();
        DatabaseReference reference2 = database.getReference("Foods");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhamList.add(sanPham);
                }
                tvFood.setText(""+sanPhamList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticalManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

        hoaDonChiTietAdminList.clear();
        sum1 =0;
        sum2 = 0;
        DatabaseReference reference3 = database.getReference("Detailed_Invoices");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    HoaDonChiTietAdmin hoaDonChiTietAdmin = dataSnapshot.getValue(HoaDonChiTietAdmin.class);
                    hoaDonChiTietAdminList.add(hoaDonChiTietAdmin);
                    sum1 = hoaDonChiTietAdminList.size();
                    sum2 = sum2+ Integer.parseInt(hoaDonChiTietAdmin.getSum_Price());
                }
                tvSumoder.setText(String.valueOf(sum1));
                tvSummoney.setText("$ "+(String.valueOf(sum2)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticalManageActivity.this, "Get list faild!", Toast.LENGTH_LONG).show();
            }
        });

    }

}