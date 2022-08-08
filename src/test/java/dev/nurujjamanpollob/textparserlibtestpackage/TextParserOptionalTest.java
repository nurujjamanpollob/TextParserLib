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

public class TextParserOptionalTest {

    /**
     * Method to test a simple identifier, that defined an optional value, but no value passed in key-set
     */
    @Test
    public void simpleTestWithOptionalIdentifier() throws TemplateException {
        String text = "Hi, I am *(?name defVal=\"John Doe\")* and I am *(?age defVal=\"23\")* years old.";
        String matchTo = "Hi, I am John Doe and I am 23 years old.";

        // Create template instance
        Template template = new Template("*(", ")*");

        // Create parser instance
        TextParser parser = new TextParser(text, template);

        // Assert and parse the text
        Assertions.assertTrue(parser.parseSynchronously().contains(matchTo));
    }

    /**
     * Method to test a simple identifier, that defined an optional value, the default value will be overridden by the value passed in key-set
     */
    @Test
    public void simpleTestWithOptionalIdentifierAndOverrideDefaultValue() throws TemplateException {
        String text = "Hi, I am *(?name defVal=\"John\")* and I am *(?age defVal=\"20\")* years old.";
        String matchTo = "Hi, I am Nurujjaman Pollob and I am 23 years old.";

        // Create template instance
        Template template = new Template("*(", ")*");
        // Create parser instance
        TextParser parser = new TextParser(text, template);


        parser.putVariableNameAndValue("name", "Nurujjaman Pollob");

        // Override the age identifier
        parser.putVariableNameAndValue("age", "23");

        Assertions.assertTrue(parser.parseSynchronously().contains(matchTo));
    }

    /**
     * Test to assert Escaped character in the text, will be ignored by the parser
     * You can define escaped character followed by <b>*"</b>
     */
    @Test
    public void testWithEscapedCharacter() throws TemplateException {
        String text = "Hi, I am *(?name defVal=\"*\"John*\"\")* and I am *(?age defVal=\"20\")* years old.";
        String matchTo = "Hi, I am \"John\" and I am 20 years old.";

        // Create template instance
        Template template = new Template("*(", ")*");
        // Create parser instance
        TextParser parser = new TextParser(text, template);

        Assertions.assertTrue(parser.parseSynchronously().contains(matchTo));
    }

    /**
     * Test Option identifier with a nil defVal, it should not throw any exception
     */
    @Test
    public void testWithOptionalIdentifierWithNilDefaultValue() throws TemplateException {
        String text = "Hi, I am *(?name defVal=\"\")* and I am *(?age defVal=\"20\")* years old.";
        String matchTo = "Hi, I am  and I am 20 years old.";

        // Create template instance
        Template template = new Template("*(", ")*");
        // Create parser instance
        TextParser parser = new TextParser(text, template);

        Assertions.assertTrue(parser.parseSynchronously().contains(matchTo));
    }

    /**
     * This method test Optional Parameter, that do not define parameter called "defVal" and should throw an exception
     */
    @Test
    public void simpleTestWithOptionalIdentifierAndNoDefaultValue() throws TemplateException {
        String text = "Hi, I am *(?name)* and I am *(?age)* years old.";
        String matchTo = "Hi, I am Nurujjaman Pollob and I am 23 years old.";

        // Create template instance
        Template template = new Template("*(", ")*");
        // Create parser instance
        TextParser parser = new TextParser(text, template);

        // Override the age identifier
        parser.putVariableNameAndValue("age", "23");

        // Override the name identifier
        parser.putVariableNameAndValue("name", "Nurujjaman Pollob");

        // Assert Exception
        TemplateException templateException = Assertions.assertThrows(TemplateException.class, parser::parseSynchronously);

        // Assert Exception message
        Assertions.assertTrue(templateException.getMessage().contains("Def value not found, you can define one using defVal=\"defValue\""));

    }



}
