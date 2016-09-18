package com.deltabit.bazaar;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.deltabit.bazaar.data.BazaarContract;
import com.deltabit.bazaar.data.BazaarDbHelper;
import com.deltabit.bazaar.data.BazaarProvider;

public class CadastroActivity extends AppCompatActivity {

    EditText mTxtUsername,mTxtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);
    }

    public void btnCadastrar_onClick(View view) {
        String username = mTxtUsername.getText().toString();
        String password = mTxtPassword.getText().toString();

        ContentValues userValues = new ContentValues();
        userValues.put(BazaarContract.UserEntry.COLUMN_USERNAME,username);
        userValues.put(BazaarContract.UserEntry.COLUMN_PASSWORD,password);

        Uri uri = getContentResolver().insert(BazaarContract.UserEntry.CONTENT_URI,userValues);
        Toast.makeText(CadastroActivity.this,uri.toString() , Toast.LENGTH_SHORT).show();
    }
}
