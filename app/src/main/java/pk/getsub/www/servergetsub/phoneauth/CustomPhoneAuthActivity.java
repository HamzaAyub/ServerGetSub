package pk.getsub.www.servergetsub.phoneauth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import pk.getsub.www.servergetsub.R;
import pk.getsub.www.servergetsub.UserSharPrefer;
import pk.getsub.www.servergetsub.checkinternet.ConnectionDetector;
import pk.getsub.www.servergetsub.map.OrderMapActivity;
import pk.getsub.www.servergetsub.retrofit.LaraService;
import pk.getsub.www.servergetsub.retrofit.UserPojo;
import pk.getsub.www.servergetsub.retrofit.UserProfileActivity;
import pk.getsub.www.servergetsub.splashscreen.SplashScreen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomPhoneAuthActivity extends AppCompatActivity {


    private static final String TAG = "HTAG";
    private EditText editNumber;
    private EditText editCode;
    private Button btnNumber;
    private Button btnCode;
    private TextView txtNumber;
    private TextView txtCode;
    private ImageView imgNumber;
    private TextInputLayout editNumberText;
    private TextInputLayout editCodeText;
    private ConnectionDetector cd;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mVerificationId = null;

    private String phoneNumber;
    private UserSharPrefer spUser; // if number already exit in database

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_phone_auth);

        spUser = new UserSharPrefer(this);
        editNumber = (EditText) findViewById(R.id.edit_auth_number);
        editCode = (EditText) findViewById(R.id.edit_auth_code);
        btnNumber = (Button) findViewById(R.id.btn_auth_number);
        btnCode = (Button) findViewById(R.id.btn_auth_code);
        txtNumber = (TextView) findViewById(R.id.txt_auth_number);
        txtCode = (TextView) findViewById(R.id.txt_auth_code);
        imgNumber = (ImageView) findViewById(R.id.img_auth_number);
        editNumberText = (TextInputLayout) findViewById(R.id.edit_auth_number_txt_layout);
        editCodeText = (TextInputLayout) findViewById(R.id.edit_auth_code_txt_layout);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(CustomPhoneAuthActivity.this, UserProfileActivity.class); // splsh screen sy wapis aey to yanha jaey
                    startActivity(intent);
                    finish();
                }

            }
        };
        cd = new ConnectionDetector(this);
        if (!(cd.CheckConnected())) {
            showMessage("No Internet Connection");
        }
// click Listener to send number
        btnNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(cd.CheckConnected())) {
                    showMessage("No Internet Connection");
                } else {


                    phoneNumber = editNumber.getText().toString();

                    if (phoneNumber.equals("")) {
                        showMessage("please Enter the number");

                        Log.d(TAG, "onClick:  empty number");

                    } else if (isValidMobile(phoneNumber) == true) {

                        String finalNumber = phoneNumber.substring(1);
                        phoneNumber = "+92"+finalNumber;

                        if (phoneNumber.length() == 13) {
                            Log.d(TAG, "onClick: validddd");

                            checkPhoneNumnber(phoneNumber);

                            UserSharPrefer storeUser = new UserSharPrefer(CustomPhoneAuthActivity.this);
                            storeUser.setUserPhone(phoneNumber);
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, CustomPhoneAuthActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                                    // if user already exit
                                    if (!(spUser.getName().equals("mNull") && spUser.getUserAddress().equals("mNull"))) {
                                        startActivity(new Intent(CustomPhoneAuthActivity.this, OrderMapActivity.class));
                                        finish();
                                      //  System.exit(0);
                                    }
                                    startActivity(new Intent(CustomPhoneAuthActivity.this , UserProfileActivity.class));
                                    Log.d(TAG, "onVerificationCompleted: ");
                                }

                                @Override
                                public void onVerificationFailed(FirebaseException e) {
                                    Log.e(TAG, "onVerificationFailed: ", e.getCause());
                                    Log.d(TAG, "onVerificationFailed: ");
                                    Log.e(TAG, "onVerificationFailed: ", e);
                                }

                                @Override
                                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);
                                    mVerificationId = s;
                                    Log.d(TAG, "onCodeSent: ");
                                }

                                @Override
                                public void onCodeAutoRetrievalTimeOut(String s) {
                                    super.onCodeAutoRetrievalTimeOut(s);
                                    Log.d(TAG, "onCodeAutoRetrievalTimeOut: ");
                                }
                            });

                            btnNumber.setVisibility(View.GONE);
                            txtNumber.setVisibility(View.GONE);
                            imgNumber.setVisibility(View.GONE);
                            editNumber.setVisibility(View.GONE);
                            editNumberText.setVisibility(View.GONE);

                            btnCode.setVisibility(View.VISIBLE);
                            editCode.setVisibility(View.VISIBLE);
                            txtCode.setVisibility(View.VISIBLE);
                            editCodeText.setVisibility(View.VISIBLE);

                        } else {
                            Log.d(TAG, "onClick: Enter valid nummber");
                            showMessage("Enter full valid number");
                        }
                    } else {
                        Log.d(TAG, "onClick: falssssssssssssss");
                        showMessage("Enter Valid Number");
                    }

                } // end internet if else
                /////////////////////////////////////////////////////////////////

              /*  else{
                    UserSharPrefer storeUser = new UserSharPrefer(CustomPhoneAuthActivity.this);
                    storeUser.setUserPhone(phoneNumber);
                if (TextUtils.isEmpty(phoneNumber)) {
                    return;
                }
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+923328469195", 60, TimeUnit.SECONDS, CustomPhoneAuthActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {

                        }

                        @Override
                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            mVerificationId = s;


                        }

                        @Override
                        public void onCodeAutoRetrievalTimeOut(String s) {
                            super.onCodeAutoRetrievalTimeOut(s);
                        }
                    });
                    //////////////////////////////////

                    btnNumber.setVisibility(View.GONE);
                    txtNumber.setVisibility(View.GONE);
                    imgNumber.setVisibility(View.GONE);
                    editNumber.setVisibility(View.GONE);
                    editNumberText.setVisibility(View.GONE);

                    btnCode.setVisibility(View.VISIBLE);
                    editCode.setVisibility(View.VISIBLE);
                    txtCode.setVisibility(View.VISIBLE);
                    editCodeText.setVisibility(View.VISIBLE);


                }*/
                ///////////////////////////////////////////////////////////////


            }
        }); // end btnNumber

        btnCode.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                String code = editCode.getText().toString();

                if(!(code.equals(""))) {
                    if(code.length() == 6){

                        if (!(spUser.getName().equals("mNull") && spUser.getUserAddress().equals("mNull"))) {
                            startActivity(new Intent(CustomPhoneAuthActivity.this, OrderMapActivity.class));
                            finish();
                          //  System.exit(0);
                        }
                        else {
                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                            signInWithCrediential(credential);
                            startActivity(new Intent(CustomPhoneAuthActivity.this , SplashScreen.class));
                        }


                    }
                }
                else{
                    showMessage("Enter Correct Code");
                }
                
               /* PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithCrediential(credential);
                startActivity(new Intent(CustomPhoneAuthActivity.this , SplashScreen.class));*/
            }
        });

    }

    private void signInWithCrediential(PhoneAuthCredential phoneAuthCrediential) {
        mAuth.signInWithCredential(phoneAuthCrediential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(CustomPhoneAuthActivity.this, UserProfileActivity.class)
                                    .putExtra("phone", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty())
                            );
                            finish();
                        }
                    }
                });

    }


    public void showMessage(final String msg) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Alert Message")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  Snackbar.make( constraintLayout, msg ,Snackbar.LENGTH_SHORT).show();
                        Log.d(TAG, "showMessageBox: " + msg);
                        return;
                    }
                })
                .show();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }



    private void checkPhoneNumnber(String number ){

        Gson gson = new GsonBuilder().setLenient().create();  // if there is some syntext error in json array
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://getsub.pk/mlarafolder/laraserver/public/index.php/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        LaraService services = retrofit.create(LaraService.class);
        Call<UserPojo> client = services.getUser(number);
        client.enqueue(new Callback<UserPojo>() {
            @Override
            public void onResponse(Call<UserPojo> call, Response<UserPojo> response) {


                Log.d(TAG, "onResponse:" + response);

                Log.d(TAG, "onResponse: Signup : " + response.message());
                Log.d(TAG, "onResponse:" + response.body().getId());

                int myId = response.body().getId();


                spUser.setUserId(myId);
                spUser.setName(response.body().getName());
                spUser.setUserPhone(response.body().getPhone());
                spUser.setUserAddress(response.body().getAddress());

            //    startActivity(new Intent(CustomPhoneAuthActivity.this , OrderMapActivity.class));



                //   startActivity(new Intent(UserProfileActivity.this, FrontPageActivity.class));

                //      startActivity(new Intent(UserProfileActivity.this, OrderMapActivity.class));




            }

            @Override
            public void onFailure(Call<UserPojo> call, Throwable t) {
                Log.d(TAG, "onFailure:" + t);
                showMessage("Some Connection Error");
            }
        });

    }



}


