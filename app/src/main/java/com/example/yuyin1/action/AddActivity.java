package com.example.yuyin1.action;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import com.example.yuyin1.entity.Message;
import com.example.yuyin1.R;
import com.example.yuyin1.util.FucUtil;
import com.example.yuyin1.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks{
    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;


    private TimePickerView pvTime;
    private EditText date2;
    private EditText name;
    private EditText flightId;
    private EditText amount;
    private EditText goods;
    private EditText boxer;
    private EditText starter;
    private EditText remark;
    private RadioGroup radioGroup;
    private RadioButton zancun;
    private RadioButton fangqi;
    private RadioButton tuihui;
    private RadioButton tuoyun;
    private RadioButton yijiao;
    private RadioButton xiedai;
    private RadioButton xianliang;

    private static String TAG = MainActivity.class.getSimpleName();
    private SpeechRecognizer mAsr;
    private Toast mToast;
    private static final String GRAMMAR_TYPE_BNF = "bnf";
    private static final String GRAMMAR_TYPE_ABNF = "abnf";
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    int ret = 0;
    // 缓存
    private SharedPreferences mSharedPreferences;
    private static final String KEY_GRAMMAR_ABNF_ID = "grammar_abnf_id";
    // 语法、词典临时变量
    String mContent;
    // 云端语法文件
    private String mCloudGrammar = null;
    Message m = new Message();
    int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ZXingLibrary.initDisplayOpinion(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initTimePicker();
        requestPermissions();
        // 初始化识别对象
        mAsr = SpeechRecognizer.createRecognizer(AddActivity.this, mInitListener);
        mCloudGrammar = FucUtil.readFile(this,"rules.abnf","utf-8");

        mSharedPreferences = getSharedPreferences(getPackageName(),	MODE_PRIVATE);
        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        findViewById(R.id.yuyin).setOnClickListener(AddActivity.this);

        date2 = (EditText)findViewById(R.id.date1);
        name = (EditText)findViewById(R.id.name1);
        flightId = (EditText) findViewById(R.id.flightId1);
        amount = (EditText)findViewById(R.id.amount1);
        goods = (EditText)findViewById(R.id.goods1);
        boxer = (EditText)findViewById(R.id.boxer1);
        starter = (EditText)findViewById(R.id.starter1);
        remark = (EditText)findViewById(R.id.remark1);
        Button addnew = (Button)this.findViewById(R.id.addnew);
        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m.getWay() == null){
                    Toast.makeText(v.getContext(), "请选择一个方式", Toast.LENGTH_SHORT).show();
                }else{
                    String date3 = date2.getText().toString();
                    String name1 = name.getText().toString();
                    String flightId1 = flightId.getText().toString();
                    String amount1 = amount.getText().toString();
                    String goods1 = goods.getText().toString();
                    String boxer1 = boxer.getText().toString();
                    String starter1 = starter.getText().toString();
                    String remark1 =remark.getText().toString();

                    m.setDate(date3);
                    m.setName(name1);
                    m.setFlightId(flightId1);
                    m.setAmount(Integer.parseInt(amount1));
                    m.setGoods(goods1);
                    m.setBoxer(boxer1);
                    m.setStarter(starter1);
                    m.setRemark(remark1);
                    if(m.save()){
                        Toast.makeText(AddActivity.this,"添加成功！",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddActivity.this,"添加失败！",Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }

            }
        });

        Button timepick = (Button)findViewById(R.id.timepicker);
        timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show(v);
            }
        });
        radioGroup=(RadioGroup)findViewById(R.id.radioGroupID);
        zancun=(RadioButton)findViewById(R.id.zancunID);
        fangqi=(RadioButton)findViewById(R.id.fangqiID);
        tuihui=(RadioButton)findViewById(R.id.tuihuiID);
        tuoyun=(RadioButton)findViewById(R.id.tuoyunID);
        yijiao=(RadioButton)findViewById(R.id.yijiaoID);
        xiedai=(RadioButton)findViewById(R.id.xiedaiID);
        xianliang=(RadioButton)findViewById(R.id.xianliangID);
        radioGroup.setOnCheckedChangeListener(new RadioGroupListener());
        Button scan = (Button)findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraTask();

            }
        });

        ;
    }
    //点击语音识别按钮逻辑
    @Override
    public void onClick(View view) {
        if( null == mAsr ){
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip( "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化" );
            return;
        }

        if(null == mEngineType) {
            this.showTip("请先选择识别引擎类型");
            return;
        }
        switch(view.getId())
        {
            // 开始识别
            case R.id.yuyin:
                this.showTip("上传预设关键词/语法文件");
                // 在线-构建语法文件，生成语法id
                ((EditText)findViewById(R.id.goods1)).setText(mCloudGrammar);
                mContent = new String(mCloudGrammar);
                //指定引擎类型
                mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
                mAsr.setParameter(SpeechConstant.TEXT_ENCODING,"utf-8");
                ret = mAsr.buildGrammar(GRAMMAR_TYPE_ABNF, mContent, mCloudGrammarListener);
                if(ret != ErrorCode.SUCCESS)
                    showTip("语法构建失败,错误码：" + ret+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                ((EditText)findViewById(R.id.goods1)).setText(null);// 清空显示内容
                // 设置参数
                if (!setParam()) {
                    this.showTip("请先构建语法。");
                    return;
                };

                ret = mAsr.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    this.showTip("识别失败,错误码: " + ret+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                }
                break;


        }
    }

    /**
     * 点击空白区域时隐藏键盘
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };

    /**
     * 云端构建语法监听器。
     */
    private GrammarListener mCloudGrammarListener = new GrammarListener() {
        @Override
        public void onBuildFinish(String grammarId, SpeechError error) {
            if(error == null){
                String grammarID = new String(grammarId);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                if(!TextUtils.isEmpty(grammarId))
                    editor.putString(KEY_GRAMMAR_ABNF_ID, grammarID);
                editor.commit();
                showTip("语法构建成功：" + grammarId);
            }else{
                showTip("语法构建失败,错误码：" + error.getErrorCode()+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };
    /**
     * 识别监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据："+data.length);
        }

        @Override
        public void onResult(final RecognizerResult result, boolean isLast) {
            if (null != result) {
                Log.d(TAG, "recognizer result：" + result.getResultString());
                String text ;
                if("cloud".equalsIgnoreCase(mEngineType)){
                    text = JsonParser.parseGrammarResult(result.getResultString());
                }else {
                    text = JsonParser.parseLocalGrammarResult(result.getResultString());

                }

                // 显示

                ((EditText)findViewById(R.id.goods1)).setText(text);
                cut(text);
            } else {
                Log.d(TAG, "recognizer result : null");
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            showTip("onError Code："	+ error.getErrorCode());
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };
    /**
     * 语音识别参数设置
     * @return
     */
    public boolean setParam(){
        boolean result = false;
        //设置识别引擎
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //设置返回结果为json格式
        mAsr.setParameter(SpeechConstant.RESULT_TYPE, "json");

        if("cloud".equalsIgnoreCase(mEngineType))
        {
            String grammarId = mSharedPreferences.getString(KEY_GRAMMAR_ABNF_ID, null);
            if(TextUtils.isEmpty(grammarId))
            {
                result =  false;
            }else {
                //设置云端识别使用的语法id
                mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
                result =  true;
            }
        }

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mAsr.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mAsr.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/asr.wav");
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    //以下四种方法是使用谷歌的easypermissions的接口需要实现的四种方法
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);
    }

    class RadioGroupListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId==zancun.getId()){
                m.setWay("zancun");

            }else if (checkedId==fangqi.getId()){
                m.setWay("fangqi");
            }else if (checkedId==tuihui.getId()){
                m.setWay("tuihui");
            }else if (checkedId==tuoyun.getId()){
                m.setWay("tuoyun");
            }else if (checkedId==yijiao.getId()){
                m.setWay("yijiao");
            }else if (checkedId==xiedai.getId()){
                m.setWay("xiedai");
            }else if (checkedId==xianliang.getId()){
                m.setWay("xianliang");
            }
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 初始化日历控件
     */
    private void initTimePicker() {//Dialog 模式下，在底部弹出

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(v.getContext(), getTime(date), Toast.LENGTH_SHORT).show();
                date2.setText(String.valueOf(getTime(date)));

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {

                    }
                })
                .setType(new boolean[]{false, true, true, true, true, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    /**
     * 扫一扫界面返回的识别结果用此函数接收
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
             //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    Toast.makeText(this, "解析结果:kong" , Toast.LENGTH_LONG).show();
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    String[] results = result.split(" ");
                    Log.d("scan",result);
                    if(results.length == 1){
                        flightId.setText("扫描失败");
                    }else{
                        flightId.setText(String.valueOf(results[0]));
                        name.setText(String.valueOf(results[1]));

                    }

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }




    private void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (hasCameraPermission()) {
            // Have permission, do the thing!
            Intent intent = new Intent(this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);

        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    "想访问你的相机",
                    RC_CAMERA_PERM,
                    Manifest.permission.CAMERA);
        }
    }
    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);
    }
    //动态获取相关权限
    private void requestPermissions() {
        //要获取的权限
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!


        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, "想要的权限",
                    RC_LOCATION_CONTACTS_PERM, perms);
        }
    }

    private void cut(String res){
        // 按指定模式在字符串查找

        String pattern = "[\\u4e00-\\u9fa5]{4}(\\D*)数量为(\\d+) 选择(.*)开机员是(.*)开箱员是(.*)";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(res);
        if (m.find( )) {
            Log.d("aaa","Found value: " + m.group(1) );
            ((EditText)findViewById(R.id.goods1)).setText(m.group(1));
            Log.d("aaa","Found value: " + m.group(2) );
            ((EditText)findViewById(R.id.amount1)).setText(m.group(2));
            Log.d("aaa","Found value: " + m.group(3) );
            if (m.group(3).equals("暂存")){
                ((RadioButton)findViewById(R.id.zancunID)).setChecked(true);

            }else if (m.group(3).equals("放弃")){
                ((RadioButton)findViewById(R.id.fangqiID)).setChecked(true);
            }else if (m.group(3).equals("退回")){
                ((RadioButton)findViewById(R.id.tuihuiID)).setChecked(true);
            }else if (m.group(3).equals("托运")){
                ((RadioButton)findViewById(R.id.tuoyunID)).setChecked(true);
            }else if (m.group(3).equals("移交")){
                ((RadioButton)findViewById(R.id.yijiaoID)).setChecked(true);
            }else if (m.group(3).equals("携带")){
                ((RadioButton)findViewById(R.id.xiedaiID)).setChecked(true);
            }else if (m.group(3).equals("限量")){
                ((RadioButton)findViewById(R.id.xianliangID)).setChecked(true);
            }

            Log.d("aaa","Found value: " + m.group(4) );
            ((EditText)findViewById(R.id.starter1)).setText(m.group(4));
            Log.d("aaa","Found value: " + m.group(5) );
            ((EditText)findViewById(R.id.boxer1)).setText(m.group(5));
        } else {
            Log.d("aaa","NO MATCH");
            ((EditText)findViewById(R.id.goods1)).setText("请重新输入");
        }

    }


}
