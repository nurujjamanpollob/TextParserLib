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

package dev.nurujjamanpollob.textparserlib;

import dev.nurujjamanpollob.textparserlib.event.ParseEventListener;
import dev.nurujjamanpollob.textparserlib.exception.TemplateException;
import dev.nurujjamanpollob.textparserlib.parser.TextParser;

import java.util.HashMap;
import java.util.Objects;

/**
 * @apiNote Class that represents a template for a text. For example, a template can be like this:
 * <(var1)> <(var2)> <(var3)> so here we have three variables, var1, var2 and var3. that starts with <( and ends with )>.
 *
 * The job of this class to store the template that can be identified by their start and ending characters.
 * later on, the parser will use this information to identify the variables in the text.
 *
 * This class do not support no-arg constructor, or neither support inheritance.
 *
 * This class also store comment template information, so the comment will be ignored when parsing the text. This rule is used by parser class.
 * @author Nurujjaman Pollob
 * @version 1.0.0
 * @since 1.0.0
 *
 */
public final class Template {

    /**
     * Suppress default constructor for non-instantiability without args
     */
    private Template() {
        throw new AssertionError("This class is not intended to be instantiated without args");
    }

    private final String startTemplate;
    private final String endTemplate;

    /**
     * Constructor that initialize the template with the start and end characters.
     * @param startTemplate the start characters of the template.
     * @param endTemplate the end characters of the template.
     */
    public Template(String startTemplate, String endTemplate) throws TemplateException {

        // Check for template start and end characters.
        if (startTemplate == null || startTemplate.isEmpty() || endTemplate == null || endTemplate.isEmpty()) {
            throw new TemplateException("Template start or end character cannot be null or empty");
        }
        this.startTemplate = startTemplate;
        this.endTemplate = endTemplate;
    }

    /**
     * Get the start template character.
     * @return the start template character.
     */
    public String getStartTemplate() {
        return startTemplate;
    }

    /**
     * Get the end template character.
     * @return the end template character.
     */
    public String getEndTemplate() {
        return endTemplate;
    }



    @Override
    public String toString() {
        return "Template{" +
                "startTemplate='" + startTemplate + '\'' +
                ", endTemplate='" + endTemplate + '\'' +
                '}';
    }

    /**
     *@apiNote Method to parse the text synchronously with the template instance. This method works same as the {@link TextParser#parseSynchronously()} method.
     * @param text the text to parse.
     * @param keyValueSets the set of identifier and their values, to look up at text and replace with their values.
     * @return the parsed text.
     * @throws TemplateException you are requested to check the exception for the specific error.
     */
    public String parseSynchronously(String text, HashMap<String, String> keyValueSets) throws TemplateException {

        return new TextParser(text, this, keyValueSets).parseSynchronously();
    }

    /**
     *@apiNote Method to parse the text synchronously with the template instance. This method works same as the {@link TextParser#parseSynchronously()} method.
     * @param text the text to parse.
     * @param keyValueSets the set of identifier and their values, to look up at text and replace with their values.
     * @param isCheckForBasicSyntaxError if true, the parser will check for basic syntax error, otherwise it will not.
     * @return the parsed text.
     * @throws TemplateException You are requested to check the exception for the specific error.
     */
    public String parseSynchronously(String text, HashMap<String, String> keyValueSets, Boolean isCheckForBasicSyntaxError) throws TemplateException {

        return new TextParser(text, this, keyValueSets, isCheckForBasicSyntaxError).parseSynchronously();
    }

    /**
     * @apiNote  Method to parse the text asynchronously with the template instance. This method works same as the {@link TextParser#parseAsynchronously(ParseEventListener)} method.
     * @param text the text to parse.
     * @param keyValueSets the set of identifier and their values, to look up at text and replace with their values.
     * @param parseEventListener the listener to receive the event callbacks.
     * @throws TemplateException You are requested to check the exception for the specific error.
     */
    public void parseAsynchronously(String text, HashMap<String, String> keyValueSets, ParseEventListener parseEventListener) throws TemplateException {

        new TextParser(text, this, keyValueSets).parseAsynchronously(parseEventListener);
    }

    /**
     * @apiNote  Method to parse the text asynchronously with the template instance. This method works same as the {@link TextParser#parseAsynchronously(ParseEventListener)} method.
     * @param text the text to parse.
     * @param keyValueSets the set of identifier and their values, to look up at text and replace with their values.
     * @param parseEventListener the listener to receive the event callbacks.
     * @param isCheckForBasicSyntaxError if true, the parser will check for basic syntax error, otherwise it will not.
     * @throws TemplateException You are requested to check the exception for the specific error.
     */
    public void parseAsynchronously(String text, HashMap<String, String> keyValueSets, ParseEventListener parseEventListener, Boolean isCheckForBasicSyntaxError) throws TemplateException {

        new TextParser(text, this, keyValueSets).parseAsynchronously(parseEventListener);
    }

}
