package android.tx.com.dgiot_amis;

import android.tx.com.dgiot_amis.bean.ReceiveMsgBean;
import android.util.Log;
import android.tx.com.dgiot_amis.WebActivity;

import com.alibaba.fastjson.JSONObject;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


public class AndroidWebServer extends NanoHTTPD {


    public AndroidWebServer(int port) {
        super(port);
    }

    private WebActivity webActivity = new WebActivity();

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String PreflightRequest;
        Log.d("hallow", "session=" + session.getHeaders().toString());
        if (isPreflightRequest(session)) {
//              ���������CORS��Ӧ�������HTTP����֧�ֵ�METHOD��HEADERS������Դ
            return responseCORS(session);
        }
        if (session.getUri().equals("/photo")) {
//              ���������CORS��Ӧ�������HTTP����֧�ֵ�METHOD��HEADERS������Դ
            return photo(session);
        }

        if (session.getUri().equals("/scancode")) {
//              ���������CORS��Ӧ�������HTTP����֧�ֵ�METHOD��HEADERS������Դ
            return scancode(session);
        }

        ;
        //���Կ�����ʲô����ʽ
        Method method = session.getMethod();
        Response resp = newFixedLengthResponse(session.getUri());

        return resp;

    }





    public Response photo(IHTTPSession session) {

        JSONObject map = new JSONObject();
        JSONObject data = new JSONObject();

        String Json = "{'code':200,'datetimes':'','deviceid':'1db7727cc6','instruct':'photo','msg':'success','objectId':'1db7727cc6','username':'username'}";
        ReceiveMsgBean msgBean = JSONObject.parseObject(Json, ReceiveMsgBean.class);
        Log.d("hallow", "Json=" + Json);
        Log.d("hallow", "msgBean=" + msgBean);
        EventBus.getDefault().post(msgBean);
        String text=webActivity.geturl();

        data.put("photo", text);
        map.put("status", 0);
        map.put("msg", "");
        map.put("data", data);
        Response resp = newFixedLengthResponse(map.toJSONString());
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, PROPPATCH, MKCOL, MOVE, COPY, LOCK, UNLOCK");
        Log.d("hallow", "map=" + text);

        String origin=session.getHeaders().get("origin");
        resp.addHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Headers", "*");
        resp.addHeader("Access-Control-Max-Age", "3600");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        return resp;
    }

    public  String returntext(){
        String text=webActivity.geturl();
        if (text.equals("")){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            returntext();
        }
        return text;
    }




    public Response scancode(IHTTPSession session) {

        JSONObject map = new JSONObject();
        JSONObject data = new JSONObject();
        String Json = "{'code':200,'datetimes':'','deviceid':'1db7727cc6','instruct':'scancode','msg':'success','objectId':'1db7727cc6','username':'username'}";
        ReceiveMsgBean msgBean = JSONObject.parseObject(Json, ReceiveMsgBean.class);
        Log.d("hallow", "Json=" + Json);
        Log.d("hallow", "msgBean=" + msgBean);
        EventBus.getDefault().post(msgBean);
        String text=webActivity.geturl();
        data.put("scancode", text);
//        data.put("text","asd");
        map.put("status", 0);
        map.put("msg", "");
        map.put("data", data);
        Response resp = newFixedLengthResponse(map.toJSONString());
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, PROPPATCH, MKCOL, MOVE, COPY, LOCK, UNLOCK");
        Log.d("hallow", "map=" + map.toJSONString());


//        String Origin = O.substring(0,O.length()-2);
//        resp.addHeader("Access-Control-Allow-Origin",Origin );
        String origin=session.getHeaders().get("origin");
        resp.addHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Headers", "*");
        resp.addHeader("Access-Control-Max-Age", "3600");
        resp.addHeader("Access-Control-Allow-Credentials", "true");

        return resp;
    }

    /**
     * �ж��Ƿ�ΪCORS Ԥ����������(Preflight)
     *
     * @param session
     * @return
     */
    private static boolean isPreflightRequest(IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        return Method.OPTIONS.equals(session.getMethod())
                && headers.containsKey("origin")
                && headers.containsKey("access-control-request-method")
                && headers.containsKey("access-control-request-headers");
//    return true;
    }

    /**
     * ����Ӧ�������CORS��ͷ����
     *
     * @param session
     * @return
     */
    private Response responseCORS(IHTTPSession session) {
        Response resp = wrapResponse(session, newFixedLengthResponse(""));
        Map<String, String> headers = session.getHeaders();
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS, TRACE, CONNECT, PATCH, PROPFIND, PROPPATCH, MKCOL, MOVE, COPY, LOCK, UNLOCK");
//        resp.addHeader("Access-Control-Max-Age", "86400");
//        resp.addHeader("Access-Control-Allow-Origin", headers.getOrDefault("origin","*"));
//        resp.addHeader("Access-Control-Allow-Origin", "*");
        String origin=session.getHeaders().get("origin");
        resp.addHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Headers", "email,Connection,author,access-control-max-age,access-control-allow-credentials,access-control-allow-methods,access-control-allow-headers,access-control-allow-origin,platform,$$comments,hagan-token,departmenttoken,token,Content-Type,X-Requested-With,Origin, sessionToken, X-Requested-With, Content-Type, Accept,WG-App-Version, WG-Device-Id, WG-Network-Type, WG-Vendor, WG-OS-Type, WG-OS-Version, WG-Device-Model, WG-CPU, WG-Sid, WG-App-Id, WG-Token");
//        resp.addHeader("Access-Control-Allow-Headers", "*");
        resp.addHeader("Access-Control-Max-Age", "3600");
        resp.addHeader("Access-Control-Allow-Credentials", "true");

        return resp;
    }

    private Response responsS(IHTTPSession session) {
        Response resp = wrapResponse(session, newFixedLengthResponse(""));
        Map<String, String> headers = session.getHeaders();
        String msg = "<html><body><h1>asdfghjk</h1>\n";
        return newFixedLengthResponse(msg);
    }

    /**
     * ��װ��Ӧ��
     *
     * @param session http����
     * @param resp    ��Ӧ��
     * @return resp
     */
    private Response wrapResponse(IHTTPSession session, Response resp) {
        if (null != resp) {
            Map<String, String> headers = session.getHeaders();

            // �������ͷ�а���'Origin',����Ӧͷ��'Access-Control-Allow-Origin'ʹ�ô�ֵ����Ϊ'*'
            // nanohttd����������ͷ������ǿ��תΪ��Сд
//            String origin = MoreObjects.firstNonNull(headers.get("origin", "*");

//            String  requestHeaders = headers.get("access-control-request-headers");
//            if(requestHeaders != null){
//
//            }
        }
        return resp;
    }
}
