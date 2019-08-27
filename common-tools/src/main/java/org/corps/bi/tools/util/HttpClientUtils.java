package org.corps.bi.tools.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicExpiresHandler;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

public final class HttpClientUtils {

    private static final int STATUSCODE_200 = 200;
    private static final int TIME = 30;
    private static final int THOUSAND = 1000;
    
    public static final String DEFAULT_ENCODE="UTF-8";
    
    public enum RequestMethod{
    	GET,POST;
    }
    
    private static final String[] DEFAULT_DATE_PATTERNS = new String[] {
        DateUtils.PATTERN_RFC1123,
        DateUtils.PATTERN_RFC1036,
        DateUtils.PATTERN_ASCTIME,
        "EEE, dd-MMM-yyyy HH:mm:ss z",
        "EEE, dd-MMM-yyyy HH-mm-ss z",
        "EEE, dd MMM yy HH:mm:ss z",
        "EEE dd-MMM-yyyy HH:mm:ss z",
        "EEE dd MMM yyyy HH:mm:ss z",
        "EEE dd-MMM-yyyy HH-mm-ss z",
        "EEE dd-MMM-yy HH:mm:ss z",
        "EEE dd MMM yy HH:mm:ss z",
        "EEE,dd-MMM-yy HH:mm:ss z",
        "EEE,dd-MMM-yyyy HH:mm:ss z",
        "EEE, dd-MM-yyyy HH:mm:ss z",
        "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz",
        "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z",
        "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z",
        "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z",
        "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z",
        "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z",
        "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z",
        "E, dd-MMM-yyyy HH:mm:ss zzz", 
        "E, d-MMM-yyyy HH:mm:ss zzz",
        "EEEE, dd-MMM-yy HH:mm:ss zzz" 
    };
    
    private static ThreadLocal<Boolean> IS_DATE_COOKIE_PARSE=new ThreadLocal<Boolean>();
    
    static{
    	IS_DATE_COOKIE_PARSE.set(true);
    }

    private HttpClientUtils() {
    }
    
    public static void setIsDateCookieParse(boolean value){
    	IS_DATE_COOKIE_PARSE.set(value);
    }
    
    public static boolean isDateCookieParse(){
    	return IS_DATE_COOKIE_PARSE.get();
    }

    static class GzipDecompressingEntity extends HttpEntityWrapper {
        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }

        public InputStream getContent() throws IOException {
            InputStream wrappedin = wrappedEntity.getContent();
            return new GZIPInputStream(wrappedin);
        }

        public long getContentLength() {
            return -1;
        }
    }
    
    static class LenientCookieSpec extends BrowserCompatSpec {
        public LenientCookieSpec() {
            super();
            registerAttribHandler(ClientCookie.EXPIRES_ATTR,
                    new BasicExpiresHandler(DEFAULT_DATE_PATTERNS) {
                        @Override
                        public void parse(SetCookie cookie, String value)
                                throws MalformedCookieException {
                        	if(!isDateCookieParse()){
                        		return ;
                        	}
                            super.parse(cookie, value);
                        }
                    });
        }
    }
    
    public static DefaultHttpClient getHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setUserAgent(params, "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
        //HttpClientParams.setCookiePolicy(params, CookiePolicy.BROWSER_COMPATIBILITY);
        HttpConnectionParams.setConnectionTimeout(params, TIME * THOUSAND);
        HttpConnectionParams.setSoTimeout(params, TIME * THOUSAND);
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }
            }

        });
        httpclient.addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                Header ceheader = entity.getContentEncoding();
                if (ceheader != null) {
                    HeaderElement[] codecs = ceheader.getElements();
                    for (int i = 0; i < codecs.length; i++) {
                        if (codecs[i].getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(new GzipDecompressingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }

        });
        httpclient.getCookieSpecs().register("chinasource",
                new CookieSpecFactory() {
                    public CookieSpec newInstance(HttpParams params) {
                        return new LenientCookieSpec();
                    }
                });
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, "chinasource");
        return httpclient;
    }
    
	public static HttpClient getHttpsClient() {
		try {
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setUserAgent(params, "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
			HttpClientParams.setCookiePolicy(params, CookiePolicy.BROWSER_COMPATIBILITY);
			HttpConnectionParams.setConnectionTimeout(params, TIME * THOUSAND);
			HttpConnectionParams.setSoTimeout(params, TIME * THOUSAND);
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", 443, ssf));
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(registry);
			DefaultHttpClient httpsClient = new DefaultHttpClient(ccm, params);
			httpsClient.addRequestInterceptor(new HttpRequestInterceptor() {
				public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
					if (!request.containsHeader("Accept-Encoding")) {
						request.addHeader("Accept-Encoding", "gzip");
					}
				}

			});
			httpsClient.addResponseInterceptor(new HttpResponseInterceptor() {
				public void process(final HttpResponse response, final HttpContext context) throws HttpException, IOException {
					HttpEntity entity = response.getEntity();
					Header ceheader = entity.getContentEncoding();
					if (ceheader != null) {
						HeaderElement[] codecs = ceheader.getElements();
						for (int i = 0; i < codecs.length; i++) {
							if (codecs[i].getName().equalsIgnoreCase("gzip")) {
								response.setEntity(new GzipDecompressingEntity(response.getEntity()));
								return;
							}
						}
					}
				}

			});
			return httpsClient;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

    public static String executeRequest(RequestMethod reqeustMethod,String url, List<NameValuePair> parameters,String encode) throws Exception{
    	HttpClient httpclient = HttpClientUtils.getHttpClient();
    	if(url.contains("https")){
    		httpclient=HttpClientUtils.getHttpsClient();
    	}else{
    		httpclient = HttpClientUtils.getHttpClient();
    	}
    	
    	if(RequestMethod.GET.ordinal()==reqeustMethod.ordinal()){
    		return executeGetRequest(httpclient, url,parameters, encode);
    	}else{
    		return executePostRequest(httpclient, url, parameters, encode);
    	}
    }
    
    public static String executeRequest(HttpClient httpclient, RequestMethod reqeustMethod,String url, List<NameValuePair> parameters,String encode) throws Exception{
    	if(RequestMethod.GET.ordinal()==reqeustMethod.ordinal()){
    		return executeGetRequest(httpclient, url,parameters, encode);
    	}else{
    		return executePostRequest(httpclient, url, parameters, encode);
    	}
    }

    public static String executeGetRequest(String url,String encode) throws Exception {
        return executeRequest(RequestMethod.GET, url, null, encode);
    }
    
    public static String executePostRequest(String url,Map<String,String> params,String encode) throws Exception {
    	List<NameValuePair> parameters =null;
    	if(params!=null&&!params.isEmpty()){
    		parameters=new ArrayList<NameValuePair>();
    		for(Entry<String,String> entry:params.entrySet()){
    			parameters.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
    		}
    	}
        return executeRequest(RequestMethod.POST, url, parameters, encode);
    }
    
    public static String executeGetRequest(HttpClient httpclient, String url, List<NameValuePair> parameters,String encode) throws Exception {
        InputStream input = null;
        
        if(parameters!=null){
        	url=concatUrlSerialParameters(url, parameters, true);
        }

        HttpGet get = new HttpGet(url);
        HttpResponse res = httpclient.execute(get);
        StatusLine status = res.getStatusLine();
        if (status.getStatusCode() != STATUSCODE_200) {
            throw new RuntimeException("url:"+url+" errorStatusCode:"+status.getStatusCode());
        }
        if (res.getEntity() == null) {
            return null;
        }
        input = res.getEntity().getContent();
        
        return IOUtils.convertStreamToString(input, encode);
    }
    
    public static String executePostRequest(HttpClient httpclient, String url, List<NameValuePair> parameters,String encode) throws Exception {
        InputStream input = null;
       
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, encode);
        HttpPost post=new HttpPost(url);
        post.setEntity(entity);
        HttpResponse res = httpclient.execute(post);
        StatusLine status = res.getStatusLine();
        if (status.getStatusCode() != STATUSCODE_200) {
            throw new RuntimeException("request error status:"+status.getStatusCode());
        }
        if (res.getEntity() == null) {
            return null;
        }
        input = res.getEntity().getContent();
        
        return IOUtils.convertStreamToString(input, encode);
    }
    
    

	public static String concatUrlSerialParameters(String url,List<NameValuePair> parameters, boolean onlySerialValue) throws Exception{
		if(url==null){
			return null;
		}
		if(url.indexOf("?")<0){
			url+="?";
		}
		url+=concatSerialParameters(parameters, onlySerialValue);
		return url;
	}
    
    public static String concatSerialParameters(List<NameValuePair> parameters, boolean onlySerialValue) throws UnsupportedEncodingException {
        String str = "";
        for (int i = 0; i < parameters.size(); i++) {
            NameValuePair item = parameters.get(i);
            if (onlySerialValue) {
                str += item.getName() + "=" + URLEncoder.encode(item.getValue(), HttpClientUtils.DEFAULT_ENCODE);

            } else {
                str += item.getName() + "=" + item.getValue();
            }
            if (i < parameters.size() - 1) {
                str += "&";
            }
        }
        if (!onlySerialValue) {
            str = URLEncoder.encode(str, HttpClientUtils.DEFAULT_ENCODE);
        }
        return str;
    }
    
    public static String concatSerialParams(Map<String,String> params, boolean onlySerialValue) throws UnsupportedEncodingException {
    	StringBuilder sb=new StringBuilder();
    	int index=0;
    	int psize=params.size();
    	for(String key:params.keySet()){
    		String value=params.get(key);
    		sb.append(key);
    		sb.append("=");
    		if(onlySerialValue){
    			sb.append(URLEncoder.encode(value, HttpClientUtils.DEFAULT_ENCODE));
    		}else{
    			sb.append(value);
    		}
    		if (index++ < psize - 1) {
    			sb.append("&");
            }
    	}
    	if (!onlySerialValue) {
    		return URLEncoder.encode(sb.toString(), HttpClientUtils.DEFAULT_ENCODE);
        }
    	return sb.toString();
    }
    public static String concatUrlSerialParams(String url,Map<String,String> params, boolean onlySerialValue) throws UnsupportedEncodingException {
    	String ret=null;
    	if(url.indexOf("?")<0){
    		ret=url+"?"+concatSerialParams(params,onlySerialValue);
    	}else{
    		ret=url+"&"+concatSerialParams(params,onlySerialValue);
    	}
    	return ret;
    }
    
    public static int requestResponseStatusCode(String url){
    	try {
			HttpClient httpclient = HttpClientUtils.getHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse res = httpclient.execute(get);
			StatusLine status = res.getStatusLine();
			return status.getStatusCode();
		}catch (Exception e) {
			return -1;
		}
    }
}
