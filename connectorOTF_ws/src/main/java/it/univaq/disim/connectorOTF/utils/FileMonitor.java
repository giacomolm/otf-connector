/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.univaq.disim.connectorOTF.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileMonitor {
    // A hardcoded path to a folder you are monitoring .
    public static final String FOLDER = "./";

    public void monitor(String path) throws Exception {
        // The monitor will perform polling on the folder every 5 seconds
        final long pollingInterval = 5 * 1000;

        File folder = new File(FOLDER);

        if (!folder.exists()) {
            // Test to see if monitored folder exists
            throw new RuntimeException("Directory not found: " + FOLDER);
        }

        FileAlterationObserver observer = new FileAlterationObserver(folder);
        FileAlterationMonitor monitor =
                new FileAlterationMonitor(pollingInterval);
        FileAlterationListener listener = new SpringAlterationListener();

        observer.addListener(listener);
        monitor.addObserver(observer);
        monitor.start();
    }
}   
