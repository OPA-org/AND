package cse.opa.and;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private Button btn_signup;
    private EditText et_signup_email;
    private EditText et_signup_password;
    private EditText et_signup_confirm_password;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    //Static Strings
    private final String ERROR_EMPTY_EMAIL="Email can't be left blank";
    private final String ERROR_INVALID_EMAIL="Invalid e-mail address";
    private final String ERROR_EMPTY_PASSWORD="Password can't be left blank";
    private final String ERROR_INVALID_PASSWORD_SIZE="Password must be at least 8 characters";
    private final String ERROR_PASSWORDS_MISSMATCH="Passwords do not match";
    private final String SIGNUP_USER="Registering User...";
    private final String SUCCESSFULL_SIGNUP="Registered Successfully";
    private final String ERROR_SIGNUP="Error Registering user , try again later";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_signup=(Button)findViewById(R.id.btn_signup);
        et_signup_email=(EditText)findViewById(R.id.et_signup_email);
        et_signup_password=(EditText)findViewById(R.id.et_signup_password);
        et_signup_confirm_password=(EditText)findViewById(R.id.et_signup_confirm_password);
        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        btn_signup.setOnClickListener(this);
    }

    //TODO: Validate Email & password

    @Override
    public void onClick(View view) {//this is a onclick function for any clickable view
        if (view==btn_signup){
            String email=et_signup_email.getText().toString().trim();
            String password=et_signup_password.getText().toString().trim();
            String confirm_password=et_signup_confirm_password.getText().toString().trim();
            if (email.isEmpty()){
                Toast.makeText(this,ERROR_EMPTY_EMAIL,Toast.LENGTH_SHORT).show();
                return;
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){ // email validation
                Toast.makeText(this,ERROR_INVALID_EMAIL,Toast.LENGTH_SHORT).show();
                return;
            }
            else if (password.isEmpty()){
                Toast.makeText(this,ERROR_EMPTY_PASSWORD,Toast.LENGTH_SHORT).show();
                return;
            }
            else if (password.length()<8){//password length validation
                Toast.makeText(this,ERROR_INVALID_PASSWORD_SIZE,Toast.LENGTH_SHORT).show();
                return;
            }
            else if (!password.equals(confirm_password)){
                Toast.makeText(this,ERROR_PASSWORDS_MISSMATCH,Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.setMessage(SIGNUP_USER);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {//account creation
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){//if account created successfully
                        progressDialog.cancel();
                        Toast.makeText(SignUp.this,SUCCESSFULL_SIGNUP,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this,MainActivity.class));
                        finish();
                    }
                    else {
                        progressDialog.cancel();
                        Toast.makeText(SignUp.this,ERROR_SIGNUP,Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }
}
