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

import com.nhnacademy.http.channel.HttpJob;
import com.nhnacademy.http.channel.RequestChannel;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class SimpleHttpServer {

    private final int port;
    private static final int DEFAULT_PORT=8080;
    private final ServerSocket serverSocket;


    //TODO#1 CRLF를 선언합니다. CRLF는 Carriage Return (CR) 와 Line Feed (LF)를 의미하며, HTTP 프로토콜에서 줄 바꿈을 나타내기 위해 사용됩니다. 이는 \r\n으로 표현됩니다.
    private static final String CRLF="FIXME";

    private final AtomicLong atomicCounter;

    private final RequestChannel requestChannel;

    public SimpleHttpServer(){
        //TODO#2 기본 port는 DEFAULT_PORT을 사용합니다.
        port =0;
        serverSocket = null;
    }

    private WorkerThreadPool workerThreadPool;

    public SimpleHttpServer(int port) {
        if(port<=0){
            throw new IllegalArgumentException(String.format("Invalid Port:%d",port));
        }
        this.port = port;
        // RequestChannel() 초기화 합니다.
        requestChannel = new RequestChannel();

        // workerThreadPool 초기화 합니다.
        workerThreadPool = new WorkerThreadPool(requestChannel);
    }

    public void start(){
        // workerThreadPool을 시작 합니다.
        workerThreadPool.start();

        try(ServerSocket serverSocket = new ServerSocket(8080);){
            while(true){
                Socket client = serverSocket.accept();
                //Queue(requestChannel)에 HttpJob 객체를 배치 합니다.
                requestChannel.addHttpJob(new HttpJob(client));
            }
        }catch (IOException e){
            log.error("server error:{}",e);

        }
    }
}
