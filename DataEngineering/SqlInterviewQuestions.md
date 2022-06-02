## Sql Query Optimization

Reference: <https://www.sisense.com/blog/8-ways-fine-tune-sql-queries-production-databases/>

* SELECT fields instead of using SELECT *, to reduce the dimension of the table.
* Avoid SELECT DISTINCT
  To accomplish this goal however, a large amount of processing power is required. Additionally, data may be grouped to the point of being inaccurate. To avoid using SELECT DISTINCT, select more fields to create unique results.
* Avoid too many joins
* Create joins with INNER JOIN (not WHERE)
* Use WHERE instead of HAVING to define filters
* Use wildcards at the end of a phrase only
* Use LIMIT to sample query results
* Run your query during off-peak hours

## Indexing in Database

Reference: <https://stackoverflow.com/questions/1108/how-does-database-indexing-work#:~:text=The%20index%20is%20nothing%20but,table%20has%20thousands%20of%20rows.>

What is indexing?

Indexing is a way of sorting a number of records on multiple fields. Creating an index on a field in a table creates another data structure which holds the field value, and a pointer to the record it relates to. This index structure is then sorted, allowing Binary Searches to be performed on it.

The downside to indexing is that these indices require additional space on the disk since the indices are stored together in a table using the MyISAM engine, this file can quickly reach the size limits of the underlying file system if many fields within the same table are indexed.

## Database Throttling

Reference: <https://www.progress.com/blogs/throttling-database-using-rate-limits-for-sql-or-rest>

Data throttling is required when it is exposed to external consumers and due to 
following reasons,

* A naïve developer who keeps hogging all the resources due to an inefficiently written client request.
* A low priority user who keeps hogging the resources, causing service outages for a high priority users
* A malicious user who keeps attacking your API endpoints to cause DDoS for all other users

Introducing Rate Limits API
With the recent release of Hybrid Data Pipeline, admins can now throttle Data APIs (ODBC, JDBC or OData) with fine granularity to improve the overall QoS. With the Rate Limits API, you can configure the following parameters:

MaxFetchRows: Maximum number of rows that can be fetched per query
PasswordLockoutInterval: The duration, in seconds, for counting the number of consecutive failed authentication attempts
PasswordLockoutLimit: The number of consecutive failed authentication attempts that are allowed before locking the user account
PasswordLockoutPeriod: The duration, in seconds, for which a user account will not be allowed to authenticate to the system when the PasswordLockoutLimit is reached
CORSBehavior: Configuration parameter for CORS behavior. Setting the value to 0 disables the CORS filter.  Setting the value to 1 enables the CORS filter.
  

## Sql Aggregate Functions

* COUNT counts how many rows are in a particular column.
* SUM adds together all the values in a particular column.
* MIN and MAX return the lowest and highest values in a particular column, respectively.
* AVG calculates the average of a group of selected values.

## Types of Databases
Reference: <https://www.matillion.com/resources/blog/the-types-of-databases-with-examples>


* Relational Database: sql, mysql, postgresql, oracle, teradata, ibm db2.
* No Sql Database: Mongodb, Couch DB
* Cloud Database: Aws relational, azure sql db.
* Object-oriented database: An object-oriented database is based on object-oriented programming, so data and all of its attributes, are tied together as an object. Object-oriented databases are managed by object-oriented database management systems (OODBMS).
* Columnar Database: Google BigQuery, Cassandra, HBase, MariaDB, Azure SQL Data Warehouse
* Document Database: Document databases, also known as document stores, use JSON-like documents to model data instead of rows and columns.
  MongoDB, Amazon DocumentDB, Apache CouchDB
* Graph Database: Graph databases are a type of NoSQL database that are based on graph theory. Graph-Oriented Database Management Systems (DBMS) software is designed to identify and work with the connections between data points
Datastax Enterprise Graph, Neo4J

## Windows Functions

Reference: <https://www.geeksforgeeks.org/window-functions-in-sql/>

Window functions applies aggregate and ranking functions over a particular window (set of rows). OVER clause is used with window functions to define that window. OVER clause does two things : 

Partitions rows into form set of rows. (PARTITION BY clause is used) 
Orders rows within those partitions into a particular order. (ORDER BY clause is used) 

Types of Window Functions
In SQL, there are basically two types of window functions – Aggregate window functions and analytical window functions.

* Aggregate Window Functions – As the name suggests, these types of window functions calculate the aggregated values of a group of rows from the table. Some examples of aggregate window functions are SUM, AVG, MIN, MAX etc. You need to use the GROUP BY clause in order to use these aggregate window functions with some other columns. This usually returns a scalar value
* Analytical Window Functions – These types of functions are used to calculate some window based on the current row and then calculate the results based on that window of records. The result is often returned in the form of multiple records in SQL. Common examples include RANK, DENSE_RANK, CUME_DIST, RANK, LEAD, LAG,etc.

Basic Syntax

```

SELECT coulmn_name1, 
 window_function(cloumn_name2),
 OVER([PARTITION BY column_name1] [ORDER BY column_name3]) AS new_column
FROM table_name;

```

Example – 
Find average salary of employees for each department and order employees within a department by age.

```

SELECT Name, Age, Department, Salary, 
 AVERAGE(Salary) OVER( PARTITION BY Department ORDER BY Age) AS Avg_Salary
 FROM employee

```

## Write queries

### Scenario 1
SQL query to get count of all employees department wise but some department can have 0 employee in it.
2 Tables were:
Employees(Employee Name and Department Name)
Departments(Department Name, Department Id)

