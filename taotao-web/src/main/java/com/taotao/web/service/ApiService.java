package com.taotao.web.service;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.bean.HttpResult;

@Service
public class ApiService {
    
    @Autowired
    private CloseableHttpClient httpClient;
    
    @Autowired
    private RequestConfig requestConfig;
    
    /**
     * 没有参数的doGet
     * @param url 请求地址
     * @return 响应200,返回内容，其它返回null
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doGet(String url) throws ClientProtocolException, IOException{
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
        
        return null;
    }
    
    /**
     * 带有参数的doGet
     * @param url 请求地址
     * @param map 请求参数
     * @return 响应200,返回内容，其它返回null
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doGet(String url,Map<String, Object> map) throws URISyntaxException, ClientProtocolException, IOException{
        // 定义请求的参数
        URIBuilder uriBuilder = new URIBuilder(url);
        
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            uriBuilder.addParameter(entry.getKey(), entry.getValue().toString());
        }
        
      return doGet(uriBuilder.build().toString());
    }
    
    /**
     * 带有参数Post
     * @param url 请求地址
     * @param map 请求参数
     * @return 返回HttpResult包括状态码code,主体 body
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public HttpResult doPost(String url,Map<String, Object> map) throws ClientProtocolException, IOException{

        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        // 设置参数
        List<NameValuePair> parameters = new ArrayList<NameValuePair>(0);
        
        if (map!=null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
            }
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
            
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }
        httpPost.setConfig(requestConfig);
        
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            if (response.getEntity()!=null) {
               return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
            return new HttpResult(response.getStatusLine().getStatusCode(), null);
          
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    
    /**
     * 没有参数Post
     * @param url 请求地址
     * @return 返回HttpResult包括状态码code,主体 body
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResult doPost(String url) throws ClientProtocolException, IOException{
        return doPost(url,null);
    }
}
