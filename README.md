# TextParserLib
A light-weight Java library to parse text with identifier and then replace the identifier with its value. supports both synchronous and asynchronous mode.

As this library is very simple, it's also very easy to work with. Consider this scenario, where you need to parse this text:

<pre>
    <code> Hi, I am *(name)* and I am *(age)* years old. I am a *(occupation)*. I am a *(nationality)* </code>
    
</pre>

So, if you closely look at this example, you will figure out that, there is four identifier <b>(name, age, occupation, nationality) </b>  <br />

and start tag of *( and

the end tag of )* <b>(Sorry for Github is interpreting my text wrong, so I can't maintain good formatting) </b>

So, this library read them by factors such as tag start, tag end and identifier text range.

So, later on, those tag with identifier will be replaced by their value passed with key value pair, for example <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/d66a87b575e6401642193f36e0b2d10f3abfddf2/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java#L106">public TextParser(String textToParse, Template template, Map<String, String> keyValuePairs)</a>


Or passed by method reference from <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/d66a87b575e6401642193f36e0b2d10f3abfddf2/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java#L323">TextParser#putVariableNameAndValue(String variableName, String variableValue)</a>

Okay, let's dive into implementation part, where, I will cover both asynchronous and synchronous implementation technic with code example.

## Building this library

<b>cd</b> to the root directory of this project. This project is usages gradle <b><i>7.4</i></b>, so you must have this version or later version in the <b>PATH</b>.

To check, which version you have installed, run <pre><code>gradle --version</code></pre>

Now, run this command to build the project:

<pre><code>gradle jar</code></pre>

The output will be there -> <b><i>build/libs/textparserlib-{version}.jar</i></b>

## Synchronous Implementation

So, let's parse this text:

<pre>
  <code> Hi, I am *(name)* and I am *(age)* years old. </code>
</pre>

We want the output of <pre> <code> Hi, I am Nurujjaman Pollob and I am 23 years old. </code> </pre>

So, let's create a variable for input String

  <pre><code>String text = "Hi, I am *(name)* and I am *(age)* years old.";</code></pre>
  
 Create a new instance of <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/main/java/dev/nurujjamanpollob/textparserlib/Template.java">Template</a>
 
 <pre>
    <code> Template template = new Template("*(", ")*");</code> 
</pre>

Create a new instance of <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/d66a87b575e6401642193f36e0b2d10f3abfddf2/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java">TextParser</a><b>(You can explore all constructor params to fit your needs)</b>

In this example, we are going to use <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/d66a87b575e6401642193f36e0b2d10f3abfddf2/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java#L151">public TextParser(String textToParse, Template template) </a>

<pre>
  <code> TextParser parser = new TextParser(text, template); </code>
</pre>

Now, put the identifier name sets with with value by calling <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/d66a87b575e6401642193f36e0b2d10f3abfddf2/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java#L323">TextParser#putVariableNameAndValue(String variableName, String variableValue)</a>
You can so the same job passing <b>Hashmap<String, String> keyValSets; </b> with constructor.

<pre>
  <code> parser.putVariableNameAndValue("name", "Nurujjaman Pollob");
         parser.putVariableNameAndValue("age", "23");
  </code>
</pre>

Now, call <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/d66a87b575e6401642193f36e0b2d10f3abfddf2/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java#L165">TextParser#parseSynchronously()</a> to get parsed text.

<pre>
    <code> String parsedText = parser.parseSynchronously() </code>
 </pre>
 
 You can also skip creating a new instance of <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java">TextParser</a>, instead, calling <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/main/java/dev/nurujjamanpollob/textparserlib/Template.java#L144">Template#parseSynchronously(String text, HashMap<String, String> keyValueSets)</a> will do same job!
 
 
 In case, you need to study the synchronous implementation a little bit more, you can check this Unit test class: <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/test/java/dev/nurujjamanpollob/textparserlibtestpackage/TextParserSynchronousTest.java">TextParserSynchronousTest.java</a>
 
 
 ## Asynchronous implementation
 
 asynchronous implementation works in almost same way. All you need to call <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java#L178">TextParser#parseAsynchronously(ParseEventListener listener)</a> with a valid new instance of <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/main/java/dev/nurujjamanpollob/textparserlib/event/ParseEventListener.java"> ParseEventListener.java</a>
 
 This code fragment shows this in real action:
 
 <pre>
 	<code>
	    
		String textToParse = "Hi, I am *(name)* and I am *(age)* years old. I am a *(occupation)*. I am a *(nationality)*";

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
			
			    // Do action on call success

            }

            /**
             * This method is invoked when {@link dev.nurujjamanpollob.textparserlib.parser.TextParser} found an exception, see the exception for more information.
             */
            @Override
            public void onException(TemplateException templateException) {

                // Do action on call failed
				

            }
        });
		
	</code>
</pre>

You can also skip create a new instance of <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/main/java/dev/nurujjamanpollob/textparserlib/parser/TextParser.java">TextParser</a>, instead calling <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/main/java/dev/nurujjamanpollob/textparserlib/Template.java#L169"> Template#parseAsynchronously(String text, HashMap<String, String> keyValueSets, ParseEventListener parseEventListener) </a> can do the same job!

If you want to study the asynchronous implementation a bit, you can look at this unit test class from here: <a href="https://github.com/nurujjamanpollob/TextParserLib/blob/master/src/test/java/dev/nurujjamanpollob/textparserlibtestpackage/TextParserAsynchronousTest.java">TextParserAsynchronousTest.java</a>


Any contribution, suggestions are highly welcome.
 

 


 
 

