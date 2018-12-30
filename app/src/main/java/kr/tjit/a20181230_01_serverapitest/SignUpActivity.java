package kr.tjit.a20181230_01_serverapitest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import kr.tjit.a20181230_01_serverapitest.utils.PasswordUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

//                1. 서버와의 통신을 담당할 client 변수 생성
                OkHttpClient client = new OkHttpClient();

//                2. 서버에 첨부할 파라미터들을 담아줄 requestBody 객체 생성
                RequestBody requestBody = new FormBody.Builder()
                        .add("user_id", userIdEdt.getText().toString())
                        .add("password", PasswordUtil.getEncryptedPassword(UserPwEdt.getText().toString()))
                        .add("name", userNameEdt.getText().toString())
                        .add("phone", userPhoneEdt.getText().toString())
                        .add("email", userEmailEdt.getText().toString())
                        .build();

//                3. 어느 서버로 갈지 설정하는 request 변수 생성 (자동완성 시 okhttp3 확인)
                Request request = new Request.Builder()
                        .url("http://api-dev.lebit.kr/auth")
                        .put(requestBody)
                        .build();

//                4. 통신 담당 client 변수를 사용해 위의 request를 실제로 실행
//                   서버 응답은 Callback()으로 처리
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
//                        5. response 변수는 단 한번만 접근 가능하므로 따로 저장해서 사용
                        String responseBody = response.body().string();
                        Log.d("회원가입리스폰스", responseBody);

                        try {
//                            6. 이 응답 JSON데이터를 가져올 땐 formatter로 보기좋게 해놓고 뽑아옴
//                               필요한 내용들을 파싱해서 변수에 저장
                            JSONObject root = new JSONObject(responseBody);
                            final int code = root.getInt("code");
                            final String message = root.getString("message");

                            Log.d("회원가입리스폰스", "코드 : " + code);
                            Log.d("회원가입리스폰스", "메시지 : " + message);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                  if (code == 200) {
                                      Toast.makeText(mContext, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                      finish();
                                  }
                                  else {
                                      Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                  }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
