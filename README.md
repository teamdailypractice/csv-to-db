# CSV file to H2 Database

* Load one, two or more CSV file to H2 database.
* Why?
* Its easy to analyse data using SQL

## Steps

* Define the csv schema in a json file
* Read the csv schema from the json
* Create a table as per the schema
* Load the data
* If any of the rows are not compatible with schema, write into separate file and report the warning/error

## API Design - Draft

* createTable(schema, tablename)
  * If tablename already exists, report error: Table already exists
* createTable(schema, tablename, overwriteExisting)
  * Existing table will be dropped. New table with the specified schema would be created
* LoadData(csvFilepath, tablename)
  * What are the exception scenarios?
  * csv file does not match table schema?
  * some of the rows does not match existing schema
* getData(tablename1, tablename2, tablename3, selectColumns, joinColumns, filterCondition)

## maven Project creation from command line

```bash
PROJECT_HOME='/d/git/csv-to-db'
cd ${PROJECT_HOME}
touch pom.xml
mkdir -p src/main/java
mkdir -p src/main/java/info/dailypractice
touch src/main/java/info/dailypractice/Main.java
mkdir -p src/main/resources

mkdir -p test/main/java
mkdir -p test/main/resources
```
