/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prx.wellformer;

import prx.constant.CommonConstant;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import static prx.wellformer.SyntaxState.*;

/**
 *
 * @author Eden
 */
public class SyntaxWellformer {

    private Character quote;

    public String check(String src) {
        StringBuilder writer = new StringBuilder();
        StringBuilder openTag = new StringBuilder();
        StringBuilder closeTag = new StringBuilder();
        StringBuilder attrName = new StringBuilder();
        StringBuilder attrValue = new StringBuilder();
        StringBuilder content = new StringBuilder();

        Map<String, String> attributes = new HashMap<>();
        Stack<String> stack = new Stack<>();
        String state = CONTENT;
        boolean isEmptyTag = false, isOpenTag = false, isCloseTag = false;

        src = src + CommonConstant.SPACE;
        char[] reader = src.toCharArray();
        for (int i = 0; i < reader.length; i++) {
            char c = reader[i];

            switch (state) {
                case CONTENT:
                    if (LT == c) {
                        state = OPEN_BRACKET;
                        writer.append(content.toString().trim().replace(String.valueOf(CommonConstant.AMPERSAND), "&amp;"));
                    } else {
                        content.append(c);
                    }
                    break;
                case OPEN_BRACKET:
                    if (isStartTagChars(c)) {
                        state = OPEN_TAG_NAME;

                        isOpenTag = true;
                        isCloseTag = false;
                        isEmptyTag = false;
                        openTag.setLength(0);
                        openTag.append(c);
                    } else if (SLASH == c) {
                        state = CLOSE_TAG_SLASH;

                        isOpenTag = false;
                        isCloseTag = true;
                        isEmptyTag = false;
                    }
                    break;
                case OPEN_TAG_NAME:
                    if (isTagChars(c)) {
                        openTag.append(c);
                    } else if (isSpaceOrLineBreak(c)) {
                        state = TAG_INNER;

                        attributes.clear();
                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                    } else if (SLASH == c) {
                        state = EMPTY_SLASH;
                    }
                    break;
                case TAG_INNER:
                    if (isSpaceOrLineBreak(c)) {//loop

                    } else if (isStartAttrChars(c)) {
                        state = ATTR_NAME;
                        attrName.setLength(0);
                        attrName.append(c);
                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                    } else if (SLASH == c) {
                        state = EMPTY_SLASH;
                    }
                    break;
                case ATTR_NAME:
                    if (isAttrChars(c)) { //loop
                        attrName.append(c);
                    } else if (EQ == c) {
                        state = EQUAL;
                    } else if (isSpaceOrLineBreak(c)) {
                        state = EQUAL_WAIT;
                    } else {// Exception
                        // end tag empty value attribute
                        if (SLASH == c) {
                            attributes.put(attrName.toString(), CommonConstant.TRUE_STRING);
                            state = EMPTY_SLASH;
                        } else if (GT == c) {
                            // start tag empty value
                            attributes.put(attrName.toString(), CommonConstant.TRUE_STRING);
                            state = CLOSE_BRACKET;
                        }
                    }
                    break;
                case EQUAL_WAIT:
                    if (isSpaceOrLineBreak(c)) {//loop

                    } else if (EQ == c) {
                        state = EQUAL;
                    } else {// Exception
                        // next empty value attribute
                        if (isStartAttrChars(c)) {
                            attributes.put(attrName.toString(), CommonConstant.TRUE_STRING);
                            state = ATTR_NAME;

                            attrName.setLength(0);
                            attrName.append(c);
                        }
                    }
                    break;
                case EQUAL:
                    if (isSpaceOrLineBreak(c)) {//loop

                    } else if (S_QUOT == c || D_QUOT == c) {
                        quote = c;
                        state = ATTR_VALUE_Q;
                        attrValue.setLength(0);
                    } else if (!isSpaceOrLineBreak(c) && GT != c) {
                        state = ATTR_VALUE_NQ;

                        attrValue.setLength(0);
                        attrValue.append(c);
                    }
                    break;
                case ATTR_VALUE_Q:
                    if (quote != c) {//loop
                        attrValue.append(c);
                    } else if (quote == c) {
                        state = TAG_INNER;
                        attributes.put(attrName.toString(), attrValue.toString());
                    }
                    break;
                case ATTR_VALUE_NQ:
                    if (!isSpaceOrLineBreak(c) && GT != c) { // loop
                        attrValue.append(c);
                    } else if (isSpaceOrLineBreak(c)) {
                        state = TAG_INNER;
                        attributes.put(attrName.toString(), attrValue.toString());
                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                        attributes.put(attrName.toString(), attrValue.toString());
                    }
                    break;
                case EMPTY_SLASH:
                    if (GT == c) {
                        state = CLOSE_BRACKET;
                        isEmptyTag = true;
                    }
                    break;
                case CLOSE_TAG_SLASH: // </tag>
                    if (isStartTagChars(c)) {
                        state = CLOSE_TAG_NAME;

                        closeTag.setLength(0);
                        closeTag.append(c);
                    }
                    break;
                case CLOSE_TAG_NAME:
                    if (isTagChars(c)) {
                        closeTag.append(c);
                    } else if (isSpaceOrLineBreak(c)) {
                        state = WAIT_END_TAG_CLOSE;
                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                    }
                    break;
                case WAIT_END_TAG_CLOSE:
                    if (isSpaceOrLineBreak(c)) { //loop

                    } else if (GT == c) {
                        state = CLOSE_BRACKET;
                    }
                    break;
                case CLOSE_BRACKET:
                    if (isOpenTag) {
                        String openTagName = openTag.toString().toLowerCase();
                        if (INLINE_TAGS.contains(openTagName)) {
                            isEmptyTag = true;
                        }
                        writer.append(LT)
                                .append(openTagName)
                                .append(this.convertAttribute(attributes))
                                .append((isEmptyTag ? CommonConstant.SLASH : CommonConstant.EMPTY))
                                .append(CommonConstant.GT);
                        attributes.clear();

                        //STACK: push open-tag
                        if (!isEmptyTag) {
                            stack.push(openTagName);
                        }
                    } else if (isCloseTag) {
                        // STACK: pop out open-tag having the same name
                        // Case : An open-tag is missing => not in stack => ignore it
                        
                        // Case :A close-tag is missing => must appear in stack
                        String closeTagName = closeTag.toString().toLowerCase();
                        if (!stack.isEmpty() && stack.contains(closeTagName)) {
                            //write end tag until tagName in stack peek match with closeTag
                            while (!stack.isEmpty() && !stack.peek().equals(closeTagName)) {
                                // </tag>
                                writer.append(LT)
                                        .append(SLASH)
                                        .append(stack.pop())
                                        .append(GT);
                            }
                            if (!stack.isEmpty() && stack.peek().equals(closeTagName)) {
                                writer.append(LT)
                                        .append(SLASH)
                                        .append(stack.pop())
                                        .append(GT);
                            }
                        } // end-close-tag missing
                    }

                    if (LT == c) {
                        state = OPEN_BRACKET;
                    } else {
                        state = CONTENT;

                        content.setLength(0);
                        content.append(c);
                    }
                    break;
            }// end switch case
        } // end reader
        // pop out all left tags
        while (!stack.isEmpty()) {
            writer.append(LT)
                    .append(SLASH)
                    .append(stack.pop())
                    .append(GT);
        }
        return writer.toString();
    }

    public String convertAttribute(Map<String, String> attributes) {
        if (attributes.isEmpty()) {
            return CommonConstant.EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        // attrKey1="attrValue1" attrKey2="attrValue2" attrKey3="" 
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String value = entry.getValue()
                    .replaceAll(String.valueOf(CommonConstant.AMPERSAND), "&amp;")
                    .replaceAll(String.valueOf(CommonConstant.D_QUOT), "&quot;")
                    .replaceAll(String.valueOf(CommonConstant.S_QUOT), "&apos;")
                    .replaceAll(String.valueOf(CommonConstant.LT), "&lt;")
                    .replaceAll(String.valueOf(CommonConstant.GT), "&gt;");
            builder.append(entry.getKey())
                    .append(CommonConstant.EQ)
                    .append(CommonConstant.D_QUOT)
                    .append(value).append(CommonConstant.D_QUOT)
                    .append(CommonConstant.SPACE);
        }
        String result = builder.toString().trim();
        if (!CommonConstant.EMPTY.equals(result)) {
            result = CommonConstant.SPACE + result;
        }
        return result;
    }
}
