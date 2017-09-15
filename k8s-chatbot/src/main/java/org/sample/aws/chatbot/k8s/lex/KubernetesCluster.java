package org.sample.aws.chatbot.k8s.lex;

public class KubernetesCluster {
    int masterNodes;
    int workerNodes;
    String region;
    String s3Bucket;

    public KubernetesCluster(int masterNodes, int workerNodes, String region, String s3Bucket) {
        this.masterNodes = masterNodes;
        this.workerNodes = workerNodes;
        this.region = region;
        this.s3Bucket = s3Bucket;
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
}
