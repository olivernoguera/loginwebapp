package com.onoguera.loginwebapp.view;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public class HtmlViewTest {

    private final static class MockHtmlView extends HtmlView{

        public MockHtmlView(String templateName) throws IOException {
            super(templateName);
        }
    }



    @Test
    public void htmlViewWithParams() throws IOException {

        HtmlViewTest.MockHtmlView mockHtmlView = new HtmlViewTest.MockHtmlView("testWithParams.html");
        String expectedValue1 = "mockParam1";
        String expectedValue2 = "mockParam2";

        Map<String,String> params = new HashMap<>();
        String outputWithoutParams = mockHtmlView.setOutput(params);

        String expectedOutputWithoutParams = "<html>\n" +
                "\n" +
                "    <body>\n" +
                "\n" +
                "        <h1></h1>\n" +
                "        <h2></h2>\n" +
                "    </body>\n" +
                "</html>\n";

        Assert.assertThat("HtmlViewTest htmlViewWithParams outPutWithoutParams",
                expectedOutputWithoutParams, is(outputWithoutParams));

        params.put("param1",expectedValue1);
        outputWithoutParams = mockHtmlView.setOutput(params);
        expectedOutputWithoutParams = expectedOutputWithoutParams.replaceAll("<h1></h1>","<h1>"+expectedValue1+"</h1>");

        Assert.assertThat("HtmlViewTest htmlViewWithParams output with one param",
                expectedOutputWithoutParams, is(outputWithoutParams));

        params.put("param2",expectedValue2);
        outputWithoutParams = mockHtmlView.setOutput(params);
        expectedOutputWithoutParams = expectedOutputWithoutParams.replaceAll("<h2></h2>","<h2>"+expectedValue2+"</h2>");
        Assert.assertThat("HtmlViewTest htmlViewWithParams output with two param",
                expectedOutputWithoutParams, is(outputWithoutParams));

    }

    @Test
    public void htmlViewWithBadParams() throws IOException {

        HtmlViewTest.MockHtmlView mockHtmlView = new HtmlViewTest.MockHtmlView("testWithParams.html");
        String outputWithoutParams = mockHtmlView.setOutput(null);

        String expectedOutputWithoutParams = "<html>\n" +
                "\n" +
                "    <body>\n" +
                "\n" +
                "        <h1></h1>\n" +
                "        <h2></h2>\n" +
                "    </body>\n" +
                "</html>\n";

        Assert.assertThat("HtmlViewTest htmlViewWithParams with null params",
                expectedOutputWithoutParams, is(outputWithoutParams));

    }


}
