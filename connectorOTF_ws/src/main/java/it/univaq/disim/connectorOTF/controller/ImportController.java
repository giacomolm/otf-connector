/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.controller;

import it.univaq.disim.connectorOTF.utils.FileMonitor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ImportController {
    	@RequestMapping(value="/")
	public ModelAndView test(HttpServletResponse response) throws IOException{
                try {
                    (new FileMonitor()).monitor("./");                    
                } catch (Exception ex) {
                    Logger.getLogger(ImportController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return new ModelAndView("index");
	}
}
