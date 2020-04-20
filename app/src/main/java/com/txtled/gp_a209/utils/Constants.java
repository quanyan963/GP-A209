package com.txtled.gp_a209.utils;

import android.Manifest;

/**
 * Created by Mr.Quan on 2020/3/11.
 */
public class Constants {
    public static String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION };
    public static final int THIN = 0;
    public static final int BOLD = 1;
    public static final String TOPIC = "topic";
    public static final String MESSAGE = "message";
    public static final int WHAT = 0x11;
    public static final int RESULT = 2000;
    public static final int NAME_RESULT = 2001;
    public static final int WIFI_RESULT = 2002;
    public static final int INFO = 2003;
    public static final int LOGIN = 2004;
    public static final int APP = 2005;
    public static final int OK = 200;
    public static final String PUBLISH = "$aws/things/%s/shadow/update";
    public static final String SUBSCRIBE = "$aws/things/%s/shadow/update/accepted";
    public static final String REJECTED = "$aws/things/%s/shadow/update/rejected";
    public static final String GET_DATA = "$aws/things/%s/shadow/get";
    public static final String DATA_SOUND = "{\"state\":{\"desired\":{\"sound\":%d}}}";
    public static final String DATA_VOLUME = "{\"state\":{\"desired\":{\"volume\":%d}}}";
    public static final String DATA_LIGHT = "{\"state\":{\"desired\":{\"light\":%d}}}";
    public static final String DATA_DEVICE = "{\"state\":{\"desired\":{\"device\":%s}}}";
    public static final String DATA_DURATION = "{\"state\":{\"desired\":{\"duration\":%d}}}";
    public static final String IDENTITY_POOL_ID = "us-east-1:fdd912d6-7ba0-4ce9-8a7f-9d3398b21540";
    public static final String USER_ID = "UserId";
    public static final String DB_NAME = "FOX";
    public static final String THING_DIR = "ThingDir";
    public static final int SOCKET_UDP_PORT = 9001;
    public static final String MY_OIT_CE = "myiotce";
    public static final String ENDPOINT = "endpoint";
    public static final String NAME = "name";
    public static final String VERSION = "version";
    public static final String WIFI = "wifi";
    public static final String TYPE = "type";
    public static final String DISCOVERY = "{\"discovery\":1}";
    public static final String SEND_THING_NAME = "{\"endpoint\":\"%s\",\"thing\":\"%s\"}";
    public static final String SEND_CA_ONE = "{\"ca0\":\"%s\"}";
    public static final String SEND_CA_TWO = "{\"ca1\":\"%s\"}";
    public static final String SEND_CERT_ONE = "{\"cert0\":\"%s\"}";
    public static final String SEND_CERT_TWO = "{\"cert1\":\"%s\"}";
    public static final String SEND_KEY_ONE = "{\"key0\":\"%s\"}";
    public static final String SEND_KEY_TWO = "{\"key1\":\"%s\"}";
    public static final String REBOOT = "{\"reboot\":1}";
    public static final String FRIENDLY_NAME = "{\"friendlyname\":\"%s\"}";
    public static final String REST_API = "a311cdvk7hqtsk-ats.iot.us-east-1.amazonaws.com";
    public static final String POLICY_JSON = "{\n" +
            "  \"Version\": \"2012-10-17\",\n" +
            "  \"Statement\": [\n" +
            "    {\n" +
            "      \"Effect\": \"Allow\",\n" +
            "      \"Action\": \"iot:*\",\n" +
            "      \"Resource\": \"*\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    public static final String CA = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDQTCCAimgAwIBAgITBmyfz5m/jAo54vB4ikPmljZbyjANBgkqhkiG9w0BAQsF\n" +
            "ADA5MQswCQYDVQQGEwJVUzEPMA0GA1UEChMGQW1hem9uMRkwFwYDVQQDExBBbWF6\n" +
            "b24gUm9vdCBDQSAxMB4XDTE1MDUyNjAwMDAwMFoXDTM4MDExNzAwMDAwMFowOTEL\n" +
            "MAkGA1UEBhMCVVMxDzANBgNVBAoTBkFtYXpvbjEZMBcGA1UEAxMQQW1hem9uIFJv\n" +
            "b3QgQ0EgMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALJ4gHHKeNXj\n" +
            "ca9HgFB0fW7Y14h29Jlo91ghYPl0hAEvrAIthtOgQ3pOsqTQNroBvo3bSMgHFzZM\n" +
            "9O6II8c+6zf1tRn4SWiw3te5djgdYZ6k/oI2peVKVuRF4fn9tBb6dNqcmzU5L/qw\n" +
            "IFAGbHrQgLKm+a/sRxmPUDgH3KKHOVj4utWp+UhnMJbulHheb4mjUcAwhmahRWa6\n" +
            "VOujw5H5SNz/0egwLX0tdHA114gk957EWW67c4cX8jJGKLhD+rcdqsq08p8kDi1L\n" +
            "93FcXmn/6pUCyziKrlA4b9v7LWIbxcceVOF34GfID5yHI9Y/QCB/IIDEgEw+OyQm\n" +
            "jgSubJrIqg0CAwEAAaNCMEAwDwYDVR0TAQH/BAUwAwEB/zAOBgNVHQ8BAf8EBAMC\n" +
            "AYYwHQYDVR0OBBYEFIQYzIU07LwMlJQuCFmcx7IQTgoIMA0GCSqGSIb3DQEBCwUA\n" +
            "A4IBAQCY8jdaQZChGsV2USggNiMOruYou6r4lK5IpDB/G/wkjUu0yKGX9rbxenDI\n" +
            "U5PMCCjjmCXPI6T53iHTfIUJrU6adTrCC2qJeHZERxhlbI1Bjjt/msv0tadQ1wUs\n" +
            "N+gDS63pYaACbvXy8MWy7Vu33PqUXHeeE6V/Uq2V8viTO96LXFvKWlJbYK8U90vv\n" +
            "o/ufQJVtMVT8QtPHRh8jrdkPSHCa2XV4cdFyQzR1bldZwgJcJmApzyMZFo6IQ6XU\n" +
            "5MsI+yMRQ+hDKXJioaldXgjUkK642M4UwtBV8ob2xJNDd2ZhwLnoQdeXeGADbkpy\n" +
            "rqXRfboQnoZsG4q5WTP468SQvvG5\n" +
            "-----END CERTIFICATE-----\n";
}
