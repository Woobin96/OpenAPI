package com.wooeun18.openapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> arrayList= new ArrayList<String>();
    ListView listView;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        aaa.add("a");
//        aaa.add("b");
//        aaa.add("c");

        listView= findViewById(R.id.listView);
        adapter=new ArrayAdapter(this, R.layout.listview_item,arrayList);
        listView.setAdapter(adapter);
    }

    public void click(View view) {
        Thread t= new Thread(){
            @Override
            public void run() {
                Date date= new Date();
                date.setTime( date.getTime() );

                SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
                String dataStr= sdf.format(date);

                //Api 인증키
                String apikey= "wkxxynonuE07XQTJp9mPQ0z3iH9ga5jwVB%2F5QDFGxtu0SvdS5WJv2F%2Fb7hUVCyCbo9r42%2Fyeo2PPqKI1mwKUvw%3D%3D";

                String address= "http://apis.data.go.kr/1360000/AsosDalyInfoService/getWthrDataList?"
                        +"serviceKey="+apikey
                        +"&dataCd=ASOS"
                        +"&dateCd=DAY"
                        +"&startDt=20201201"
                        +"&endDt=20201229"
                        +"&stnIds=108";

                try {
                    URL url= new URL(address);
                    InputStream is= url.openStream();
                    InputStreamReader isr= new InputStreamReader(is);

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp= factory.newPullParser();
                    xpp.setInput(isr);

                    int eventType = xpp.getEventType();
                    StringBuffer buffer = null;

                    while (eventType!=XmlPullParser.END_DOCUMENT){

                        switch (eventType){

                            case XmlPullParser.START_DOCUMENT :
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                                break;

                            case XmlPullParser.START_TAG :
                                String tagname= xpp.getName();
                                if(tagname.equals("item")){
                                    buffer=new StringBuffer();
                                }else if(tagname.equals("stnNm")){
                                    buffer.append("지점 : ");
                                    xpp.next();
                                    buffer.append(xpp.getText()+"\n");

                                }else if(tagname.equals("tm")){
                                    buffer.append("시간 : ");
                                    xpp.next();
                                    buffer.append(xpp.getText()+"\n");

                                }else if(tagname.equals("avgTa")){
                                    buffer.append("평균 기온 : ");
                                    xpp.next();
                                    buffer.append(xpp.getText()+"\n");

                                }else if(tagname.equals("minTa")) {
                                    buffer.append("최저 기온 : ");
                                    xpp.next();
                                    buffer.append(xpp.getText());
                                }
                                break;

                            case XmlPullParser.TEXT :
                                break;

                            case XmlPullParser.END_TAG :
                                String tagname2= xpp.getName();
                                if(tagname2.equals("item")){
                                    arrayList.add(buffer.toString());

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                                break;
                        }//switch
                        eventType= xpp.next();
                    }//while



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }//catch
                catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }//run
        };//T
        t.start();
    }//c
}//Main