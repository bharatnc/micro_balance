package com.bharat.nallan.microbalance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Examples {

    public static void main(String args[]) throws InterruptedException, ExecutionException, IOException {
        ArrayList<String> sampleDomain = new ArrayList<String>() {{
            add("http://www.bharatnc.com/cv/");
            add("http://www.bharatnc.com/about/");
        }};

        String sampleContent = "Hello World!";
        RequestRoundRobin rr =  new RequestRoundRobin(sampleDomain);
        rr.get("");
        rr.post("", sampleContent);

    }
}