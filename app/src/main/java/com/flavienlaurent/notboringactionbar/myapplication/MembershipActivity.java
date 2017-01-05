package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.flavienlaurent.notboringactionbar.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MembershipActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnNickname;
    Button btnArea;
    Button btnAge;
    Button btnTall;
    Button btnSmoking;
    Button btnReligion;
    Button btnMent;
    Button btnDrink;
    Button btnBody;
    Button btnJob;
    Button btnSchool;
    /***********
     * 회원정보 전송
     ******/

    RadioButton radioMan;
    RadioButton radioWoman;
    String manORwoman = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        /*******키보드 숨기기*****/
        final Button editName = (Button) findViewById(R.id.btnNickname);
        LinearLayout layoutProfile = (LinearLayout) findViewById(R.id.layout);
        layoutProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
            }
        });

        /**********회원정보 전송********/
        btnNickname = (Button) findViewById(R.id.btnNickname);
        btnArea = (Button) findViewById(R.id.btnArea);
        btnAge = (Button) findViewById(R.id.btnAge);
        btnTall = (Button) findViewById(R.id.btnHeight);
        btnSmoking = (Button) findViewById(R.id.btnSmoking);
        btnReligion = (Button) findViewById(R.id.btnReligion);
        btnMent = (Button) findViewById(R.id.btnSpeech);
        btnDrink = (Button) findViewById(R.id.btnDrink);
        btnBody = (Button) findViewById(R.id.btnBody);
        btnJob = (Button) findViewById(R.id.btnJob);
        btnSchool = (Button) findViewById(R.id.btnSchool);

        radioMan = (RadioButton) findViewById(R.id.radioMan);
        radioWoman = (RadioButton) findViewById(R.id.radioWoman);
        radioMan.setOnClickListener(optionOnClickListener);
        radioWoman.setOnClickListener(optionOnClickListener);

        btnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAgePicker();
            }
        });

        btnTall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeightPicker();
            }
        });

        btnNickname.setOnClickListener(this);
        btnArea.setOnClickListener(this);
        btnSmoking.setOnClickListener(this);
        btnReligion.setOnClickListener(this);
        btnMent.setOnClickListener(this);
        btnDrink.setOnClickListener(this);
        btnBody.setOnClickListener(this);
        btnSchool.setOnClickListener(this);
        btnJob.setOnClickListener(this);
        btnBody.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnNickname)) { // 닉네임 입력
            final EditText etEdit = new EditText(this);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("닉네임");
            etEdit.setWidth(1);
            etEdit.setMaxWidth(20);
            dialog.setView(etEdit);

            // OK 버튼 이벤트
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String inputValue = etEdit.getText().toString();
                    btnNickname.setText(inputValue);
                }
            });
            // Cancel 버튼 이벤트
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        } else if (v.equals(btnArea)) { // 지역입력
            final CharSequence[] items = new CharSequence[]{"서울", "경기", "인천", "대전", "충북",
                    "충남", "강원", "부산", "경북", "경남", "대구", "울신", "광주", "전북", "전남", "제주"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("지역");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                // 리스트 선택 시 이벤트
                public void onClick(DialogInterface dialog, int which) {
                    btnArea.setText(items[which]);
                }
            });
            dialog.show();
        } else if (v.equals(btnSmoking)) { // 흡연 여부 입력
            final CharSequence[] items = new CharSequence[]{"흡연", "비흡연"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("흡연여부");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                // 리스트 선택 시 이벤트
                public void onClick(DialogInterface dialog, int which) {
                    btnSmoking.setText(items[which]);
                }
            });
            dialog.show();
        } else if (v.equals(btnReligion)) { // 흡연 여부 입력
            final CharSequence[] items = new CharSequence[]{"종교없음", "기독교", "천주교"
            , "불교", "유교"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("종교 선택");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                // 리스트 선택 시 이벤트
                public void onClick(DialogInterface dialog, int which) {
                    btnReligion.setText(items[which]);
                }
            });
            dialog.show();
        }else if (v.equals(btnMent)) { // 하고 싶은말 입력
            final EditText etEdit = new EditText(this);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("이성에게 하고 싶은 말(20자)");
            etEdit.setWidth(1);
            etEdit.setMaxWidth(20);
            dialog.setView(etEdit);

            // OK 버튼 이벤트
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String inputValue = etEdit.getText().toString();
                    btnMent.setText(inputValue);
                }
            });
            // Cancel 버튼 이벤트
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        } else if (v.equals(btnDrink)){ // 주량 입력
            final CharSequence[] items = new CharSequence[]{
                    "즐겨하는 편", "가끔 마시는편", "분위기를 즐김", "마시지 않음"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("술 선택");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                // 리스트 선택 시 이벤트
                public void onClick(DialogInterface dialog, int which) {
                    btnDrink.setText(items[which]);
                }
            });
            dialog.show();
        } else if (v.equals(btnBody)){// 체형 입력
            final CharSequence[] items = new CharSequence[]{
                    "뚱뚱한 편", "통통한 편", "보통", "마른 편",
                    "거울보고 만족", "내가봐도 섹시", "보면 코피"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("체형 선택");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                // 리스트 선택 시 이벤트
                public void onClick(DialogInterface dialog, int which) {
                    btnBody.setText(items[which]);
                }
            });
            dialog.show();
        } else if (v.equals(btnJob)){ // 직업입력
            final EditText etEdit = new EditText(this);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("직업을 입력하여 주세요.");
            etEdit.setWidth(1);
            etEdit.setMaxWidth(20);
            dialog.setView(etEdit);

            // OK 버튼 이벤트
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String inputValue = etEdit.getText().toString();
                    btnJob.setText(inputValue);
                }
            });
            // Cancel 버튼 이벤트
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        } else if (v.equals(btnSchool)) { //학교 입력
            final EditText etEdit = new EditText(this);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MembershipActivity.this);
            dialog.setTitle("학교를 입력하여 주세요.");
            etEdit.setWidth(1);
            etEdit.setMaxWidth(20);
            dialog.setView(etEdit);

            // OK 버튼 이벤트
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String inputValue = etEdit.getText().toString();
                    btnSchool.setText(inputValue);
                }
            });
            // Cancel 버튼 이벤트
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }

    public void onBtnClicked(View v) { //다음화면 넘어가기
        String Nick_name = btnNickname.getText().toString();
        String Sex = manORwoman;
        String Area = btnArea.getText().toString();
        String Tall = btnTall.getText().toString();
        String Smoke_or_not = btnSmoking.getText().toString();
        String Religion = btnReligion.getText().toString();
        String Saying = btnMent.getText().toString();
        String Job = btnJob.getText().toString();
        String School = btnSchool.getText().toString();
        String Age = btnAge.getText().toString();
        String Body = btnBody.getText().toString();
        String Drink = btnDrink.getText().toString();

        insertToDatabase(Nick_name, Sex, Area, Tall, Smoke_or_not, Religion, Saying, Job, School, Age, Body, Drink);

        Intent intent = new Intent(getApplication(), UploadsActivity.class);
        intent.putExtra("nickname", Nick_name);
        startActivity(intent);
    }

    //DB로 전송
    private void insertToDatabase(String nickname, String sex, String area, String tall,
                                  String smoke, String religion, String saying, String job,
                                  String school, String age, String body, String drink) {

        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MembershipActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                try {

                    String nickname = (String) params[0];
                    String sex = (String) params[1];
                    String area = (String) params[2];
                    String tall = (String) params[3];
                    String smoke = (String) params[4];
                    String religion = (String) params[5];
                    String saying = (String) params[6];
                    String job = (String) params[7];
                    String school = (String) params[8];
                    String age = (String) params[9];
                    String body = (String) params[10];
                    String drink= (String) params[11];


                    /***************수정**************/
                    String link = "http://164.125.154.54/insert.php";
                    String data = URLEncoder.encode("Nick_name", "UTF-8") + "=" + URLEncoder.encode(nickname, "UTF-8");
                    data += "&" + URLEncoder.encode("Sex", "UTF-8") + "=" + URLEncoder.encode(sex, "UTF-8");
                    data += "&" + URLEncoder.encode("Area", "UTF-8") + "=" + URLEncoder.encode(area, "UTF-8");
                    data += "&" + URLEncoder.encode("Tall", "UTF-8") + "=" + URLEncoder.encode(tall, "UTF-8");
                    data += "&" + URLEncoder.encode("Smoke_or_not", "UTF-8") + "=" + URLEncoder.encode(smoke, "UTF-8");
                    data += "&" + URLEncoder.encode("Religion", "UTF-8") + "=" + URLEncoder.encode(religion, "UTF-8");
                    data += "&" + URLEncoder.encode("Saying", "UTF-8") + "=" + URLEncoder.encode(saying, "UTF-8");
                    data += "&" + URLEncoder.encode("Job", "UTF-8") + "=" + URLEncoder.encode(job, "UTF-8");
                    data += "&" + URLEncoder.encode("School", "UTF-8") + "=" + URLEncoder.encode(school, "UTF-8");
                    data += "&" + URLEncoder.encode("Age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8");
                    data += "&" + URLEncoder.encode("Body", "UTF-8") + "=" + URLEncoder.encode(body, "UTF-8");
                    data += "&" + URLEncoder.encode("Drink", "UTF-8") + "=" + URLEncoder.encode(drink, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(nickname, sex, area, tall, smoke, religion, saying, job, school, age, body, drink);
    }

    //RadioButton 클릭리스너
    RadioButton.OnClickListener optionOnClickListener
            = new RadioButton.OnClickListener() {

        public void onClick(View v) {
            if (radioMan.isChecked()) {
                manORwoman = "남자";
            } else if (radioWoman.isChecked()) {
                manORwoman = "여자";
            }
        }
    };
    private void showAgePicker() {

        final Dialog AgeDialog = new Dialog(this);
        AgeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AgeDialog.setContentView(R.layout.dialog);

        Button okBtn = (Button) AgeDialog.findViewById(R.id.dialog_btn_ok);
        Button cancelBtn = (Button) AgeDialog.findViewById(R.id.dialog_btn_cancel);

        final NumberPicker np = (NumberPicker) AgeDialog.findViewById(R.id.dialogPicker);
        np.setMinValue(18);
        np.setMaxValue(40);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(np, android.R.color.white);
        np.setWrapSelectorWheel(false);
        np.setValue(20);
        np.setBackgroundColor(getResources().getColor(R.color.main));
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAge.setText(String.valueOf(np.getValue()));
                AgeDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgeDialog.dismiss();
            }
        });

        AgeDialog.show();
    }
    private void showHeightPicker() {

        final Dialog AgeDialog = new Dialog(this);
        AgeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AgeDialog.setContentView(R.layout.dialog);

        Button okBtn = (Button) AgeDialog.findViewById(R.id.dialog_btn_ok);
        Button cancelBtn = (Button) AgeDialog.findViewById(R.id.dialog_btn_cancel);

        final NumberPicker np = (NumberPicker) AgeDialog.findViewById(R.id.dialogPicker);
        np.setMinValue(130);
        np.setMaxValue(220);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(np, android.R.color.white);
        np.setWrapSelectorWheel(false);
        np.setValue(170);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTall.setText(String.valueOf(np.getValue()));
                AgeDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgeDialog.dismiss();
            }
        });

        AgeDialog.show();
    }

    private void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}