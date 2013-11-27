package javafxapplication2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
 
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
 
public class WebMap extends Application {
 
 
    private Scene scene;
    MyBrowser myBrowser;
    double lat;
    double lon;
    private Stage stage;
    public static boolean terminated=false;
    File file;
    public static void main(String[] args) {
        launch(args);
    }
    
    public void setFile(File file){
        this.file=file;
    }
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
 
 
        myBrowser = new MyBrowser();
        Scene scene = new Scene(myBrowser);
 
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(600);
        stage.show();
    }
    @Override  
    public void stop() throws Exception {  
    System.out.println("Stopping: stop()");  
    terminated=true;
    }  
    //stuff and stuff
 
    class MyBrowser extends Pane implements Runnable{
 
        String parseString="";
        BufferedReader br=null;
        FileReader fr=null;
        String [] data;
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        
        
        public void run()
        {
            
            
            File readFile= new File("/Users/Joe/Dropbox/應用程式/Simple Example/EDR/2013-11-15_113326.edr");
            setFile(readFile);
            if(readFile==null)
                terminated=true;
            lat = 25.058;
            lon = 121.545;
            try {
                 fr = new FileReader(file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
            }
            br = new BufferedReader(fr);
            
            while(!terminated)
            {
                
                   Platform.runLater(new Runnable(){
                                @Override
                                 public void run() 
                                 {
                                   System.out.println("Starting JavaFX ...");
                                    System.out.println("JavaFX ended");
                                    try {
                                        parseString=br.readLine();
                                        data= parseString.split(",");
                                        
                                        if(!data[6].equals(data[7]))
                                        {
                                            lat=Double.parseDouble(data[7]);
                                            lon=Double.parseDouble(data[6]);
                                            System.out.println(lat+" "+lon);
                                        }
                                        
                                    } catch (IOException ex) {
                                        Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                   // double addon=0.00005;
                                  //  for(;;)
                                    {
                                    //lon+=addon;
                                    //lon+=addon;
                                        try
                                        {
                                        webEngine.executeScript("" +
                                                "window.lat = " + lat + ";" +
                                                "window.lon = " + lon + ";" +
                                                "document.goToLocation(window.lat, window.lon);"
                                            );
                                        }
                                        catch(Exception e)
                                        {
                                           // e.printStackTrace();
                                        }
                                            //lat+=addon;
                                            //lon+=addon;
                                               
                                    } 
                                } 
                            });
                 
                            try {
                                   Thread.sleep(1000);
                                        } 
                            catch (InterruptedException ex) {
                                   Logger.getLogger(WebMap.class.getName()).log(Level.SEVERE, null, ex);
                            }
                             
            }                
                        
        }
        public MyBrowser() throws InterruptedException {
            final URL urlGoogleMaps = getClass().getResource("demo.html");
            webEngine.load(urlGoogleMaps.toExternalForm());
            webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
                @Override
                public void handle(WebEvent<String> e) {
                    System.out.println(e.toString());
                }
            });
           
             final Thread a = new Thread(this);
                   
            getChildren().add(webView);

            final TextField latitude = new TextField("" + 35.857908 * 1.00007);
            final TextField longitude = new TextField("" + 10.598997 * 1.00007);
            Button update = new Button("Update");
            
            Thread t = new Thread() {
                        @Override
                        public void run() {
                            
                            
                        }
                };

                t.start();
                t.join();
            update.setOnAction(new EventHandler<ActionEvent>() {
 
                @Override
                public void handle(ActionEvent arg0) {
                    lat = Double.parseDouble(latitude.getText());
                    lon = Double.parseDouble(longitude.getText());
 
                    System.out.printf("%.2f %.2f%n", lat, lon);
 
                    webEngine.executeScript("" +
                        "window.lat = " + lat + ";" +
                        "window.lon = " + lon + ";" +
                        "document.goToLocation(window.lat, window.lon);"
                    );
                    //a.start();
                     System.out.println("ending action");

                }
            });
                            
            HBox toolbar  = new HBox();
            toolbar.getChildren().addAll(latitude, longitude, update);
 
            getChildren().addAll(toolbar);
            a.start();
        }
     
    }

 
}