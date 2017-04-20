package com.onoguera.loginwebapp.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by olivernoguera on 25/06/2016.
 */
public abstract class HtmlView {

    private final StringBuffer template;
    private final static Pattern pattern = Pattern.compile("\\:\\:(\\w+)\\:\\:");

    public HtmlView(String templateName) throws IOException {
        template = load(templateName);
    }

    private StringBuffer load(String templateName) throws IOException {
        StringBuffer buffer = new StringBuffer();
        String line;
        InputStream is = ClassLoader.getSystemResourceAsStream(templateName);
        if( is == null){
            throw new IOException("Not exists view with path"+templateName);
        }
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            do {
                line = reader.readLine();
                if(line != null) {
                    buffer.append(line + "\n");
                }
            } while(line != null);
        }
        return buffer;
    }

    public String setOutput(Map<String,String> values) {
        StringBuffer html = new StringBuffer(template);

        while(true) {

            Matcher matcher = pattern.matcher(html);
            if(matcher.find()) {
                String match = matcher.group(1);
                String value = values.get(match);
                if( value == null){
                    value = "";
                }
                html.replace(matcher.start(), matcher.end(), value);
            } else {
                return html.toString();
            }
        }
    }


}

