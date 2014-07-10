/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.utils;

import it.univaq.disim.connectorOTF.controller.ImportController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringAlterationListener extends FileAlterationListenerAdaptor{
    
        Map fileContent = new HashMap();
        // Is triggered when a file is created in the monitored folder
        @Override
        public void onFileCreate(File file) {
            try {
                // "file" is the reference to the newly created file
                System.out.println("File created: "
                        + file.getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        // Is triggered when a file is deleted from the monitored folder
        @Override
        public void onFileDelete(File file) {
            try {
                // "file" is the reference to the removed file
                System.out.println("File removed: "
                        + file.getCanonicalPath());
                // "file" does not exists anymore in the location
                System.out.println("File still exists in location: "
                        + file.exists());
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }

        
        @Override
        public void onFileChange(File file) {
            try {
                // "file" is the reference to the changed file
                System.out.println("File changed: "
                        + file.getCanonicalPath());
                BufferedReader br = new BufferedReader(new FileReader(file));
                String sCurrentLine, result="";
                while ((sCurrentLine = br.readLine()) != null) {
				result += sCurrentLine;
			}
                System.out.println(file.getName());
                fileContent.put(file.getName(), result);
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        
        @RequestMapping(value="/log")
	public ModelAndView log() throws IOException{
                BufferedReader br = new BufferedReader(new FileReader("log.txt"));
                String sCurrentLine, result="";
                while ((sCurrentLine = br.readLine()) != null) {
                    result += sCurrentLine+"\n";
                }

                return new ModelAndView("welcome", "log", result);
	}
}
