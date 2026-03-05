package fr.novaretail.etl;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.col;

public class EtlPipeline {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/novaretail_legacy";
    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "root";

    // private static final String DB_URL = "jdbc:mysql://localhost:3306/novaretail_legacy";
    // private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    // private static final String DB_USER = "root";
    // private static final String DB_PASSWORD = "";

    public void run() {
        SparkSession spark = SparkConfiguration.initializeSession();

        try {
            Dataset<Row> rawData = extract(spark);
            Dataset<Row> cleanedData = transform(rawData);
            load(cleanedData);

        } catch (Exception e) {
            System.err.println("Erreur critique dans le pipeline : " + e.getMessage());
        } finally {
            spark.stop();
        }
    }

    private Dataset<Row> extract(SparkSession spark) {
        System.out.println("Extraction des données depuis la DB...");
        return spark.read()
                .format("jdbc")
                .option("url", DB_URL)
                .option("dbtable", "public.customer_transactions")
                .option("user", DB_USER)
                .option("password", DB_PASSWORD)
                .option("driver", DB_DRIVER)
                .load();
    }

    private Dataset<Row> transform(Dataset<Row> rawData) {
        System.out.println("Nettoyage, anonymisation et tri (MapReduce)...");
        Dataset<Row> transformedData = rawData
                .filter(col("country").isNotNull())
                .drop("customer_email")
                .orderBy(col("country").asc(), col("purchase_amount").desc());

        transformedData.show();
        return transformedData;
    }

    private void load(Dataset<Row> cleanedData) {
        System.out.println("Archivage vers le Data Lake (partitionnement en cours)...");
        cleanedData.write()
                .mode(SaveMode.Overwrite)
                .partitionBy("country")
                .json("datalake_novaretail");

        System.out.println("Export terminé avec succès !");
    }
}