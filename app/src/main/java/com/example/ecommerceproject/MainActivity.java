package com.example.ecommerceproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerceproject.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private ImageView imgFacebook, imgGmail;
    private Button btnLogin, btnSignUp;
    private ArrayList<User> users = new ArrayList<>();
    //    private String keyManager;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 10000;
    protected FirebaseDatabase database;
    protected DatabaseReference reference;
    protected FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Users");
        auth = FirebaseAuth.getInstance();
        createRequest();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    User user = item.getValue(User.class);
//                    if(user.getRole() == "Manager") {
//                        keyManager = user.getKey();
//                    }
                    users.add(user);

                    System.out.println(user.getEmail() + " " + user.getPassword() + " " + user.getRole());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkE = false;
                String e = edtEmail.getText().toString();
                String p = edtPassword.getText().toString();
                if (e.isEmpty()) {
                    edtEmail.setError("Email is not blank!");
                    return;
                }

                if (!e.contains("@") || !e.contains(".com")) {
                    edtEmail.setError("Email is wrong format");
                    return;
                }

                if (p.isEmpty()) {
                    edtPassword.setError("Password is not blank!");
                    return;
                }

                if (p.length() < 6) {
                    edtPassword.setError("No less than 6");
                    return;
                }
                for (User user : users) {
                    if (user.getEmail().equals(e)) {
                        checkE = true;
                        if (!user.getPassword().equals(p)) {
                            edtPassword.setError("Wrong password");
                            return;
                        }
                    }
                }
                if (checkE == false) {
                    edtEmail.setError("Email does not exist");
                    return;
                }
                if (e.equals("quanganh160999@gmail.com")) {
                    auth.signInWithEmailAndPassword(e, p)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Intent intent = new Intent(MainActivity.this, ManagerActivity.class);
                                    intent.putExtra("email_manager", e);
                                    startActivity(intent);
                                }
                            });
                } else {

                    auth.signInWithEmailAndPassword(e, p)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                                    for (User user : users) {
                                        if (user.getEmail().equals(e + "")) {
                                            intent.putExtra("customer", user);
                                            break;
                                        }
                                    }
                                    startActivity(intent);
                                }
                            });
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                System.out.println(e.getMessage());
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            User u = null;
                            boolean checkUser = false;
                            for (User user : users) {
                                if ((account.getEmail() + "").equals(user.getEmail())) {
                                    checkUser = true;
                                    u = user;
                                    break;
                                }
                            }
                            if (checkUser == false) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("email", account.getEmail() + "");
                                data.put("name", "null");
                                data.put("password", "null");
                                data.put("phone", "");
                                data.put("role", "Customer");
                                String key = String.valueOf(new Random().nextInt());
                                data.put("key", key);
                                reference.child("User - " + key).setValue(data);
                                u = new User(account.getEmail(), "null", "null", "", "Customer", key);
                            }

                            Intent intent = new Intent(MainActivity.this, ShopActivity.class);
                            intent.putExtra("customer", u);
                            startActivity(intent);
                            mGoogleSignInClient.signOut();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    private void initView() {
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        imgFacebook = findViewById(R.id.img_facebook);
        imgGmail = findViewById(R.id.img_gmail);
        btnLogin = findViewById(R.id.btn_login_login);
        btnSignUp = findViewById(R.id.btn_signUp_login);
    }
}