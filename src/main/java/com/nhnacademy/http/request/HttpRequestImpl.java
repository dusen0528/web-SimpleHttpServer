/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.http.request;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
public class HttpRequestImpl implements HttpRequest {
    // #1: 필요한 필드 선언하기
    // - headerMap: HTTP 헤더와 요청 정보를 저장할 Map (String, Object)
    // - attributeMap: 요청 처리 중 필요한 데이터 저장할 Map (String, Object)
    // - 상수 정의: HTTP-METHOD, HTTP-QUERY-PARAM-MAP, HTTP-REQUEST-PATH 키 값들
    // - HEADER_DELIMER 상수 정의 (":")
    private Map<String, Object> headerMap = new HashMap<>();
    private Map<String,Object> attributeMap = new HashMap<>();
    private final static String KEY_HTTP_METHOD = "HTTP-METHOD";
    private final static String KEY_QUERY_PARAM_MAP = "HTTP-QUERY-PARAM-MAP";
    private final static String KEY_REQUEST_PATH="HTTP-REQUEST-PATH";
    private final static String HEADER_DELIMER=":";

    private final Socket client;

    //  #2: 생성자에서 initialize() 호출하도록 구현
    public HttpRequestImpl(Socket socket) {
        this.client = socket;
        initialize();
    }

    // #3: initialize() 메소드 구현
    // 1. BufferedReader 생성하여 소켓에서 데이터 읽기
    // 2. while문으로 라인별로 읽으면서:
    //    - isFirstLine()이면 parseHttpRequestInfo() 호출
    //    - isEndLine()이면 break
    //    - 그 외는 parseHeader() 호출
    private void initialize() {
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while(true){
                String line = bufferedReader.readLine();
                if(isFirstLine(line)){
                    parseHttpRequestInfo(line);
                }else if(isEndLine(line)){
                    break;
                }else{
                    parseHeader(line);
                }
            }

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // #4: isFirstLine() 메소드 구현
    // - GET 또는 POST가 포함된 라인인지 확인 (대소문자 구분 없이)
    private boolean isFirstLine(String line) {
        line.toUpperCase();
        return line.contains("GET") || line.contains("POST");
    }

    // #5: isEndLine() 메소드 구현
    // - null이거나 빈 문자열인지 확인
    private boolean isEndLine(String s) {
        return s.isBlank() || s.isEmpty() || s == null;
    }

    // #6: parseHeader() 메소드 구현
    // - HEADER_DELIMER(:)로 분리
    // - 키와 값의 공백 제거(trim)
    // - headerMap에 저장
    private void parseHeader(String s) {
        String[] hStr = s.split(HEADER_DELIMER);
        String key = hStr[0].trim();
        String value = hStr[1].trim();
        headerMap.put(key,value);
    }

    // #7: parseHttpRequestInfo() 메소드 구현
    // 1. 공백으로 문자열 분리
    // 2. HTTP 메소드 파싱 -> headerMap에 저장
    // 3. 쿼리 파라미터 파싱:
    //    - ? 위치 찾기
    //    - 경로와 쿼리스트링 분리
    //    - 쿼리스트링을 &로 분리하여 key=value 형태로 queryMap에 저장
    // 4. 경로와 queryMap을 headerMap에 저장
    private void parseHttpRequestInfo(String s) {
        String[] arr = s.split(" ");
        // arr[0] = GET
        // arr[1] = /users?id=123&name=inho
        // arr[2] = HTTP/1.1

        // 2. HTTP 메소드 저장
        if(arr.length>0){
            headerMap.put(KEY_HTTP_METHOD, arr[0]);
        }

        // 3. 쿼리 파라미터 처리
        if(arr.length>2){ // 정해진 양식에 맞게 최소 3개가 있는지
            Map <String, String> queryMap = new HashMap<>();

            // 3-1 ? 위치 찾기
            int questionIndex = arr[1].indexOf("?");

            // 3-2 경로와 쿼리 스트링 분리
            String httpRequestPath;
            if(questionIndex > 0){
                httpRequestPath = arr[1].substring(0, questionIndex);
            }else{
                httpRequestPath = arr[1];
            }

            // 3-3 쿼리스트링을 &로 분리하여 key=value 형태로 queryMap 저장
            // id=123&name=inho

            if(questionIndex>0){
                String queryString = arr[1].substring(questionIndex + 1);
                String [] params = queryString.split("&");
                //params[1] : id=123
                for(String param : params){
                    String [] keyValue = param.split("=");
                    if(keyValue.length == 2){
                        queryMap.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }
            }
            headerMap.put(KEY_REQUEST_PATH, httpRequestPath);
            headerMap.put(KEY_QUERY_PARAM_MAP, queryMap);
        }
    }

    @Override
    public String getMethod() {
        return String.valueOf(headerMap.get(KEY_HTTP_METHOD));
    }

    @Override
    public String getParameter(String name) {
        return String.valueOf(getParameterMap().get(name));
    }

    @Override
    public Map<String, String> getParameterMap() {
        return (Map <String,String>) headerMap.get(KEY_QUERY_PARAM_MAP);
    }

    @Override
    public String getHeader(String name) {
        return String.valueOf(headerMap.get(name));
    }

    @Override
    public void setAttribute(String name, Object o) {
        attributeMap.put(name, o);
    }

    @Override
    public Object getAttribute(String name) {
        return String.valueOf(headerMap.get(KEY_REQUEST_PATH));
    }

    @Override
    public String getRequestURI() {
        return String.valueOf(headerMap.get(KEY_REQUEST_PATH));
    }

}