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

package com.nhnacademy;

import com.nhnacademy.exceptions.ServerInitializationException;
import com.nhnacademy.http.SimpleHttpServer;
import lombok.extern.slf4j.Slf4j;
import java.io.*;

@Slf4j
public class App {
    public static void main(String[] args) {
        try {
            SimpleHttpServer simpleHttpServer = new SimpleHttpServer(8080);
            simpleHttpServer.start();
        } catch (ServerInitializationException e) {
            log.error("서버 초기화 실패: ", e);
            System.exit(1); // 심각한 오류로 인한 종료
        } catch (IOException e) {
            log.error("서버 실행 중 오류 발생: ", e);
            System.exit(1);
        }
    }
}