package com.bosssoft.pay.sdk.core.internal.util;

import com.bosssoft.pay.sdk.core.ThirdpayConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Title Web工具类
 * @Description
 * @Author 陈超雷(chenchaoleicn@gmail.com)
 * @Date 2019/01/05
 */
public abstract class WebUtils {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_GET = "GET";
    private static SSLContext ctx = null;
    private static HostnameVerifier verifier = null;
    private static SSLSocketFactory socketFactory = null;
    private static final int SSL_SESSION_TIME_OUT = ThirdpayConstants.DEFAULT_SSL_SESSION_TIME_OUT;
    private static final int SSL_SESSION_CACHE_SIZE = ThirdpayConstants.DEFAULT_SSL_SESSION_CACHE_SIZE;

    public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout) throws IOException {
        String contentType = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = new byte[0];
        if(query != null) {
            content = query.getBytes(charset);
        }
        return doPost(url, contentType, content, connectTimeout, readTimeout);
    }

    public static String doPost(String url, String contentType, byte[] content, int connectTimeout, int readTimeout) throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            Map map;
            try {
                conn = getConnection(new URL(url), METHOD_POST, contentType);
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                throw e;
            }
        } finally {
            if(out != null) {
                out.close();
            }
            if(conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static String doGet(String url, Map<String, String> params, String charset) throws IOException {
        HttpURLConnection conn = null;
        String rsp = null;
        try {
            String contentType = "application/x-www-form-urlencoded;charset=" + charset;
            String query = buildQuery(params, charset);
            try {
                conn = getConnection(buildGetUrl(url, query), METHOD_GET, contentType);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                throw e;
            }
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    private static HttpURLConnection getConnection(URL url, String method, String contentType) throws IOException {
        Object conn = null;
        if("https".equals(url.getProtocol())) {
            HttpsURLConnection connHttps = (HttpsURLConnection)url.openConnection();
            connHttps.setSSLSocketFactory(socketFactory);
            connHttps.setHostnameVerifier(verifier);
            conn = connHttps;
        } else {
            conn = (HttpURLConnection)url.openConnection();
        }

        ((HttpURLConnection)conn).setRequestMethod(method);
        ((HttpURLConnection)conn).setDoInput(true);
        ((HttpURLConnection)conn).setDoOutput(true);
        ((HttpURLConnection)conn).setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        ((HttpURLConnection)conn).setRequestProperty("User-Agent", "aop-sdk-java");
        ((HttpURLConnection)conn).setRequestProperty("Content-Type", contentType);
        ((HttpURLConnection)conn).setRequestProperty(ThirdpayConstants.TRACE_ID, MDC.get(ThirdpayConstants.TRACE_ID));
        return (HttpURLConnection)conn;
    }

    /**
     * 构建基于占位符的url
     * @param strUrl
     * @param params
     * @return
     * @throws IOException
     */
    public static String buildGetUrlForRestPlaceHolder(String strUrl, Map<String, String> params) throws IOException {
        String query = buildQueryForRestPlaceHolder(params);
        URL url = buildGetUrl(strUrl, query);
        return url.toString();
    }

    /**
     * 构建url
     * @param strUrl
     * @param params
     * @param charset
     * @return
     * @throws IOException
     */
    public static String buildGetUrl(String strUrl, Map<String, String> params, String charset) throws IOException {
        String query = buildQuery(params, charset);
        URL url = buildGetUrl(strUrl, query);
        return url.toString();
    }

    /**
     * 构建url
     * @param strUrl
     * @param query
     * @return
     * @throws IOException
     */
    public static URL buildGetUrl(String strUrl, String query) throws IOException {
        URL url = new URL(strUrl);
        if(StringUtils.isEmpty(query)) {
            return url;
        } else {
            if(StringUtils.isEmpty(url.getQuery())) {
                if(strUrl.endsWith("?")) {
                    strUrl = strUrl + query;
                } else {
                    strUrl = strUrl + "?" + query;
                }
            } else if(strUrl.endsWith("&")) {
                strUrl = strUrl + query;
            } else {
                strUrl = strUrl + "&" + query;
            }
            return new URL(strUrl);
        }
    }

    /**
     * 构建query string
     * @param params
     * @param charset
     * @return
     * @throws IOException
     */
    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if(params != null && !params.isEmpty()) {
            StringBuilder query = new StringBuilder();
            Set entries = params.entrySet();
            boolean hasParam = false;
            Iterator it = entries.iterator();

            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                String name = (String)entry.getKey();
                String value = (String)entry.getValue();
                if(StringUtils.isNotEmpty(name) && value != null) {
                    if(hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }
                    query.append(name).append("=").append(URLEncoder.encode(value, charset));
                }
            }
            return query.toString();
        } else {
            return null;
        }
    }

    /**
     * 构建基于占位符的query string
     * @param params
     * @return
     * @throws IOException
     */
    public static String buildQueryForRestPlaceHolder(Map<String, String> params) {
        if(params != null && !params.isEmpty()) {
            StringBuilder query = new StringBuilder();
            Set entries = params.entrySet();
            boolean hasParam = false;
            Iterator it = entries.iterator();

            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                String name = (String)entry.getKey();
                String value = (String)entry.getValue();
                if(StringUtils.isNotEmpty(name)) {
                    if(hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }
                    query.append(name).append("=").append("{" + value + "}");
                }
            }
            return query.toString();
        } else {
            return null;
        }
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if(es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if(StringUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();
            char[] chars = new char[256];
            boolean count = false;

            int count1;
            while((count1 = reader.read(chars)) > 0) {
                writer.write(chars, 0, count1);
            }

            String result = writer.toString();
            return result;
        } finally {
            if(stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String contentType) {
        String charset = DEFAULT_CHARSET;
        if(StringUtils.isNotEmpty(contentType)) {
            String[] params = contentType.split(";");
            for(int i = 0; i < params.length; i++) {
                String param = params[i];
                param = param.trim();
                if(param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if(pair.length == 2 && !StringUtils.isEmpty(pair[1])) {
                        charset = pair[1].trim();
                    }
                    break;
                }
            }
        }
        return charset;
    }

    public static String buildForm(String baseUrl, Map<String, String> parameters, String charset) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + charset + "\"/></head><body>\n");
        sb.append("<form name=\"thirdpay_form\" method=\"post\" action=\"");
        sb.append(baseUrl);
        sb.append("\">\n");
        sb.append(buildHiddenFields(parameters));
        sb.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\">\n");
        sb.append("</form>\n");
        sb.append("</body>\n");
        sb.append("<script type=\"text/javascript\">document.all.thirdpay_form.submit();</script>\n");
        sb.append("</html>");
        String form = sb.toString();
        return form;
    }

    private static String buildHiddenFields(Map<String, String> parameters) {
        if(parameters != null && !parameters.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            Set keys = parameters.keySet();
            Iterator result = keys.iterator();

            while(result.hasNext()) {
                String key = (String)result.next();
                String value = (String)parameters.get(key);
                if(key != null && value != null) {
                    sb.append(buildHiddenField(key, value));
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    private static String buildHiddenField(String key, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"hidden\" name=\"");
        sb.append(key);
        sb.append("\" value=\"");
        String v = value.replace("\"", "&quot;");
        sb.append(v).append("\">\n");
        return sb.toString();
    }

    public static String decode(String value, String charset) {
        String result = null;
        if(!StringUtils.isEmpty(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }
        return result;
    }

    public static String encode(String value, String charset) {
        String result = null;
        if(!StringUtils.isEmpty(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        }
        return result;
    }

    public static String buildHtmlContentWithRawFormat(String content) {
        return "<div style='white-space:pre-wrap;'>"+ content +"</div>";
    }

    static {
        try {
            ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            ctx.getClientSessionContext().setSessionTimeout(SSL_SESSION_TIME_OUT);
            ctx.getClientSessionContext().setSessionCacheSize(SSL_SESSION_CACHE_SIZE);
            socketFactory = ctx.getSocketFactory();
        } catch (Exception var1) {
            ;
        }

        verifier = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return false;
            }
        };
    }

    private static class DefaultTrustManager implements X509TrustManager {
        private DefaultTrustManager() {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }
}
