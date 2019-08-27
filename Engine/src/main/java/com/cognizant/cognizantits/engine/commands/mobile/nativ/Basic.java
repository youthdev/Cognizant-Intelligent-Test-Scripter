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
 */
package com.cognizant.cognizantits.engine.commands.mobile.nativ;

import com.cognizant.cognizantits.engine.core.CommandControl;
import com.cognizant.cognizantits.engine.execution.exception.element.ElementException;
import com.cognizant.cognizantits.engine.support.Status;
import com.cognizant.cognizantits.engine.support.methodInf.Action;
import com.cognizant.cognizantits.engine.support.methodInf.InputType;
import com.cognizant.cognizantits.engine.support.methodInf.ObjectType;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MultiTouchAction;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.support.ui.Wait;

import java.awt.*;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
@SuppressWarnings("rawtypes")
public class Basic extends MobileNativeCommand {

    public Basic(CommandControl cc) {
        super(cc);
    }

    /**
     * method for tapping the center of an element on the screen
     *
     * @see AppiumDriver#tap(int, org.openqa.selenium.WebElement, int)
     */
    @Action(object = ObjectType.MOBILE, desc = "Tap on the [<Object>]", input = InputType.OPTIONAL)

    public void tap() {
        try {
            if (Element != null) {
                int nof = this.getInt(Data, 0, 1);
                TouchAction touchAction = new TouchAction(((MobileDriver) Driver));
                do {
                    touchAction.tap(TapOptions.tapOptions().withElement(ElementOption.element(Element)));
                    touchAction.release().perform();
                } while (--nof > 0);
                Report.updateTestLog(Action, "Tapped on '" + ObjectName + "'", Status.PASS);
            } else {
                throw new ElementException(ElementException.ExceptionType.Element_Not_Found, Condition);
            }
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * method for tapping a position on the screen
     *
     * @see AppiumDriver#tap(int, int, int, int)
     */
    @Action(object = ObjectType.BROWSER, desc = "Tap at [<Data>]", input = InputType.YES)

    public void tapAt() {
        try {
            int nof = this.getInt(Data, 0, 1);
            int x = this.getInt(Data, 1, 10);
            int y = this.getInt(Data, 2, 10);
            TouchAction touchAction = new TouchAction(((MobileDriver) Driver));
            do {
                touchAction.tap(TapOptions.tapOptions().withPosition(PointOption.point(x, y)));
                touchAction.release().perform();
            } while (--nof > 0);
            Report.updateTestLog(Action, "Tapped at co-ordinates '" + x + "','" + y + "'", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * method for "zooming in" on an element on the screen.
     *
     * @see AppiumDriver#zoom(org.openqa.selenium.WebElement)
     */
    @Action(object = ObjectType.MOBILE, desc = "Zoom in [<Object>]")

    public void zoom() {
        try {
            if (Element != null) {
                int l = 150;
                TouchAction action0 = new TouchAction(((MobileDriver) Driver));
                TouchAction action1 = new TouchAction(((MobileDriver) Driver));
                action0.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(Element)))
                        .moveTo(PointOption.point(0, l))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                        .release();
                action1.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(Element)))
                        .moveTo(PointOption.point(0, -l))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                        .release();
                new MultiTouchAction(((MobileDriver) Driver)).add(action0).add(action1).perform();
                Report.updateTestLog(Action, "Zoomed in '" + ObjectName + "'", Status.PASS);
            } else {
                throw new ElementException(ElementException.ExceptionType.Element_Not_Found, Condition);
            }
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * method for "zooming in" on an element on the screen.
     *
     * @see AppiumDriver#zoom(int, int)
     */
    @Action(object = ObjectType.BROWSER, desc = "Zoom at [<Data>]", input = InputType.YES)
    public void zoomAt() {
        try {
            int x = this.getInt(Data, 0, 10);
            int y = this.getInt(Data, 1, 10);
            int l = 100;
            TouchAction action0 = new TouchAction(((MobileDriver) Driver));
            TouchAction action1 = new TouchAction(((MobileDriver) Driver));
            action0.longPress(LongPressOptions.longPressOptions().withPosition(PointOption.point(x, y + l)))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
                    .moveTo(PointOption.point(0, 200))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
                    .release();
            action1.longPress(LongPressOptions.longPressOptions().withPosition(PointOption.point(x + 50, y - l)))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
                    .moveTo(PointOption.point(0, -200))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
                    .release();
            new MultiTouchAction(((MobileDriver) Driver)).add(action0).add(action1).perform();
            Report.updateTestLog(Action, "Zoomed at '" + x + "','" + y + "'", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * method for pinching an element on the screen.
     *
     * @see AppiumDriver#pinch(org.openqa.selenium.WebElement)
     */
    @Action(object = ObjectType.MOBILE, desc = "Pinch  [<Object>]")
    public void pinch() {
        try {
            if (Element != null) {
                int l = 150;
                TouchAction action0 = new TouchAction(((MobileDriver) Driver));
                TouchAction action1 = new TouchAction(((MobileDriver) Driver));
                action0.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(Element)))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
                        .moveTo(PointOption.point(0, l))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                        .release();
                action1.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(Element)))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
                        .moveTo(PointOption.point(0, -l))
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                        .release();
                new MultiTouchAction(((MobileDriver) Driver)).add(action0).add(action1).perform();
                Report.updateTestLog(Action, "Pinched '" + ObjectName + "'", Status.PASS);
            } else {
                throw new ElementException(ElementException.ExceptionType.Element_Not_Found, Condition);
            }
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * method for pinching an element on the screen.
     *
     * @see AppiumDriver#pinch(int, int)
     */
    @Action(object = ObjectType.MOBILE, desc = "Pinch at [<Data>]", input = InputType.YES)
    public void pinchAt() {
        try {
            int x = this.getInt(Data, 0, 10);
            int y = this.getInt(Data, 1, 10);
            int l = 350;
            TouchAction action0 = new TouchAction(((MobileDriver) Driver));
            TouchAction action1 = new TouchAction(((MobileDriver) Driver));
            action0.longPress(LongPressOptions.longPressOptions().withPosition(PointOption.point(x, y - l)))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
                    .moveTo(PointOption.point(0, l - 200))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                    .release();
            action1.longPress(LongPressOptions.longPressOptions().withPosition(PointOption.point(x, y + l)))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)))
                    .moveTo(PointOption.point(0, 200 - l))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                    .release();
            new MultiTouchAction(((MobileDriver) Driver)).add(action0).add(action1).perform();
            Report.updateTestLog(Action, "Pinched at'" + x + "','" + y + "'", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lock the device (bring it to the lock screen) for a given number of
     * seconds
     *
     * @see AppiumDriver#lockScreen(int)
     */
    @Action(object = ObjectType.MOBILE, desc = "Lock the screen", input = InputType.YES)
    public void lockScreen() {
        try {
            if (Driver instanceof AndroidDriver) {
                ((AndroidDriver) Driver).lockDevice();
            } else {
                int time = this.getInt(Data, 5);
                ((IOSDriver) Driver).lockDevice(Duration.ofSeconds(time));
            }
            Report.updateTestLog(Action, "Screen locked", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Switch context of the driver
     *
     * @see AppiumDriver#lockScreen(int)
     */
    @Action(object = ObjectType.MOBILE, desc = "Switch the context to [<Data>]", input = InputType.YES)
    public void switchContext() {
        try {
            ((MobileDriver) Driver).context(Data);
            if (Data.equals(((MobileDriver) Driver).getContext())) {
                Report.updateTestLog(Action, "Context switched to " + Data, Status.DONE);
            } else {
                Report.updateTestLog(Action, "Unable to swtich to context " + Data
                        + " , in " + ((MobileDriver) Driver).getContextHandles(), Status.FAIL);
            }
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            System.out.println(((MobileDriver) Driver).getContextHandles());
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    /**
     * Launch the app which was provided in the capabilities at session creation
     *
     * @see AppiumDriver#launchApp()
     */
    @Action(object = ObjectType.MOBILE, desc = "Clean app data and launch the app given in Capabillities")
    public void launchApp() {
        try {
            ((AppiumDriver) Driver).launchApp();
            Report.updateTestLog(Action, "Application launched", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Install an app on the mobile device
     *
     * @see AppiumDriver#launchApp()
     */
    @Action(object = ObjectType.MOBILE, desc = "Install the App [<Data>]", input = InputType.YES)
    public void installApp() {
        try {
            ((AppiumDriver) Driver).installApp(Data);
            Report.updateTestLog(Action, "Application Installed", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Toggle location services state
     *
     * @see AppiumDriver#toggleLocationServices()
     */
    @Action(object = ObjectType.MOBILE, desc = "Toggle the Location Services(android)")

    public void toggleLocationServices() {
        try {
            ((AndroidDriver) Driver).toggleLocationServices();
            Report.updateTestLog(Action, "Location Service toggled", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Hide the software keyboard
     *
     * @see AppiumDriver#hideKeyboard()
     */
    @Action(object = ObjectType.MOBILE, desc = "Hide the Keyboard")
    public void hideKeyboard() {
        try {
            ((AppiumDriver) Driver).hideKeyboard();
            Report.updateTestLog(Action, "Keyboard hidden", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reset the currently running app(given in capabilities) for this session
     *
     * @see AppiumDriver#hideKeyboard()
     */
    @Action(object = ObjectType.MOBILE, desc = "Reset (reinstall) the app")
    public void resetApp() {
        try {
            ((AppiumDriver) Driver).resetApp();
            Report.updateTestLog(Action, "Application reset successful", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Opens the openNotifications
     *
     * @see AppiumDriver#hideKeyboard()
     */
    @Action(object = ObjectType.MOBILE, desc = "Open the Notifications(android)")
    public void openNotifications() {
        try {
            ((AndroidDriver) Driver).openNotifications();
            Report.updateTestLog(Action, "Notification Opened", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Action(object = ObjectType.MOBILE, desc = "Close the app on device")
    public void closeApp() {
        try {
            ((AppiumDriver) Driver).closeApp();
            Report.updateTestLog(Action, "Application is closed successful", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Action(object = ObjectType.MOBILE, desc = "Put the app to backgroun for [Data] seconds and then resume", input = InputType.YES)
    public void runAppInBackground() {
        try {
            ((AppiumDriver) Driver).runAppInBackground(Duration.ofSeconds(this.getInt(Data, 0, 1)));
            Report.updateTestLog(Action, "Application is put to background successful", Status.PASS);
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Action(object = ObjectType.MOBILE, desc = "Kill the app and relaunch without cleaning app data")
    public void restartApp() {
        try {
            String appId = "";
            if (Driver instanceof  AndroidDriver) {
                appId = ((AndroidDriver) Driver).getCurrentPackage();
                if (appId.isEmpty()) {
                    Report.updateTestLog(Action, "Can not detect appPackage so, the restart action won't work", Status.DEBUG);
                }
            }
            else if (Driver instanceof IOSDriver) {
                appId = ((AppiumDriver) Driver).getCapabilities().getCapability("bundleId").toString();
                if (appId.isEmpty()) {
                    Report.updateTestLog(Action, "Capability \"bundleId\" must be configured to restart an iOS app", Status.DEBUG);
                }
            }

            if (!appId.isEmpty()) {
                ((AppiumDriver) Driver).closeApp();
                ((AppiumDriver) Driver).activateApp(appId);
                Report.updateTestLog(Action, "Application is restarted successful", Status.PASS);
            }
        } catch (Exception ex) {
            Report.updateTestLog(Action, ex.getMessage(), Status.DEBUG);
            Logger.getLogger(Basic.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
