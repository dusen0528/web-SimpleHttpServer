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

import java.util.Objects;

import com.nhnacademy.http.channel.RequestChannel;

public class WorkerThreadPool {
    private final int poolSize;

    private final static int DEFAULT_POOL_SIZE=5;

    private final Thread[] workerThreads;
    private final RequestChannel requestChannel;

    public WorkerThreadPool(RequestChannel requestChannel){
        this(DEFAULT_POOL_SIZE, requestChannel);
    }
    public WorkerThreadPool(int poolSize, RequestChannel requestChannel) {
        //poolSize <1 다면 IllegalArgumentException이 발생합니다. 적절히 ErrorMessage를 작성하세요
        if (poolSize < 1) {
            throw new IllegalArgumentException("pool Size는 1보다 커야합니다");
        }
        // requestChannel null check. 적절히 ErrorMessage를 작성하세요
        if (requestChannel == null) {
            throw new IllegalArgumentException("RequestChannel cannot be null");
        }
        // pooSize, requestChannel 초기화
        this.poolSize = poolSize;
        this.requestChannel = requestChannel;

        //requestChannel을 이용하여 httpRequestHandler 객체를 생성 합니다.
        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(requestChannel);

        // workerThreads를 초기화 합니다. poolSize 만큼 Thread를 생성 합니다.
        workerThreads = new Thread[poolSize];

        for(int i=0; i<poolSize; i++){
            // workerThread 생성및 이름 설정 :  thread-1,thread-2, thread-3 ...
            workerThreads[i] = new Thread(httpRequestHandler);
            workerThreads[i].setName("thread-"+ i + 1);
            workerThreads[i].start();
        }
    }
    public synchronized void start(){
        // workerThreads에 초가화된 모든 Thread를 start 합니다.
        for (int i = 0; i < poolSize; i++) {
            workerThreads[i].start();
        }

    }

    public synchronized void stop(){

        /* interrupt()를 실행해서 thread를 종료 합니다.
            - thread가 종료되는 과정에서 동기화 되어야 합니다.
         */
        for (Thread thread : workerThreads) {
            if (Objects.isNull(thread) && thread.isAlive()) {
                thread.interrupt();
            }
        }
        
      
        // join()를 호출해서 모든 thread가 종료될 떄 까지 대기 합니다.
          for (Thread thread : workerThreads) {
            try {
                thread.join();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }


    }
}
