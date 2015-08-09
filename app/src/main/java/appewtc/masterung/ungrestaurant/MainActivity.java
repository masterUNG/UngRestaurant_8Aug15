package appewtc.masterung.ungrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private UserTABLE objUserTABLE;
    private FoodTABLE objFoodTABLE;
    private OrderTABLE objOrderTABLE;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();

        //Connected Database
        connectedDatabase();

        //Test Add Data
       // testAddData();

        //Syn JSON to SQLite
        synJSONtoSQLite();

    }   // onCreate

    public void clickLogin(View view) {

        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check ZERO
        if (userString.equals("") || passwordString.equals("") ) {

            //Have Space
            errorDialog("Have Space", "Please Fill Every Blank");

        } else {

            //No Space
            checkUserPassword();

        }

    }   // clickLogin

    private void checkUserPassword() {

        try {

            String[] strMyResult = objUserTABLE.searchUser(userString);

            //Check Password
            if (passwordString.equals(strMyResult[2])) {
                //Welcome User
                welcomeUser(strMyResult[3]);
            } else {
                errorDialog("Password False", "Please Try Again Password False");
            }

        } catch (Exception e) {
            errorDialog("ไม่มี User นี่้", "ไม่มี " + userString + " ในฐานข้อมูลของฉัน");
        }

    }   // checkUserPassword

    private void welcomeUser(final String strName) {
        final AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.restaurant);
        objBuilder.setTitle("ยินดีต้อนรับ");
        objBuilder.setMessage("สวัสดี " + strName);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent objIntent = new Intent(MainActivity.this, OrderActivity.class);
                objIntent.putExtra("Officer", strName);
                startActivity(objIntent);
                finish();

                dialogInterface.dismiss();
            }
        });
        objBuilder.show();
    }   // welcomeUser

    private void errorDialog(String strTitle, String strMessage) {

        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.danger);
        objBuilder.setTitle(strTitle);
        objBuilder.setMessage(strMessage);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();

    }   // errorDialog


    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
    }

    private void synJSONtoSQLite() {

        //Setup my Policy
        StrictMode.ThreadPolicy myPolicy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);

        //Delete All Data
        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("Restaurant.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("userTABLE", null, null);
        objSqLiteDatabase.delete("foodTABLE", null, null);
        objSqLiteDatabase.delete("orderTABLE", null, null);

        int intTime = 0;
        while (intTime <= 1) {


            //1. Setup Var&Constant
            InputStream objInputStream = null;
            String strJSON = null;
            String strUserURL = "http://swiftcodingthai.com/8aug/php_get_data_master.php";
            String strFoodURL = "http://swiftcodingthai.com/8aug/php_get_data_food.php";
            HttpPost objHttpPost;

            //2. Create InputStream
            try {

                HttpClient objHttpClient = new DefaultHttpClient();
                if (intTime != 1) {
                    objHttpPost = new HttpPost(strUserURL);
                } else {
                    objHttpPost = new HttpPost(strFoodURL);
                }

                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();

            } catch (Exception e) {
                Log.d("Rest", "Input ==> " + e.toString());
            }

            //3. Create strJSON
            try {

                BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;
                while ((strLine = objBufferedReader.readLine()) != null  ) {
                    objStringBuilder.append(strLine);
                }   // while
                objInputStream.close();
                strJSON = objStringBuilder.toString();

            } catch (Exception e) {
                Log.d("Rest", "strJSON ==> " + e.toString());
            }



            //4. Update to SQLite
            try {

                final JSONArray objJsonArray = new JSONArray(strJSON);
                for (int i = 0; i < objJsonArray.length(); i++) {

                    JSONObject jsonObject = objJsonArray.getJSONObject(i);

                    if (intTime != 1) {

                        //Update userTABLE
                        String strUser = jsonObject.getString("User");
                        String strPassword = jsonObject.getString("Password");
                        String strName = jsonObject.getString("Name");
                        objUserTABLE.addUser(strUser, strPassword, strName);

                    } else {

                        //Update foodTABLE
                        String strFood = jsonObject.getString("Food");
                        String strSource = jsonObject.getString("Source");
                        String strPrice = jsonObject.getString("Price");
                        objFoodTABLE.addFood(strFood, strSource, strPrice);

                    }

                }   // for

            } catch (Exception e) {
                Log.d("Rest", "Update ==> " + e.toString());
            }





            intTime += 1;
        }   // while

    }   // synJSONtoSQLite

    private void testAddData() {
        objUserTABLE.addUser("testUser", "testPass", "testName");
        objFoodTABLE.addFood("ข้าวผัด", "Path", "45");
        objOrderTABLE.addOrder("Officer", "Desk", "Food", "5");
    }

    private void connectedDatabase() {
        objUserTABLE = new UserTABLE(this);
        objFoodTABLE = new FoodTABLE(this);
        objOrderTABLE = new OrderTABLE(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}   // Main Class
