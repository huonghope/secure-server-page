package com.openeg.openegscts.solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread
{
    InputStream is;

    public StreamGobbler(InputStream is) {
        this.is = is;
    }

    public void run()
    {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null) {
            }
        } catch (IOException ioe) {
        }
    }
}