package com.youdy.mo.mochart.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.youdy.mo.mochart.utils.cookie.CookieJarImpl;
import com.youdy.mo.mochart.utils.cookie.PersistentCookieStore;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Integer.parseInt;

/**
 * Created by mo on 2017/6/22.
 */

public class CSUConnect {
    Context mContext;
   // private PersistentCookieStore persistentCookieStore;
    private OkHttpClient okHttpClient;
    private String cookie1;
    private Handler mHandler;

    private static final String loginUrl = "http://csujwc.its.csu.edu.cn/Logon.do?method=logon";
    private static final String randomCodeUrl = "http://csujwc.its.csu.edu.cn/verifycode.servlet";
    private static final String downloadUrl = "http://csujwc.its.csu.edu.cn";
    private static final String encodedUrl = "http://csujwc.its.csu.edu.cn/Logon.do?method=logon&flag=sess";
    private static final String referer = "http://csujwc.its.csu.edu.cn/jsxsd/xk/LoginToXk?ticket=201400003489&methodlogin=jwxtlogin";
    private static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    public CSUConnect(Context mContext,Handler mHandler){
        //try
        this.mContext = mContext;
        this.mHandler = mHandler;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        PersistentCookieStore persistentCookieStore = new PersistentCookieStore(mContext.getApplicationContext());
        CookieJarImpl cookieJarImpl = new CookieJarImpl(persistentCookieStore);
        builder.cookieJar(cookieJarImpl);
        okHttpClient = builder.build();
    }

    public void getNetRandomCode(){
        Request request = new Request.Builder().url(randomCodeUrl+"?t="+ Math.random()).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is  = response.body().byteStream();
                Bitmap b =  BitmapFactory.decodeStream(is);

                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0).substring(0, cookies.get(0).indexOf(";"));
                cookie1 = cookies.get(1).substring(0,  cookies.get(1).indexOf(";")) + "; " + session;

                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = b;
                mHandler.sendMessage(msg);
                Log.d("info_cookies", "onResponse-size: " + cookies );
                Log.d("info_cookies", "cookie: " + cookie1 );
            }
        });
    }

    public void login(final String userName, final String password ,final String randomCode){
        /*
        var scode=dataStr.split("#")[0];
        var sxh=dataStr.split("#")[1];
        var code=document.getElementById("userAccount").value+"%%%"+document.getElementById("userPassword").value;
        var encoded="";
        for(var i=0;i<code.length;i++){
        if(i<20){
            encoded=encoded+code.substring(i,i+1)+scode.substring(0,parseInt(sxh.substring(i,i+1)));
            scode = scode.substring(parseInt(sxh.substring(i,i+1)),scode.length);
        }else{
            encoded=encoded+code.substring(i,code.length);
            i=code.length;
        }
    }				document.getElementById("encoded").value=encoded;
				document.getElementById("frm").submit();

*/
        Headers headers = new Headers.Builder().add("Cookie",cookie1).build();
        Request request = new Request.Builder().headers(headers).url(encodedUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String dataStr  = response.body().string();
                String encoded = dealEncoded(userName,password,dataStr);
                postData(encoded,randomCode);
                Log.d("getEncoded",encoded);
            }
        });
    }

    private String dealEncoded(String userName,String password ,String dataStr){
        String scode = dataStr.split("#")[0];
        String sxh = dataStr.split("#")[1];
        String code = userName + "%%%" + password;
        Log.d("getdataStr",dataStr);
        String encoded = "";
        for(int i=0; i<code.length() ;i++){
            if(i<20){
                encoded = encoded + code.substring(i,i+1)+scode.substring(0,parseInt(sxh.substring(i,i+1)));
                scode = scode.substring(parseInt(sxh.substring(i,i+1)),scode.length());
            }else{
                encoded = encoded + code.substring(i,code.length());
                i=code.length();
            }
        }
        return encoded;
    }

    private void postData(String encoded, String randomCode){
        /*view:0
         useDogCode:
         encoded:38NY9P0x1atI1642480W5615F4jbO%1QM%31%F8I0q28214TCG6rg.d47z7z1q
         RANDOMCODE:xmmm*/
        Headers headers = new Headers.Builder()
                .add("Host","csujwc.its.csu.edu.cn")
                .add("Cookie",cookie1)
                .add("Origin","http://csujwc.its.csu.edu.cn")
                .add("Referer",referer)
                .add("Upgrade-Insecure-Requests","1")
                .add("User-Agent",userAgent)
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("view", "0")
                .add("useDogCode","")
                .add("encoded",encoded)
                .add("RANDOMCODE",randomCode)
                .build();
        Request request = new Request.Builder().headers(headers).post(formBody).url(loginUrl).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Document document = Jsoup.parse(response.body().string());
                Elements elements = document.select("font[color]");
                Message msg = mHandler.obtainMessage();
                if(elements.first() == null){
                    msg.what = 3;
                    mHandler.sendMessage(msg);
                    downloadChart();
                    Log.d("postData succeed", "good job");
                }else {
                    Log.d("postData fail", "not good job");
                    Element e = elements.first();
                    String why = e.text();
                    msg.what = 2;
                    msg.obj = why;
                    mHandler.sendMessage(msg);
                    Log.d("result ", "onResponse: " + e.text());
                }
//                Log.d("result ", "onResponse: " + response.body().string());
            }
        });
    }
 //   td width="123" height="28" align="center"  view-source:http://csujwc.its.csu.edu.cn/jsxsd/xskb/xskb_list.do?Ves632DSdyV=NEW_XSD_WDKB
    private void downloadChart() {
        Headers headers = new Headers.Builder()
                .add("Cookie",cookie1)
                .add("Referer",referer)
                .add("User-Agent",userAgent)
                .build();
        int termNum,first,second;
        if(DateUtils.getMonth()>=2 || DateUtils.getMonth()<=7) {
            termNum = 2;
            first = DateUtils.getYear()-1;
            second = DateUtils.getYear();
        } else{
            termNum = 1;
            if(DateUtils.getMonth() >=8) {
                first = DateUtils.getYear();
                second = DateUtils.getYear() + 1;
            }else{
                first = DateUtils.getYear()-1;
                second = DateUtils.getYear();
            }
        }
        String xq = first+"-"+second+"-" + termNum;
        Request request = new Request.Builder().headers(headers).url(downloadUrl+"/jsxsd/xskb/xskb_print.do?xnxq01id="+xq+"&zc=").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                FileOutputStream fos = mContext.openFileOutput("network.xls",Context.MODE_PRIVATE);
                byte[] b = new byte[1024];
                int len;
                while((len = is.read(b)) != -1){
                    fos.write(b,0,len);
                }
                is.close();
                fos.close();
                Message msg = mHandler.obtainMessage();
                msg.what = 4;
                mHandler.sendMessage(msg);

                //创建文件夹 MyDownLoad，在存储卡下
      /*            String dirName = Environment.getExternalStorageDirectory() + "/youdy520/";
                File file = new File(dirName);
                //不存在创建
                if (!file.exists()) {
                    file.mkdir();
                }
                //下载后的文件名
                String fileName = dirName + "5.xls";
                File file1 = new File(fileName);
                if (file1.exists()) {
                    file1.delete();
                }
                //创建字节流
                byte[] bs = new byte[1024];
                int len;
                OutputStream os = new FileOutputStream(fileName);
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                is.close();
                os.close();
*/
//                Log.d("down",response.body().string());
            }
        });
    }

    private void getAttendDay(){
        /*view:0
         useDogCode:
         encoded:38NY9P0x1atI1642480W5615F4jbO%1QM%31%F8I0q28214TCG6rg.d47z7z1q
         RANDOMCODE:xmmm*/
        Headers headers = new Headers.Builder()
                .add("Cookie",cookie1)
                .add("User-Agent",userAgent)
                .build();

        Request request = new Request.Builder().headers(headers).url("http://csujwc.its.csu.edu.cn/jsxsd/xskb/xskb_list.do?Ves632DSdyV=NEW_XSD_WDKB").build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}
            @Override
            public void onResponse(Call call, Response response) throws IOException {;

            }
        });
    }
    //   td width="123" height="28" align="center"  view-source:http://csujwc.its.csu.edu.cn/jsxsd/xskb/xskb_list.do?Ves632DSdyV=NEW_XSD_WDKB
}
