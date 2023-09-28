package com.bcsd.common.utils.http;


import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName HttpUtilsNew
 * @Description: TODO
 * @Author zhaofei
 * @Date 2023/9/26
 * @Version V1.0
 **/
@Slf4j
public class HttpUtilsNew {

    private HttpUtilsNew() {
    }

    private static void configTimeout(HttpClient httpClient) {
        HttpConnectionManagerParams params = httpClient.getHttpConnectionManager().getParams();
        params.setConnectionTimeout(20000);
        params.setSoTimeout(20000);
    }

    public static String post(String url, String reqParam) throws Exception {

        HttpClient httpClient = new HttpClient();

        // 请求url
        PostMethod method = new PostMethod(url);

        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

        RequestEntity entity = new StringRequestEntity(reqParam);

        method.setRequestEntity(entity);

        // 执行post请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();

        return respResult;
    }

    public static String jsonPost(String url, String reqParam) throws Exception {

        HttpClient httpClient = new HttpClient();

        // 请求url
        PostMethod method = new PostMethod(url);

        configTimeout(httpClient);

        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

        RequestEntity entity = new StringRequestEntity(reqParam, "application/json", "utf-8");

        method.setRequestEntity(entity);

        // 执行post请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }

    public static String post(String url, String reqParam, String charSet) throws Exception {

        HttpClient httpClient = new HttpClient();

        // 请求url
        PostMethod method = new PostMethod(url);

        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);

        RequestEntity entity = new StringRequestEntity(reqParam);

        method.setRequestEntity(entity);
        // 执行post请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }

    public static String get(String url, String reqParam) throws Exception {

//		reqParam = URLEncoder.encode(reqParam, "UTF-8");

        HttpClient httpClient = new HttpClient();
        GetMethod method;

        if (StringUtil.isNullOrEmpty(reqParam)) {
            // 请求url及参数
            method = new GetMethod(url);
        } else {
            System.out.println("------请求URL：" + url + "?" + reqParam);
            // 请求url及参数
            method = new GetMethod(url + "?" + reqParam);
        }
        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

        // 执行get请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }

    public static String get(String url, String reqParam, String charSet) throws Exception {

//		reqParam = URLEncoder.encode(reqParam, "UTF-8");

        HttpClient httpClient = new HttpClient();
        GetMethod method;

        if (StringUtil.isNullOrEmpty(reqParam)) {
            // 请求url及参数
            method = new GetMethod(url);
        } else {
            System.out.println("请求URL字符串：" + url + "?" + reqParam);
            // 请求url及参数
            method = new GetMethod(url + "?" + reqParam);
        }
        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);

        // 执行get请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }

    /**
     * 添加header "Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"
     */
    public static String postHeader(String url, String reqParam) throws Exception {

        HttpClient httpClient = new HttpClient();

        HttpClientParams params = new HttpClientParams();
        //另外设置http client的重试次数，默认是3次；当前是禁用掉（如果项目量不到，这个默认即可）
        params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
        httpClient.setParams(params);

        // 请求url
        PostMethod method = new PostMethod(url);
        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        RequestEntity entity = new StringRequestEntity(reqParam);

        method.setRequestEntity(entity);


        // 执行post请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }

    @SuppressWarnings({"deprecation", "unused"})
    private static HttpPost postForm(String url, Map<String, String> params) {

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<>();

        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }

    /**
     * post请求<设置请求头>
     *
     * @param url           请求地址
     * @param reqParamName  请求参数名称
     * @param reqParamValue 请求参数值
     * @param charSet       编码格式
     * @author yew
     * date 2017年7月26日 下午2:11:40
     */
    public static String postRequset(String url, String reqParamName, String reqParamValue, String charSet) throws Exception {

        HttpClient httpClient = new HttpClient();

        // 请求url
        PostMethod method = new PostMethod(url);
        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, charSet);
        //设置参数请求头
        method.setParameter(reqParamName, reqParamValue);

        // 执行post请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }

    public static String jsonPostHeader(String url, String reqParam, String accessToken, String version) throws Exception {

        HttpClient httpClient = new HttpClient();

        // 请求url
        PostMethod method = new PostMethod(url);
        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader("accessToken", accessToken);
        method.addRequestHeader("version", version);

        RequestEntity entity = new StringRequestEntity(reqParam, "application/json", "utf-8");

        method.setRequestEntity(entity);

        // 执行post请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }

    public static String jsonPostHeader(String url, String reqParam) throws Exception {

        HttpClient httpClient = new HttpClient();

        // 请求url
        PostMethod method = new PostMethod(url);
        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        RequestEntity entity = new StringRequestEntity(reqParam, "application/xml", "utf-8");

        method.setRequestEntity(entity);

        // 执行post请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }

    /**
     * post key-value形式传参
     *
     * @param url       请求url
     * @param paramsMap 请求的map数据
     * @return
     */
    public static String postFormMap(String url, Map<String, Object> paramsMap) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        //配置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)
                .build();
        HttpPost httpPost = new HttpPost(url);
        //设置超时时间
        httpPost.setConfig(requestConfig);
        //装配post请求参数
        List<NameValuePair> list = new ArrayList<>();
        for (String key : paramsMap.keySet()) {
            list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
        }
        try {
            //将参数进行编码为合适的格式,如将键值对编码为param1=value1&param2=value2
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "utf-8");
            httpPost.setEntity(urlEncodedFormEntity);
            //执行 post请求
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpPost);
            String result = "";
            if (null != closeableHttpResponse) {
                System.out.println(closeableHttpResponse.getStatusLine().getStatusCode());
                if (closeableHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = closeableHttpResponse.getEntity();
                    result = EntityUtils.toString(httpEntity);
                } else {
                    result = "Error Response" + closeableHttpResponse.getStatusLine().getStatusCode();
                }
            }
            log.info("请求{}返回：{}", url, result);
            return result;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "协议异常";
        } catch (ParseException e) {
            e.printStackTrace();
            return "解析异常";
        } catch (IOException e) {
            e.printStackTrace();
            return "传输异常";
        } finally {
            try {
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param url
     * @param reqParam    请求参数
     * @param headerParam 请求头参数
     * @return
     */

    public static String post(String url, String reqParam, Map<String, Object> headerParam) throws Exception {

        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);

        // 请求url
        PostMethod method = new PostMethod(url);

        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        for (Map.Entry<String, Object> vo : headerParam.entrySet()) {
            method.addRequestHeader(vo.getKey(), vo.getKey());
        }
        RequestEntity entity = new StringRequestEntity(reqParam);

        method.setRequestEntity(entity);

        // 执行post请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();

        return respResult;
    }

    public static String get(String url, Map<String, Object> headerParam) throws Exception {

        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        httpClient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
        GetMethod method;
        method = new GetMethod(url);
        configTimeout(httpClient);
        // 设置请求编码UTF-8
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        method.addRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        for (Map.Entry<String, Object> vo : headerParam.entrySet()) {
            method.addRequestHeader(vo.getKey(), vo.getKey());
        }
        // 执行get请求
        httpClient.executeMethod(method);

        // 请求返回
        String respResult = method.getResponseBodyAsString();

        // 释放连接
        method.releaseConnection();
        log.info("请求{}返回：{}", url, respResult);
        return respResult;
    }


}
