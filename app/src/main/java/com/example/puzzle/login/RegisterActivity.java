package com.example.puzzle.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.puzzle.BaseActivity;
import com.example.puzzle.R;
import com.example.puzzle.dialog.CustomDialog;
import com.example.puzzle.utils.EmailValidator;
import com.example.puzzle.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.linearLayoutBack)
    LinearLayout linearLayoutBack;
    @BindView(R.id.editTextSignUpUserName)
    EditText editTextSignUpUserName;
    @BindView(R.id.editTextSignUpEmail)
    EditText editTextSignUpEmail;
    @BindView(R.id.editTextSignUpPassword)
    EditText editTextSignUpPassword;
    @BindView(R.id.mTextViewUserNameError)
    TextView mTextViewUserNameError;
    @BindView(R.id.mTextViewEmailError)
    TextView mTextViewEmailError;
    @BindView(R.id.mTextViewPasswordError)
    TextView mTextViewPasswordError;
    @BindView(R.id.layoutSignUp)
    LinearLayout layoutSignUp;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EmailValidator emailValidator;
    boolean isUserNameAvail, isEmailAvail, isPasswordAvail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        try {
            Utils.hideSoftKeyboard(RegisterActivity.this);
            editTextSignUpUserName.addTextChangedListener(new TextChange(editTextSignUpUserName));
            editTextSignUpEmail.addTextChangedListener(new TextChange(editTextSignUpEmail));
            editTextSignUpPassword.addTextChangedListener(new TextChange(editTextSignUpPassword));

            editTextSignUpUserName.setOnEditorActionListener(editorListener);
            editTextSignUpEmail.setOnEditorActionListener(editorListener);
            editTextSignUpPassword.setOnEditorActionListener(editorListener);
            emailValidator = new EmailValidator();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private final TextView.OnEditorActionListener editorListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_NEXT:
                    break;
                case EditorInfo.IME_ACTION_DONE:
                    validate(editTextSignUpUserName.getText().toString(), editTextSignUpEmail.getText().toString().trim(), editTextSignUpPassword.getText().toString());
                    break;
            }
            return false;
        }
    };

    @OnClick(R.id.linearLayoutBack)
    void onBackClick() {
        try {
            Utils.hideSoftKeyboard(RegisterActivity.this);
            invalidateErrorMessages();
            finish();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutSignUp)
    void onSignUpClick() {
        try {
            if (validate(editTextSignUpUserName.getText().toString(), editTextSignUpEmail.getText().toString().trim(), editTextSignUpPassword.getText().toString())) {
                Utils.hideSoftKeyboard(RegisterActivity.this);
                if (Utils.isNetworkConnectionAvailable(this)) {
                    showProgress();
//                    registration();
                    firebaseRegister();
                } else {
                    showCustomDialog("", getResources().getString(R.string.error_network), getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void firebaseRegister() {
        try {
            firebaseAuth.createUserWithEmailAndPassword(editTextSignUpEmail.getText().toString(), editTextSignUpPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgress();
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                storeUserInfo(userId, editTextSignUpUserName.getText().toString(), editTextSignUpEmail.getText().toString());
                                showCustomDialog("", "Register successfully", getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                            } else {

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgress();
                    showCustomDialog("", e.getMessage(), getResources().getString(R.string.ok), getResources().getString(R.string.warning), null);
                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }

    }

    CustomDialog.OnDismissListener onDismissListener = () -> finish();

    private void storeUserInfo(String userId, String userName, String email) {
        try {
            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("userId", "" + userId);
            addFieldInfo.put("userName", "" + userName);
            addFieldInfo.put("userEmailAddress", "" + email);
            DocumentReference databaseReference = firebaseFirestore.collection("Users").document(userId);
            databaseReference.set(addFieldInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void invalidateErrorMessages() {
        try {
            mTextViewUserNameError.setText("");
            mTextViewEmailError.setText("");
            mTextViewPasswordError.setText("");
            mTextViewUserNameError.setVisibility(View.GONE);
            mTextViewEmailError.setVisibility(View.GONE);
            mTextViewPasswordError.setVisibility(View.GONE);
            editTextSignUpUserName.setText("");
            editTextSignUpEmail.setText("");
            editTextSignUpPassword.setText("");
            editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public boolean validate(String strFirstName, String strEmail, String strPassword) {
        boolean valid = true;
        try {
            if (strFirstName.isEmpty()) {
                mTextViewUserNameError.setVisibility(View.VISIBLE);
                editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewUserNameError.setText("Please enter the username");
                valid = false;
            }
            if (strEmail.isEmpty()) {
                mTextViewEmailError.setVisibility(View.VISIBLE);
                editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewEmailError.setText("Please enter the email");
                valid = false;
            }
            if (!emailValidator.validate(strEmail)) {
                mTextViewEmailError.setVisibility(View.VISIBLE);
                editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewEmailError.setText("Please enter the valid email");
                valid = false;
            }
            if (strPassword.isEmpty()) {
                mTextViewPasswordError.setVisibility(View.VISIBLE);
                editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewPasswordError.setText("Please enter the password");
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    private class TextChange implements TextWatcher {
        View view;

        private TextChange(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            try {
                String s = charSequence.toString();
                switch (view.getId()) {
                    case R.id.editTextSignUpEmail:
                        isEmailAvail = s.length() > 0;
                        if (isEmailAvail) {
                            mTextViewEmailError.setVisibility(View.GONE);
                            editTextSignUpEmail.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        }
                        break;
                    case R.id.editTextSignUpUserName:
                        isUserNameAvail = s.length() > 0;
                        if (isUserNameAvail) {
                            mTextViewUserNameError.setVisibility(View.GONE);
                            editTextSignUpUserName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        }
                        break;
                    case R.id.editTextSignUpPassword:
                        isPasswordAvail = s.length() > 0;
                        if (isPasswordAvail) {
                            mTextViewPasswordError.setVisibility(View.GONE);
                            editTextSignUpPassword.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        }
                        break;
                }
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            try {
                String s = editable.toString();
                if (view.getId() == R.id.editTextSignUpEmail) {
                    if (!s.equals(s.toLowerCase())) {
                        s = s.toLowerCase();
                        editTextSignUpEmail.setText(s);
                        editTextSignUpEmail.setSelection(editTextSignUpEmail.getText().toString().length());
                    }
                }
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }
    }

    public void showProgress() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }
}