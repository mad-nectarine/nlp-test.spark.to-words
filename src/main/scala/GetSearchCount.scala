package mad_nectarine.spark

import java.util.Properties
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{MapWritable, Text}
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.{Logging, SparkConf}
import org.elasticsearch.hadoop.mr.EsInputFormat

object GetSearchCount extends Logging {
  def main(args: Array[String]) {

    //validate args
    if (args.length < 1) {
      throw new IllegalArgumentException("search word is required")
    }

    //create spark conf
    val sparkConf = new SparkConf()
    sparkConf.setAppName("mad_nectarine.GetTweetsSearchCount")
    val context = new JavaSparkContext(sparkConf)

    try {
      //load config
      System.out.println("executing... [load config]")
      val fs = FileSystem.get(context.hadoopConfiguration());
      val propertiesStream = fs.open(new Path("hdfs:///tmp/spark.to-words.properties"))
      val properties = new Properties()
      properties.load(propertiesStream)

      //create es conf
      System.out.println("executing... [create es conf]")
      val esConf = new JobConf()
      esConf.set("es.nodes", properties.getProperty("logic.search-count.nodes"))
      esConf.set("es.resource", properties.getProperty("logic.search-count.resource"))
      var query = properties.getProperty("logic.search-count.query").replace("@@search_word", args(0))
      query = query.replace("\\r\\n","")
      query = query.replace("\\n","")
      query = query.replace("\\r","")
      System.out.println(s"query is ${query}")
      esConf.set("es.query", query)

      //load data from elasticsearch
      System.out.println("executing... [load data from elasticsearch]")
      val esRDD = context.hadoopRDD(esConf,
        classOf[EsInputFormat[Text, MapWritable]],
        classOf[Text],
        classOf[MapWritable]
      )
      System.out.println("Count of records founds is " + esRDD.count())

    } finally{
      context.stop()
    }
  }
}
