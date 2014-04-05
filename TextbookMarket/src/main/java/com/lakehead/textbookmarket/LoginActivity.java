package com.lakehead.textbookmarket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * This OnTaskCompleted is an interface that Activities receiving callbacks from async tasks should implement.
 */
public class LoginActivity extends Activity implements OnTaskCompleted {
    private static final String TAG = "LoginActivity";
    String deptCode;

    EditText emailText;
    EditText passText;

    Button loginButton;
    Button toRegButton;

    SharedPreferences prefs;
    ProgressBar bar;

    String tokenString;
    JSONArray jArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.email_field);
        passText = (EditText) findViewById(R.id.pass_field);
        //necessary for some reason to prevent the password field fonts from defaulting to courier
        passText.setTypeface(Typeface.DEFAULT);
        passText.setTransformationMethod(new PasswordTransformationMethod());

        loginButton = (Button) findViewById(R.id.login_button);
        toRegButton = (Button) findViewById(R.id.no_account_button);
        bar = (ProgressBar) findViewById(R.id.loader);

        prefs = this.getSharedPreferences("com.lakehead.textbookmarket", Context.MODE_PRIVATE);

        // Keep this here for testing logging in and registering
        //prefs.edit().clear().commit();

        tokenString = prefs.getString("remember_token","");

        if(!tokenString.equals(""))
        {
            Log.i(TAG, "onCreate() -> " + "Found some token in prefs: " + tokenString);
            hideUI();
            NameValuePair ext = new BasicNameValuePair("user_id", tokenString);
            NameValuePair count = new BasicNameValuePair("count", "1");

            //this call is just to test whether the token is valid/not expired. We don't actually care about sells.
            new GetJSONArrayTask(this, "/api/sell").execute(ext, count);
        }
        bar.setVisibility(View.INVISIBLE);


    }

    //Get the login info, encode the email address, and then put the info into a String.

    /**
     * Executes a LoginTask
     * @param v
     */
    public void login(View v)
    {
        String email = emailText.getText().toString();
        String pass = passText.getText().toString();

        if(email.equals("") || pass.equals(""))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid email address or password", Toast.LENGTH_SHORT);
            toast.show();
            passText.setText("");
        }
        else
        {
            try
            {
                email = URLEncoder.encode(email, "utf-8");
            }
            catch(UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

            NameValuePair ext = new BasicNameValuePair("email", email);
            NameValuePair count = new BasicNameValuePair("password", pass);
            Log.i(TAG, "Login() -> " + "Attempting login with user: " + email);
            new GetJSONArrayTask(this, "/api/login").execute(ext, count);
            hideUI();
            //new LoginTask(this).execute(url, emailAddress, password);
        }
    }

    public void goToRegister(View v)
    {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void goToMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTaskCompleted(Object obj)
    {
        if(obj == null)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Internal server error", Toast.LENGTH_SHORT);
            toast.show();
            showUI();
            passText.setText("");
        }
        else if(obj.getClass() == JSONArray.class)
        {
            this.jArray = (JSONArray)obj;
            String token = "";
            String emailAddress = "";
            try
            {
                String kind = jArray.getJSONObject(0).getString("kind");
                if(kind.equals("error")) //login failed
                {
                    Log.e(TAG, "OnTaskCompleted() -> " + "Login Failed.");
                    Toast toast = Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT);
                    toast.show();
                    showUI();
                    passText.setText("");
                }
                else if(kind.equals("token")) //login success
                {

                    token = jArray.getJSONObject(0).getJSONObject("data").getString("token");
                    Log.i(TAG, "OnTaskCompleted() -> " + "Token Returned. This is a new login. Token = " + token);
                    emailAddress = emailText.getText().toString();

                    prefs.edit().putString("remember_token", token).commit();
                    prefs.edit().putString("email_address", emailAddress).commit();
                    bar.setVisibility(View.GONE);
                    goToMainActivity();
                }
                else //You have a valid token already
                {
                    Log.i(TAG, "OnTaskCompleted() -> " + "Token was already valid");
                    bar.setVisibility(View.GONE);
                    goToMainActivity();
                }
            }
            catch(JSONException e)
            {
                Log.d("Exceptions", e.toString());
            }
        }
    }

    /**
     * A simple function used to hide the user interface while executing a background task.
     */
    private void hideUI()
    {
        emailText.setVisibility(View.INVISIBLE);
        passText.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        toRegButton.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);
    }

    /**
     * A simple function used to show the user interface after executing a background task.
     */
    private void showUI()
    {
        emailText.setVisibility(View.VISIBLE);
        passText.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        toRegButton.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);
    }
}