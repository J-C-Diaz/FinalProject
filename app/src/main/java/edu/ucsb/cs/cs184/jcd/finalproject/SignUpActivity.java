package edu.ucsb.cs.cs184.jcd.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import static android.support.constraint.Constraints.TAG;

public class SignUpActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private EditText mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mAuth;
    private Button mButton;
    private DatabaseReference mDatabase;
    private Spinner mSpinner;
    int spinnerSelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mEmailView = (EditText) findViewById(R.id.email2);


        mPasswordView = (EditText) findViewById(R.id.password2);
        mButton = (Button) findViewById(R.id.sign_up_button);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

         mSpinner = (Spinner) findViewById(R.id.spinner);
         mSpinner.setOnItemSelectedListener(this);
    // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choices_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);
    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);



        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //updateUI(null);
                        }


                    }
                });
        // [END create_user_with_email]
    }

    public void createAccount(View view) {
        String email = "";
        String password = "";

        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        if ((email == "") || (password == "")){
            Toast.makeText(getApplicationContext(), "Email or Password is blank", Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "createAccount:" + email);



        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserData(user.getUid(),mEmailView.getText().toString(),Integer.toString(spinnerSelection));
                            startActivity(new Intent(SignUpActivity.this, CalendarActivity.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            //updateUI(null);
                        }


                    }
                });
        // [END create_user_with_email]
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        spinnerSelection = pos;
        if(pos == 0){
            mButton.setVisibility(View.INVISIBLE);
        }
        else
            mButton.setVisibility(View.VISIBLE);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Hide SignUpButton
        mButton.setVisibility(View.INVISIBLE);

    }

    public void addUserData(String uid, String email, String preference){
        User user=new User();
        user.setUid(uid);
        user.setEmail(email);
        user.setPreference(preference);
        mDatabase.child("user").child(uid).setValue(user);
    }


}

@IgnoreExtraProperties
class User {

    public String uid;
    public String email;
    public String preference;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id) {
        this.uid = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}