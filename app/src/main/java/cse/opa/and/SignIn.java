package cse.opa.and;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity implements View.OnClickListener  {
    private Button btn_login;
    private TextView tv_signup;
    private EditText et_login_email;
    private EditText et_login_password;
    private FirebaseAuth mAuth; // responsible of authenticating users
    private ProgressDialog progressDialog;
    //Static Strings
    private final String ERROR_EMPTY_EMAIL="Email can't be left blank";
    private final String ERROR_EMPTY_PASSWORD="Password can't be left blank";
    private final String LOGIN_USER="Logging In...";
    private final String SUCCESSFULL_LOGIN="Successfully logged in";
    private final String ERROR_LOGIN="Error logging in , try again later";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btn_login = (Button)findViewById(R.id.btn_login);
        tv_signup = (TextView)findViewById(R.id.tv_signup);
        et_login_email=(EditText)findViewById(R.id.et_login_email);
        et_login_password=(EditText)findViewById(R.id.et_login_password);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);

    }
    //TODO: Validate Email & password
    @Override
    public void onClick(View view) {//this is a onclick function for any clickable view
        if (view==btn_login){
            String email=et_login_email.getText().toString().trim();
            String password=et_login_password.getText().toString().trim();
            if (email.isEmpty()){
                Toast.makeText(this,ERROR_EMPTY_EMAIL,Toast.LENGTH_SHORT).show();
                return;
            }
            else if (password.isEmpty()){
                Toast.makeText(this,ERROR_EMPTY_PASSWORD,Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.setMessage(LOGIN_USER);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//try to login in with email and password
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){//if logged in successfully
                        progressDialog.cancel();
                        Toast.makeText(SignIn.this,SUCCESSFULL_LOGIN,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignIn.this,MainActivity.class));
                        finish();
                    }
                    else {
                        progressDialog.cancel();
                        Toast.makeText(SignIn.this,ERROR_LOGIN,Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else if (view==tv_signup){
            startActivity(new Intent(SignIn.this,SignUp.class));
            finish();
        }
    }
    @Override
    public void onStart() {//checks on start of application if user is already signed in
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       //update UI ->redirect to home screen with username for example
    }
}
