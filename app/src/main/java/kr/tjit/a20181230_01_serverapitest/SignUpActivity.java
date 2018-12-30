package kr.tjit.a20181230_01_serverapitest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import kr.tjit.a20181230_01_serverapitest.utils.PasswordUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends BaseActivity {

    private android.widget.EditText userIdEdt;
    private android.widget.EditText UserPwEdt;
    private android.widget.EditText userPwCheckEdt;
    private android.widget.EditText userNameEdt;
    private android.widget.EditText userEmailEdt;
    private android.widget.EditText userPhoneEdt;
    private android.widget.Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                몇가지 검증 절차

                if (userIdEdt.getText().toString().equals("")) {
                    Toast.makeText(mContext, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
//                    아이디를 입력 안했으면 onclick메소드 강제종료
                    return;
                }

//                아이디는 반드시 8글자 이상

                if (userIdEdt.getText().toString().length() < 8) {
                    Toast.makeText(mContext, "아이디는 8글자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                비밀번호도 8글자 이상
                if (userIdEdt.getText().toString().length() < 8) {
                    Toast.makeText(mContext, "비밀번호는 8글자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                비밀번호와 다시 입력한 비밀번호가 같아야 함
                String firstPw = UserPwEdt.getText().toString();
                String secondPw = userPwCheckEdt.getText().toString();

                if (!firstPw.equals(secondPw)) {
                    Toast.makeText(mContext, "비밀번호를 동일하게 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("user_id", userIdEdt.getText().toString())
                        .add("password", PasswordUtil.getEncryptedPassword(UserPwEdt.getText().toString()))
                        .add("name", userNameEdt.getText().toString())
                        .add("phone", userPhoneEdt.getText().toString())
                        .add("email", userEmailEdt.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://api-dev.lebit.kr/auth")
                        .put(requestBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "서버 통신 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("회원가입리스폰스", response.body().string());

                    }
                });


            }
        });
    }

    @Override
    public void setValues() {

    }

    @Override
    public void bindViews() {

        this.signUpBtn = (Button) findViewById(R.id.signUpBtn);
        this.userPhoneEdt = (EditText) findViewById(R.id.userPhoneEdt);
        this.userEmailEdt = (EditText) findViewById(R.id.userEmailEdt);
        this.userNameEdt = (EditText) findViewById(R.id.userNameEdt);
        this.userPwCheckEdt = (EditText) findViewById(R.id.userPwCheckEdt);
        this.UserPwEdt = (EditText) findViewById(R.id.UserPwEdt);
        this.userIdEdt = (EditText) findViewById(R.id.userIdEdt);
    }
}
