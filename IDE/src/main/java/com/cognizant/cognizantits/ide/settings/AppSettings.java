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
package com.cognizant.cognizantits.ide.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cognizant.cognizantits.engine.constants.SystemDefaults;

/**
 *
 *
 */
public class AppSettings {

    private static final File APPSETT = new File("Configuration" + File.separator + "app.settings");
    private static Properties settings;

    public enum APP_SETTINGS {
        THEME("theme", "Sapphire"),
        THEMES("themes", "Amethyst,Coral,Pearl,Sapphire"),
        ADDON_PORT("addonPort", "8887"),
        HAR_PORT("harPort", "11234"),
        DEF_LOG_LEVEL("defaultLogLevel", "INFO"),
        SHOW_DATE_TIME("showDateTime", "true"),
        DATE_TIME_FORMAT("dateTimeFormat", "yyyy-MM-dd HH:mm:ss:SSS Z"),
        LOG_BACKUP_LOC("logBackupLoc", "backup/log"),
        LOG_FILE("logfile", "log.txt"),
        MAX_FILE_SIZE("maxFileSize", "4.5"),
        DEFAULT_WAIT_TIME("defaultWaitTime", SystemDefaults.defaultWaitTime.toString()),
        ELEMENT_WAIT_TIME("elementWaitTime", SystemDefaults.defaultElementWaitTime.toString()),
        LOAD_RECENT("loadRecentProject", "true"),
        STANDALONE_REPORT("standaloneReport", "false"),
        HELP_DOC("helpdoc", "https://cognizantqahub.github.io/Cognizant-Intelligent-Test-Scripter-Helpdoc");

        private final String key;
        private final String val;

        APP_SETTINGS(String key, String val) {
            this.key = key;
            this.val = val;
        }

        public String getKey() {
            return key;
        }

        public String getVal() {
            return val;
        }

        public static String getByKey(String key) {
            for (APP_SETTINGS value : APP_SETTINGS.values()) {
                if (value.getKey().equals(key)) {
                    return value.getVal();
                }
            }
            return "";
        }

    }

    public static void load() {
        try {
            settings = new Properties();
            if (new File(APPSETT.getAbsolutePath()).exists()) {
                settings.load(new FileInputStream(APPSETT.getAbsolutePath()));
            } else {
                for (APP_SETTINGS value : APP_SETTINGS.values()) {
                    settings.put(value.getKey(), value.getVal());
                }
                store("Created");
            }
        } catch (IOException ex) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void set(String key, String val) {
        check();
        settings.put(key, val);
    }

    public static String get(String key) {
        check();
        if (settings.containsKey(key)) {
            return settings.getProperty(key);
        }
        return getDefault(key);
    }

    public static String getDefault(String key) {
        String val = APP_SETTINGS.getByKey(key);
        settings.put(key, val);
        return val;
    }

    public static void store(String cmnt) {
        check();
        try {
            settings.store(new FileOutputStream(APPSETT.getAbsolutePath()), cmnt);
        } catch (IOException ex) {
            Logger.getLogger(AppSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Boolean canOpenRecentProjects() {
        return Boolean.valueOf(AppSettings.get(APP_SETTINGS.LOAD_RECENT.getKey()));
    }

    public static void openRecentProjectsOnLaunch(Boolean value) {
        AppSettings.set(APP_SETTINGS.LOAD_RECENT.getKey(), String.valueOf(value));
    }

    public static String getHelpLoc() {
        return AppSettings.get(APP_SETTINGS.HELP_DOC.getKey());
    }

    private static void check() {
        if (settings == null) {
            load();
        }
    }

}
