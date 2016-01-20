cd /opt/spark.to-words

echo removing [spark.to-words.properties] from hdfs...
sudo -u hdfs hdfs dfs -rm /tmp/spark.to-words.properties

echo putting [spark.to-words.properties] to hdfs...
sudo -u hdfs hdfs dfs -put /opt/spark.to-words/spark.to-words.properties /tmp/spark.to-words.properties

echo executing app on spark...
${SPARK_HOME}/bin/spark-submit --master yarn-client --class mad_nectarine.spark.GetSearchCount /opt/spark.to-words/spark.to-words-assembly-1.0.jar $1

echo end all
