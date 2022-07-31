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

package dev.nurujjamanpollob.textparserlib.event;

import dev.nurujjamanpollob.textparserlib.exception.TemplateException;
import dev.nurujjamanpollob.textparserlib.parser.TextParser;

/**
 * @author Nurujjaman Pollob
 * @version 1.0.0
 * @since 1.0.0
 * @apiNote This interface is used to listen to the events of the asynchronous parsing process for this class {@link TextParser}.
 * @see TextParser#parseAsynchronously(ParseEventListener)  for more information on how to use this interface.
 */
public interface ParseEventListener {
    

    /**
     * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} finishes parsing the text.
     */
    default void onParseFinished(String result) {}

    /**
     * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} found an exception, see the exception for more information.
     */
    default void onException(TemplateException templateException) {}
}
