package com.example.hackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleSignIn";
    private static final String PREFS_NAME = "MyPrefs";
    private static final String USER_ID_KEY = "userId";

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is already logged in
        String userId = getUserIdFromPreferences();
        Log.d(TAG, "Retrieved userId from preferences: " + userId); // Log retrieved userId
        if (userId != null) {
            // User is already signed in, navigate to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // Google Sign-In button
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        // Skip button for guest mode
        findViewById(R.id.skip_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Directly navigate to MainActivity for guest access
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("guest_mode", true); // Pass flag indicating guest mode
                startActivity(intent);
                finish(); // Close LoginActivity
            }
        });
    }

    // Save user ID in SharedPreferences
    private void saveUserIdToPreferences(String userId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.apply();

        // Log saved user ID for debugging
        Log.d(TAG, "Saved userId to preferences: " + userId);
    }

    // Retrieve user ID from SharedPreferences
    private String getUserIdFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userId = sharedPreferences.getString(USER_ID_KEY, null);

        // Log the retrieval of user ID
        Log.d(TAG, "Retrieved userId from SharedPreferences: " + userId);
        return userId;
    }

    // Trigger the Google Sign-In flow
    private void signInWithGoogle() {
        // Sign out the last user first to force the account selection dialog
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    // Handle result of the Google Sign-In intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(LoginActivity.this, "Google sign-in failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Authenticate with Firebase using the Google account
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign-in success, get the signed-in user's info
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid(); // Get the UID

                                // Save user ID in SharedPreferences
                                saveUserIdToPreferences(userId);

                                // Log the UID for debugging purposes
                                Log.d(TAG, "User UID: " + userId);

                                // Check if user exists in Firebase Database using UID
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                                databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // User exists, navigate to MainActivity and fetch user details
                                            Log.d(TAG, "User exists in database: " + userId); // Log if user exists
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // User doesn't exist, navigate to UserProfileActivity
                                            Log.d(TAG, "User does not exist, redirecting to profile setup."); // Log if user doesn't exist
                                            String firstName = user.getDisplayName() != null ? user.getDisplayName().split(" ")[0] : "";
                                            String lastName = user.getDisplayName() != null ? user.getDisplayName().split(" ")[1] : "";
                                            Intent intent = new Intent(LoginActivity.this, ChooseRoleActivity.class);
                                            intent.putExtra("firstName", firstName);
                                            intent.putExtra("lastName", lastName);
                                            intent.putExtra("email", user.getEmail());
                                            intent.putExtra("uid", userId); // Pass UID to the next activity
                                            startActivity(intent);
                                        }
                                        finish(); // Close the login activity
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle possible errors.
                                        Log.e(TAG, "Database error: " + error.getMessage());
                                        Toast.makeText(LoginActivity.this, "Error checking user data", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            // If sign-in fails, display a message to the user
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
