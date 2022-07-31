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
import dev.nurujjamanpollob.textparserlib.exception.TemplateException;
import dev.nurujjamanpollob.textparserlib.parser.TextParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class to test the {@link TextParser#parseSynchronously()} method with different params.
 * @author Nurujjaman Pollob
 * @version 1.0.0
 * @since 1.0.0
 * @see TextParser#parseSynchronously() for more details.
 */
public class TextParserSynchronousTest {


    /**
     * Test parse a simple String.
     */
    @Test
    public void testParseSimpleString() throws TemplateException {
        String text = "Hi, I am *(name)* and I am *(age)* years old.";
        String matchTo = "Hi, I am Nurujjaman Pollob and I am 23 years old.";

        // Create template instance
        Template template = new Template("*(", ")*");

        // Create parser instance
        TextParser parser = new TextParser(text, template);

        // Put name and age into the identifier map
        parser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        parser.putVariableNameAndValue("age", "23");

        // Assert and parse the text
        Assertions.assertTrue(parser.parseSynchronously().contains(matchTo));

    }

    /**
     * Test to parse a long set of text
     */
    @Test
    public void testParseLongText() throws TemplateException {
        String text = "Hi, I am *(name)* and I am *(age)* years old. I am a *(occupation)*. I am a *(nationality)*";
        String matchTo = "Hi, I am Nurujjaman Pollob and I am 23 years old. I am a Student. I am a Bangladeshi";

        // Create template instance
        Template template = new Template("*(", ")*");

        // Create parser instance
        TextParser parser = new TextParser(text, template);

        // Put name and age into the identifier map
        parser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        parser.putVariableNameAndValue("age", "23");
        parser.putVariableNameAndValue("occupation", "Student");
        parser.putVariableNameAndValue("nationality", "Bangladeshi");

        // Assert and parse the text
        Assertions.assertTrue(parser.parseSynchronously().contains(matchTo));
    }

    /**
     * Test parse a simple String with a variable name that is not in the template.
     */
    @Test
    public void testParseSimpleStringWithVariableNameNotInTemplate() throws TemplateException {
        String text = "Hi, I am *(name)* and I am *(age)* years old.";

        // Create template instance
        Template template = new Template("*(", ")*");

        // Create parser instance
        TextParser parser = new TextParser(text, template);

        // Put name and age into the identifier map
        parser.putVariableNameAndValue("name", "Nurujjaman Pollob");

        // Assert and parse the text
        TemplateException templateException = Assertions.assertThrows(TemplateException.class, parser::parseSynchronously);

        // assert execution message
        Assertions.assertEquals("Variable 'age' has no value passed. try to put value though constructor or putVariableNameAndValue method.", templateException.getMessage());

    }


    /**
     * Test parse a text that has started an identifier, and still not closed it, but while iterating the text, there is another identifier is started.
     * So, this should throw an exception.
     */
    @Test
    public void testParseTextWithIdentifierStartedButNotClosed() throws TemplateException {
        String text = "Hi, I am *(name and I am *(age)* years old.";

        // Create template instance
        Template template = new Template("*(", ")*");

        // Create parser instance with check for basic syntax error
        TextParser parser = new TextParser(text, template, true);

        // Put name and age into the identifier map
        parser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        parser.putVariableNameAndValue("age", "23");

        // Assert and parse the text
        TemplateException templateException = Assertions.assertThrows(TemplateException.class, parser::parseSynchronously);

        // assert execution message
        Assertions.assertEquals("Syntax error. Found another start template, while iterating for end template. There is likely a syntax error in the text. Please fix that first.", templateException.getMessage());

    }

    /**
     * Test parse a text, that has never closed an identifier.
     */
    @Test
    public void testParseTextWithNeverClosedIdentifier() throws TemplateException {
        String text = "Hi, I am *(name)* and I am *(age years old.";

        // Create template instance
        Template template = new Template("*(", ")*");

        // Create parser instance with check for basic syntax error
        TextParser parser = new TextParser(text, template, true);

        // Put name and age into the identifier map
        parser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        parser.putVariableNameAndValue("age", "23");

        // Assert and parse the text
        TemplateException templateException = Assertions.assertThrows(TemplateException.class, parser::parseSynchronously);

        // assert execution message
        Assertions.assertEquals("End template not found.", templateException.getMessage());

    }

    /**
     * Test parse with null text.
     */
    @Test
    public void testParseNullText() throws TemplateException {


        // Create template instance
        Template template = new Template("*(", ")*");

        // Create parser instance with check for basic syntax error
        TextParser parser = new TextParser(null, template, true);

        // Put name and age into the identifier map
        parser.putVariableNameAndValue("name", "Nurujjaman Pollob");
        parser.putVariableNameAndValue("age", "23");

        // Assert and parse the text
        TemplateException templateException = Assertions.assertThrows(TemplateException.class, parser::parseSynchronously);

        // assert execution message
        Assertions.assertEquals("Input string, Identifier with values or template instance is null.", templateException.getMessage());

    }



}
