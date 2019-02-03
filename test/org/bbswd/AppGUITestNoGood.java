package org.bbswd;

import static org.junit.Assert.*;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class AppGUITestNoGood {
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
	
	/*
	 * This test verifies that we can start up the application and get its version.
	 */
	@Test
	public void AppGUIStart() {
		//fail("Not yet implemented");
 		int retval=0;
		try {
//			retval = run("C:\\eclipse-workspace\\DemoNG\\bin\\org\\bbswd\\AppGUI");
//			retval = run("java C:\\eclipse-workspace\\DemoNG\\bin\\org\\bbswd\\AppGUI");
//			retval = run("java org.bbswd..AppGUI");
//			retval = run("java");
//			retval = run("C:/Program Files/Java/jre1.8.0_151/bin/java.exe -version");
//			retval = run("C:/Progra~1/Java/jre1.8.0_151/bin/java.exe -version");
			retval = run("java.exe -version");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(0, retval);
	}
	
    public int run(String clazz) throws IOException, InterruptedException {  
//        ProcessBuilder pb = new ProcessBuilder("java", clazz);
        ProcessBuilder pb = new ProcessBuilder(clazz);
        pb.redirectError();
        pb.directory(new File("C:\\Progra~1\\Java\\jre1.8.0_151\\bin\\"));
        Process p = pb.start();
        InputStreamConsumer consumer = new InputStreamConsumer(p.getInputStream());
        consumer.start();

        int result = p.waitFor();

        consumer.join();

        System.out.println(consumer.getOutput());

        return result;
    }
    
    public class InputStreamConsumer extends Thread {

        private InputStream is;
        private IOException exp;
        private StringBuilder output;

        public InputStreamConsumer(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            int in = -1;
            output = new StringBuilder(64);
            try {
                while ((in = is.read()) != -1) {
                    output.append((char) in);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                exp = ex;
            }
        }

        public StringBuilder getOutput() {
            return output;
        }

        public IOException getException() {
            return exp;
        }
    }

}
