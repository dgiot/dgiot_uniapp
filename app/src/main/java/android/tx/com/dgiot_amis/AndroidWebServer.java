package android.tx.com.dgiot_amis;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class AndroidWebServer extends NanoHTTPD {
    public AndroidWebServer(int port) {
        super(port);
    }

    public AndroidWebServer(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        if(isPreflightRequest(session)){
            // ���������CORS��Ӧ�������HTTP����֧�ֵ�METHOD��HEADERS������Դ
            return responseCORS(session);
        }
        //���Կ�����ʲô����ʽ
        Method method = session.getMethod();
        System.out.println(method);
        try {
            /*
             * ����post��������Ҫ�ȵ���parseBody()������
             * ֱ�Ӵ�һ���򵥵��¹����map������
             */
            session.parseBody(new HashMap());
            Map parms = new HashMap();
            //Ȼ���ٵ���getParams()����
            parms = session.getParms();
            System.out.println(parms);
            //ReceiveMsgBean msgBean = JSONObject.parseObject(message.getPayload(), ReceiveMsgBean.class);
            //EventBus.getDefault().post(msgBean);
            if (parms.get("username") == null) {
                msg += "<form action='?' method='get'>\n";
                msg += "<p>Your name: <input type='text' name='username'></p>\n";
                msg += "</form>\n";
            } else {
                msg += "<p>Hello, " + parms.get("username") + "!</p>";
            }

            //��ȡ����uri
            String uri = session.getUri();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(msg + "</body></html>\n");
    }

    /**
     * �ж��Ƿ�ΪCORS Ԥ����������(Preflight)
     * @param session
     * @return
     */
    private static boolean isPreflightRequest(IHTTPSession session) {
        Map<String, String> headers = session.getHeaders();
        return Method.OPTIONS.equals(session.getMethod())
                && headers.containsKey("origin")
                && headers.containsKey("access-control-request-method")
                && headers.containsKey("access-control-request-headers");
    }
    /**
     * ����Ӧ�������CORS��ͷ����
     * @param session
     * @return
     */
    private Response responseCORS(IHTTPSession session) {
        Response resp = wrapResponse(session,newFixedLengthResponse(""));
        Map<String, String> headers = session.getHeaders();
        resp.addHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS");

        String requestHeaders = headers.get("access-control-request-headers");
        //String allowHeaders = MoreObjects.firstNonNull(requestHeaders, "Content-Type");
        //resp.addHeader("Access-Control-Allow-Headers", allowHeaders);
        //resp.addHeader("Access-Control-Max-Age", "86400");
        resp.addHeader("Access-Control-Max-Age", "0");
        return resp;
    }
    /**
     * ��װ��Ӧ��
     * @param session http����
     * @param resp ��Ӧ��
     * @return resp
     */
    private Response wrapResponse(IHTTPSession session,Response resp) {
        if(null != resp){
            Map<String, String> headers = session.getHeaders();
            resp.addHeader("Access-Control-Allow-Credentials", "true");
            // �������ͷ�а���'Origin',����Ӧͷ��'Access-Control-Allow-Origin'ʹ�ô�ֵ����Ϊ'*'
            // nanohttd����������ͷ������ǿ��תΪ��Сд
            //String origin = MoreObjects.firstNonNull(headers.get("origin", "*");
           // resp.addHeader("Access-Control-Allow-Origin", origin);
            String  requestHeaders = headers.get("access-control-request-headers");
            if(requestHeaders != null){
                resp.addHeader("Access-Control-Allow-Headers", requestHeaders);
            }
        }
        return resp;
    }
}