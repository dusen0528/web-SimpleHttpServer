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

package com.nhnacademy.http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Objects;


public class HttpResponseImpl implements HttpResponse {
    //#4 HttpResponse를 구현 합니다.

    private final Socket socket;
    private final DataOutputStream out;
    private String charset = "UTF-8";

    public HttpResponseImpl(Socket socket) {
        if(Objects.isNull(socket)){
            throw new IllegalArgumentException();
        }
        this.socket = socket;

        try{
            this.out = new DataOutputStream(socket.getOutputStream());
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public PrintWriter getWriter() throws IOException {
        // DataOutputStream을 이용해서 구현하세요
        // Java에서 기본 데이터 타입들을 이진 형식으로 출력하는 데 사용되는 클래스 입니다.
        // 예를 들어, 파일이나 네트워크 소켓에 데이터를 효율적으로 저장하거나 전송할 때 유용하게 사용할 수 있습니다.
        // https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/DataOutputStream.html
        PrintWriter printWriter = new PrintWriter(out, false, Charset.forName(getCharacterEncoding()));
        return printWriter;
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.charset = charset;

    }

    @Override
    public String getCharacterEncoding() {
        return charset;
    }
}
