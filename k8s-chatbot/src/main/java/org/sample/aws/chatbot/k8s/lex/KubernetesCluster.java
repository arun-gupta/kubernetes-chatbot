package org.sample.aws.chatbot.k8s.lex;

public class KubernetesCluster {
    int masterNodes;
    int workerNodes;
    String region;
    String s3Bucket;
    String name;

    static final String DEFAULT_REGION = "us-west-1";

    public KubernetesCluster(int masterNodes, int workerNodes) {
        this.masterNodes = masterNodes;
        this.workerNodes = workerNodes;
    }

    public KubernetesCluster(int masterNodes, int workerNodes, String region, String s3Bucket, String name) {
        this.masterNodes = masterNodes;
        this.workerNodes = workerNodes;
        this.region = region;
        this.s3Bucket = s3Bucket;
        this.name = name;
    }

    public int getMasterNodes() {
        return masterNodes;
    }

    public void setMasterNodes(int masterNodes) {
        this.masterNodes = masterNodes;
    }

    public int getWorkerNodes() {
        return workerNodes;
    }

    public void setWorkerNodes(int workerNodes) {
        this.workerNodes = workerNodes;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
