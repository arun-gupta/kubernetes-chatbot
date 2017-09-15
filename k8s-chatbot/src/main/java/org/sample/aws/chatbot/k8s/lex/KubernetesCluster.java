package org.sample.aws.chatbot.k8s.lex;

public class KubernetesCluster {
    int masterNodes;
    int workerNodes;
    String region;
    String s3Bucket;
    String name;
    String availabilityZones;

    static final String DEFAULT_REGION = "us-west-1";

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

    public String getAvailabilityZones() {
        return availabilityZones;
    }

    public void setAvailabilityZones(String availabilityZones) {
        this.availabilityZones = availabilityZones;
    }
}
