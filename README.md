# CUP

Centro Unico Prenotazioni

[Leggi il compito assegnato per avere più informazioni](WEB_Project_2018_2019.pdf)

## Descrizione

Questo servizio consente l'accesso al sistema di prenotazione per i Servizi Sanitari delle visite specialistiche e delle prestazioni di diagnostica strumentale.

Al servizio possono accedere pazienti, dottori e specialisti, un dottore e uno specialista sono anche pazienti ma predispongono di determinate funzionalià in più.

Di seguito le funzionalità suddivise:

> Paziente

- Visualizzare le proprie informazioni personali
- Scegliere e/o cambiare un dottore
- Modificare la password del proprio profilo
- Cambiare foto profilo
- Visualizzare gli esami prescritti
- Visualizzare i farmaci prescritti
- Visualizzare e segnare come letti gli esami con report
- Visulizzare e scaricare i propri referti

> Dottore

- Visualizzare la scheda personale dei propri pazienti
- Prescrivere esami ai propri pazienti
- Prescrivere farmaci ai propri pazienti

> Medici Specialisti

- Visualizzare le visite eseguite
- Visualizzare le visite da eseguire
- Scrivere un referto relativo ad una visita assegnata

> Servizio Sanitario

- Assegnare ad una visita data, ora e esecutore
- Visualizzare le visite eseguite
- Visualizzare le visite da eseguire
- Scrivere un referto relativo ad una visita assegnata

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

- ### Preparation

  1. **Clone** this repo on your host using [Git](https://git-scm.com)

     ```console
     $ git clone https://github.com/carlocorradini/cup.git
     ```

  2. **Change** current working **directory**

     ```console
     $ cd cup
     ```

  3. Create the war file

     ```console
     $ mvn clean install package
     ```

  4. Change current directory

     ```console
     $ cd target
     ```

  5. Copy the war file **cup.war**

     ```console
     $ ls
     ```

  6. Paste the war file in the folder **CATALINA_HOME/webapps**

     ```console
     $ CATALINA_HOME/webapps
     ```

  7. Change current directory
     ```console
     $ cd CATALINA_HOME/bin
     ```
  8. Start Tomcat server

     > Windows user

     ```console
     $ startup.bat
     ```

     > Linux user

     ```console
     $ startup.sh
     ```

- ### Restore the database backup

  > Change datatabase options in the file **database.propreties** before you start the database

- Create the database

  1. Open the SQL Shell
  2. Provide the data requested
  3. Enter the command

     ```console
     $ CREATE DATABASE [name]
     ```

     > Where [name] is the database name provided in **database.properties**

  4. Open command line window
  5. Go to PostgreSQL bin folder

     ```console
     $ PG_HOME/bin
     ```

  6. Restore the database
     ```console
        $ psql -U [username] -d [name] -f backup.sql
     ```

## Property files

| Property                | Description                                                                                       |
| ----------------------- | ------------------------------------------------------------------------------------------------- |
| app.properties          | Info, logo of the application and informations about the authors                                  |
| auth.properties         | Properties for cookies and session timeouts                                                       |
| database.properties     | Host, port number, name of the database and data for the authentication                           |
| email.properties        | Information about the email used to send emails to the users                                      |
| prescription.properties | Properties for the max and min prescription quantity, min and max length of the report characters |
| template.properties     | Properties for the standard layout of the application                                             |

## Built With

- [Java](https://www.java.com) - General purpose computer-programming language
- [Maven](https://maven.apache.org/) - Dependency Management
- [Git](https://git-scm.com) - Distributed version control system
- [Tomcat](https://tomcat.apache.org) - Web Server

> Java Dependencies

| Dependency                                                                                            | Description                                                                                                                                                                                                                |
| ----------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Java Servlet](https://www.oracle.com/technetwork/java/index-jsp-135475.html)                         | Java servlets can respond to many types of requests, they most commonly implement web containers for hosting web applications on web servers and thus qualify as a server-side servlet web API                             |
| [JSF](http://www.javaserverfaces.org)                                                                 | JavaServer Faces (JSF) è una tecnologia Java, basata sul design pattern architetturale Model-View-Controller (MVC), il cui scopo è quello di semplificare lo sviluppo dell'interfaccia utente (UI) di una applicazione Web |
| [Annotation Api](https://docs.oracle.com/javase/8/docs/api/java/lang/annotation/package-summary.html) | Provides library support for the Java programming language annotation facility                                                                                                                                             |
| [JAXB Api for Server](https://www.xml.com/pub/a/2003/01/08/jaxb-api.html)                             | Java Architecture for XML Binding (JAXB) is a Java standard that defines how Java objects are converted from and to XML. It uses a standard set of mappings                                                                |
| [Apache Commons Configuration](https://commons.apache.org/configuration/)                             | The Commons Configuration software library provides a generic configuration interface which enables a Java application to read configuration data from a variety of sources                                                |
| [Apache Commons IO](https://commons.apache.org/io/)                                                   | Commons IO is a library of utilities to assist with developing IO functionality                                                                                                                                            |
| [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)                                                | PostgreSQL JDBC Driver (PgJDBC for short) allows Java programs to connect to a PostgreSQL database using standard, database independent Java code                                                                          |
| [Apache Commons Email](https://commons.apache.org/email/)                                             | Commons Email aims to provide a API for sending email                                                                                                                                                                      |
| [JBCrypt](https://github.com/jeremyh/jBCrypt)                                                         | jBCrypt is a Java™ implementation of OpenBSD's Blowfish password hashing code                                                                                                                                              |
| [FAST JSON](https://github.com/alibaba/fastjson)                                                      | Fastjson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object.                                             |
| [REST API](https://restfulapi.net/)                                                                   | Descrivono una serie di linee guida e di approcci che definiscono lo stile con cui i dati vengono trasmessi                                                                                                                |
| [SCALR Image](http://javadox.com/org.imgscalr/imgscalr-lib/4.2/org/imgscalr/Scalr.html)               | imgscalr is an simple and efficient best-practices image-scaling and manipulation library implemented in pure Java                                                                                                         |
| [Apache PDFBox](https://pdfbox.apache.org/)                                                           | The Apache PDFBox library is an open source Java tool for working with PDF documents                                                                                                                                       |
| [Apache POI](https://poi.apache.org/)                                                                 | The Java API for Microsoft Documents                                                                                                                                                                                       |
| [Google ZXing QR Code](https://www.callicoder.com/generate-qr-code-in-java-using-zxing/)              | library to generate QR codes for our application                                                                                                                                                                           |  |

> Javascript libraries

| Library                                                                 | Description                                                                                                                                                                       |
| ----------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [cookieconsent](https://cookieconsent.osano.com/)                       | Cookie Consent is a lightweight JavaScript plugin for alerting users about the use of cookies on your website, it is designed to help you quickly comply with the EU Cookie Law   |
| [datatables](https://datatables.net/)                                   | DataTables is a table enhancing plug-in for the jQuery Javascript library, adding sorting, paging and filtering abilities to plain HTML tables with minimal effort                |
| [jquery](https://jquery.com/)                                           | It makes things like HTML document traversal and manipulation, event handling, animation, and Ajax much simpler with an easy-to-use API that works across a multitude of browsers |
| [moment.js](https://momentjs.com/)                                      | A lightweight JavaScript date library for parsing, validating, manipulating, and formatting dates                                                                                 |
| [semantic-ui](https://semantic-ui.com/)                                 | Semantic is a development framework that helps create beautiful, responsive layouts using human-friendly HTML                                                                     |
| [URI.js](http://www.html.it/script/uri-js/)                             | javascript library for working with URLs                                                                                                                                          |
| [mixitup](https://www.kunkalabs.com/mixitup/)                           | A high-performance, dependency-free library for animated filtering, sorting, insertion, removal and more...                                                                       |
| [Semantic-UI-Calendar](https://github.com/mdehoog/Semantic-UI-Calendar) | javascript library for formatting date and time                                                                                                                                   |

## Versioning

We use [Git](https://git-scm.com) for versioning.

## Authors

- **Carlo Corradini** - _Initial work_ - 192451
- **Luca Santoro** - _Initial work_ - 195342
- **Ayoub Saghir** - _Initial work_ - 195605

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

© CUP 2019
