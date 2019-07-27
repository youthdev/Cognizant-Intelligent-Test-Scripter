/*
 * Copyright 2014 - 2017 Cognizant Technology Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cognizant.cognizantits.engine.constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemDefaults {
    public static AtomicInteger defaultWaitTime = new AtomicInteger(30);
    public static AtomicInteger defaultElementWaitTime = new AtomicInteger(30);
    public static AtomicBoolean defaultStopExecution = new AtomicBoolean();
    public static AtomicBoolean defaultPauseExecution = new AtomicBoolean(false);
    public static AtomicBoolean defaultDebugMode = new AtomicBoolean();
    public static AtomicBoolean defaultStopCurrentIteration = new AtomicBoolean();
    public static AtomicBoolean defaultGetClassesFromJar = new AtomicBoolean();
    public static AtomicBoolean defaultReportComplete = new AtomicBoolean();
    public static AtomicBoolean defaultNextStepflag = new AtomicBoolean(true);

    public static AtomicInteger waitTime = defaultWaitTime;
    public static AtomicInteger elementWaitTime = defaultElementWaitTime;
    public static AtomicBoolean stopExecution = defaultStopExecution;
    public static AtomicBoolean pauseExecution = defaultPauseExecution;
    public static AtomicBoolean debugMode = defaultDebugMode;
    public static AtomicBoolean stopCurrentIteration = defaultStopCurrentIteration;
    public static AtomicBoolean getClassesFromJar = defaultGetClassesFromJar;
    public static AtomicBoolean reportComplete = defaultReportComplete;
    public static AtomicBoolean nextStepflag = defaultNextStepflag;
    public static Map<String, String> CLVars = new HashMap<>();
    public static Map<String, String> EnvVars = new HashMap<>();

    private static Properties buildProperties;

    static {
        buildProperties = new Properties();
        try {
            buildProperties.load(SystemDefaults.class.getResourceAsStream("/engine/build.properties"));
        } catch (IOException ex) {
            Logger.getLogger(SystemDefaults.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static String getBuildVersion() {
        return buildProperties.getProperty("Bundle-Version");
    }

    public static void pollWait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SystemDefaults.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void resetAll() {
        waitTime = defaultWaitTime;
        elementWaitTime = defaultElementWaitTime;
        stopExecution = defaultStopExecution;
        debugMode = defaultDebugMode;
        stopCurrentIteration = defaultStopCurrentIteration;
        reportComplete = defaultReportComplete;
        nextStepflag = defaultNextStepflag;
        pauseExecution = defaultPauseExecution;
    }

    public static boolean canLaunchSummary() {
        return !CLVars.containsKey("dontLaunchSummary");
    }

    public static void printSystemInfo() {
        System.out.println("Run Information");
        System.out.println("========================");
        System.out.println("cognizant.intelligent.test.scripter.engine : " + getBuildVersion());
        printSystemInfo("java.runtime.name");
        printSystemInfo("java.version");
        printSystemInfo("java.home");
        printSystemInfo("os.name");
        printSystemInfo("os.arch");
        printSystemInfo("os.version");
        printSystemInfo("file.encoding");
        System.out.println("========================");
    }

    private static void printSystemInfo(String key) {
        System.out.println(String.format("%-20s : %s", key, System.getProperty(key)));
    }

    public static boolean debug() {
        return debugMode.get() || Boolean.valueOf(CLVars.get("debug"));
    }
}
