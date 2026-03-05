package fr.novaretail.etl;

import org.apache.spark.sql.SparkSession;

public class SparkConfiguration {

    public static SparkSession initializeSession() {
        System.setProperty("hadoop.home.dir", "C:\\Hadoop\\hadoop-3.3.6");

        SparkSession spark = SparkSession.builder()
                .appName("NovaRetail_ETL")
                .master("local[*]")
                .getOrCreate();

        spark.sparkContext().setLogLevel("ERROR");

        return spark;
    }
}