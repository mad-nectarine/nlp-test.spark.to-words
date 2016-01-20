# Setup

1. setup jar file
    you need download and copy below components to lib folder.
    * [elasticsearch-hadoop](http://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-hadoop)

# Compile

1. install jdk 1.8 and sbt
2. execute sbt-assembly
    
    *※ if you execute on windows, use "win.compile.bat" in this folder or copy this folder to "c:\" before execute below.*
    ```console
    cd /to-this-folder
    sbt assembly
    ```

# Execute on Spark

1. copy files to "/opt/spark.to-words" on Spark client server.
   * spark.to-words.properties
   * target/scala-2.11/spark.to-words-assembly-1.0.jar
   * execute.sh

1. execute app by execute.sh

   ```console
   cd /opt/spark.to-words
   command ./execute.sh "hoge-search-word"
   ...
   ...
   ...
   Count of records founds is 10000
   ...
   ```

# What's the "execute.sh" doing

1. remove and put "spark.to-words.properties" to hdfs.

    ```console
    sudo -u hdfs hdfs dfs -rm /tmp/spark.to-words.properties
    sudo -u hdfs hdfs dfs -put /opt/spark.to-words/spark.to-words.properties /tmp/spark.to-words.properties
    ```
 
1. execute app (using yarn)
   
   ```console
   ${SPARK_HOME}/bin/spark-submit \
   --master yarn-client \
   --class mad_nectarine.spark.GetSearchCount \
   /opt/spark.to-words/spark.to-words-assembly-1.0.jar \
   $1
   ```