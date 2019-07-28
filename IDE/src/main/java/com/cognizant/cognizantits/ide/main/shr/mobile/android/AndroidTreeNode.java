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
package com.cognizant.cognizantits.ide.main.shr.mobile.android;

import com.cognizant.cognizantits.ide.main.shr.mobile.MobileTreeNode;
import com.cognizant.cognizantits.ide.main.shr.mobile.Rect;
import com.cognizant.cognizantits.ide.util.Utility;

/**
 *
 * 
 */
public class AndroidTreeNode extends MobileTreeNode {

    public AndroidTreeNode() {
    }

    public AndroidTreeNode(String text) {
        setUserObject(text);
    }

    @Override
    public Rect getBounds() {
        return Rect.fromString(getAttribute("bounds"));
    }

    @Override
    public String getClassName() {
        return getAttribute("class");
    }

    public String getIndex() {
        return getAttribute("index");
    }

    @Override
    public String getId() {
        return getAttribute("resource-id");
    }

    @Override
    public String getText() {
        return getAttribute("text");
    }

    @Override
    public String getPageName() {
        return getPackageName();
    }

    public String getPackageName() {
        return getAttribute("package");
    }

    public String getContentDescName() {
        return getAttribute("content-desc");
    }

    @Override
    public String getTextContent() {
        return getTextContent(this, 0);
    }

    private String getTextContent(AndroidTreeNode node, int depth) {
        if (depth >= 10) return "";

        int count = node.getChildCount();
        String text = node.getText().trim();
        for(int i = 0; i < count; i++) {
            String tmp = getTextContent((AndroidTreeNode) node.getChildAt(i), depth + 1);
            if (!tmp.trim().isEmpty()) {
                if (!text.isEmpty()) text += " ";
                text += tmp;
            }
        }
        return text;
    }

    @Override
    public String getValidName() {
        String name = getText();
        if (name != null && !name.isEmpty()) {
            return Utility.sanitizeFilename(name);
        }
        name = this.getTextContent();
        if (name != null && !name.isEmpty()) {
            if (name.length() > 100) {
                name = name.substring(0, 99);
            }
            return Utility.sanitizeFilename(name);
        }
        name = getAttribute("content-desc");
        if (name != null && !name.isEmpty()) {
            return Utility.sanitizeFilename(name);
        }
        name = getId();
        if (name != null && !name.isEmpty()) {
            return name.replaceFirst("(\\S)+:id/", "");
        }
        name = getClassName();
        return name.replaceFirst("(android.(widget|view).)?", "") + " " + getAttribute("index");
    }

}
