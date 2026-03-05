package fr.novaretail.etl;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- DÉMARRAGE DU PIPELINE ETL NOVARETAIL ---");

        EtlPipeline pipeline = new EtlPipeline();
        pipeline.run();

        System.out.println("--- ARRÊT DU SYSTÈME ---");
    }
}