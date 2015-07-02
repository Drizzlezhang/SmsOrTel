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
import android.util.Log;
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
import com.drizzle.app.smsortel.service.SmsService;

import java.util.Calendar;

import info.hoang8f.widget.FButton;


public class SmsActivity extends ActionBarActivity implements View.OnClickListener{

    private EditText smsOkTo;
    private EditText msgInput;
    private RippleView smsOkChooseTime;
    private RippleView smsOkChooseDate;
    private TextView smsOkHour;
    private TextView smsOkMinute;
    private TextView smsOkYear;
    private TextView smsOkMonth;
    private TextView smsOkDay;
    private SmsOrTelDB smsortelDB;
    private Button smsOkChooseContact;
    private FButton ChooseResource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sms);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_speaker_notes_white_36dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        smsOkTo=(EditText)findViewById(R.id.sms_ok_to);
        msgInput=(EditText)findViewById(R.id.msg_input);
        smsOkChooseTime=(RippleView)findViewById(R.id.sms_ok_choose_time);
        smsOkChooseTime.setOnClickListener(this);
        smsOkChooseDate=(RippleView)findViewById(R.id.sms_ok_choose_date);
        smsOkChooseDate.setOnClickListener(this);
        smsOkHour=(TextView) findViewById(R.id.sms_ok_hour);
        smsOkMinute=(TextView) findViewById(R.id.sms_ok_minute);
        smsOkDay=(TextView) findViewById(R.id.sms_ok_day);
        smsOkYear=(TextView) findViewById(R.id.sms_ok_year);
        smsOkMonth=(TextView) findViewById(R.id.sms_ok_month);
        smsOkChooseContact=(Button)findViewById(R.id.sms_ok_choose_contact);
        smsOkChooseContact.setOnClickListener(this);
        ChooseResource=(FButton)findViewById(R.id.sms_ok_choose);
        ChooseResource.setOnClickListener(this);
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
                    smsOkTo.setText(phoneNumber);
                }
                phones.close();
            }
            cursor.close();
        }else if(requestCode==1024&&resultCode==RESULT_OK){
            String smsdata=data.getStringExtra("sms");
            msgInput.setText(smsdata);
        }
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
         if(id==R.id.sms_ok_choose_time){
            Calendar calendar=Calendar.getInstance();
            new TimePickerDialog(SmsActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            smsOkHour.setText(String.format("%02d",hourOfDay));
                            smsOkMinute.setText(String.format("%02d", minute));
                        }
                    },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
        }else if(id==R.id.sms_ok_choose_contact){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 100);
        }else if(id==R.id.sms_ok_choose_date){
            Calendar calendar=Calendar.getInstance();
            new DatePickerDialog(SmsActivity.this,new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    smsOkYear.setText(String.format("%02d",year));
                    smsOkDay.setText(String.format("%02d",dayOfMonth));
                    smsOkMonth.setText(String.format("%02d",monthOfYear+1));
                }
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
        }else if(id==R.id.sms_ok_choose){
            Intent intent=new Intent(SmsActivity.this,ResourceActivity.class);
            startActivityForResult(intent, 1024);
             overridePendingTransition(R.anim.right_in, R.anim.not_move);
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
        getMenuInflater().inflate(R.menu.menu_sms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
               break;
            case R.id.action_settings_sms:
                String smsoknumber=smsOkTo.getText().toString();
                String smsokhour=smsOkHour.getText().toString();
                String smsokminute=smsOkMinute.getText().toString();
                String smsokword=msgInput.getText().toString();
                String smsokyear=smsOkYear.getText().toString();
                String smsokmonth=smsOkMonth.getText().toString();
                String smsokday=smsOkDay.getText().toString();
                Time mTime=new Time();
                boolean flag=mTime.tTime(smsokyear, smsokmonth, smsokday, smsokhour, smsokminute);
                if(flag) {
                    if (smsOkYear.getText().length() != 0 &&smsOkMonth.getText().length() != 0 &&smsOkDay.getText().length() != 0 &&smsOkTo.getText().length() != 0 && smsOkHour.getText().length() != 0 && smsOkMinute.getText().length() != 0 && msgInput.getText().length() != 0) {
                        int smsokid = (int) (System.currentTimeMillis());
                        Log.d("SmsActivity", smsokid + "");
                        Mission smsokMission = new Mission();
                        smsokMission.setMissionId(smsokid);
                        smsokMission.setMissionNumber(smsoknumber);
                        smsokMission.setMissionHour(smsokhour);
                        smsokMission.setMissionMinute(smsokminute);
                        smsokMission.setMissionWord(smsokword);
                        smsokMission.setMissionYear(smsokyear);
                        smsokMission.setMissionMonth(smsokmonth);
                        smsokMission.setMissionDay(smsokday);
                        smsokMission.setImageId(R.mipmap.ic_sms_ok_black);
                        smsortelDB.saveMission(smsokMission);
                        Intent intent = new Intent(SmsActivity.this, SmsService.class);
                        intent.putExtra("extra_smsokhour", smsokhour);
                        intent.putExtra("extra_smsokminute", smsokminute);
                        intent.putExtra("extra_smsokyear", smsokyear);
                        intent.putExtra("extra_smsokmonth", smsokmonth);
                        intent.putExtra("extra_smsokday", smsokday);
                        intent.putExtra("extra_smsokid", smsokid);
                        startService(intent);
                        Toast.makeText(this, "任务已保存", Toast.LENGTH_SHORT).show();
//                    Intent intent1 = new Intent(SmsActivity.this, MainActivity.class);
//                    startActivity(intent1);
                        finish();
                    } else {
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
