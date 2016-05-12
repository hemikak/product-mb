/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.mb.tools.cli;

import org.wso2.mb.tools.cli.resources.DestinationHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Message
 */
public class MBCli {
    private static final Logger log = Logger.getLogger(MBCli.class.getName());
    private String userKey;
    private static Utils utils = new Utils();
    static Map<String, Class> supportedResources = new HashMap<>();



    static {
        supportedResources.put("destinations", DestinationHandler.class);
//        supportedResources.add("permissions");
//        supportedResources.add("messages");
//        supportedResources.add("subscriptions");
    }

    public MBCli(String key) {
        this.userKey = key;
    }

    public void start() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (true) {
            log.info("=> ");
            line = in.readLine();
            if (line.isEmpty()) {
                continue;
            } else if (utils.exitPredication.test(line)) {
                log.info("Goodbye...");
                break;
            } else if (line.toLowerCase().contains("use")) {
                if (line.split(" ").length > 1) {
                    String resource = line.split(" ")[1];
                    boolean valid = supportedResources.keySet().contains(resource.toLowerCase());
                    if (valid) {
                        Class aClass = supportedResources.get(resource.toLowerCase());
                        Method startMethod = aClass.getDeclaredMethod("start", String.class);
                        startMethod.invoke(userKey);
                    } else {
                        log.severe("Unknown resource selected. \"use " + supportedResources + "\"");
                    }
                } else {
                    log.severe("Resource not selected. \"use " + supportedResources + "\"");
                }
            } else if ("help".equalsIgnoreCase(line)) {
                log.severe("\"use\" keyword is not used along with a resource. Example : \"use " + supportedResources
                           + "\"");
            } else {
                log.severe("Resource not selected. \"use " + supportedResources + "\"");
            }
        }
    }
}
