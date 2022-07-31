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

package dev.nurujjamanpollob.textparserlibtestpackage;


import dev.nurujjamanpollob.textparserlib.Template;
import dev.nurujjamanpollob.textparserlib.event.ParseEventListener;
import dev.nurujjamanpollob.textparserlib.exception.TemplateException;
import dev.nurujjamanpollob.textparserlib.parser.TextParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Class to test the method reference of the {@link dev.nurujjamanpollob.textparserlib.parser.TextParser#parseAsynchronously(ParseEventListener)}
 * <p>
 * If you would like to learn more about, see this doc: {@link dev.nurujjamanpollob.textparserlib.parser.TextParser}
 *
 * @author NurujJaman Pollob
 * @version 1.0.0
 * @since 1.0.0
 */
public class TextParserAsynchronousTest {

    /**
     * Parse two text asynchronously and print the result.
     * Note: There is effect of method calling stack time, so the result order might be different.
     * <p>
     * If you run test from {@link TextParserSynchronousTest#testParseLongText()},
     * you may notice that, after process two text, the time is still same, I mean the execution time, where this mentioned method is parsed one text.
     * <p>
     * So, this test is to test the asynchronous method, and it verified usages two thread to do the parsing of two text, at the same time, where the result order is not guaranteed.
     */
    @Test
    public void testParseAsynchronously() throws TemplateException {
        String textToParse = "Hi, I am *(name)* and I am *(age)* years old. I am a *(occupation)*. I am a *(nationality)*";
        String expectedOutput = "Hi, I am Nurujjaman Pollob and I am 23 years old. I am a Student. I am a Bangladeshi";

        // Define template instance
        Template template = new Template("*(", ")*");

        // Create a NEW instance of TextParser
        TextParser textParser = new TextParser(textToParse, template);

        // put identifiers and values
        textParser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        textParser.putVariableNameAndValue("age", "23");
        textParser.putVariableNameAndValue("occupation", "Student");
        textParser.putVariableNameAndValue("nationality", "Bangladeshi");

        // call parseAsynchronously() method with a ParseEventListener instance
        textParser.parseAsynchronously(new ParseEventListener() {

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} finishes parsing the text.
             * @param result result of the parsing
             */
            @Override
            public void onParseFinished(String result) {

                // Print the result
                System.out.println(result);

                // Assert the result
                Assertions.assertTrue(result.contains(expectedOutput));

            }

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} found an exception, see the exception for more information.
             */
            @Override
            public void onException(TemplateException templateException) {

                // Assert the exception
                Assertions.fail();

            }
        });


        // Create another instance of TextParser and pass the same text to parseAsynchronously() method
        TextParser textParser2 = new TextParser(textToParse, template);

        // Put identifiers and values
        textParser2.putVariableNameAndValue("name", "Nurujjaman Pollob");
        textParser2.putVariableNameAndValue("age", "23");
        textParser2.putVariableNameAndValue("occupation", "Student");
        textParser2.putVariableNameAndValue("nationality", "Bangladeshi");


        textParser2.parseAsynchronously(new ParseEventListener() {

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} finishes parsing the text.
             * @param result result of the parsing
             */
            @Override
            public void onParseFinished(String result) {

                // Print the result
                System.out.println(result);

                // Assert the result
                Assertions.assertTrue(result.contains(expectedOutput));

            }

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} found an exception, see the exception for more information.
             */
            @Override
            public void onException(TemplateException templateException) {

                System.out.println(templateException.getMessage());
                // Assert the exception
                Assertions.fail();

            }
        });

    }

    /**
     * Text to test the asynchronous method with a syntax error.
     */
    @Test
    public void testParseAsynchronouslyWithSyntaxError() throws TemplateException {
        String textToParse = "Hi, I am *(name and I am *(age)* years old.";

        // Define template instance
        Template template = new Template("*(", ")*");

        // Create a NEW instance of TextParser
        TextParser textParser = new TextParser(textToParse, template, true);

        // put identifier with their values
        textParser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        textParser.putVariableNameAndValue("age", "23");

        // call parseAsynchronously() method with a ParseEventListener instance
        textParser.parseAsynchronously(new ParseEventListener() {

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} finishes parsing the text.
             * @param result result of the parsing
             */
            @Override
            public void onParseFinished(String result) {

                // Assert the result
                Assertions.fail();

            }

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} found an exception, see the exception for more information.
             */
            @Override
            public void onException(TemplateException templateException) {

                System.out.println(templateException.getMessage());

                // Assert the exception
                Assertions.assertTrue(templateException.getMessage().contains("Syntax error"));

            }
        });

    }

    /**
     * Method to test the asynchronous method with a null text.
     */
    @Test
    public void testParseAsynchronouslyWithNullText() throws TemplateException {


        // Define template instance
        Template template = new Template("*(", ")*");

        // Create a NEW instance of TextParser
        TextParser textParser = new TextParser(null, template);

        // put identifier with their values
        textParser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        textParser.putVariableNameAndValue("age", "23");

        // call parseAsynchronously() method with a ParseEventListener instance
        textParser.parseAsynchronously(new ParseEventListener() {

            @Override
            public void onParseFinished(String result) {

                // If this method invoked, it means thrown in an error, so assert the error
                Assertions.fail();
            }

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} found an exception, see the exception for more information.
             */
            @Override
            public void onException(TemplateException templateException) {

                System.out.println(templateException.getMessage());

                // Assert the exception
                Assertions.assertTrue(templateException.getMessage().contains("Input string or template instance is null."));

            }
        });

    }

    /**
     * Method to test the asynchronous method with a null template.
     */
    @Test
    public void testParseAsynchronouslyWithNullTemplate() throws TemplateException {
        String textToParse = "Hi, I am *(name and I am *(age)* years old.";

        // Create a NEW instance of TextParser
        TextParser textParser = new TextParser(textToParse, null);

        // put identifier with their values
        textParser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        textParser.putVariableNameAndValue("age", "23");

        // call parseAsynchronously() method with a ParseEventListener instance
        textParser.parseAsynchronously(new ParseEventListener() {

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} finishes parsing the text.
             * @param result result of the parsing
             */
            @Override
            public void onParseFinished(String result) {

                // Assert the result
                Assertions.fail();

            }

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} found an exception, see the exception for more information.
             */
            @Override
            public void onException(TemplateException templateException) {

                System.out.println(templateException.getMessage());

                // Assert the exception
                Assertions.assertTrue(templateException.getMessage().contains("Input string, Identifier with values or template instance is null."));

            }
        });

    }

    /**
     * Method to test the asynchronous method with a null listener.
     */
    @Test
    public void testParseAsynchronouslyWithNullListener() throws TemplateException {
        String textToParse = "Hi, I am *(name and I am *(age)* years old.";

        // Define template instance
        Template template = new Template("*(", ")*");

        // Create a NEW instance of TextParser
        TextParser textParser = new TextParser(textToParse, template);

        // put identifier with their values
        textParser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        textParser.putVariableNameAndValue("age", "23");

        // call parseAsynchronously() method with a ParseEventListener instance
        try {
            textParser.parseAsynchronously(null);
        } catch (TemplateException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e.getMessage().contains("The listener cannot be null."));
        }

    }

    /**
     * Method to test the asynchronous method where a starting template is not closed.
     */
    @Test
    public void testParseAsynchronouslyWithCaseWhileIteratingOverText() throws TemplateException {
        String textToParse = "Hi, I am *(name)* and I am *(age years old.";

        // Define template instance
        Template template = new Template("*(", ")*");

        // Create a NEW instance of TextParser
        TextParser textParser = new TextParser(textToParse, template);

        // put identifier with their values
        textParser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        textParser.putVariableNameAndValue("age", "23");

        // call parseAsynchronously() method with a ParseEventListener instance
        textParser.parseAsynchronously(new ParseEventListener() {

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} finishes parsing the text.
             * @param result result of the parsing
             */
            @Override
            public void onParseFinished(String result) {

                // Assert the result
                Assertions.fail();

            }

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} found an exception, see the exception for more information.
             */
            @Override
            public void onException(TemplateException templateException) {

                System.out.println(templateException.getMessage());

                // Assert the exception
                Assertions.assertTrue(templateException.getMessage().contains("End template not found."));

            }
        });

    }
}
