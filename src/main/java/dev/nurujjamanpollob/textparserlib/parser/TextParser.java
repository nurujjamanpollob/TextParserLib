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
 * @version {@value Variable#VERSION}
 * @apiNote This class is used to parse text. It works in both asynchronous and synchronous mode.
 * You can register listener in order to receive callback for registration event.
 * This method usages a short-lived background thread to parse text. If you want to scale your hardware and divide workload with non-blocking approach, the asynchronous mode is recommended.
 * You can also use this class to parse text in synchronous mode. synchronous mode is recommended for short text parsing, with no scaling with separate thread.
 *
 * To create a new instance of this class, you must pass a {@link Template} object. The {@link Template} object is used to determine identifiers, later on they will be replaced by their values.
 * This code example show you how to do it with synchronous mode:
 * <pre>
 *     <code>
 *         String text = "Hi, I am *(name)* and I am *(age)* years old.";
 *
 *         // Create template instance
 *         Template template = new Template("*(", ")*");
 *
 *         // Create parser instance
 *         TextParser parser = new TextParser(text, template);
 *
 *         // Put name and age into the identifier map
 *         parser.putVariableNameAndValue("name", "Nurujjaman Pollob");
 *         parser.putVariableNameAndValue("age", "23");
 *
 *         // Get parsed text
 *         String parsedText = parser.parseSynchronously();
 *     </code>
 * </pre>
 *
 * Identifier that define by their name and no question mark (?) are mandatory, and their value should be passed as a key-value pair to the {@link #putVariableNameAndValue(String, String)} method or using the constructor
 * {@link #TextParser(String, Template, Map)} method. You can also define optional identifiers, which are defined by their name and question mark (?) ath the front of identifier name. Optional identifiers must have a parameter named "defVal="Value". Else, the library throw error.
 *
 * Look at this little example: <b>*(?name defVal="John Doe")*</b>, here we define an optional parameter with name "name" and default value "John Doe". The default value only be used when there is no value is passed for this identifier.
  * As this library is used a double quote to indicate an end point of a text, so the problem is that the default value is enclosed by double quote. So, If you need to add a double quote in the default value, you need to escape it using * character.
 * @see TextParser#parseAsynchronously(ParseEventListener)
 * @see TextParser#parseSynchronously()
 * @since 1.0.0
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
     *
     * @param textToParse   the text to parse.
     *                      This text will be parsed according to definition of {@link Template} class.
     * @param template      the template that will be used to determine the variables and comments in the text.
     * @param keyValuePairs the key-value pairs that will be used to replace the variables in the text.
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
     *
     * @param textToParse              the text to parse.
     *                                 This text will be parsed according to definition of {@link Template} class.
     * @param template                 the template that will be used to determine the variables and comments in the text.
     * @param keyValuePairs            the key-value pairs that will be used to replace the variables in the text.
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
     *
     * @param textToParse              the text to parse.
     *                                 This text will be parsed according to definition of {@link Template} class.
     * @param template                 the template that will be used to determine the variables and comments in the text.
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
     *
     * @param textToParse the text to parse.
     *                    This text will be parsed according to definition of {@link Template} class.
     * @param template    the template that will be used to determine the variables and comments in the text.
     */
    public TextParser(String textToParse, Template template) {
        // Split the text into lines.
        this.textToParse = textToParse + "\n";
        this.template = template;
        this.keyValuePairs = new HashMap<>();
        this.isCheckForBasicSyntax = false;
    }


    /**
     * @return the parsed text.
     * @throws TemplateException if any error occurs during parsing.
     * @apiNote Parse the text synchronously.
     */
    public String parseSynchronously() throws TemplateException {

        return parseWithoutComment();
    }

    /**
     * @param listener the listener that will be used to receive callback for registration event.
     * @throws TemplateException if any error occurs during parsing or the listener is null.
     * @apiNote Parse the text asynchronously.
     * This method usages a short-lived background thread to parse text. If you want to scale your CPU threads and divide workload, the asynchronous mode is recommended.
     * <p>
     * This method usages {@link ExecutorService} to run the parsing task, and shutdown immediately when the task is finished or received error.
     */
    public void parseAsynchronously(ParseEventListener listener) throws TemplateException {

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


        for (int i = 0; i < textToParse.length(); i++) {

            if (i + templateStartLen < textLen) {

                String buffer = textToParse.substring(i, i + templateStartLen);

                // Start template found.
                if (buffer.equals(template.getStartTemplate())) {

                    // The key
                    String key;

                    // Buffer for end template.
                    for (int j = i + templateStartLen; j < textLen; j++) {

                        // Check for syntax error only when isCheckForBasicSyntax is true.
                        if (isCheckForBasicSyntax && j + templateStartLen < textLen) {

                            // Create a buffer of startTemplate, so during iterating, if we found another start point, this is likely a syntax error.
                            String buffer2 = textToParse.substring(j, j + templateStartLen);

                            // If we found another start template, this is likely a syntax error.
                            if (buffer2.equals(template.getStartTemplate())) {
                                throw new TemplateException("Syntax error. Found another start template, while iterating for end template. There is likely a syntax error in the text. Please fix that first.");
                            }

                        }
                        // Avoid null pointer
                        if (j + templateEndLen < textLen) {

                            String endBuf = textToParse.substring(j, j + templateEndLen);

                            if (endBuf.equals(template.getEndTemplate())) {


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
                } else {

                    // Append the character to the parsed text.
                    parsedText.append(textToParse.charAt(i));
                }

            } else {

                // append to result
                parsedText.append(textToParse.charAt(i));
            }

        }


        // Return the parsed text.
        return parsedText.toString();

    }

    /**
     * Method to get the value from key-value pairs from a key.
     *
     * @param key the key to get the value from.
     * @return value the value from the key.
     * @throws TemplateException if the key is not found in the key-value pairs.
     */
    private String getValueFromKeyValuePairs(String key) throws TemplateException {

        // So, here, we got the key, this may contain parameters or flags, we need to read them all.
        KeyObjects keyObjects = generateKeyObjectFromIdentifier(key);

        // check if value is optional
        String val = valueFromSets(keyObjects.getKeyName());

        if (keyObjects.isOptional()) {

            return val != null ? val : keyObjects.getOptionalValue();
        } else {

            if (val != null) {
                return val;
            } else {
                throw new TemplateException("Variable '" + keyObjects.getKeyName() + "' has no value passed. try to put value though constructor or putVariableNameAndValue method.");
            }

        }


    }

    private String valueFromSets(String key) {
        for (Map.Entry<String, String> entry : keyValuePairs.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Method to get library version.
     *
     * @return library version.
     */
    public static String getVersion() {
        return Variable.VERSION;
    }

    /**
     * This method used to define the identifier with values, that will be used to replace the variables in the text.
     */
    public void putVariableNameAndValue(String variableName, String variableValue) {
        keyValuePairs.put(variableName, variableValue);
    }

    /**
     * This method extract all necessary information from the identifier.
     *
     * @param keyData the identifier to extract information from.
     * @return the keyObjects.
     * @throws TemplateException if there is key is optional but no optional value is provided.
     */
    private KeyObjects generateKeyObjectFromIdentifier(String keyData) throws TemplateException {

        boolean isOptional = false;

        // Check if the key started with ? mark, this should be optional.
        if (keyData.startsWith("?")) {

            String[] keyAndDefVal = extractIdentifierNameAndDefValue(keyData.substring(1));
            return new KeyObjects(true, keyAndDefVal[0], keyAndDefVal[1]);
        }

        return new KeyObjects(isOptional, keyData, null);
    }


    /**
     * Method to extract identifier name and defValue from the key data
     *
     * @param keyData the key data containing identifier name and def value.
     * @return KeyObjects the key object containing identifier name and def value. The first element is the identifier name, the second element is the def value.
     * @throws TemplateException if the key data is not valid, such as missing identifier name or def value.
     */
    private String[] extractIdentifierNameAndDefValue(String keyData) throws TemplateException {

        String[] identifierNameAndDefValue = new String[2];

        StringBuilder keyExtraction = new StringBuilder();
        StringBuilder defValueExtraction = new StringBuilder();
        boolean isDefValueMatched = false;


        // First loop start to extract key
        for (int i = 0; i < keyData.length(); i++) {

            // Stop extract identifier when a whitespace is found.
            if (Character.isWhitespace(keyData.charAt(i))) {

                // Add to identifier name.
                identifierNameAndDefValue[0] = keyExtraction.toString();

                // Validation for NullPointer
                if (i + 1 >= keyData.length()) {
                    // There is likely no def value, an optional parameter must have a def value
                    throw new TemplateException("Missing def value for optional parameter.");
                }

                // There is possible index, let's check if there is def value exists
                else {

                    // Define def value parameter identifier
                    String defValueIdentifier = "defVal=\"";
                    // second loo[ to extract def value
                    for (int j = i + 1; j < keyData.length(); j++) {

                        // Read by buffer size of defValueIdentifier.
                        if (j + defValueIdentifier.length() < keyData.length()) {
                            String buffer = keyData.substring(j, j + defValueIdentifier.length());
                            if (buffer.equals(defValueIdentifier)) {
                                isDefValueMatched = true;

                                // Extract def value by using third loop
                                for (int k = j + defValueIdentifier.length(); k < keyData.length(); k++) {

                                    char c = keyData.charAt(k);

                                    // End point, you can use double quotes to end the def value, and If prev char is \, then it is an escape char.
                                    if (c == '"' && keyData.charAt(k - 1) != '*') {
                                        identifierNameAndDefValue[1] = defValueExtraction.toString();
                                        break;
                                    }

                                    else if( k + 1 <= keyData.length() && (c == '*' && keyData.charAt(k + 1) == '"')) {

                                        // Add the escape char to the def value.
                                        defValueExtraction.append('"');
                                        k++;
                                    }
                                    else {
                                        defValueExtraction.append(c);
                                    }


                                }


                            }
                        }


                    }
                }
            } else {

                // Append to key extraction.
                keyExtraction.append(keyData.charAt(i));
            }

        }

        if (!isDefValueMatched) {
            throw new TemplateException("Def value not found, you can define one using defVal=\"defValue\"");
        }
        return identifierNameAndDefValue;
    }

}
