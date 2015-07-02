package com.drizzle.app.smsortel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.drizzle.app.smsortel.model.Mission;
import com.drizzle.app.smsortel.model.RippleView;
import com.drizzle.app.smsortel.model.SmsOrTelDB;
import com.drizzle.app.smsortel.model.Time;
import com.drizzle.app.smsortel.service.TelService;

import java.util.Calendar;


public class TelActivity extends ActionBarActivity implements View.OnClickListener{

    private EditText telOkTo;
    private RippleView telOkChooseTime;
    private TextView telOkHour;
    private TextView telOkMinute;
    private SmsOrTelDB smsortelDB;
    private Button telOkChooseContact;
    private RippleView telOkChooseDate;
    private TextView  telOkYear;
    private TextView  telOkMonth;
    private TextView  telOkDay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tel);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_settings_phone_white_36dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        telOkTo=(EditText)findViewById(R.id.tel_ok_to);
        telOkChooseTime=(RippleView)findViewById(R.id.tel_ok_choose_time);
        telOkChooseTime.setOnClickListener(this);
        telOkChooseDate=(RippleView)findViewById(R.id.tel_ok_choose_date);
        telOkChooseDate.setOnClickListener(this);
        telOkHour=(TextView) findViewById(R.id.tel_ok_hour);
        telOkMinute=(TextView) findViewById(R.id.tel_ok_minute);
        telOkYear=(TextView) findViewById(R.id.tel_ok_year);
        telOkMonth=(TextView) findViewById(R.id.tel_ok_month);
        telOkDay=(TextView) findViewById(R.id.tel_ok_day);
        telOkChooseContact=(Button)findViewById(R.id.tel_ok_choose_contact);
        telOkChooseContact.setOnClickListener(this);
        smsortelDB= SmsOrTelDB.getInstance(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK){
            Uri data2=data.getData();
            Cursor cursor=getContentResolver().query(data2,null,null,null,null);
            if(cursor.moveToFirst()){
                Cursor phones=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)),null,null);
                if(phones.moveToFirst()){
                    String phoneNumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    telOkTo.setText(phoneNumber);
                }
                phones.close();
            }
            cursor.close();
        }
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
         if(id==R.id.tel_ok_choose_time){
            Calendar calendar=Calendar.getInstance();
            new TimePickerDialog(TelActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            telOkHour.setText(String.format("%02d",hourOfDay));
                            telOkMinute.setText(String.format("%02d",minute));
                        }
                    },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
        }else if(id==R.id.tel_ok_choose_contact){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent,100);
        }else if(id==R.id.tel_ok_choose_date){
            Calendar calendar=Calendar.getInstance();
            new DatePickerDialog(TelActivity.this,new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    telOkYear.setText(String.format("%02d",year));
                    telOkDay.setText(String.format("%02d",dayOfMonth));
                    telOkMonth.setText(String.format("%02d",monthOfYear+1));
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }
    @Override
    public void onBackPressed() {

//        Intent intent=new Intent(this,MainActivity.class);
//        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tel, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings_tel:
                int telokid=(int)(System.currentTimeMillis());
                String teloknumber=telOkTo.getText().toString();
                String telokhour=telOkHour.getText().toString();
                String telokminute=telOkMinute.getText().toString();
                String telokyear=telOkYear.getText().toString();
                String telokmonth=telOkMonth.getText().toString();
                String telokday=telOkDay.getText().toString();
                Time mTime=new Time();
                boolean flag=mTime.tTime(telokyear, telokmonth, telokday, telokhour, telokminute);
                if(flag) {
                    if(telOkYear.getText().length() != 0 &&telOkMonth.getText().length() != 0 &&telOkDay.getText().length() != 0 &&telOkTo.getText().length()!=0&&telOkHour.getText().length()!=0&&telOkMinute.getText().length()!=0) {
                        Mission telokMission=new Mission();
                        telokMission.setMissionId(telokid);
                        telokMission.setMissionNumber(teloknumber);
                        telokMission.setMissionHour(telokhour);
                        telokMission.setMissionMinute(telokminute);
                        telokMission.setMissionWord("拨打电话任务");
                        telokMission.setMissionYear(telokyear);
                        telokMission.setMissionMonth(telokmonth);
                        telokMission.setMissionDay(telokday);
                        telokMission.setImageId(R.mipmap.ic_tel_ok_black);
                        smsortelDB.saveMission(telokMission);
                        Intent intent1=new Intent(TelActivity.this, TelService.class);
                        intent1.putExtra("extra_telokhour", telokhour);
                        intent1.putExtra("extra_telokminute", telokminute);
                        intent1.putExtra("extra_telokid", telokid);
                        intent1.putExtra("extra_telokyear", telokyear);
                        intent1.putExtra("extra_telokmonth", telokmonth);
                        intent1.putExtra("extra_telokday", telokday);
                        startService(intent1);
                        Toast.makeText(this, "任务已保存", Toast.LENGTH_SHORT).show();
//                Intent intent2=new Intent(TelActivity.this,MainActivity.class);
//                startActivity(intent2);
                        finish();
                    }else{
                        Toast.makeText(this, "任务无效", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "时间设置有误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
        return true;
    }



}
