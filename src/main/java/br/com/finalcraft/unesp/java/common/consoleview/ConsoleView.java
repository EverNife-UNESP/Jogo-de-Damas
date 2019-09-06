/**
 * Copyright (C) 2015 uphy.jp
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.finalcraft.unesp.java.common.consoleview;

import br.com.finalcraft.unesp.java.main.JavaFXMain;
import br.com.finalcraft.unesp.java.main.javafx.view.MyFXMLs;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

public class ConsoleView extends BorderPane {

    public static ConsoleView instance;
    private static Stage dialog;

    private final PrintStream out;
    private final TextArea textArea;
    private final InputStream in;
    private final Thread consoleListeningThread;

    public static ConsoleView createConsole(){
        ConsoleView console = new ConsoleView();
        System.setOut(console.getOut());
        System.setIn(console.getIn());
        System.setErr(console.getOut());
        return console;
    }

    public static void initialize(){
        new Thread(){
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ConsoleView console = new ConsoleView();
                    Scene consoleScene = new Scene(console);
                    URL url = MyFXMLs.getConsoleCSS();
                    if (url != null) consoleScene.getStylesheets().add(url.toString());

                    System.setOut(console.getOut());
                    System.setIn(console.getIn());
                    System.setErr(console.getOut());

                    dialog = new Stage();
                    dialog.initOwner(JavaFXMain.thePrimaryStage);
                    dialog.setScene(consoleScene);
                    dialog.show();
                });
            }
        }.start();
    }

    public ConsoleView() {
        this(Charset.defaultCharset());
    }

    public ConsoleView(Charset charset) {
        getStyleClass().add("consoleview");
        this.textArea = new TextArea();
        this.textArea.setWrapText(true);
        setCenter(this.textArea);

        final TextInputControlStream stream = new TextInputControlStream(this.textArea, Charset.defaultCharset());
        try {
            this.out = new PrintStream(stream.getOut(), true, charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        this.in = stream.getIn();

        final ContextMenu menu = new ContextMenu();
        menu.getItems().add(createItem("Clear consoleview", e -> {
            try {
                stream.clear();
                this.textArea.clear();
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }));
        this.textArea.setContextMenu(menu);

        setPrefWidth(400);
        setPrefHeight(300);

        this.consoleListeningThread = new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Scanner input = new Scanner(System.in);
                        String line = input.nextLine();
                        switch (line){
                            case "cls":
                            case "clear":
                            case "limpar":
                                textArea.clear();
                                break;
                            case "exit":
                                JavaFXMain.shutDown();
                                break;
                        }
                    }catch (Exception e){
                    }
                }
            }
        };
        this.consoleListeningThread.setDaemon(true);
        this.consoleListeningThread.start();
    }

    private MenuItem createItem(String name, EventHandler<ActionEvent> a) {
        final MenuItem menuItem = new MenuItem(name);
        menuItem.setOnAction(a);
        return menuItem;
    }

    public PrintStream getOut() {
        return out;
    }

    public InputStream getIn() {
        return in;
    }

}
