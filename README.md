# techdegree-project-06 - Analyze Public Data with Hibernate

Create an application that loads World Bank data about Internet usage and adult literacy into a database. This command-line Java application models and analyzes the public dataset using Hibernate to manage the relationships between Java objects and relational data in a SQL-based database.
_______________________________________________________________________________

Project contains Hibernate configuration file specifying the following: Connection URL, DB driver class, Mapped entity class

Uses Hibernate to retrieve lists of entity objects and single entity objects
Uses transactions with Hibernate sessions to save and delete single entity objects
Uses the builder pattern to create new entities

All data is presented as formatted table, with column heading, 2 decimals of precision, and null valuea as "--"

Maximum, minimum and average are calculated with the use of Java streams
Correlation coefficient is calculated without the use of a third-party library

UI contains functioning menu options for all features : view data table, viewing statistics, adding a country, editing a country, deleting a country
