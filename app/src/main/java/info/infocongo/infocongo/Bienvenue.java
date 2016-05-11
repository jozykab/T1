package info.infocongo.infocongo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Bienvenue extends AppCompatActivity {

    Spinner spinnerPage, spinnerEnv, spinnerSite;
    ImageButton buildButton, clearButton, openButton, apiButton, mpsAdmin;
    ArrayAdapter<CharSequence> adapterPage, adapterEnv, adapterSite;
    String url = "",page="",env="", site="", link="", apiUrl="", apiDomain="";
    String path = "path=test&", section = "cat=homepage&", params = "adunit=topbanner&CACHESKIP=1", apiResponse="";
    EditText urlView;
    boolean isApiCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerPage =  (Spinner) findViewById(R.id.testPageSpinner);
        adapterPage = ArrayAdapter.createFromResource(this, R.array.page_spinner_arr, android.R.layout.simple_spinner_item);
        adapterPage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPage.setAdapter(adapterPage);
        spinnerPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                url = getString(R.string.client_domain);
                page = parent.getItemAtPosition(position).toString();
                if (page.equalsIgnoreCase("lazy load")) {
                    page = getString(R.string.lazy_load_page);

                }
                if (page.equalsIgnoreCase("Single Ad")) {
                    page = getString(R.string.single_ad_page);
                }
                if (page.equalsIgnoreCase("Multi Ads")) {
                    page = getString(R.string.multi_ad_page);
                }
                if (page.equalsIgnoreCase("Other")) {
                    page = "";//add a field to type the page param
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinnerEnv =  (Spinner) findViewById(R.id.env_spinner);
        adapterEnv = ArrayAdapter.createFromResource(this, R.array.env_spinner_arr, android.R.layout.simple_spinner_item);
        adapterEnv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnv.setAdapter(adapterEnv);
        spinnerEnv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                env = parent.getItemAtPosition(position).toString();
                if (env.equalsIgnoreCase("stage")) {
                    env = "env=" + getString(R.string.o_stage) + "-mps.nbcuni.com&";
                    apiDomain = "http://"+getString(R.string.o_stage) + "-mps.nbcuni.com";
                }
                if (env.equalsIgnoreCase("develop")) {
                    env = "env=" + getString(R.string.develop) + "-mps.nbcuni.com&";
                    apiDomain = "http://" + getString(R.string.develop) + "-mps.nbcuni.com";
                }
                if (env.equalsIgnoreCase("master")) {
                    env = "env=" + getString(R.string.master) + "-mps.nbcuni.com&";
                    apiDomain = "http://"+ getString(R.string.master) + "-mps.nbcuni.com";
                }
                if (env.equalsIgnoreCase("Other")) {
                    env = "";//add a field to type the page param

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSite =  (Spinner) findViewById(R.id.site_spinner);
        adapterSite = ArrayAdapter.createFromResource(this, R.array.site_spinner_arr, android.R.layout.simple_spinner_item);
        adapterSite.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSite.setAdapter(adapterSite);
        spinnerSite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                site = parent.getItemAtPosition(position).toString().toLowerCase();
                //env=o-stage-mps.nbcuni.com&
                if (!site.equalsIgnoreCase("other")) {
                    site = "site=" + site + "&";
                } else {
                    site = "";//ajouter un prompt avec text edit pour entrer nom du site
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buildButton = (ImageButton) findViewById(R.id.buildUrl);
        buildButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //block below apres to grab params from text edit views. harcoding les params pour le moment

/*if(findViewById(R.id.path_var).toString().isEmpty()){
                    Toast.makeText(getBaseContext(), " parth is missing!", Toast.LENGTH_LONG).show();
                }else{
                path = findViewById(R.id.path_var).toString()+"&";
                    url=url + path;
                }

                if(findViewById(R.id.section_var).toString().isEmpty()){
                    section = "";
                }else{
                    section = findViewById(R.id.section_var).toString()+"&";
                    url = url + section;
                }
                if (findViewById(R.id.params_var).toString().isEmpty()) {
                    params = "";
                }else{                    {
                        params = findViewById(R.id.params_var).toString();
                    url = url + params;
                    }
                }*/

                link = url + page + env + site + path + section + params;
                urlView = (EditText) findViewById(R.id.url);
                urlView.setText(link);
            }
        });


       apiButton = (ImageButton) findViewById(R.id.buildApi);
        apiButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                link = apiDomain +getString(R.string.api_base_url)+"&"+ site + path + section + params;
                urlView = (EditText) findViewById(R.id.url);
                urlView.setText(link);
                setToApi(true);
            }
        });
        mpsAdmin= (ImageButton) findViewById(R.id.mpsAdmin);
        mpsAdmin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                link = apiDomain;
                urlView = (EditText) findViewById(R.id.url);
                urlView.setText(link);
                setToApi(false);
            }
        });

        clearButton = (ImageButton) findViewById(R.id.clearUrl);
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View b) {

                link = "";
                urlView.setText(link);

            }
        });





        openButton = (ImageButton) findViewById(R.id.openPage);
        openButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText urlView2 = (EditText) findViewById(R.id.url);
                link = urlView2.getText().toString();
                if (link.isEmpty()){
                    link = apiDomain;
                    Toast.makeText(getBaseContext(), "URL Missing! >>> In MPS", Toast.LENGTH_LONG).show();
                }

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                //if (!isApiCall){
                startActivity(browserIntent);}
               // else{
                    //
                 //   apiResponse = makeApiCall(link);
                //a ameliorer pour consommer l'api de MPS
                //    }
           // }
        });

    }
    public final void setToApi(boolean booleanVal){
        isApiCall = booleanVal;
    }

    public final void reset(){
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
//re activer une fois decide de consommer l'api
    public static String makeApiCall(String endPoint) {
        String output2 = "";
        try {

            URL url = new URL(endPoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                output2="Error!";
            }

            InputStreamReader abc = new InputStreamReader(conn.getInputStream());

            String output = IOUtils.toString(abc);
            output2 = output;
            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return output2;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bienvenue, menu);
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
}
