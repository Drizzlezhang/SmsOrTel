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
import com.drizzle.app.smsortel.service.SmsNotService;

import java.util.Calendar;


public class SmsNotActivity extends ActionBarActivity implements View.OnClickListener{

    private EditText smsNotTo;
    private RippleView smsNotChooseTime;
    private TextView smsNotHour;
    private TextView smsNotMinute;
    private SmsOrTelDB smsortelDB;
    private Button smsNotChooseContact;
    private RippleView smsNotChooseDate;
    private TextView smsNotYear;
    private TextView smsNotMonth;
    private TextView smsNotDay;
//    private static final String U="http://apis.juhe.cn/mobile/get?phone=";
//    private static final String L="&key=ad77742b0c49a73912c8b3738bfcad72";
    private static final String TAG="smsnotactivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sms_not);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_question_answer_white_36dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        smsNotTo=(EditText)findViewById(R.id.sms_not_to);
        smsNotChooseTime=(RippleView)findViewById(R.id.sms_not_choose_time);
        smsNotChooseTime.setOnClickListener(this);
        smsNotChooseDate=(RippleView)findViewById(R.id.sms_not_choose_date);
        smsNotChooseDate.setOnClickListener(this);
        smsNotHour=(TextView) findViewById(R.id.sms_not_hour);
        smsNotMinute=(TextView) findViewById(R.id.sms_not_minute);
        smsNotChooseContact=(Button) findViewById(R.id.sms_not_choose_contact);
        smsNotChooseContact.setOnClickListener(this);
        smsNotYear=(TextView) findViewById(R.id.sms_not_year);
        smsNotMonth=(TextView) findViewById(R.id.sms_not_month);
        smsNotDay=(TextView) findViewById(R.id.sms_not_day);
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
                    smsNotTo.setText(phoneNumber);
                }
                phones.close();
            }
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.sms_not_choose_time){
            Calendar calendar=Calendar.getInstance();
            new TimePickerDialog(SmsNotActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            smsNotHour.setText(String.format("%02d",hourOfDay));
                            smsNotMinute.setText(String.format("%02d",minute));
                        }
                    },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
        }else if(id==R.id.sms_not_choose_contact){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent,100);
        }else if(id==R.id.sms_not_choose_date){
        Calendar calendar=Calendar.getInstance();
        new DatePickerDialog(SmsNotActivity.this,new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                smsNotYear.setText(String.format("%02d",year));
                smsNotDay.setText(String.format("%02d",dayOfMonth));
                smsNotMonth.setText(String.format("%02d",monthOfYear+1));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    }
    @Override
    public void onBackPressed() {
//
//        Intent intent=new Intent(this,MainActivity.class);
//        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sms_not, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings_sms_not:
                Log.d(TAG,"1");
                final int smsnotid=(int)(System.currentTimeMillis());
                final String smsnotnumber=smsNotTo.getText().toString();
                final String smsnothour=smsNotHour.getText().toString();
                final String smsnotminute=smsNotMinute.getText().toString();
                final String smsnotyear=smsNotYear.getText().toString();
                final String smsnotmonth=smsNotMonth.getText().toString();
                final String smsnotday=smsNotDay.getText().toString();
                Time mTime=new Time();
                boolean flag=mTime.tTime(smsnotyear, smsnotmonth, smsnotday, smsnothour, smsnotminute);
                Log.d(TAG,"2");
                if(flag) {
                    if(smsNotYear.getText().length() != 0 &&smsNotMonth.getText().length() != 0 &&smsNotDay.getText().length() != 0 &&smsNotTo.getText().length()!=0&&smsNotHour.getText().length()!=0&&smsNotMinute.getText().length()!=0) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Log.d(TAG,"3");
//                                    HttpClient httpClient = new DefaultHttpClient();
//                                    HttpGet httpGet = new HttpGet(U + smsnotnumber + L);
//                                    HttpResponse httpResponse = httpClient.execute(httpGet);
//                                    if(httpResponse.getStatusLine().getStatusCode()==200) {
//                                        Log.d(TAG,"4");
//                                        HttpEntity entity = httpResponse.getEntity();
//                                        String response = EntityUtils.toString(entity, "uft-8");
//                                        JSONObject jsonObject;
//                                        jsonObject = new JSONObject(response);
//                                        JSONObject jo = jsonObject.getJSONObject("result");
//                                        String string2=jo.getString("city");
//                                        String string3=jo.getString("company");
//                                        String smsnotcompany=string2+string3.substring(2,4);
//
//                                        Log.d(TAG, "5");
//                                    }
//                                }catch (JSONException e){
//                                    e.printStackTrace();
//                                } catch (IOException e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
                        Log.d(TAG, "6");
                        Mission smsnotMission = new Mission();
                        smsnotMission.setMissionId(smsnotid);
                        smsnotMission.setMissionNumber(smsnotnumber);
                        smsnotMission.setMissionHour(smsnothour);
                        smsnotMission.setMissionMinute(smsnotminute);
                        smsnotMission.setMissionWord("发送短信任务");
                        smsnotMission.setMissionYear(smsnotyear);
                        smsnotMission.setMissionMonth(smsnotmonth);
                        smsnotMission.setMissionDay(smsnotday);
                        smsnotMission.setImageId(R.mipmap.ic_sms_not_black);
                      //  smsnotMission.setMissionCompany(smsnotcompany);
                        smsortelDB.saveMission(smsnotMission);
                        Intent intent = new Intent(SmsNotActivity.this, SmsNotService.class);
                        intent.putExtra("extra_smsnothour", smsnothour);
                        intent.putExtra("extra_smsnotminute", smsnotminute);
                        intent.putExtra("extra_smsnotid", smsnotid);
                        intent.putExtra("extra_smsnotyear", smsnotyear);
                        intent.putExtra("extra_smsnotmonth", smsnotmonth);
                        intent.putExtra("extra_smsnotday", smsnotday);
                        startService(intent);
                        Toast.makeText(SmsNotActivity.this, "任务已保存", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(SmsNotActivity.this, "任务无效", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SmsNotActivity.this, "时间设置有误", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;

        }
        return true;
    }



}
