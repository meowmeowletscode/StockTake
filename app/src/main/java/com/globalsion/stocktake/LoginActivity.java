package com.globalsion.stocktake;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.globalsion.stocktake.tools.DBHelper;

public class LoginActivity extends AppCompatActivity {

    EditText txtUserID;
    EditText txtPassword;
    Button btnClear;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtUserID = findViewById(R.id.txtUserID);
        txtPassword = findViewById(R.id.txtPassword);

        btnClear = findViewById(R.id.btnClear);
        btnLogin = findViewById(R.id.btnLogin);

        btnClear.setOnClickListener(new BtnClearClickListener(txtUserID, txtPassword));
        btnLogin.setOnClickListener(new BtnLoginClickListener(txtUserID, txtPassword));
    }

    private class BtnClearClickListener implements View.OnClickListener {

        private EditText txtUserID;
        private EditText txtPassword;

        public BtnClearClickListener(EditText txtUserID, EditText txtPassword) {
            this.txtUserID = txtUserID;
            this.txtPassword = txtPassword;
        }

        @Override
        public void onClick(View v) {
            if (txtUserID != null) {
                txtUserID.setText("");
            }
            if (txtPassword != null) {
                txtPassword.setText("");
            }
        }
    }

    private class BtnLoginClickListener implements View.OnClickListener {

        private EditText txtUserID;
        private EditText txtPassword;

        public BtnLoginClickListener(EditText txtUserID, EditText txtPassword) {
            this.txtUserID = txtUserID;
            this.txtPassword = txtPassword;
        }

        @Override
        public void onClick(View v) {
            String userId = txtUserID.getText().toString();
            String password = txtPassword.getText().toString();

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DBHelper dbHelper = new DBHelper(LoginActivity.this);

            Cursor cursor = dbHelper.getUserData(userId);

            if (cursor != null && cursor.moveToFirst()) {

                boolean isValidPassword = dbHelper.validUser(password);

                if (isValidPassword) {
                    dbHelper.updateUserLogin(userId);
                    Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect password. Please try again.", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
            } else {
                // User not found
                Toast.makeText(LoginActivity.this, "User not found. Please check and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        LoginActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
