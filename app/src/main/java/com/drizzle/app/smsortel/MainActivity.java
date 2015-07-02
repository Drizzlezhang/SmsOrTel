package com.drizzle.app.smsortel;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.drizzle.app.smsortel.model.Mission;
import com.drizzle.app.smsortel.model.MissionAdapter;
import com.drizzle.app.smsortel.model.SmsOrTelDB;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.shamanland.fab.FloatingActionButton;

import java.util.List;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private FloatingActionButton sendMessage;
    private FloatingActionButton callTel;
    private SmsOrTelDB smsortelDB;
    private List<Mission> missionList;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_perm_phone_msg_white_36dp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.list_view);
        smsortelDB=SmsOrTelDB.getInstance(this);
        missionList=smsortelDB.findMissions();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(MainActivity.this);
                Mission mission = missionList.get(position);
                int typeId = mission.getImageId();
                if (typeId == R.mipmap.ic_sms_ok_black) {  //显示一个已编辑短信任务对话框
                    dialogBuilder.withTitle("这是一条编辑好的短信任务")
                            .withDialogColor("#3d5afe")
                            .withMessage(
                                    "预定时间 : " + mission.getMissionYear() + "年" + mission.getMissionMonth() + "月" + mission.getMissionDay()
                                            + "日" + mission.getMissionHour() + "时" + mission.getMissionMinute() + "分" + "\n" + "\n"
                                            + "致 :  " + mission.getMissionNumber() + "\n" + "\n"
                                            + mission.getMissionWord())
                            .withEffect(Effectstype.Fall)
                            .withDuration(450)
                            .withButton1Text("长按可以删除该任务")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                }
                            })
                            .show();
                } else if (typeId == R.mipmap.ic_sms_not_black) {//显示一个短信任务对话框
                    dialogBuilder.withTitle("这是一条没编辑的短信任务")
                            .withDialogColor("#3d5afe")
                            .withMessage(
                                    "预定时间 : " + mission.getMissionYear() + "年" + mission.getMissionMonth() + "月" + mission.getMissionDay()
                                            + "日" + mission.getMissionHour() + "时" + mission.getMissionMinute() + "分" + "\n" + "\n"
                                            + "致 :  " + mission.getMissionNumber())
                            .withEffect(Effectstype.Fall)
                            .withDuration(450)
                            .withButton1Text("长按可以删除该任务")
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                }
                            })
                            .show();
                } else if (typeId == R.mipmap.ic_tel_ok_black) {
                    dialogBuilder.withTitle("这是一个电话任务")
                            .withDialogColor("#3d5afe")
                            .withMessage(
                                    "预定时间 : " + mission.getMissionYear() + "年" + mission.getMissionMonth() + "月" + mission.getMissionDay()
                                            + "日" + mission.getMissionHour() + "时" + mission.getMissionMinute() + "分" + "\n" + "\n"
                                            + "致 :  " + mission.getMissionNumber())
                            .withEffect(Effectstype.Fall)
                            .withButton1Text("长按可以删除该任务")
                            .withDuration(450)
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.dismiss();
                                }
                            })
                            .show();
                } else if (typeId == R.mipmap.ic_alarm) {
                    dialogBuilder.withTitle("这个任务没什么用")
                            .withDialogColor("#e51c23")
                            .withMessage("快添加新任务吧~")
                            .withEffect(Effectstype.Fall)
                            .withDuration(450)
                            .show();
                }


            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Mission mission = missionList.get(position);
                final int missionId = mission.getMissionId();
                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                View view1 = getLayoutInflater().inflate(R.layout.dialog, null);
                alertDialog.setView(view1, 0, 0, 0, 0);
                alertDialog.show();
                int width = getWindowManager().getDefaultDisplay().getWidth();
                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();//得到这个dialog界面的参数对象
                params.width = width - (width / 6);//设置dialog的界面宽度
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;//设置dialog高度为包裹内容
                params.gravity = Gravity.CENTER;
                alertDialog.getWindow().setAttributes(params);
                Button dialogOk = (Button) view1.findViewById(R.id.dialog_ok);
                Button dialogCancel = (Button) view1.findViewById(R.id.dialog_cancel);
                dialogOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        smsortelDB.deleteMission(missionId);
                        missionList = smsortelDB.findMissions();
                        if (missionList.size() != 0) {
                            MissionAdapter adapter = new MissionAdapter(MainActivity.this, R.layout.item, missionList);
                            listView.setAdapter(adapter);
                        }
                        if (missionList.size() == 0) {
                            initMissions();
                            MissionAdapter adapter = new MissionAdapter(MainActivity.this, R.layout.item, missionList);
                            listView.setAdapter(adapter);
                        }
                        alertDialog.cancel();

                    }
                });
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
            return true;
            }

        });
        callTel=(FloatingActionButton)findViewById(R.id.floatButtontel);
        callTel.setOnClickListener(this);
        // listView.setOnTouchListener(new ShowHideOnScroll(callTel, R.anim.floating_action_button_show, R.anim.floating_action_button_hide));
        sendMessage=(FloatingActionButton)findViewById(R.id.floatButtonsms);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });
//        ShowHideOnScroll s1=new ShowHideOnScroll(callTel, R.anim.floating_action_button_show, R.anim.floating_action_button_hide);
//        ShowHideOnScroll s2=new ShowHideOnScroll(sendMessage, R.anim.floating_action_button_show, R.anim.floating_action_button_hide);
//        listView.setOnTouchListener(s2);
//        listView2.setOnTouchListener(s1);

        //listView.setOnTouchListener(new ShowHideOnScroll(sendMessage, R.anim.floating_action_button_show, R.anim.floating_action_button_hide));





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsortelDB=SmsOrTelDB.getInstance(this);
        missionList=smsortelDB.findMissions();
        if(missionList.size()!=0){
            MissionAdapter adapter=new MissionAdapter(MainActivity.this,R.layout.item,missionList);
            listView.setAdapter(adapter);
        }if(missionList.size()==0){
            initMissions();
            MissionAdapter adapter=new MissionAdapter(MainActivity.this,R.layout.item,missionList);
            listView.setAdapter(adapter);
        }

    }

    private void initMissions(){
        Mission noMission=new Mission();
        noMission.setMissionId(1024);
        noMission.setMissionNumber("无任务");
        noMission.setMissionWord("无任务内容");
        noMission.setMissionHour("XX");
        noMission.setMissionMinute("XX");
        noMission.setMissionMonth("XX");
        noMission.setMissionDay("XX");
        noMission.setMissionYear("XXXX");
        noMission.setImageId(R.mipmap.ic_alarm);
        missionList.add(noMission);
    }

    private void showPopWindow(){
        Button smsOk;
        Button smsNotOk;
        final PopupWindow popupWindow;
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.choose_sms, null);
        Point size=new Point();
        popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.update();
        if(!popupWindow.isShowing()) {
           popupWindow.showAtLocation(inflater.inflate(R.layout.activity_main, null), Gravity.CENTER, 0, 0);
       }
        smsOk=(Button) view.findViewById(R.id.sms_ok);
        smsOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SmsActivity.class);
                startActivity(intent);
//                finish();
                popupWindow.dismiss();
            }
        });
        smsNotOk=(Button) view.findViewById(R.id.sms_not_ok);
        smsNotOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SmsNotActivity.class);
                startActivity(intent);
                //finish();
                popupWindow.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.floatButtontel){
            Intent intent=new Intent(this,TelActivity.class);
            startActivity(intent);;
            //finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private void delete(ApplicationInfo item) {
        // delete app
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.fromParts("package", item.packageName, null));
            startActivity(intent);
        } catch (Exception e) {
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}



