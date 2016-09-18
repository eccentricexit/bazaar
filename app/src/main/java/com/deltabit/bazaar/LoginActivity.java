package com.deltabit.bazaar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.deltabit.bazaar.data.BazaarContract;
import com.deltabit.bazaar.data.BazaarProvider;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    EditText mTxtUsername, mTxtPassword;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Skip login if user has already logged in once
        mSharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        int userId = mSharedPreferences.getInt(getString(R.string.user_id_resource),-1);
        if(userId!=-1) {
            Utility.userId = userId;
            startActivity(new Intent(this, MainActivity.class));
        }


        //set fields
        mTxtUsername = (EditText) findViewById(R.id.txtUsername);
        mTxtPassword = (EditText) findViewById(R.id.txtPassword);

        //testValues
        mTxtUsername.setText("joao");
        mTxtPassword.setText("spock");
    }

    public void btnLogin_click(View view){
        String username = mTxtUsername.getText().toString();
        String password = mTxtPassword.getText().toString();


        if(authenticateUser(username,password)){
            startActivity(new Intent(this,MainActivity.class));
            mSharedPreferences.edit().putInt(getString(R.string.user_id_resource),userId);
            Utility.userId = userId;
        }else{
            Toast.makeText(LoginActivity.this, "Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnCadastrar_onClick(View view){
        startActivity(new Intent(this,CadastroActivity.class));
    }

    boolean authenticateUser(String username, String password){
        Cursor result = getContentResolver().query(
                BazaarContract.UserEntry.CONTENT_URI,
                null,
                BazaarProvider.sUserSelection,
                new String[]{username,password},
                null
        );

        if(result.getCount()==0){
            return false;
        }else{
            result.moveToFirst();
            int idColumnIndex = result.getColumnIndex(BazaarContract.UserEntry._ID);
            userId = result.getInt(idColumnIndex);
            mSharedPreferences.edit().putInt(getString(R.string.user_id_resource),userId).commit();
            return true;
        }
    }
}
