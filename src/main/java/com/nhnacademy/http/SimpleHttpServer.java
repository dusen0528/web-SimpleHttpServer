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

package com.nhnacademy.http;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
public class SimpleHttpServer {

    private final int port;
    private static final int DEFAULT_PORT=8080;
    private final ServerSocket serverSocket;

    //TODO#1 CRLF를 선언합니다. CRLF는 Carriage Return (CR) 와 Line Feed (LF)를 의미하며, HTTP 프로토콜에서 줄 바꿈을 나타내기 위해 사용됩니다. 이는 \r\n으로 표현됩니다.
    private static final String CRLF="FIXME";

    private final AtomicLong atomicCounter;

    public SimpleHttpServer(){
        //TODO#2 기본 port는 DEFAULT_PORT을 사용합니다.
        port =0;
        serverSocket = null;
    }

    public SimpleHttpServer(int port) {

        //port <=0 이면 IllegalArgumentException 발생합니다. 적절한 Error Message를 작성하세요.
        if(port<=0) throw new IllegalArgumentException("port는 0 미만일 수 없습니다");
        // port와 atomicCounter를 초기화 합니다.
        this.port = port;
        atomicCounter = new AtomicLong();
    }

    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(port);){

            HttpRequestHandler httpRequestHandlerA = new HttpRequestHandler();
            HttpRequestHandler httpRequestHandlerB = new HttpRequestHandler();

            //threadA를 생성하고 시작 합니다. thread-name : threadA 설정 합니다.
            Thread threadA = new Thread(httpRequestHandlerA);
            threadA.setName("ThreadA");
            threadA.start();

            //threadB를 생성하고 시작 합니다. thread-name: threadB 설정 합니다.
            Thread threadB = new Thread(httpRequestHandlerB);
            threadB.setName("ThreadB");
            threadB.start();

            while(true){
                Socket client = serverSocket.accept();
                /* count값이 짝수이면 httpRequestHandlerA에 client를 추가 합니다.
                          count값이 홀수라면 httpRequestHandlerB에 clinet를 추가 합니다.
                */
                long count = atomicCounter.incrementAndGet();
                log.debug("count:{}",atomicCounter);
                if(count%2==0){
                    httpRequestHandlerA.addRequest(client);
                }else{
                    httpRequestHandlerB.addRequest(client);
                }count++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        //TODO#3 port range <=0 IllegalArgumentException 예외가 발생 합니다. 적절한 Error Message를 작성해주세요.

        this.port = 0;


        //TODO#4 serverSocket을 초기화 합니다.
        serverSocket = null;
    }

    public void start() throws IOException {

        while(true){

            try(
                //TODO#5 client가 연결될 때 까지 대기 합니다.
                Socket client = null;

                //TODO#6 입출력을 위해서  Reader, Writer를 선언 합니다.
                BufferedReader bufferedReader = null;
                BufferedWriter bufferedWriter = null;
            ) {

                StringBuilder requestBuilder = new StringBuilder();
                log.debug("------HTTP-REQUEST_start()");
                while (true) {
                    String line = bufferedReader.readLine();
                    //TODO#7  requestBuilder에 append 합니다.

                    log.debug("{}", line);

                    //TODO#8 종료 조건 null or size==0
                    break;
                }
                log.debug("------HTTP-REQUEST_end()");

                //TODO#9 clinet에 응답할 html을 작성합니다.
                /*
                    <html>
                        <body>
                            <h1>hello hava</h1>
                        </body>
                    </html>
                */

                StringBuilder responseBody = new StringBuilder();
                responseBody.append("<html>");
                //html tag를 추가하세요.
                responseBody.append("</html>");

                StringBuilder responseHeader = new StringBuilder();

                //TODO#10 HTTP/1.0 200 OK
                responseHeader.append("fixme");

                responseHeader.append(String.format("Server: HTTP server/0.1%s",CRLF));

                //TODO#11 Content-type: text/html; charset=UTF-8"
                responseHeader.append("fixme");


                //TODO#12  Connection: close 헤더를 사용하면 해당 요청 후에 연결이 닫히게 된다.
                responseHeader.append("fixme");

                //TODO#13 responseBody의  Content-Length를 설정 합니다.
                responseHeader.append("fixme");

                //TODO#14 write Response Header
                bufferedWriter.write("");

                //TODO#15 write Response Body
                bufferedWriter.write("");

                //TODO#16 buffer에 등록된 Response (header, body) flush 합니다.(socket을 통해서 clent에 응답 합니다.)


                log.debug("header:{}",responseHeader);
                log.debug("body:{}",responseBody);

            }catch (IOException e){
                log.error("socket error : {}",e);
            }
        }//end while
    }//end start
}
