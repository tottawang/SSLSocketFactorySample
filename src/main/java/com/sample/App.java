package com.sample;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

public class App {

  // change constants below before running the program
  private static final String CONSUMER_KEY = "";
  private static final String CONSUMER_SECRET = "";
  private static final String ACCESS_TOKEN = "";
  private static final String ACCESS_SECRET = "";
  private static final String TEST_URL = "";

  public static void main(String[] args) throws IOException, OAuthMessageSignerException,
      OAuthExpectationFailedException, OAuthCommunicationException {

    // enable logs for SSL connection
    System.setProperty("javax.net.debug", "all");

    // oauth1 sample
    oauth1HttpClientSample();
  }

  public static void oauth1HttpClientSample() throws IOException, OAuthMessageSignerException,
      OAuthExpectationFailedException, OAuthCommunicationException {
    OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
    consumer.setTokenWithSecret(ACCESS_TOKEN, ACCESS_SECRET);

    HttpGet httpget = new HttpGet(TEST_URL);
    consumer.sign(httpget);

    // Access https://www.ssllabs.com/ssltest/ to list supported ciphers by the target server
    // TEST_URL, e.g. TLS_RSA_WITH_AES_256_CBC_SHA which should be supported by jdk as well,
    // otherwise SSLConnectionSocketFactory throws error 'Cannot support
    // TLS_RSA_WITH_AES_256_CBC_SHA with currently installed providers'

    SSLConnectionSocketFactory sslConnectionSocketFactory =
        new SSLConnectionSocketFactory(SSLContexts.createDefault(),
            new String[] {"TLSv1.2", "TLSv1"}, new String[] {"TLS_RSA_WITH_AES_256_CBC_SHA"},
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());

    CloseableHttpClient httpClient =
        HttpClientBuilder.create().setSSLSocketFactory(sslConnectionSocketFactory).build();

    HttpResponse response = httpClient.execute(httpget);
    System.out.println(response.getStatusLine().toString());
    HttpEntity entity = response.getEntity();
    System.out.println(EntityUtils.toString(entity));
  }

}
