package com.tencent.cloud.huiyansdkface.sampledemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.tencent.cloud.huiyansdkface.sampledemo.R;
import com.tencent.cloud.huiyansdkface.facelight.api.WbCloudFaceContant;


public class SettingActivity extends Activity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    public static final String BEAUTY_FACE = "beautyFace";

    private boolean isShowSuccess;
    private boolean isShowFail;
    private boolean isRecord;
    private boolean isPlayVoice;
    private String compareType;
    private String colorMode;
    private String language;

    private SlipButton showSuccessPage;
    private SlipButton showFailPage;
    private SlipButton recordVideo;
    private SlipButton playVoice;
    private ImageView back;

    private RadioGroup colorChoose;
    private RadioButton blackBtn;
    private RadioButton whiteBtn;
    private RadioButton customBtn;

    private RadioGroup srcChoose;
    private RadioButton noneSrcBtn;
    private RadioButton idSrcBtn;

    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (getIntent() != null) {
            Bundle data = getIntent().getExtras();
            if (data != null) {
                isShowSuccess = data.getBoolean(WbCloudFaceContant.SHOW_SUCCESS_PAGE, true);
                isShowFail = data.getBoolean(WbCloudFaceContant.SHOW_FAIL_PAGE, true);
                isRecord = data.getBoolean(WbCloudFaceContant.VIDEO_UPLOAD, false);
                isPlayVoice = data.getBoolean(WbCloudFaceContant.PLAY_VOICE, false);
                colorMode = data.getString(WbCloudFaceContant.COLOR_MODE, WbCloudFaceContant.WHITE);
                compareType = data.getString(WbCloudFaceContant.COMPARE_TYPE, WbCloudFaceContant.ID_CARD);
                language = data.getString(WbCloudFaceContant.LANGUAGE, WbCloudFaceContant.LANGUAGE_ZH_CN);
            }
        }
        init();
    }

    private void init() {
        showSuccessPage = (SlipButton) findViewById(R.id.wbcf_demo_show_success_slip_btn);
        showFailPage = (SlipButton) findViewById(R.id.wbcf_demo_show_fail_slip_btn);
        recordVideo = (SlipButton) findViewById(R.id.wbcf_demo_record_btn);
        playVoice = (SlipButton) findViewById(R.id.wbcf_demo_play_voice_btn);
        back = (ImageView) findViewById(R.id.wbcf_demo_setting_back);

        colorChoose = (RadioGroup) findViewById(R.id.wbcf_demo_color_choose_rg);
        blackBtn = (RadioButton) findViewById(R.id.wbcf_demo_black_btn);
        whiteBtn = (RadioButton) findViewById(R.id.wbcf_demo_white_btn);
        customBtn = (RadioButton) findViewById(R.id.wbcf_demo_custom_btn);

        srcChoose = (RadioGroup) findViewById(R.id.wbcf_demo_src_choose_rg);
        noneSrcBtn = (RadioButton) findViewById(R.id.wbcf_demo_none_src_btn);
        idSrcBtn = (RadioButton) findViewById(R.id.wbcf_demo_id_src_btn);

        languageSpinner = (Spinner) findViewById(R.id.langguage_choose_spinner);

        final String[] languageItem = {"简体中文", "繁体中文", "英语", "日语", "韩语", "泰语", "印尼语"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(SettingActivity.this, R.layout.activity_spinner, languageItem);
        spinnerAdapter.setDropDownViewResource(R.layout.activity_spinner_drop);
        languageSpinner.setAdapter(spinnerAdapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                language = parseLanguage(position);
                Log.d(TAG, "spinner select language:" + language);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int pos = parseLanguage2Spinner(language);
        Log.d(TAG, "pos=" + pos);
        languageSpinner.setSelection(pos);

        if (isShowSuccess) {
            showSuccessPage.setCheck(true);
        } else {
            showSuccessPage.setCheck(false);
        }
        showSuccessPage.SetOnChangedCallBack(new SlipButton.onChangedCallBack() {
            @Override
            public void OnChanged(boolean CheckState) {
                isShowSuccess = CheckState;
            }
        });

        if (isShowFail) {
            showFailPage.setCheck(true);
        } else {
            showFailPage.setCheck(false);
        }
        showFailPage.SetOnChangedCallBack(new SlipButton.onChangedCallBack() {
            @Override
            public void OnChanged(boolean CheckState) {
                isShowFail = CheckState;
            }
        });

        if (isRecord) {
            recordVideo.setCheck(true);
        } else {
            recordVideo.setCheck(false);
        }
        recordVideo.SetOnChangedCallBack(new SlipButton.onChangedCallBack() {
            @Override
            public void OnChanged(boolean CheckState) {
                isRecord = CheckState;
            }
        });

        if (isPlayVoice) {
            playVoice.setCheck(true);
        } else {
            playVoice.setCheck(false);
        }
        playVoice.SetOnChangedCallBack(new SlipButton.onChangedCallBack() {
            @Override
            public void OnChanged(boolean CheckState) {
                isPlayVoice = CheckState;
            }
        });

        if (colorMode.equals(WbCloudFaceContant.WHITE)) {
            whiteBtn.setChecked(true);
        } else if (colorMode.equals(WbCloudFaceContant.BLACK)) {
            blackBtn.setChecked(true);
        } else {
            customBtn.setChecked(true);
        }
        colorChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.wbcf_demo_black_btn) {
                    colorMode = WbCloudFaceContant.BLACK;
                } else if (checkedId == R.id.wbcf_demo_white_btn) {
                    colorMode = WbCloudFaceContant.WHITE;
                } else if (checkedId == R.id.wbcf_demo_custom_btn) {
                    colorMode = WbCloudFaceContant.CUSTOM;
                }
            }
        });

        if (WbCloudFaceContant.ID_CARD.equals(compareType)) {
            idSrcBtn.setChecked(true);
        } else if (compareType.equals(WbCloudFaceContant.NONE)) {
            noneSrcBtn.setChecked(true);
        }
        srcChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.wbcf_demo_id_src_btn) {
                    compareType = WbCloudFaceContant.ID_CARD;
                } else if (checkedId == R.id.wbcf_demo_none_src_btn) {
                    compareType = WbCloudFaceContant.NONE;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                // 监听到返回按钮点击事件
                data.putExtra(WbCloudFaceContant.SHOW_SUCCESS_PAGE, isShowSuccess);
                data.putExtra(WbCloudFaceContant.SHOW_FAIL_PAGE, isShowFail);
                data.putExtra(WbCloudFaceContant.VIDEO_UPLOAD, isRecord);
                data.putExtra(WbCloudFaceContant.PLAY_VOICE, isPlayVoice);
                data.putExtra(WbCloudFaceContant.COMPARE_TYPE, compareType);
                data.putExtra(WbCloudFaceContant.COLOR_MODE, colorMode);
                data.putExtra(WbCloudFaceContant.LANGUAGE, language);

                setResult(RESULT_OK, data);
                SettingActivity.this.finish();
            }
        });
    }

    private String parseLanguage(int position) {
        String key;
        switch (position) {
            case 0:
                key = WbCloudFaceContant.LANGUAGE_ZH_CN;
                break;
            case 1:
                key = WbCloudFaceContant.LANGUAGE_ZH_HK;
                break;
            case 2:
                key = WbCloudFaceContant.LANGUAGE_EN;
                break;
            case 3:
                key = WbCloudFaceContant.LANGUAGE_JA;
                break;
            case 4:
                key = WbCloudFaceContant.LANGUAGE_KO;
                break;
            case 5:
                key = WbCloudFaceContant.LANGUAGE_TH;
                break;
            case 6:
                key = WbCloudFaceContant.LANGUAGE_ID;
                break;
            default:
                key = WbCloudFaceContant.LANGUAGE_ZH_CN;
                break;
        }
        return key;
    }

    private int parseLanguage2Spinner(String language) {
        int key;
        switch (language) {
            case WbCloudFaceContant.LANGUAGE_ZH_CN:
                key = 0;
                break;
            case WbCloudFaceContant.LANGUAGE_ZH_HK:
                key = 1;
                break;
            case WbCloudFaceContant.LANGUAGE_EN:
                key = 2;
                break;
            case WbCloudFaceContant.LANGUAGE_JA:
                key = 3;
                break;
            case WbCloudFaceContant.LANGUAGE_KO:
                key = 4;
                break;
            case WbCloudFaceContant.LANGUAGE_TH:
                key = 5;
                break;
            case WbCloudFaceContant.LANGUAGE_ID:
                key = 6;
                break;
            default:
                key = 0;
                break;
        }
        return key;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent data = new Intent();

            // 监听到返回按钮点击事件
            data.putExtra(WbCloudFaceContant.SHOW_SUCCESS_PAGE, isShowSuccess);
            data.putExtra(WbCloudFaceContant.SHOW_FAIL_PAGE, isShowFail);
            data.putExtra(WbCloudFaceContant.VIDEO_UPLOAD, isRecord);
            data.putExtra(WbCloudFaceContant.PLAY_VOICE, isPlayVoice);
            data.putExtra(WbCloudFaceContant.COMPARE_TYPE, compareType);
            data.putExtra(WbCloudFaceContant.COLOR_MODE, colorMode);
            data.putExtra(WbCloudFaceContant.LANGUAGE, language);

            setResult(RESULT_OK, data);
            SettingActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
