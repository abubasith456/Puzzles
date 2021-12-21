package com.example.puzzle.login;

import androidx.annotation.NonNull;

import android.content.Intent;
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

import com.example.puzzle.BaseActivity;
import com.example.puzzle.dashboard.DashboardActivity;
import com.example.puzzle.R;
import com.example.puzzle.dialog.CustomDialog;
import com.example.puzzle.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.editTextMobileNumberInput)
    EditText editTextMobileNumberInput;
    @BindView(R.id.mTextViewErrorMobileNUmber)
    TextView mTextViewErrorMobileNUmber;
    @BindView(R.id.editTextPasswordInput)
    EditText editTextPasswordInput;
    @BindView(R.id.mTextViewErrorPassword)
    TextView mTextViewErrorPassword;
    @BindView(R.id.layoutSignIn)
    LinearLayout layoutSignIn;
    @BindView(R.id.layoutSignUp)
    LinearLayout layoutSignUp;
    @BindView(R.id.layoutForgotPassword)
    LinearLayout layoutForgotPassword;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        try {
            editTextMobileNumberInput.addTextChangedListener(new LoginActivity.TextChange(editTextMobileNumberInput));
            editTextPasswordInput.addTextChangedListener(new LoginActivity.TextChange(editTextPasswordInput));
            editTextMobileNumberInput.setOnEditorActionListener(editorListener);
            editTextPasswordInput.setOnEditorActionListener(editorListener);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                invalidateErrorMessages();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userId", String.valueOf(userId));
                startActivity(intent);
                finish();
            }
        }catch (Exception exception) {
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
                    validate(editTextMobileNumberInput.getText().toString().trim(), editTextPasswordInput.getText().toString());
                    break;
            }
            return false;
        }
    };

    @OnClick(R.id.layoutSignIn)
    void onSignInClick() {
        try {
            if (validate(editTextMobileNumberInput.getText().toString().trim(), editTextPasswordInput.getText().toString())) {
                Utils.hideSoftKeyboard(LoginActivity.this);
                if (Utils.isNetworkConnectionAvailable(this)) {
                    showProgress();
                    firebaseLogin();
                } else {
                    showCustomDialog("", "please check your internet connection", getResources().getString(R.string.ok), getResources().getString(R.string.confirm), null);
                }
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutForgotPassword)
    void onLayoutForgotPasswordClick() {
        try {

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutSignUp)
    void onSignUpClick() {
        try {
            Utils.hideSoftKeyboard(LoginActivity.this);
            invalidateErrorMessages();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void firebaseLogin() {
        try {
            firebaseAuth.signInWithEmailAndPassword(editTextMobileNumberInput.getText().toString(), editTextPasswordInput.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideProgress();
                                userId=firebaseAuth.getCurrentUser().getUid();
                                showCustomDialog("", "Login successfully", getResources().getString(R.string.ok), getResources().getString(R.string.success), onDismissListener);
                            } else {
                                hideProgress();
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

    CustomDialog.OnDismissListener onDismissListener = () -> {
        invalidateErrorMessages();
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("userId", String.valueOf(userId));
        startActivity(intent);
        finish();
    };

    public boolean validate(String strEmail, String password) {
        boolean valid = true;
        try {
            if (strEmail.isEmpty()) {
                editTextMobileNumberInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorMobileNUmber.setVisibility(View.VISIBLE);
                mTextViewErrorMobileNUmber.setText("Please enter the mobile number");
                valid = false;
            }
            if (password.isEmpty()) {
                editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorPassword.setVisibility(View.VISIBLE);
                mTextViewErrorPassword.setText("Please enter the password");
                valid = false;
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    private void invalidateErrorMessages() {
        try {
            mTextViewErrorMobileNUmber.setText("");
            mTextViewErrorPassword.setText("");
            mTextViewErrorMobileNUmber.setVisibility(View.GONE);
            mTextViewErrorPassword.setVisibility(View.GONE);
            editTextMobileNumberInput.setText("");
            editTextPasswordInput.setText("");
            editTextMobileNumberInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
            editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
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
                if (view.getId() == R.id.editTextMobileNumberInput) {
                    if (s.length() > 0) {
                        editTextMobileNumberInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        mTextViewErrorMobileNUmber.setVisibility(View.GONE);
                    }
                } else if (view.getId() == R.id.editTextPasswordInput) {
                    if (s.length() > 0) {
                        editTextPasswordInput.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                        mTextViewErrorPassword.setVisibility(View.GONE);
                    }
                }
            } catch (Exception exception) {
                Log.e("Error ==> ", "" + exception);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

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