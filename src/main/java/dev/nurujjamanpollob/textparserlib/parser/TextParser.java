/*
 * Copyright (c) 2022 Nurujjaman Pollob, All Right Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 * If you have contributed in codebase,
 * and want to add your name or copyright in a particular class or method,
 * you must follow this following pattern:
 * <code>
 *     // For a new method created by you,
 *     //like this example method with name fooMethod()
 *     //then use following format:
 *
 *     >>>
 *     @author $Name and $CurrentYear.
 *     $Documentation here.
 *     $Notes
 *     public boolean fooMethod(){}
 *     <<<
 *
 *     // For an existing method
 *
 *     >>>
 *     $Current Method Documentation(Update if needed)
 *
 *     Updated by $YourName
 *     $Update summery
 *     $Notes(If any)
 *     <<<
 *
 *     // For a new class of file, that is not created by anyone else
 *     >>>
 *     Copyright (c) $CurrentYear $Name, All right reserved.
 *
 *     $Copyright Text.
 *     $Notes(If Any)
 *     <<<
 *
 *     // For a existing class, if you want to add your own copyright for your work.
 *
 *     >>>
 *     $Current Copyright text
 *
 *     $YourCopyrightText
 *     <<<
 *
 *     Done! Clean code!!
 * </code>
 */

package dev.nurujjamanpollob.textparserlib.parser;

import dev.nurujjamanpollob.textparserlib.KeyObjects;
import dev.nurujjamanpollob.textparserlib.Template;
import dev.nurujjamanpollob.textparserlib.Variable;
import dev.nurujjamanpollob.textparserlib.event.ParseEventListener;
import dev.nurujjamanpollob.textparserlib.exception.TemplateException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Nurujjaman Pollob
 * @version 1.0.0
 * @since 1.0.0
 * @apiNote This class is used to parse text. It works in both asynchronous and synchronous mode.
 * You can register listener in order to receive callback for registration event.
 * This method usages a short-lived background thread to parse text. If you want to scale your hardware and divide workload with non-blocking approach, the asynchronous mode is recommended.
 * You can also use this class to parse text in synchronous mode. synchronous mode is recommended for short text parsing, with no scaling with separate thread.
 * @see TextParser#parseAsynchronously(ParseEventListener)
 * @see TextParser#parseSynchronously()
 */
public class TextParser {


    /**
     * Suppress default constructor for non-instantiability without parameters.
     */
    private TextParser() {
        throw new AssertionError("This class cannot be instantiated without parameters.");
    }

    private final String textToParse;
    private final Template template;
    private final Map<String, String> keyValuePairs;
    private final Boolean isCheckForBasicSyntax;

    /**
     * Create an instance of TextParser class.
     * @param textToParse the text to parse.
     *                    This text will be parsed according to definition of {@link Template} class.
     * @param template the template that will be used to determine the variables and comments in the text.
     * @param keyValuePairs the key-value pairs that will be used to replace the variables in the text.
     *
     */
    public TextParser(String textToParse, Template template, Map<String, String> keyValuePairs) {
        // Split the text into lines.
        this.textToParse = textToParse + "\n";
        this.template = template;
        this.keyValuePairs = keyValuePairs;
        this.isCheckForBasicSyntax = false;
    }

    /**
     * Create an instance of TextParser class.
     * @param textToParse the text to parse.
     *                    This text will be parsed according to definition of {@link Template} class.
     * @param template the template that will be used to determine the variables and comments in the text.
     * @param keyValuePairs the key-value pairs that will be used to replace the variables in the text.
     * @param isUseBasicSyntaxChecking if true, the parser will check for basic syntax errors.
     */
    public TextParser(String textToParse, Template template, Map<String, String> keyValuePairs, Boolean isUseBasicSyntaxChecking) {
        // Split the text into lines.
        this.textToParse = textToParse + "\n";
        this.template = template;
        this.keyValuePairs = keyValuePairs;
        this.isCheckForBasicSyntax = isUseBasicSyntaxChecking;
    }

    /**
     * Create an instance of TextParser class.
     * @param textToParse the text to parse.
     *                    This text will be parsed according to definition of {@link Template} class.
     * @param template the template that will be used to determine the variables and comments in the text.
     * @param isUseBasicSyntaxChecking if true, the parser will check for basic syntax errors.
     */
    public TextParser(String textToParse, Template template, Boolean isUseBasicSyntaxChecking) {
        // Split the text into lines.
        this.textToParse = textToParse + "\n";
        this.template = template;
        this.keyValuePairs = new HashMap<>();
        this.isCheckForBasicSyntax = isUseBasicSyntaxChecking;
    }

    /**
     * Create an instance of TextParser class.
     * @param textToParse the text to parse.
     *                    This text will be parsed according to definition of {@link Template} class.
     * @param template the template that will be used to determine the variables and comments in the text.
     */
    public TextParser(String textToParse, Template template) {
        // Split the text into lines.
        this.textToParse = textToParse + "\n";
        this.template = template;
        this.keyValuePairs = new HashMap<>();
        this.isCheckForBasicSyntax = false;
    }


    /**
     * @apiNote Parse the text synchronously.
     * @return the parsed text.
     * @throws TemplateException if any error occurs during parsing.
     */
    public String parseSynchronously() throws TemplateException{

        return parseWithoutComment();
    }

    /**
     * @apiNote Parse the text asynchronously.
     * This method usages a short-lived background thread to parse text. If you want to scale your CPU threads and divide workload, the asynchronous mode is recommended.
     *
     * This method usages {@link ExecutorService} to run the parsing task, and shutdown immediately when the task is finished or received error.
     * @param listener the listener that will be used to receive callback for registration event.
     * @throws TemplateException if any error occurs during parsing or the listener is null.
     */
    public void parseAsynchronously(ParseEventListener listener) throws TemplateException{

        if (listener == null) {
            throw new TemplateException("The listener cannot be null.");
        }

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {

                    String parsedText = parseWithoutComment();
                    listener.onParseFinished(parsedText);

                    executor.shutdown();
                } catch (TemplateException e) {
                        listener.onException(e);
                        executor.shutdown();
                }
            });



    }

    /**
     * method to parse text
     */
    private String parseWithoutComment() throws TemplateException {

        // check if input string is null
        if (template == null || keyValuePairs == null || textToParse.equals(null + "\n")) {
            throw new TemplateException("Input string, Identifier with values or template instance is null.");
        }

        final StringBuilder parsedText = new StringBuilder();
        final int textLen = textToParse.length();
        final int templateStartLen = template.getStartTemplate().length();
        final int templateEndLen = template.getEndTemplate().length();

        boolean isStarted = false;


        for(int i = 0; i < textToParse.length(); i++){

            if(i + templateStartLen < textLen){

                String buffer = textToParse.substring(i, i + templateStartLen);

                // Start template found.
                if (buffer.equals(template.getStartTemplate())){

                    // The key
                    String key;

                    // Buffer for end template.
                    for (int j = i + templateStartLen; j < textLen; j++) {

                        // Check for syntax error only when isCheckForBasicSyntax is true.
                        if(isCheckForBasicSyntax && j + templateStartLen < textLen){

                            // Create a buffer of startTemplate, so during iterating, if we found another start point, this is likely a syntax error.
                            String buffer2 = textToParse.substring(j, j + templateStartLen);

                            // If we found another start template, this is likely a syntax error.
                            if (buffer2.equals(template.getStartTemplate())) {
                                throw new TemplateException("Syntax error. Found another start template, while iterating for end template. There is likely a syntax error in the text. Please fix that first.");
                            }

                        }
                        // Avoid null pointer
                        if(j + templateEndLen < textLen){

                            String endBuf = textToParse.substring(j, j + templateEndLen);

                            if(endBuf.equals(template.getEndTemplate())){


                                // End template found.
                                key = textToParse.substring(i + templateStartLen, j);
                                // Replace the variable with the value.
                                parsedText.append(getValueFromKeyValuePairs(key));

                                // Move the pointer to the end of end template.
                                i = j + templateEndLen - 1;

                                // Break the loop.
                                break;
                            }

                        } else {

                            // ?? Why not closing text with ending identifier?
                            throw new TemplateException("End template not found.");
                        }
                    }
                }

                else {

                    // Append the character to the parsed text.
                    parsedText.append(textToParse.charAt(i));
                }

            }else {

                // append to result
                parsedText.append(textToParse.charAt(i));
            }

        }


        // Return the parsed text.
        return parsedText.toString();

    }

    /**
     * Method to get the value from key-value pairs from a key.
     * @param key the key to get the value from.
     * @return value the value from the key.
     * @throws TemplateException if the key is not found in the key-value pairs.
     */
    private String getValueFromKeyValuePairs(String key) throws TemplateException{

        // So, here, we got the key, this may contain parameters or flags, we need to read them all.

        for (Map.Entry<String, String> entry : keyValuePairs.entrySet()) {
            if(entry.getKey().equals(key)){
                return entry.getValue();
            }
        }

        throw new TemplateException("Variable '" + key + "' has no value passed. try to put value though constructor or putVariableNameAndValue method.");
    }

    /**
     * Method to get library version.
     * @return library version.
     */
    public static String getVersion() {
        return Variable.VERSION;
    }

    /**
     * This method used to define the identifier with values, that will be used to replace the variables in the text.
     */
    public void putVariableNameAndValue(String variableName, String variableValue){
        keyValuePairs.put(variableName, variableValue);
    }

    //TODO: Need implementation
    private KeyObjects generateKeyObjectFromIdentifier(String keyIdentifier){

        Boolean isOptional = false;
        String key = "";
        String keyDefValue = "";

        // First check, if the key started with quotation mark, then it is optional.

        boolean isSpaceMatched = false;

        for (int i = 0; i < keyIdentifier.length(); i++) {

            if(keyIdentifier.charAt(i) != ' '){
                isSpaceMatched = true;
            }

            // check for quotation mark, if found, then it is optional.
            //



        }

        return new KeyObjects(isOptional, key, keyDefValue);
    }

}
