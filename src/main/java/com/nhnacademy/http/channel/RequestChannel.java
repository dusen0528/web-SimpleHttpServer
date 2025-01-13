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

package com.nhnacademy.http.channel;

import java.util.LinkedList;
import java.util.Queue;

public class RequestChannel {
    private final Queue<Executable> requestQueue;
    private static final long QUEUE_MAX_SIZE = 10;

    private final long queueSize;

    public RequestChannel() {
        this(QUEUE_MAX_SIZE);
    }

    public RequestChannel(long queueSize){
        //queueSize < 0  IllegalArgumentException 발생 합니다. 적절히 Error Message를 작성하세요.
        if(queueSize < 0){ throw new IllegalArgumentException("queue size must be positive"); }
        // queueSize, requestQueue를 초기화 합니다.
        this.queueSize = queueSize;
        this.requestQueue = new LinkedList<>();
    }

    public synchronized void addHttpJob(Executable executable){
         /* queueSize >= MAX_QUEUE_SIZE 대기 합니다.
            즉 queue에 데이터가 소비될 때 까지 client Socket을 Queue에 등록하는 작업을 대기 합니다.
        */
        while(queueSize >= QUEUE_MAX_SIZE){
            try{
                wait();
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        // requestQueue에 executable를 추가 합니다.
        requestQueue.add(executable);

        //대기하고 있는 Thread를 깨웁니다.
        notifyAll();
    }

    public synchronized Executable getHttpJob(){

        // requestQueue가 비어 있다면 대기 합니다.
        while(requestQueue.isEmpty()){
            try{
                wait();
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        // 대기하고 있는 Thread를 깨우고, requestQueue에서 Executable을 반환 합니다.
        notifyAll();
        return requestQueue.poll();
    }
}
