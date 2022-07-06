package com.aem.geeks.integrations.core.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Reference;

public final class ServiceUtils {
    private static int socketTimeout = 60000;
    private static int connectionTimeout = 50000;
    private ObjectMapper mapper = new ObjectMapper();

    @Reference
    private static HttpClientBuilderFactory httpClientBuilderFactory;

    public static InputStream getRestService(String url) throws IOException {
        RequestConfig rc = RequestConfig.custom().setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout)
                .build();
        HttpClient httpClient;
        if (httpClientBuilderFactory != null
                && httpClientBuilderFactory.newBuilder() != null) {
            httpClient = httpClientBuilderFactory.newBuilder()
                    .setDefaultRequestConfig(rc)
                    .build();
        } else {
            httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(rc)
                    .build();
        }
        HttpResponse response = httpClient.execute(new HttpGet(url));
        return response.getEntity().getContent();
    }
    public static Date getDate(String rowDate) throws ParseException {
        SimpleDateFormat DateFor = new SimpleDateFormat("MM/dd/yy");
        Date date =DateFor.parse(rowDate);
        return date;
    }

/*    public static List<CoronaData> sortCoronaData(final List<CoronaData> list) {
        Collections.sort(list, new Comparator<CoronaData>() {
            public static final String NODE_PROPERTY = "nonFormattedDate";
            @Override
            public int compare(CoronaData cd1, CoronaData cd2) {
                Date dateA = null;
                Date dateB = null;
                try {
                    if (cd1!=null) {
                        dateA = (Date) cd1.getDate();
                    }
                    if (cd2!=null) {
                        dateB = (Date) cd2.getDate();
                    }
                }
                catch (Exception e) {
                    // Exception
                }
                if(dateA==null && dateB==null){
                    return 0;
                }
                if (dateA == null) {
                    return 1;
                } else if(dateB == null) {
                    return -1;
                }
                return -dateA.compareTo(dateB);

            }
        });
       // log.info("\n--------SIZE------: "+list.size());
        return list;
    }*/

    public static String getProperty(Resource resource,String property){
        if(StringUtils.isNotBlank(resource.getValueMap().get(property,String.class))){
            return resource.getValueMap().get(property,String.class);
        }
        return null;
    }
}
