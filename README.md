# Project specifications

This project has as goal the connection between a Java application and a database. Relational databases are used to store the products, the clients and the orders. Furthermore, the application uses (minimally) the following classes:

* Model classes - represent the data models of the application
* Business Logic classes - contain the application logic
* Presentation classes – classes that contain the graphical user interface
* Data access classes - classes that contain the access to the database
* Other classes and packages can be added to implement the full functionality of the application. The interface should let the user modify the contents of each table of the warehouse database.

It is required to have the operations of insert, delete, edit and view all (basically, CRUD operations). These operations require a specific implementation using reflection. Using reflection, the code will be more generic and compact.

Also, the application must have an order platform in which the users chooses the client, a product and the quantity of the product. If the quantity required by the client is bigger than the one from stock (stored in the database), the application will print an error message displaying ‘Under stock’. Otherwise, if there are as many pieces of product as needed, the stock will be decreased, and the order will be processed.

After a successful order, the data regarding the order will be inserted in a specific table and the bill will be stored in a pdf.
