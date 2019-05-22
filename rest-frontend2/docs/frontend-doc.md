# 1. Development Environment

Aplikasi client-app dari Multiplayer Battle Tank: War Tanks - Admin Portal dibangun menggunakan bahasa pemrograman Java dengan menggunakan framework JavaFX yang berbasis MVC (Model, View, Controller). Di samping komponen MVC, kita juga mengimplementasikan komponen tambahan yaitu Service.

## 1.1. Software Environment

Berikut ini detail dari software environment yang digunakan selama pengembangan client-app dari Multiplayer Battle Tank: War Tanks - Admin Portal:

Key | Value
----|----
Operating System | Arch Linux x86_64
JDK Version | OpenJDK 1.8.0u212 64-bit
Maven version | Apache Maven 3.6.0
IDE | IntelliJ IDEA 2019.1

## 1.2. Hardware Environment

Berikut ini detail dari hardware environment yang digunakan selama pengembangan client-app dari Multiplayer Battle Tank: War Tanks - Admin Portal:

Key | Value
----|----
Processor | Intel Core i3-6006 CPU @ 2.00GHz
RAM | 8GB DDR4
Graphics | Intel HD Graphics 520

## 1.3. Version Control

Version control yang kami gunakan saat pengembangan aplikasi ini adalah Git dengan alamat remote repository di https://github.com/ojoakua-10bit/MPPL-GAME-TANK. Git client yang kami gunakan adalah command-line git dengan PGP Signature dan git-over-ssh support. Setiap commit terverivikasi dengan digital signature yang disediakan oleh GnuPG.

## 1.4. Application Dependency

Selain menggunakan library internal bawaan OpenJDK, aplikasi kami juga membutuhkan beberapa modul eksternal seperti:

Dependency | Version | Keterangan
----|----|----
Google gson | 2.8.5 | Untuk mengubah JSON dari REST Server menjadi Java Object
Apache httpclient | 4.5.7 | Untuk mengirim request ke REST Server
Apache httpmime | 4.5.7 | Extensi dari httpclient. Agar bisa mengupload file ke REST Server
ControlsFX | 8.40.14 | Menyediakan tambahan pilihan untuk input control
jMetro | 5.3 | Tema UI yang digunakan

## 1.5. Persiapan

Seperti project-project maven lainnya, hal pertama yang harus dipersiapkan adalah definisi proyek pada `pom.xml`. Berikut file `pom.xml` yang kami gunakan.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.fluxhydravault</groupId>
    <artifactId>rest-frontend2</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.5</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.7</version>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpmime</artifactId>
            <version>4.5.7</version>
        </dependency>
        <dependency>
            <groupId>org.controlsfx</groupId>
            <artifactId>controlsfx</artifactId>
            <version>8.40.14</version>
        </dependency>
        <dependency>
            <groupId>org.jfxtras</groupId>
            <artifactId>jmetro</artifactId>
            <version>5.3</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>REST Client</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
            <plugin>
                <!-- Maven Shade Plugin, mempermudah dalam jar-packaging -->
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>2.1</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>
                                        com.fluxhydravault.restfrontendfx.MainApp
                                    </mainClass>
                                </transformer>
                            </transformers>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```

Selain itu, kami juga mempersiapkan struktur package yang akan digunakan pada `src/main`. Berikut ini konfigurasi package yang kami gunakan.

```
src/main
|- java
|   \-com.fluxhydravault.restfrontendfx  -- root package
|      |- config       -- berisi kelas untuk baca-tulis konfigurasi
|      |- controller   -- berisi kelas untuk mengontrol view
|      |- model        -- berisi model yang mewakili tabel
|      \- service      -- berisi service untuk menghubungkan client
|                         dengan REST Server
|
\- resource
    |- img   -- tempat menyimpan image
    |- css   -- tempat menyimpan custom css
    \- view  -- tempat menyimpan view aplikasi
```

# 2. Model

Model di sini adalah sebuah kelas POJO (Plain Old Java Object) yang merepresentasikan suatu entitas yang akan digunakan dalam aplikasi ini. Berikut ini model yang digunakan dalam aplikasi:

## 2.1. Admin

Merepresentasikan objek admin yang sedang ter-login dalam aplikasi.

```java
package com.fluxhydravault.restfrontendfx.model;

public class Admin {
    private String adminId;
    private String username;
    private String adminName;
    private String avatar;

    // getter dan setter
}
```

## 2.2. Player

Merepresentasikan data player pada database di server.

```java
package com.fluxhydravault.restfrontendfx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Player {
    // menjembatani value di table view
    // transient agar tidak ter-include pada json
    private transient final StringProperty playerIdProperty;
    private transient final StringProperty usernameProperty;

    // property sesungguhnya
    private String playerId;
    private String username;
    private String playerName;
    private int xp;
    private int rank;
    private int diamondCount;
    private int goldCount;
    private int creditBalance;
    private int inventory;
    private String avatar;
    private boolean onlineStatus;
    private boolean banStatus;

    // getter dan setter
}
```

## 2.3. Item

Merepresentasikan data item pada database di server.

```java
package com.fluxhydravault.restfrontendfx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Item {
    // menjembatani value di table view
    // transient agar tidak ter-include pada json
    private transient StringProperty itemIdProperty;
    private transient StringProperty itemNameProperty;

    // property sesungguhnya
    private String itemId;
    private ItemCategory itemCategory;
    private String itemName;
    private String description;
    private String modelLocation;

    // getter dan setter
}
```

### 2.3.1 ItemCategory enum

ItemCategory enum mewakili nilai-nilai dari itemCategory yang diterima secara valid.

```java
package com.fluxhydravault.restfrontendfx.model;

public enum ItemCategory {
    TANK,
    SKIN,
    GAMEPLAY_DROPPED_ITEM,
    INVENTORY_CAPACITY
}
```

## 2.4. Stat

Merepresentasikan data stat pada database di server.

```java
package com.fluxhydravault.restfrontendfx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Stat {
    // menjembatani value di table view
    // transient agar tidak ter-include pada json
    private transient StringProperty statIdProperty;
    private transient StringProperty statNameProperty;
    private transient StringProperty statTypeProperty;
    private transient StringProperty statValueProperty;

    // property sesungguhnya
    private Long statId;
    private StatType type;
    private String name;
    private Double value;

    // getter dan setter
}
```

### 2.4.1. StatType enum

StatType enum mewakili nilai-nilai dari type yang diterima secara valid.

```java
package com.fluxhydravault.restfrontendfx.model;

public enum StatType {
    HITPOINT,
    ATTACK,
    DEFENSE,
    SPEED,
    RELOAD_SPEED,
    HITPOINT_BOOST,
    ATTACK_BOOST,
    DEFENSE_BOOST,
    SPEED_BOOST,
    RELOAD_SPEED_BOOST,
    INV_CAPACITY_BONUS
}
```

## 2.5. StandardResponse

Merepresentasikan response standard dari REST Server. Bersifat generic karena field `data` dapat berisi objek apa saja sesuai endpoint yang dipanggil.

```java
package com.fluxhydravault.restfrontendfx.model;

import java.util.Date;

public class StandardResponse<T> {
    private Date timestamp;
    private String response;
    private String message;
    private T data;

    // getter dan setter
}
```

## 2.6. SearchResponse

Merepresentasikan response dari REST Server ketika memanggil fungsi search. Bersifat generic karena field `matchedResult` dan `possibleResults` dapat berisi objek apa saja sesuai endpoint yang dipanggil.

```java
package com.fluxhydravault.restfrontendfx.model;

import java.util.Date;
import java.util.List;

public class SearchResponse<T> {
    private Date timestamp;
    private T matchedResult;
    private List<T> possibleResults;

    // getter dan setter
}
```

## 2.7. TokenResponse

Merepresentasikan response dari REST Server ketika melakukan login. Bersifat generic karena field `data` dapat berisi objek apa saja sesuai jenis usernya.

```java
package com.fluxhydravault.restfrontendfx.model;

import java.util.Date;

public class TokenResponse<T> {
    private Date timestamp;
    private String token;
    private T data;

    // getter dan setter
}
```

## 2.8. ErrorResponse

Merepresentasikan response dari REST Server ketika terjadi error.

```java
package com.fluxhydravault.restfrontendfx.model;

import java.util.Date;

public class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    // getter dan setter
}
```

# 3. View

Aplikasi ini menggunakan framework GUI JavaFX di mana kodingan view didefinisikan menggunakan custom xml dialect yang bernama FXML. Tema yang digunakan adalah jMetro yang dapat memberikan look and feel layaknya UWP Application di Windows 10.

Terdapat 12 view yang ada dalam aplikasi ini. Semua view bersifat application-generated di mana kodingan ter-generate dari GUI Designer.

## 3.1. Initial View

Initial view adalah view yang pertama kali tampil saat aplikasi dijalankan dan tidak ada session yang tersimpan dalam komputer.

```xml
<BorderPane fx:id="rootPane" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity"
            prefHeight="400.0" prefWidth="600.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.InitialController">
    <top>
        <Label text="War Tank: Admin Portal" BorderPane.alignment="CENTER">
            <padding>
                <Insets bottom="15.0" left="15.0" right="15.0" top="15.0"/>
            </padding>
            <BorderPane.margin>
                <Insets/>
            </BorderPane.margin>
            <font>
                <Font name="Segoe UI Light" size="36.0"/>
            </font>
        </Label>
    </top>
    <center>
        <ImageView fitHeight="128.0" fitWidth="128.0" pickOnBounds="true" preserveRatio="true"
                   BorderPane.alignment="CENTER">
            <Image url="@../img/logo.png"/>
        </ImageView>
    </center>
    <bottom>
        <GridPane fx:id="initialPane" prefHeight="180.0" styleClass="background" BorderPane.alignment="CENTER">
            <columnConstraints>
                <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
            </columnConstraints>
            <rowConstraints>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            </rowConstraints>
            <Button fx:id="registerButton" mnemonicParsing="false" onAction="#doRegister" prefWidth="150.0"
                    text="Register" GridPane.halignment="CENTER" GridPane.rowIndex="1" GridPane.valignment="CENTER"/>
            <Button fx:id="loginButton" mnemonicParsing="false" onAction="#doLogin" prefWidth="150.0" text="Login"
                    GridPane.halignment="CENTER" GridPane.valignment="CENTER"/>
        </GridPane>
    </bottom>
</BorderPane>
```

## 3.2. Register View

Menampilkan halaman pendaftaran untuk admin.

```xml
<AnchorPane prefHeight="400.0" prefWidth="600.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.RegisterController">
    <GridPane layoutX="100.0" layoutY="108.0" prefHeight="256.0" prefWidth="400.0" AnchorPane.leftAnchor="100.0"
              AnchorPane.rightAnchor="100.0">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
        </columnConstraints>
        <rowConstraints>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
        </rowConstraints>
        <TextField fx:id="usernameField" promptText="Username">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </TextField>
        <TextField fx:id="nicknameField" promptText="Nickname" GridPane.rowIndex="1">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </TextField>
        <PasswordField fx:id="passwordField" promptText="Password" GridPane.rowIndex="2">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </PasswordField>
        <PasswordField fx:id="rePasswordField" promptText="Confirm Password" GridPane.rowIndex="3">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </PasswordField>
        <GridPane GridPane.halignment="CENTER" GridPane.rowIndex="5" GridPane.valignment="CENTER">
            <columnConstraints>
                <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
                <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
            </columnConstraints>
            <rowConstraints>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            </rowConstraints>
            <Button alignment="CENTER" cancelButton="true" mnemonicParsing="false" onAction="#doCancel"
                    prefWidth="150.0" text="Cancel" GridPane.halignment="CENTER" GridPane.valignment="CENTER"/>
            <Button alignment="CENTER" defaultButton="true" mnemonicParsing="false" onAction="#doRegister"
                    prefWidth="150.0" text="Register" GridPane.columnIndex="1" GridPane.halignment="CENTER"
                    GridPane.valignment="CENTER"/>
        </GridPane>
    </GridPane>
    <Label layoutX="133.0" layoutY="31.0" text="Register Your Account">
        <font>
          <Font name="Segoe UI Light" size="36.0"/>
        </font>
    </Label>
</AnchorPane>
```

## 3.3. Login View

Menampilkan halaman untuk login admin.

```xml
<AnchorPane prefHeight="180.0" prefWidth="600.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.LoginController">
    <GridPane layoutX="40.0" layoutY="14.0" prefHeight="180.0" prefWidth="600.0" styleClass="background"
              AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0"
              AnchorPane.topAnchor="0.0">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
        </columnConstraints>
        <rowConstraints>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
        </rowConstraints>
        <TextField fx:id="usernameField" maxWidth="-Infinity" prefWidth="400.0" promptText="Username"
                   GridPane.halignment="CENTER" GridPane.valignment="CENTER">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </TextField>
        <PasswordField fx:id="passwordField" maxWidth="-Infinity" prefWidth="400.0" promptText="Password"
                       GridPane.halignment="CENTER" GridPane.rowIndex="1" GridPane.valignment="CENTER">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </PasswordField>
        <GridPane GridPane.rowIndex="2">
            <columnConstraints>
                <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
                <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
            </columnConstraints>
            <rowConstraints>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            </rowConstraints>
            <Button alignment="CENTER" cancelButton="true" mnemonicParsing="false" onAction="#doCancel"
                    prefWidth="150.0" text="Cancel" GridPane.halignment="CENTER" GridPane.valignment="CENTER"/>
            <Button alignment="CENTER" defaultButton="true" mnemonicParsing="false" onAction="#doLogin"
                    prefWidth="150.0" text="Login" GridPane.columnIndex="1" GridPane.halignment="CENTER"
                    GridPane.valignment="CENTER"/>
        </GridPane>
    </GridPane>
</AnchorPane>
```

## 3.4. MainMenu View

MainMenu view adalah tampilan utama halaman setelah login berhasil. MainMenu view juga menjadi wrapper utama dari beberapa view lainnya. Selain itu, MainMenu view menjadi initial view jika user telah login pada aplikasi sebelumnya dan belum melakukan logout.

```xml
<BorderPane fx:id="rootPanel" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity"
            prefHeight="600.0" prefWidth="960.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.MainMenuController">
    <top>
        <GridPane prefHeight="88.0" prefWidth="960.0" BorderPane.alignment="CENTER">
            <columnConstraints>
                <ColumnConstraints hgrow="SOMETIMES" maxWidth="206.0" minWidth="10.0" prefWidth="199.0"/>
                <ColumnConstraints hgrow="SOMETIMES" maxWidth="400.0" minWidth="10.0" prefWidth="163.0"/>
                <ColumnConstraints hgrow="SOMETIMES" maxWidth="579.0" minWidth="10.0" prefWidth="572.0"/>
            </columnConstraints>
            <rowConstraints>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            </rowConstraints>
            <padding>
                <Insets bottom="12.0" left="12.0" right="12.0" top="12.0"/>
            </padding>
            <GridPane>
                <columnConstraints>
                    <ColumnConstraints hgrow="SOMETIMES" maxWidth="64.0" minWidth="10.0" prefWidth="64.0"/>
                    <ColumnConstraints hgrow="SOMETIMES" maxWidth="149.0" minWidth="10.0" prefWidth="122.0"/>
                </columnConstraints>
                <rowConstraints>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                </rowConstraints>
                <ImageView fx:id="adminAvatar" fitHeight="64.0" fitWidth="64.0" pickOnBounds="true"
                           preserveRatio="true">
                    <Image url="@../img/logo.png"/>
                </ImageView>
                <Label fx:id="homeLabel" onMouseClicked="#homeMenuClicked" prefHeight="64.0" prefWidth="149.0"
                       text="War Tanks" GridPane.columnIndex="1" GridPane.valignment="CENTER">
                    <font>
                        <Font name="Segoe UI" size="24.0"/>
                    </font>
                    <GridPane.margin>
                        <Insets left="16.0"/>
                    </GridPane.margin>
                    <cursor>
                        <Cursor fx:constant="HAND"/>
                    </cursor>
                </Label>
            </GridPane>
            <GridPane GridPane.columnIndex="2">
                <columnConstraints>
                    <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
                    <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
                    <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
                    <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
                    <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
                </columnConstraints>
                <rowConstraints>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                </rowConstraints>
                <Label fx:id="playerMenu" onMouseClicked="#playerMenuClicked" text="Players"
                       GridPane.halignment="CENTER" GridPane.valignment="CENTER">
                    <font>
                        <Font name="Segoe UI" size="20.0"/>
                    </font>
                    <cursor>
                        <Cursor fx:constant="HAND"/>
                    </cursor>
                </Label>
                <Label fx:id="itemMenu" onMouseClicked="#itemMenuClicked" text="Item" GridPane.columnIndex="1"
                       GridPane.halignment="CENTER" GridPane.valignment="CENTER">
                    <font>
                        <Font name="Segoe UI" size="20.0"/>
                    </font>
                    <cursor>
                        <Cursor fx:constant="HAND"/>
                    </cursor>
                </Label>
                <Label fx:id="adminMenu" onMouseClicked="#adminMenuClicked" text="{adminId}" GridPane.columnIndex="3"
                       GridPane.halignment="CENTER" GridPane.valignment="CENTER">
                    <font>
                        <Font name="Segoe UI" size="20.0"/>
                    </font>
                    <cursor>
                        <Cursor fx:constant="HAND"/>
                    </cursor>
                </Label>
                <Label fx:id="logoutMenu" onMouseClicked="#logoutMenuClicked" text="Logout" GridPane.columnIndex="4"
                       GridPane.halignment="CENTER" GridPane.valignment="CENTER">
                    <font>
                        <Font name="Segoe UI" size="20.0"/>
                    </font>
                    <cursor>
                        <Cursor fx:constant="HAND"/>
                    </cursor>
                </Label>
                <Label onMouseClicked="#statsMenuClicked" text="Stats" GridPane.columnIndex="2"
                       GridPane.halignment="CENTER" GridPane.valignment="CENTER">
                    <font>
                        <Font name="Segoe UI" size="20.0"/>
                    </font>
                    <cursor>
                        <Cursor fx:constant="HAND"/>
                    </cursor>
                </Label>
            </GridPane>
        </GridPane>
    </top>
    <bottom>
        <Pane prefHeight="32.0" prefWidth="960.0" BorderPane.alignment="CENTER">
            <Label alignment="CENTER" prefWidth="960.0" text="Copyright (c) 2019 FluxHydra Labs">
                <padding>
                    <Insets bottom="6.0" left="6.0" right="6.0" top="6.0"/>
                </padding>
                <font>
                    <Font name="Segoe UI" size="13.0"/>
                </font>
            </Label>
        </Pane>
    </bottom>
    <center>
        <GridPane fx:id="homePanel" BorderPane.alignment="CENTER">
            <columnConstraints>
                <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
            </columnConstraints>
            <rowConstraints>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            </rowConstraints>
            <Label fx:id="welcomeMessage" text="Hi {adminName}! Welcome to War Tanks - Admin Portal"
                   GridPane.halignment="CENTER" GridPane.valignment="BOTTOM">
                <font>
                    <Font name="Segoe UI Light" size="32.0"/>
                </font>
            </Label>
            <Label text="To start, please select one available menu above." GridPane.halignment="CENTER"
                   GridPane.rowIndex="1" GridPane.valignment="BOTTOM">
                <font>
                    <Font name="Segoe UI Light" size="24.0"/>
                </font>
            </Label>
        </GridPane>
    </center>
</BorderPane>
```

## 3.5. Player View

Player view merupakan tampilan dari player management.

```xml
<AnchorPane prefHeight="480.0" prefWidth="960.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.PlayerController">
    <SplitPane dividerPositions="0.4126984126984127" prefHeight="480.0" prefWidth="960.0" styleClass="background">
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="160.0" prefWidth="100.0" styleClass="background">
            <TableView fx:id="playerTable" layoutX="-12.0" layoutY="49.0" prefHeight="298.0" prefWidth="175.0"
                       AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0"
                       AnchorPane.topAnchor="0.0">
                <columns>
                    <TableColumn fx:id="idColumn" prefWidth="75.0" text="Player ID"/>
                    <TableColumn fx:id="usernameColumn" prefWidth="75.0" text="Username"/>
                </columns>
                <columnResizePolicy>
                    <TableView fx:constant="CONSTRAINED_RESIZE_POLICY"/>
                </columnResizePolicy>
            </TableView>
        </AnchorPane>
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="160.0" prefWidth="100.0" styleClass="background">
            <Label layoutX="104.0" layoutY="51.0" styleClass="label-header" text="Player Details"
                   AnchorPane.leftAnchor="5.0" AnchorPane.topAnchor="5.0">
                <font>
                    <Font name="Segoe UI Light" size="36.0"/>
                </font>
            </Label>
            <GridPane layoutX="43.0" layoutY="87.0" prefHeight="318.0" prefWidth="549.0"
                      AnchorPane.leftAnchor="5.0" AnchorPane.rightAnchor="5.0" AnchorPane.topAnchor="87.0">
                <columnConstraints>
                    <ColumnConstraints hgrow="SOMETIMES" maxWidth="269.0" minWidth="10.0" prefWidth="172.0"/>
                    <ColumnConstraints hgrow="SOMETIMES" maxWidth="387.0" minWidth="10.0" prefWidth="377.0"/>
                </columnConstraints>
                <rowConstraints>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                </rowConstraints>
                <Label text="Player ID">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Username" GridPane.rowIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Nickname" GridPane.rowIndex="2">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="XP" GridPane.rowIndex="3">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Rank" GridPane.rowIndex="4">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Diamond Count" GridPane.rowIndex="5">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="idLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="usernameLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="nicknameLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="2">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label styleClass="label-bright" text="Label" GridPane.columnIndex="1" GridPane.rowIndex="3"
                       fx:id="xpLabel">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="rankLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="4">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="diamondLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="5">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Gold Count" GridPane.rowIndex="6">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="goldLabel" text="Label" GridPane.columnIndex="1" GridPane.rowIndex="6">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Credit Balance" GridPane.rowIndex="7">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="creditLabel" text="Label" GridPane.columnIndex="1" GridPane.rowIndex="7">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Inventory Size" GridPane.rowIndex="8">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="inventoryLabel" text="Label" GridPane.columnIndex="1" GridPane.rowIndex="8">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Path to Avatar" GridPane.rowIndex="9">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Ban Status" GridPane.rowIndex="10">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="avatarLabel" text="Label" GridPane.columnIndex="1" GridPane.rowIndex="9">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="banLabel" text="Label" GridPane.columnIndex="1" GridPane.rowIndex="10">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
            </GridPane>
            <ButtonBar layoutX="54.0" layoutY="250.0" AnchorPane.bottomAnchor="10.0"
                       AnchorPane.rightAnchor="10.0">
                <buttons>
                    <Button fx:id="editButton" disable="true" mnemonicParsing="false"
                            onAction="#editButtonClicked" text="Edit..."/>
                    <Button fx:id="deleteButton" disable="true" minWidth="66.0" mnemonicParsing="false"
                            onAction="#deleteButtonClicked" prefWidth="120.0" text="Delete"/>
                </buttons>
            </ButtonBar>
        </AnchorPane>
    </SplitPane>
</AnchorPane>
```

## 3.6. Item View

Item view merupakan tampilan dari item management.

```xml
<AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="480.0"
            prefWidth="960.0" xmlns="http://javafx.com/javafx/8.0.172-ea" xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.ItemController">
    <SplitPane dividerPositions="0.4126984126984127" prefHeight="480.0" prefWidth="960.0" styleClass="background">
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="160.0" prefWidth="100.0" styleClass="background">
            <TableView fx:id="itemTable" layoutX="-12.0" layoutY="49.0" prefHeight="298.0" prefWidth="175.0"
                       AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0"
                       AnchorPane.topAnchor="0.0">
                <columns>
                    <TableColumn fx:id="idColumn" prefWidth="75.0" text="Item ID"/>
                    <TableColumn fx:id="nameColumn" prefWidth="75.0" text="Item Name"/>
                </columns>
                <columnResizePolicy>
                    <TableView fx:constant="CONSTRAINED_RESIZE_POLICY"/>
                </columnResizePolicy>
            </TableView>
        </AnchorPane>
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="160.0" prefWidth="100.0" styleClass="background">
            <Label layoutX="104.0" layoutY="51.0" styleClass="label-header" text="Item Details"
                   AnchorPane.leftAnchor="5.0" AnchorPane.topAnchor="5.0">
                <font>
                    <Font name="Segoe UI Light" size="36.0"/>
                </font>
            </Label>
            <GridPane layoutX="43.0" layoutY="87.0" prefHeight="318.0" prefWidth="549.0" AnchorPane.leftAnchor="5.0"
                      AnchorPane.rightAnchor="5.0" AnchorPane.topAnchor="87.0">
                <columnConstraints>
                    <ColumnConstraints hgrow="SOMETIMES" maxWidth="269.0" minWidth="10.0" prefWidth="172.0"/>
                    <ColumnConstraints hgrow="SOMETIMES" maxWidth="387.0" minWidth="10.0" prefWidth="377.0"/>
                </columnConstraints>
                <rowConstraints>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                </rowConstraints>
                <Label text="Item ID">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Item Category" GridPane.rowIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Item Name" GridPane.rowIndex="2">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Description" GridPane.rowIndex="3">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Model Location" GridPane.rowIndex="4">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="idLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="categoryLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="nameLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="2">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="descriptionLabel" maxWidth="377.0" styleClass="label-bright" text="Label"
                       wrapText="true" GridPane.columnIndex="1" GridPane.rowIndex="3">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="modelLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="4">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
            </GridPane>
            <ButtonBar layoutX="54.0" layoutY="250.0" AnchorPane.bottomAnchor="10.0" AnchorPane.rightAnchor="10.0">
                <buttons>
                    <Button mnemonicParsing="false" onAction="#newButtonClicked" text="New..."/>
                    <Button fx:id="editButton" disable="true" mnemonicParsing="false" onAction="#editButtonClicked"
                            text="Edit..."/>
                    <Button fx:id="deleteButton" disable="true" minWidth="66.0" mnemonicParsing="false"
                            onAction="#deleteButtonClicked" prefWidth="120.0" text="Delete"/>
                </buttons>
            </ButtonBar>
        </AnchorPane>
    </SplitPane>
</AnchorPane>
```

## 3.7. Stats View

Stats view merupakan tampilan dari stat management.

```xml
<AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="480.0"
            prefWidth="960.0" xmlns="http://javafx.com/javafx/8.0.172-ea" xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.StatsController">
    <SplitPane dividerPositions="0.4126984126984127" prefHeight="480.0" prefWidth="960.0" styleClass="background">
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="160.0" prefWidth="100.0" styleClass="background">
            <TableView fx:id="statTable" layoutX="-12.0" layoutY="49.0" prefHeight="298.0" prefWidth="175.0"
                       AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0"
                       AnchorPane.topAnchor="0.0">
                <columns>
                    <TableColumn fx:id="idColumn" prefWidth="75.0" text="Stat ID"/>
                    <TableColumn fx:id="nameColumn" prefWidth="75.0" text="Stat Name"/>
                </columns>
                <columnResizePolicy>
                    <TableView fx:constant="CONSTRAINED_RESIZE_POLICY"/>
                </columnResizePolicy>
            </TableView>
        </AnchorPane>
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="160.0" prefWidth="100.0" styleClass="background">
            <Label layoutX="104.0" layoutY="51.0" styleClass="label-header" text="Stat Details"
                   AnchorPane.leftAnchor="5.0" AnchorPane.topAnchor="5.0">
                <font>
                    <Font name="Segoe UI Light" size="36.0"/>
                </font>
            </Label>
            <GridPane layoutX="43.0" layoutY="87.0" prefHeight="318.0" prefWidth="549.0"
                      AnchorPane.leftAnchor="5.0" AnchorPane.rightAnchor="5.0" AnchorPane.topAnchor="87.0">
                <columnConstraints>
                    <ColumnConstraints hgrow="SOMETIMES" maxWidth="269.0" minWidth="10.0" prefWidth="172.0"/>
                    <ColumnConstraints hgrow="SOMETIMES" maxWidth="387.0" minWidth="10.0" prefWidth="377.0"/>
                </columnConstraints>
                <rowConstraints>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                </rowConstraints>
                <Label text="Stat ID">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Stat Type" GridPane.rowIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Stat Name" GridPane.rowIndex="2">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label text="Stat Value" GridPane.rowIndex="3">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="idLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="typeLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="1">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="nameLabel" styleClass="label-bright" text="Label" GridPane.columnIndex="1"
                       GridPane.rowIndex="2">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
                <Label fx:id="valueLabel" maxWidth="377.0" styleClass="label-bright" text="Label"
                       wrapText="true" GridPane.columnIndex="1" GridPane.rowIndex="3">
                    <font>
                        <Font name="Segoe UI" size="14.0"/>
                    </font>
                </Label>
            </GridPane>
            <ButtonBar layoutX="54.0" layoutY="250.0" AnchorPane.bottomAnchor="10.0"
                       AnchorPane.rightAnchor="10.0">
                <buttons>
                    <Button mnemonicParsing="false" onAction="#newButtonClicked" text="New..."/>
                    <Button fx:id="editButton" disable="true" mnemonicParsing="false"
                            onAction="#editButtonClicked" text="Edit..."/>
                    <Button fx:id="deleteButton" disable="true" minWidth="66.0" mnemonicParsing="false"
                            onAction="#deleteButtonClicked" prefWidth="120.0" text="Delete"/>
                </buttons>
            </ButtonBar>
        </AnchorPane>
    </SplitPane>
</AnchorPane>
```

## 3.8. Admin View

Admin view merupakan tampilan dari admin management.

```xml
<AnchorPane prefHeight="480.0" prefWidth="960.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.AdminController">
    <SplitPane dividerPositions="0.6" prefHeight="480.0" prefWidth="960.0">
        <AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity"
                    prefHeight="478.0" prefWidth="571.0" styleClass="background"
                    SplitPane.resizableWithParent="false">
            <GridPane layoutY="72.0" prefHeight="338.0" prefWidth="571.0">
                <columnConstraints>
                    <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
                </columnConstraints>
                <rowConstraints>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                    <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
                </rowConstraints>
                <padding>
                    <Insets bottom="12.0" left="12.0" right="12.0" top="12.0"/>
                </padding>
                <TextField fx:id="usernameField" promptText="Username" GridPane.rowIndex="1">
                    <font>
                        <Font name="Segoe UI" size="13.0"/>
                    </font>
                </TextField>
                <TextField fx:id="nicknameField" promptText="Nickname" GridPane.rowIndex="3">
                    <font>
                        <Font name="Segoe UI" size="13.0"/>
                    </font>
                </TextField>
                <PasswordField fx:id="passwordField" promptText="New Password" GridPane.rowIndex="6">
                    <font>
                        <Font name="Segoe UI" size="13.0"/>
                    </font>
                </PasswordField>
                <PasswordField fx:id="rePasswordField" promptText="Confirm New Password"
                               GridPane.rowIndex="7">
                    <font>
                        <Font name="Segoe UI" size="13.0"/>
                    </font>
                </PasswordField>
                <PasswordField fx:id="oldPasswordField" promptText="Current Password" GridPane.rowIndex="5">
                    <font>
                        <Font name="Segoe UI" size="13.0"/>
                    </font>
                </PasswordField>
                <Label text="Change Username">
                    <font>
                        <Font name="Segoe UI" size="18.0"/>
                    </font>
                </Label>
                <Label text="Change Password" GridPane.rowIndex="4">
                    <font>
                        <Font name="Segoe UI" size="18.0"/>
                    </font>
                </Label>
                <Label text="Change nickname" GridPane.rowIndex="2">
                    <font>
                        <Font name="Segoe UI" size="18.0"/>
                    </font>
                </Label>
            </GridPane>
            <ButtonBar layoutX="186.0" layoutY="424.0" prefHeight="40.0" prefWidth="371.0">
                <buttons>
                    <Button mnemonicParsing="false" onAction="#doDelete" prefWidth="120.0" text="Delete"/>
                    <Button mnemonicParsing="false" onAction="#doReset" prefWidth="120.0" text="Reset"/>
                    <Button mnemonicParsing="false" onAction="#doUpdate" prefWidth="120.0" text="Update"/>
                </buttons>
            </ButtonBar>
            <Label layoutX="14.0" layoutY="14.0" text="Edit your data">
                <font>
                    <Font name="Segoe UI Light" size="36.0"/>
                </font>
            </Label>
        </AnchorPane>
        <AnchorPane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity"
                    prefHeight="478.0" prefWidth="379.0" styleClass="background"
                    SplitPane.resizableWithParent="false">
            <ImageView fx:id="avatarPreview" fitHeight="256.0" fitWidth="256.0" layoutX="62.0" layoutY="111.0"
                       onMouseClicked="#chooseImage" pickOnBounds="true" preserveRatio="true"
                       AnchorPane.leftAnchor="62.0" AnchorPane.rightAnchor="62.0" AnchorPane.topAnchor="111.0">
                <cursor>
                    <Cursor fx:constant="HAND"/>
                </cursor>
                <Image url="@../img/image.png"/>
            </ImageView>
            <Button layoutX="115.0" layoutY="435.0" mnemonicParsing="false" onAction="#doUpload"
                    prefWidth="150.0" text="Upload" AnchorPane.bottomAnchor="14.0"/>
            <Label layoutX="14.0" layoutY="14.0" text="Change Avatar" AnchorPane.leftAnchor="14.0"
                   AnchorPane.topAnchor="14.0">
                <font>
                    <Font name="Segoe UI Light" size="36.0"/>
                </font>
            </Label>
            <Label layoutX="18.0" layoutY="67.0" text="Click the image to choose the file, then click upload."
                   AnchorPane.leftAnchor="18.0" AnchorPane.topAnchor="67.0">
                <font>
                    <Font name="Segoe UI" size="15.0"/>
                </font>
            </Label>
        </AnchorPane>
    </SplitPane>
</AnchorPane>
```

## 3.9. EditPlayer View

EditPlayer view merupakan tampilan dari menu edit/new player.

```xml
<AnchorPane prefHeight="480.0" prefWidth="600.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.EditPlayerController">
    <padding>
        <Insets bottom="20.0" left="20.0" right="20.0" top="20.0"/>
    </padding>
    <Label layoutX="36.0" layoutY="22.0" text="Edit Player Info" AnchorPane.leftAnchor="0.0" AnchorPane.topAnchor="0.0">
        <font>
            <Font name="Segoe UI Light" size="36.0"/>
        </font>
    </Label>
    <GridPane layoutX="40.0" layoutY="133.0" prefHeight="286.0" prefWidth="560.0" AnchorPane.leftAnchor="0.0"
              AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="73.0">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
        </columnConstraints>
        <rowConstraints>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
        </rowConstraints>
        <Label text="Change Password">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <PasswordField fx:id="passwordField" promptText="Password" GridPane.rowIndex="1">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </PasswordField>
        <PasswordField fx:id="rePasswordField" promptText="Confirm Password" GridPane.rowIndex="2">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </PasswordField>
        <Label text="Change Player's Credit" GridPane.rowIndex="3">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <TextField fx:id="creditField" GridPane.rowIndex="4">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </TextField>
        <Label text="Player Administration" GridPane.rowIndex="5">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <BorderPane prefHeight="200.0" prefWidth="200.0" GridPane.rowIndex="6">
            <left>
                <ToggleSwitch fx:id="banToggle" BorderPane.alignment="CENTER"/>
            </left>
            <center>
                <Label text="Ban this player" BorderPane.alignment="TOP_LEFT">
                    <padding>
                        <Insets left="10.0"/>
                    </padding>
                    <font>
                        <Font name="Segoe UI" size="13.0"/>
                    </font>
                </Label>
            </center>
        </BorderPane>
    </GridPane>
    <ButtonBar layoutX="334.0" layoutY="426.0" prefHeight="40.0" prefWidth="320.0" AnchorPane.bottomAnchor="0.0"
               AnchorPane.rightAnchor="0.0">
        <buttons>
            <Button cancelButton="true" mnemonicParsing="false" onAction="#doCancel" text="Cancel"/>
            <Button defaultButton="true" mnemonicParsing="false" onAction="#doSave" text="Save"/>
        </buttons>
    </ButtonBar>
</AnchorPane>
```

## 3.10. EditItem View

EditItem view merupakan tampilan dari menu edit/new item.

```xml
<AnchorPane prefHeight="520.0" prefWidth="600.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.EditItemController">
    <padding>
        <Insets bottom="20.0" left="20.0" right="20.0" top="20.0"/>
    </padding>
    <Label layoutX="36.0" layoutY="22.0" text="Edit Item Info" AnchorPane.leftAnchor="0.0" AnchorPane.topAnchor="0.0">
        <font>
            <Font name="Segoe UI Light" size="36.0"/>
        </font>
    </Label>
    <GridPane layoutX="40.0" layoutY="113.0" prefHeight="334.0" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0"
              AnchorPane.topAnchor="73.0">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0"/>
        </columnConstraints>
        <rowConstraints>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
        </rowConstraints>
        <Label text="Item Category">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <Label text="Item Name" GridPane.rowIndex="2">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <Label text="Description" GridPane.rowIndex="4">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <Label text="Model" GridPane.rowIndex="6">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <TextField fx:id="nameField" GridPane.rowIndex="3"/>
        <ComboBox fx:id="categoryBox" prefWidth="150.0" GridPane.rowIndex="1"/>
        <TextField fx:id="descField" GridPane.rowIndex="5"/>
        <Button fx:id="uploadButton" mnemonicParsing="false" onAction="#doUpload" prefWidth="200.0"
                text="Choose File..." GridPane.rowIndex="7"/>
    </GridPane>
    <ButtonBar layoutX="334.0" layoutY="426.0" prefHeight="40.0" prefWidth="407.0" AnchorPane.bottomAnchor="0.0"
               AnchorPane.rightAnchor="0.0">
        <buttons>
            <Button fx:id="statsButton" disable="true" mnemonicParsing="false" onAction="#editStats" text="Stats..."/>
            <Button cancelButton="true" mnemonicParsing="false" onAction="#doCancel" text="Cancel"/>
            <Button defaultButton="true" mnemonicParsing="false" onAction="#doSave" text="Save"/>
        </buttons>
    </ButtonBar>
</AnchorPane>
```

## 3.11. EditStat View

EditStat view merupakan tampilan dari menu edit/new stat.

```xml
<AnchorPane prefHeight="480.0" prefWidth="600.0" styleClass="background" xmlns="http://javafx.com/javafx/8.0.172-ea"
            xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.EditStatController">
    <padding>
        <Insets bottom="20.0" left="20.0" right="20.0" top="20.0"/>
    </padding>
    <Label layoutX="36.0" layoutY="22.0" text="Edit Stat Info" AnchorPane.leftAnchor="0.0" AnchorPane.topAnchor="0.0">
        <font>
            <Font name="Segoe UI Light" size="36.0"/>
        </font>
    </Label>
    <GridPane layoutX="40.0" layoutY="133.0" prefHeight="286.0" prefWidth="560.0" AnchorPane.leftAnchor="0.0"
              AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="73.0">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0"/>
        </columnConstraints>
        <rowConstraints>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES"/>
        </rowConstraints>
        <Label text="Type">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <Label text="Name" GridPane.rowIndex="2">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <TextField fx:id="valueField" promptText="Stat value..." GridPane.rowIndex="5">
            <font>
                <Font name="Segoe UI" size="13.0"/>
            </font>
        </TextField>
        <Label text="Value" GridPane.rowIndex="4">
            <font>
                <Font name="Segoe UI" size="18.0"/>
            </font>
        </Label>
        <ComboBox fx:id="typeBox" prefWidth="150.0" GridPane.rowIndex="1"/>
        <TextField fx:id="nameField" promptText="Stat name..." GridPane.rowIndex="3"/>
    </GridPane>
    <ButtonBar layoutX="334.0" layoutY="426.0" prefHeight="40.0" prefWidth="320.0" AnchorPane.bottomAnchor="0.0"
               AnchorPane.rightAnchor="0.0">
        <buttons>
            <Button cancelButton="true" mnemonicParsing="false" onAction="#doCancel" text="Cancel"/>
            <Button defaultButton="true" mnemonicParsing="false" onAction="#doSave" text="Save"/>
        </buttons>
    </ButtonBar>
</AnchorPane>
```

## 3.12. EditItemStat View

EditItemStat view merupakan tampilan dari menu tambah/hapus stat dari player.

```xml
<AnchorPane prefHeight="600.0" prefWidth="1024.0" stylesheets="@../css/bootstrap3.css"
            xmlns="http://javafx.com/javafx/8.0.172-ea" xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="com.fluxhydravault.restfrontendfx.controller.EditItemStatsController">
    <SplitPane dividerPositions="0.5" layoutX="200.0" layoutY="120.0" prefHeight="160.0" prefWidth="200.0"
               AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0"
               AnchorPane.topAnchor="0.0">
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="160.0" prefWidth="100.0" styleClass="background"
                    SplitPane.resizableWithParent="false">
            <TableView fx:id="itemStatTable" prefHeight="200.0" prefWidth="200.0" AnchorPane.bottomAnchor="64.0"
                       AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0" AnchorPane.topAnchor="93.0">
                <columns>
                    <TableColumn fx:id="itemStatName" prefWidth="145.0" text="Name"/>
                    <TableColumn fx:id="itemStatType" prefWidth="168.0" text="Type"/>
                    <TableColumn fx:id="itemStatValue" prefWidth="193.0" text="Value"/>
                </columns>
            </TableView>
            <Label layoutX="14.0" layoutY="14.0" text="Applied Item Stats" AnchorPane.leftAnchor="20.0"
                   AnchorPane.topAnchor="20.0">
                <font>
                    <Font name="Segoe UI Light" size="36.0"/>
                </font>
            </Label>
            <Button fx:id="deleteButton" disable="true" layoutX="436.0" layoutY="555.0" mnemonicParsing="false"
                    onAction="#doDelete" prefWidth="200.0" styleClass="danger" text="Delete from item"
                    AnchorPane.bottomAnchor="17.0" AnchorPane.rightAnchor="12.0"/>
        </AnchorPane>
        <AnchorPane minHeight="0.0" minWidth="0.0" prefHeight="160.0" prefWidth="100.0" styleClass="background"
                    SplitPane.resizableWithParent="false">
            <TableView fx:id="statTable" layoutX="7.0" layoutY="80.0" prefHeight="200.0" prefWidth="200.0"
                       AnchorPane.bottomAnchor="64.0" AnchorPane.leftAnchor="0.0" AnchorPane.rightAnchor="0.0"
                       AnchorPane.topAnchor="93.0">
                <columns>
                    <TableColumn fx:id="statName" prefWidth="174.0" text="Name"/>
                    <TableColumn fx:id="statType" prefWidth="153.0" text="Type"/>
                    <TableColumn fx:id="statValue" prefWidth="179.0" text="Value"/>
                </columns>
            </TableView>
            <Label layoutX="14.0" layoutY="14.0" text="Available Stats" AnchorPane.leftAnchor="20.0"
                   AnchorPane.topAnchor="20.0">
                <font>
                    <Font name="Segoe UI Light" size="36.0"/>
                </font>
            </Label>
            <ButtonBar prefHeight="40.0" prefWidth="300.0" AnchorPane.bottomAnchor="12.0"
                       AnchorPane.rightAnchor="12.0">
                <buttons>
                    <Button cancelButton="true" mnemonicParsing="false" onAction="#doCancel" text="Close"/>
                </buttons>
            </ButtonBar>
            <Button fx:id="addButton" disable="true" layoutX="20.0" layoutY="552.0" mnemonicParsing="false"
                    onAction="#doAdd" prefWidth="200.0" styleClass="primary" text="Add to item"
                    AnchorPane.bottomAnchor="17.0" AnchorPane.leftAnchor="12.0"/>
        </AnchorPane>
    </SplitPane>
</AnchorPane>
```

# 4. Controller

Pada JavaFX, controller berfungsi sebagai logic program yang mengontrol view. Sehingga pada aplikasi ini jumlah controller sama dengan view yaitu 12. 

## 4.1. InitialController

Mengontrol [Initial view](#31-initial-view).

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class InitialController {
    private Scene initialScene;
    private Stage primaryStage;

    // Menghubungkan dengan objek pada view
    @FXML
    private BorderPane rootPane;
    @FXML
    private GridPane initialPane;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setInitialScene(Scene initialScene) {
        this.initialScene = initialScene;
    }

    // Action ketika tombol login diklik, menampilkan halaman login
    @FXML
    public void doLogin() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Action ketika tombol register diklik, menampilkan halaman register
    @FXML
    public void doRegister() {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 4.2. RegisterController

Mengontrol [Register View](#32-register-view).

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statement

public class RegisterController {
    private Stage primaryStage;
    private Scene initialScene;

    // Menghubungkan dengan objek pada view
    @FXML
    private TextField usernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rePasswordField;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setInitialScene(Scene initialScene) {
        this.initialScene = initialScene;
    }

    // Validasi input, kemudian melakukan registrasi via AdminService
    @FXML
    private void doRegister() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Kembali ke menu sebelumnya ketika tombil cancel diklik
    @FXML
    private void doCancel() {
        primaryStage.setScene(initialScene);
    }

    // Menampilkan pesan error
    private void showErrorAlert(String headerMessage, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Error");
        alert.setHeaderText(headerMessage);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // Menampilkan pesan sukses
    private void showSuccessAlert(String headerMessage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Success");
        alert.setHeaderText(headerMessage);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
```

## 4.3. LoginController

Mengontrol [Login View](#33-login-view).

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statement

public class LoginController {
    private Stage primaryStage;
    private BorderPane initialRoot;
    private Scene initialScene;
    private GridPane initialPane;

    // Menghubungkan dengan obhek pada view
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setInitialRoot(BorderPane initialRoot) {
        this.initialRoot = initialRoot;
    }

    public void setInitialPane(GridPane initialPane) {
        this.initialPane = initialPane;
    }

    public void setInitialScene(Scene initialScene) {
        this.initialScene = initialScene;
    }

    // Ketika cancel diklik, kembali ke tampilan awal
    @FXML
    private void doCancel() {
        initialRoot.setBottom(initialPane);
    }

    // Lakukan login via loginService ketika login diklik. 
    // Redirect ke MainMenu jika berhasil
    @FXML
    private void doLogin() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan pesan error
    private void showAlert(String headerMessage, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(primaryStage);
        alert.setTitle("Error");
        alert.setHeaderText(headerMessage);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
```

## 4.4. MainMenuController

Mengontrol [MainMenu View](#34-mainmenu-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class MainMenuController {
    private Config config;
    private Admin admin;
    private Stage primaryStage;
    private Scene initialScene;

    // Menghubungkan dengan objek pada view
    @FXML
    private BorderPane rootPanel;
    @FXML
    private GridPane homePanel;
    @FXML
    private Label adminMenu;
    @FXML
    private Label welcomeMessage;
    @FXML
    private ImageView adminAvatar;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setInitialScene(Scene initialScene) {
        this.initialScene = initialScene;
    }

    public void setAdminAvatarImage(File image) {
        try {
            adminAvatar.setImage(new Image(new FileInputStream(image)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
        if (admin != null) {
            adminMenu.setText(admin.getUsername());
            welcomeMessage.setText("Hi " + admin.getAdminName() + "! Welcome to War Tanks - Admin Portal");
        }
    }

    // Inisialisasi program: load config, download avatar (sebagai tes koneksi)
    @FXML
    private void initialize() {
        config = Config.getConfig();
        admin = config.getCurrentAdmin();
        adminMenu.setText(admin.getUsername());
        welcomeMessage.setText("Hi "+ admin.getAdminName() +"! Welcome to War Tanks - Admin Portal");
        try {
            System.out.println("Downloading avatar...");
            File imageLocation = new File(config.getConfigLocation() + Defaults.getImageLocation());
            FileUtils.copyURLToFile(new URL(config.getFileServerUri() + admin.getAvatar()),
                    imageLocation, 15000, 15000);
            adminAvatar.setImage(new Image(new FileInputStream(imageLocation)));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(primaryStage);
            alert.setTitle("Error");
            alert.setHeaderText("Connection Error");
            alert.setContentText("An error has occurred while connecting to server.");

            alert.showAndWait();
        }
    }

    // Kembali ke tampilan default ketika menu home diklik
    @FXML
    private void homeMenuClicked() {
        rootPanel.setCenter(homePanel);
        System.out.println("Home");
    }

    // Menampilkan interface Player
    @FXML
    private void playerMenuClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Player.fxml"));
            AnchorPane panel = loader.load();
            ((PlayerController) loader.getController()).setPrimaryStage(primaryStage);
            rootPanel.setCenter(panel);
            System.out.println("Player");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Menampilkan interface Item
    @FXML
    private void itemMenuClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Item.fxml"));
            AnchorPane panel = loader.load();
            ((ItemController) loader.getController()).setPrimaryStage(primaryStage);
            rootPanel.setCenter(panel);
            System.out.println("Player");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Item");
    }

    // Menampilkan interface Stat
    @FXML
    private void statsMenuClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Stats.fxml"));
            AnchorPane panel = loader.load();
            ((StatsController) loader.getController()).setPrimaryStage(primaryStage);
            rootPanel.setCenter(panel);
            System.out.println("Stats");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Menampilkan interface Admin
    @FXML
    private void adminMenuClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
            AnchorPane panel = loader.load();
            AdminController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setMainMenuController(this);
            rootPanel.setCenter(panel);
            System.out.println("Admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Memberi pesan konfirmasi, lalu logout
    @FXML
    public void logoutMenuClicked() {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 4.5. PlayerController

Mengontrol [Player View](#35-player-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class PlayerController {
    private Player selectedPlayer;
    private PlayerService service;
    private Stage primaryStage;

    // Menghubungkan dengan objek pada view
    @FXML
    private TableView<Player> playerTable;
    @FXML
    private TableColumn<Player, String> idColumn;
    @FXML
    private TableColumn<Player, String> usernameColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label xpLabel;
    @FXML
    private Label rankLabel;
    @FXML
    private Label diamondLabel;
    @FXML
    private Label goldLabel;
    @FXML
    private Label creditLabel;
    @FXML
    private Label inventoryLabel;
    @FXML
    private Label avatarLabel;
    @FXML
    private Label banLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Mengupdate table ketika ada perubahan data
    private void updateTable(List<Player> list) {
        if (list != null)
            playerTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    // Inisialisasi interface
    @FXML
    private void initialize() {
        service = PlayerService.getInstance();
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerIdProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().getUsernameProperty());

        setSelectedPlayer(null);

        playerTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedPlayer(current));

        updateTable(service.getPlayerLists());
    }

    // Mengeset nilai selected player dan update details
    private void setSelectedPlayer(Player player) {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan interface Edit Player ketika diklik
    @FXML
    private void editButtonClicked() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan pesan konfirmasi, lalu hapus player ketika diklik
    @FXML
    private void deleteButtonClicked() {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 4.6. ItemController

Mengontrol [Item View](#36-item-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class ItemController {
    private Item selectedItem;
    private ItemService service;
    private Stage primaryStage;

    // Menghubungkan dengan objek pada view
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> idColumn;
    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label modelLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Mengupdate table ketika ada perubahan data
    private void updateTable(List<Item> list) {
        if (list != null)
            itemTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    // Inisialisasi interface
    @FXML
    private void initialize() {
        service = ItemService.getInstance();
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getItemIdProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getItemNameProperty());

        setSelectedItem(null);

        itemTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedItem(current));

        updateTable(service.getItemLists());
    }

    // Mengeset nilai selected item dan update details
    private void setSelectedItem(Item item) {
        selectedItem = item;
        editButton.disableProperty().setValue(item == null);
        deleteButton.disableProperty().setValue(item == null);

        if (item == null) {
            idLabel.setText("");
            categoryLabel.setText("");
            nameLabel.setText("");
            descriptionLabel.setText("");
            modelLabel.setText("");
        } else {
            idLabel.setText(item.getItemId());
            categoryLabel.setText(item.getItemCategory().toString());
            nameLabel.setText(item.getItemName());
            descriptionLabel.setText(item.getDescription());
            modelLabel.setText(item.getModelLocation());
        }
    }

    // Menampilkan interface Edit Admin dengan data kosong
    @FXML
    private void newButtonClicked() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan interface Edit Admin dengan data sesuai selectedItem
    @FXML
    private void editButtonClicked() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan konfirmasi, kemudian hapus player yang dipilih
    @FXML
    private void deleteButtonClicked() {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 4.7. StatsController

Mengontrol [Stats View](#37-stats-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class StatsController {
    private Stat selectedStat;
    private StatService service;
    private Stage primaryStage;

    // Menghubungkan dengan objek pada view
    @FXML
    private TableView<Stat> statTable;
    @FXML
    private TableColumn<Stat, String> idColumn;
    @FXML
    private TableColumn<Stat, String> nameColumn;
    @FXML
    private Label idLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label valueLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Mengupdate table ketika ada perubahan data
    private void updateTable(List<Stat> list) {
        if (list != null)
            statTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    // Inisialisasi interface
    @FXML
    private void initialize() {
        service = StatService.getInstance();
        idColumn.setCellValueFactory(cellData -> cellData.getValue().getStatIdProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getStatNameProperty());

        setSelectedStat(null);

        statTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedStat(current));

        updateTable(service.getStatLists());
    }

    // Mengeset nilai selected item dan update details
    private void setSelectedStat(Stat stat) {
        selectedStat = stat;
        editButton.disableProperty().setValue(stat == null);
        deleteButton.disableProperty().setValue(stat == null);

        if (stat == null) {
            idLabel.setText("");
            nameLabel.setText("");
            typeLabel.setText("");
            valueLabel.setText("");
        } else {
            idLabel.setText(selectedStat.getStatId().toString());
            nameLabel.setText(selectedStat.getName());
            typeLabel.setText(selectedStat.getType().toString());
            valueLabel.setText(selectedStat.getValue().toString());
        }
    }

    // Menampilkan interface edit stat dengan data kosong
    @FXML
    private void newButtonClicked() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan interface edit stat dengan data sesuai selectedStat
    @FXML
    private void editButtonClicked() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan pesan konfirmasi, lalu hapus item yang dipilih
    @FXML
    private void deleteButtonClicked() {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 4.8. AdminController

Mengontrol [Admin View](#38-admin-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class AdminController {
    private Admin admin;
    private AdminService adminService;
    private FileUploadService uploadService;
    private Config config;
    private File adminAvatar;
    private Stage primaryStage;
    private MainMenuController mainMenuController;

    // Menghubungkan dengan objek pada view
    @FXML
    private ImageView avatarPreview;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rePasswordField;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Inisialisasi interface
    @FXML
    private void initialize() {
        config = Config.getConfig();
        admin = config.getCurrentAdmin();
        adminService = AdminService.getInstance();
        uploadService = FileUploadService.getInstance();
        usernameField.setText(admin.getUsername());
        nicknameField.setText(admin.getAdminName());
        adminAvatar = new File(config.getConfigLocation() + Defaults.getImageLocation());
        try {
            avatarPreview.setImage(new Image(new FileInputStream(adminAvatar)));
        } catch (FileNotFoundException e) {
            adminAvatar = null;
            avatarPreview.setImage(new Image(getClass().getResource("/img/image.png").getPath()));
        }
    }

    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    // Tampilkan konfirmasi, hapus akun, lalu logout
    @FXML
    private void doDelete() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Reset inputan
    @FXML
    private void doReset() {
        usernameField.setText(admin.getUsername());
        nicknameField.setText(admin.getAdminName());
        oldPasswordField.setText("");
        passwordField.setText("");
        rePasswordField.setText("");
    }

    // Update data admin
    @FXML
    private void doUpdate() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Update data local setelah update profile
    private void postUpdateProfile(Admin result) {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan dialog untuk memilih file untuk avatar
    @FXML
    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Supported image files (*.jpg, *.jpeg, *.png)",
                        "*.jpg", "*.jpeg", "*.png"));

        System.out.println("Choosing image.");

        adminAvatar = chooser.showOpenDialog(primaryStage);
        if (adminAvatar != null) {
            try {
                avatarPreview.setImage(new Image(new FileInputStream(adminAvatar)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Upload avatar via FileUploadService
    @FXML
    private void doUpload() {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 4.9. EditPlayerController

Mengontrol [EditPlayer View](#39-editplayer-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class EditPlayerController {
    private Player player;
    private Stage stage;

    // Menghubungkan dengan objek pada view
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField rePasswordField;
    @FXML
    private TextField creditField;
    @FXML
    private ToggleSwitch banToggle;

    public void setPlayer(Player player) {
        this.player = player;
        creditField.setText(Integer.toString(player.getCreditBalance()));
        banToggle.setSelected(player.getBanStatus());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
    }

    // Close view saat cancel ditekan
    @FXML
    private void doCancel() {
        stage.close();
    }

    // Validasi input, lalu simpan value
    @FXML
    private void doSave() {
        PlayerService service = PlayerService.getInstance();
        String password = passwordField.getText();
        String rePassword = rePasswordField.getText();
        int credit = Integer.parseInt(creditField.getText());
        boolean banStatus = banToggle.isSelected();

        if (password.isEmpty() || rePassword.isEmpty()) {
            service.editPlayer(player.getPlayerId(), null, credit, banStatus);
            stage.close();
            return;
        }
        if (!password.equals(rePassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Error");
            alert.setHeaderText("Input Error");
            alert.setContentText("Password is mismatch");

            alert.showAndWait();
            return;
        }
        else {
            service.editPlayer(player.getPlayerId(), password, credit, banStatus);
        }

        player.setCreditBalance(credit);
        player.setBanStatus(banStatus);
        stage.close();
    }
}
```

## 4.10. EditItemController

Mengontrol [EditItem View](#310-edititem-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class EditItemController {
    private Item item;
    private Stage stage;
    private File file;

    // Menghubungkan dengan objek pada view
    @FXML
    private ComboBox<String> categoryBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descField;
    @FXML
    private Button statsButton;
    @FXML
    private Button uploadButton;

    public void setItem(Item item) {
        this.item = item;
        statsButton.disableProperty().setValue(item == null);

        if (item != null) {
            nameField.setText(item.getItemName());
            categoryBox.getSelectionModel().select(item.getItemCategory().toString());
            descField.setText(item.getDescription());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Inisialisasi interface
    @FXML
    private void initialize() {
        file = null;

        ObservableList<String> optionList = FXCollections.observableArrayList();
        optionList.add("TANK");
        optionList.add("SKIN");
        optionList.add("GAMEPLAY_DROPPED_ITEM");
        optionList.add("INVENTORY_CAPACITY");
        categoryBox.setItems(optionList);

        if (item != null) {
            nameField.setText(item.getItemName());
            categoryBox.getSelectionModel().select(item.getItemCategory().toString());
            descField.setText(item.getDescription());
        }
        statsButton.disableProperty().setValue(item == null);
    }

    // Memilih file asset yang akan diupload
    @FXML
    private void doUpload() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Supported archive files (*.zip)", "*.zip"));

        System.out.println("Choosing archive.");

        file = chooser.showOpenDialog(stage);

        if (file != null)
            uploadButton.setText(file.getName());
    }

    // Close interface saat cancel diklik
    @FXML
    private void doCancel() {
        stage.close();
    }

    // Validasi input lalu save
    @FXML
    private void doSave() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Menampilkan pesan error
    private void showErrorMessage(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
```

## 4.11. EditStatController

Mengontrol [EditStat View](#311-editstat-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class EditStatController {
    private Stat stat;
    private Stage stage;

    // Menghubungkan dengan objek pada view
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private TextField nameField;
    @FXML
    private TextField valueField;

    public void setStat(Stat stat) {
        this.stat = stat;
        if (stat != null) {
            typeBox.getSelectionModel().select(stat.getType().toString());
            nameField.setText(stat.getName());
            valueField.setText(stat.getValue().toString());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Inisialisasi interface
    @FXML
    private void initialize() {
        ObservableList<String> optionList = FXCollections.observableArrayList();
        optionList.add("HITPOINT");
        optionList.add("ATTACK");
        optionList.add("DEFENSE");
        optionList.add("SPEED");
        optionList.add("RELOAD_SPEED");
        optionList.add("HITPOINT_BOOST");
        optionList.add("ATTACK_BOOST");
        optionList.add("DEFENSE_BOOST");
        optionList.add("SPEED_BOOST");
        optionList.add("RELOAD_SPEED_BOOST");
        optionList.add("INV_CAPACITY_BONUS");
        typeBox.setItems(optionList);

        if (stat != null) {
            typeBox.getSelectionModel().select(stat.getType().toString());
            nameField.setText(stat.getName());
            valueField.setText(stat.getValue().toString());
        }
    }

    // Close view saat cancel diklik
    @FXML
    private void doCancel() {
        stage.close();
    }

    // Validasi input lalu save
    @FXML
    private void doSave() {
        // function body (diabaikan karena terlalu panjang)
    }

    // Tampilkan pesan error
    private void showErrorMessage(String header, String body) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(stage);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(body);

        alert.showAndWait();
    }
}
```

## 4.12. EditItemStatController

Mengontrol [EditItemStat View](#312-edititemstat-view)

```java
package com.fluxhydravault.restfrontendfx.controller;

// import statements

public class EditItemStatsController {
    private ItemService itemService;
    private StatService statService;
    private Stat selectedStat;
    private Stat selectedItemStat;
    private Item currentItem;
    private Stage stage;

    // Menghubungkan dengan objek pada view
    @FXML
    private TableView<Stat> itemStatTable;
    @FXML
    private TableColumn<Stat, String> itemStatName;
    @FXML
    private TableColumn<Stat, String> itemStatType;
    @FXML
    private TableColumn<Stat, String> itemStatValue;
    @FXML
    private TableView<Stat> statTable;
    @FXML
    private TableColumn<Stat, String> statName;
    @FXML
    private TableColumn<Stat, String> statType;
    @FXML
    private TableColumn<Stat, String> statValue;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
        updateItemStatTable(itemService.getItemStats(currentItem.getItemId()));
    }

    // Update table stat saat ada perubahan data
    private void updateStatTable(List<Stat> list) {
        if (list != null)
            statTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    // Update table item stat saat ada perubahan data
    private void updateItemStatTable(List<Stat> list) {
        if (list != null)
            itemStatTable.setItems(FXCollections.observableArrayList(list));
        else {
            System.out.println("List is empty");
        }
    }

    // Inisialisasi interface
    @FXML
    private void initialize() {
        itemService = ItemService.getInstance();
        statService = StatService.getInstance();

        setSelectedItemStat(null);
        setSelectedStat(null);

        statName.setCellValueFactory(cellData -> cellData.getValue().getStatNameProperty());
        statType.setCellValueFactory(cellData -> cellData.getValue().getStatTypeProperty());
        statValue.setCellValueFactory(cellData -> cellData.getValue().getStatValueProperty());

        statTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedStat(current));

        updateStatTable(statService.getStatLists());

        itemStatName.setCellValueFactory(cellData -> cellData.getValue().getStatNameProperty());
        itemStatType.setCellValueFactory(cellData -> cellData.getValue().getStatTypeProperty());
        itemStatValue.setCellValueFactory(cellData -> cellData.getValue().getStatValueProperty());

        itemStatTable.getSelectionModel().selectedItemProperty()
                .addListener((observable, old, current) -> setSelectedItemStat(current));
    }

    private void setSelectedStat(Stat selectedStat) {
        this.selectedStat = selectedStat;
        addButton.disableProperty().setValue(selectedStat == null);
    }

    private void setSelectedItemStat(Stat selectedItemStat) {
        this.selectedItemStat = selectedItemStat;
        deleteButton.disableProperty().setValue(selectedItemStat == null);
    }

    // Hapus stat dari item saat tombol delete diklik
    @FXML
    private void doDelete() {
        List<Stat> result = itemService.deleteItemStats(currentItem.getItemId(), selectedItemStat.getStatId());
        updateItemStatTable(result);
    }

    // Tambahkan stat ke item saat tombol add diklik
    @FXML
    private void doAdd() {
        List<Stat> result = itemService.addItemStat(currentItem.getItemId(), selectedStat.getStatId());
        updateItemStatTable(result);
    }

    // Close view saat cancel diklik.
    @FXML
    private void doCancel() {
        stage.close();
    }
}
```

# 5. Service

Service di sini bertindak sebagai layanan yang menjembatani komunikasi antara client-app dan REST Server. Terdapat 6 class service pada aplikasi ini.

## 5.1. LoginService

LoginService berfungsi untuk menghubungi REST Server untuk melakukan login.

```java
public class LoginService {
    private Gson gson;
    private Config config;

    /**
     * Instansiasi instance
     */
    private static final LoginService instance = new LoginService();

    /**
     * Constructor di-private agar singleton. Gson set custom
     * agar bisa parse snake case
     */
    private LoginService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    /**
     * Mendapatkan satu-satunya instance yang dibuat
     */
    public static LoginService getInstance() {
        return instance;
    }

    /**
     * Menghubungi REST Server dengan mengirim POST request ke 
     * /auth/admin. Akan mereturn token dan info admin bila berhasil
     */
    public void login(String username, String password) {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 5.2. PlayerService

PlayerService berfungsi untuk menghubungi REST Server untuk mendapatkan dan memanipulasi data Player yang ada di REST Server.

```java
public class PlayerService {
    private Gson gson;
    private Config config;

    /**
     * Instansiasi instance
     */
    private static final PlayerService instance = new PlayerService();

    /**
     * Constructor di-private agar singleton. Gson set custom
     * agar bisa parse snake case
     */
    private PlayerService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    /**
     * Mendapatkan satu-satunya instance yang dibuat
     */
    public static PlayerService getInstance() {
        return instance;
    }

    /**
     * Menghubungi REST Server untuk mendapatkan daftar player
     */
    public List<Player> getPlayerLists() {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mendapatkan hasil pencarian
     * berdasarkan username
     */
    public SearchResponse<Player> searchPlayer(String username) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mendapatkan hasil pencarian
     * berdasarkan playerID
     */
    public Player searchPlayerById(String playerID) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk melakukan pengeditan pada
     * player dengan id bernilai playerID
     */
    public Player editPlayer(String playerID, String password, int credit, boolean banStatus) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk menghapus data player
     * berdasarkan playerID
     */
    public void deletePlayer(String playerID) {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 5.3. ItemService

ItemService berfungsi untuk menghubungi REST Server untuk mendapatkan dan memanipulasi data Item yang ada di REST Server.

```java
public class ItemService {
    private Gson gson;
    private Config config;

    /**
     * Instansiasi instance
     */
    private static final ItemService instance = new ItemService();

    /**
     * Constructor di-private agar singleton. Gson set custom
     * agar bisa parse snake case
     */
    private ItemService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    /**
     * Mendapatkan satu-satunya instance yang dibuat
     */
    public static ItemService getInstance() {
        return instance;
    }

    /**
     * Menghubungi REST Server untuk menambahkan item baru
     */
    public Item newItem(Item item) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mendapatkan daftar item
     */
    public List<Item> getItemLists() {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mengedit item sesuai Item 
     * object yang di-passing ke parameter
     */
    public Item editItem(Item item) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk menghapus data Item
     * berdasarkan itemID
     */
    public void deleteItem(String itemID) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mendapatkan daftar stat 
     * yang diaplikasikan dalam item tersebut
     */
    public List<Stat> getItemStats(String itemID) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk menambah data stat 
     * yang diaplikasikan dalam item tersebut
     */
    public List<Stat> addItemStat(String itemID, Long statID) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk menghapus data stat 
     * yang diplikasikan dalam item tersebut
     */
    public List<Stat> deleteItemStats(String itemID, Long statID) {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 5.4. StatService

StatService berfungsi untuk menghubungi REST Server untuk mendapatkan dan memanipulasi data Stat yang ada di REST Server.

```java
public class StatService {
    private Gson gson;
    private Config config;

    /**
     * Instansiasi instance
     */
    private static StatService instance = new StatService();

    /**
     * Constructor di-private agar singleton. Gson set custom
     * agar bisa parse snake case
     */
    private StatService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    /**
     * Mendapatkan satu-satunya instance yang dibuat
     */
    public static StatService getInstance() {
        return instance;
    }

    /**
     * Menghubungi REST Server untuk menambahkan player baru
     */
    public Stat newStat(Stat stat) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mendapatkan daftar stat
     */
    public List<Stat> getStatLists() {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mengedit stat sesuai Stat 
     * object yang di-passing ke parameter
     */
    public Stat editStat(Stat stat) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk menghapus data Stat
     * berdasarkan statID
     */
    public void deleteStat(Long statID) {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 5.5. AdminService

AdminService berfungsi untuk menghubungi REST Server untuk mendapatkan dan memanipulasi data Admin yang ada di REST Server.

```java
public class AdminService {
    private Gson gson;
    private Config config;

    /**
     * Instansiasi instance
     */
    private static final AdminService instance = new AdminService();

    /**
     * Constructor di-private agar singleton. Gson set custom
     * agar bisa parse snake case
     */
    private AdminService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    /**
     * Mendapatkan satu-satunya instance yang dibuat
     */
    public static AdminService getInstance() {
        return instance;
    }

    /**
     * Menghubungi REST Server untuk mendaftarkan admin baru
     */
    public Admin registerAdmin(String username, String password, String adminName) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mengedit admin yang saat 
     * ini sedang terlogin
     */
    public Admin editAdmin(String username, String password, String adminName) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk menghapus data admin
     * yang sedang terlogin
     */
    public void deleteAdmin() {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

## 5.6. FileUploadService

FileUploadService berfungsi untuk menghubungi REST Server untuk mengupload file menuju REST Server.

```java
public class FileUploadService {
    private Gson gson;
    private Config config;

    /**
     * Instansiasi instance
     */
    private static final FileUploadService instance = new FileUploadService();

    /**
     * Constructor di-private agar singleton. Gson set custom
     * agar bisa parse snake case
     */
    private FileUploadService() {
        gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        config = Config.getConfig();
    }

    /**
     * Mendapatkan satu-satunya instance yang dibuat
     */
    public static FileUploadService getInstance() {
        return instance;
    }

    /**
     * Menghubungi REST Server untuk mengupload user avatar
     */
    public String uploadAvatar(File avatar, String userID, boolean isAdmin) {
        // function body (diabaikan karena terlalu panjang)
    }

    /**
     * Menghubungi REST Server untuk mengupload model asset item
     */
    public String uploadAsset(File asset, String itemID) {
        // function body (diabaikan karena terlalu panjang)
    }
}
```

# 6. Class Lainnya

## 6.1. MainApp

Merupakan starting point dari aplikasi. Aplikasi pertama kali berjalan dimulai dari class ini tepatnya pada method `main`.

```java
package com.fluxhydravault.restfrontendfx;

// import statements

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Config config = Config.getConfig();
        config.loadConfig();
        Scene scene;

        if (config.getUserToken() == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Initial.fxml"));
            Parent root = loader.load();

            scene = new Scene(root, 600, 400);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);

            InitialController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setInitialScene(scene);
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();

            scene = new Scene(root, 960, 600);
            new JMetro(JMetro.Style.LIGHT).applyTheme(scene);

            MainMenuController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setInitialScene(null);
        }

        primaryStage.setTitle("War Tanks: Admin Portal");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
        primaryStage.show();
    }
}
```

## 6.2. Config

Merupakan class untuk menghandle config aplikasi mulai dari baca, simpan dan ubah config.

```java
package com.fluxhydravault.restfrontendfx.config;

// import statements

public class Config {
    private final String configLocation;

    private Admin currentAdmin;
    private String userToken;
    private String baseUri;
    private String fileServerUri;

    private static final Config config = new Config();

    private Config() {
        String os = System.getProperty("os.name");

        if (os.startsWith("Windows")) {
            configLocation = System.getenv("HOMEDRIVE") + System.getenv("HOMEPATH") + "\\.tank_game\\";
        }
        else {
            configLocation = System.getenv("HOME") + "/.tank_game/";
        }

        try {
            Files.createDirectory(Paths.get(configLocation));
        } catch (IOException e) {
            System.out.println("Path already exists: " + e.getMessage());
        }

        Defaults.getDefaultConfig(this);
    }

    public void loadConfig() {
        Gson gson = new Gson();
        File configFile = new File(configLocation + "config.json");

        try {
            List<String> lines = Files.readAllLines(configFile.toPath());
            StringBuilder json = new StringBuilder();
            lines.forEach(json::append);
            Config tmp = gson.fromJson(json.toString(), Config.class);

            setCurrentAdmin(tmp.currentAdmin);
            setBaseUri(tmp.baseUri);
            setUserToken(tmp.userToken);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveConfig() {
        Gson gson = new Gson();
        File configFile = new File(configLocation + "config.json");

        if (configFile.isDirectory()) {
            try {
                Files.delete(configFile.toPath());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write(gson.toJson(this));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Config getConfig() {
        return config;
    }

    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public void setCurrentAdmin(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public String getUserToken() {
        return userToken;
    }

    public String getFileServerUri() {
        return fileServerUri;
    }

    public void setFileServerUri(String fileServerUri) {
        this.fileServerUri = fileServerUri;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}
```

## 6.3. Defaults

Merupakan class berisi nilai-nilai default pada aplikasi.

```java
package com.fluxhydravault.restfrontendfx.config;

// import statements

public class Defaults {
    private Defaults() { }

    private static final String IMAGE_LOCATION = "avatar.img";

    private static final String APP_TOKEN = "e6a065c4517d3520dbaa8b63fc25527caccc39ce6dda5026b5232c027053fb3b";

    static void getDefaultConfig(Config config) {
        config.setCurrentAdmin(null);
        config.setUserToken(null);
        config.setBaseUri("https://ojoakua.site/tankgame");
        config.setFileServerUri("https://ojoakua.site");
    }

    public static String getAppToken() {
        return APP_TOKEN;
    }

    public static String getImageLocation() {
        return IMAGE_LOCATION;
    }

    public static ResponseHandler<String> getDefaultResponseHandler() {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
            else {
                throw new ClientProtocolException(EntityUtils.toString(response.getEntity()));
            }
        };
    }
}
```