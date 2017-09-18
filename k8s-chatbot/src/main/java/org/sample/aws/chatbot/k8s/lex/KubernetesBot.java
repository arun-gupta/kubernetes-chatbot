package org.sample.aws.chatbot.k8s.lex;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesRequest;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.sample.aws.lex.request.LexRequest;
import org.sample.aws.lex.response.LexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class KubernetesBot implements RequestHandler<LexRequest, LexResponse> {

    private static final Logger log = LoggerFactory.getLogger(KubernetesBot.class);

    @Override
    public LexResponse handleRequest(LexRequest request, Context context) {
        log.info("onIntent requestId={} intent={}", context.getAwsRequestId(), request.getCurrentIntent().getName());

        if ("CreateIntent".equals(request.getCurrentIntent().getName())) {
            return getCreateResponse(request.getCurrentIntent().getSlots());
        } else if ("ScaleIntent".equals(request.getCurrentIntent().getName())) {
            return getScaleResponse(request.getCurrentIntent().getSlots());
        } else if ("DeleteIntent".equals(request.getCurrentIntent().getName())) {
            return getDeleteResponse(request.getCurrentIntent().getSlots());
        } else if ("AMAZON.HelpIntent".equals(request.getCurrentIntent().getName())) {
            return getHelpResponse();
        } else {
            throw new RuntimeException("Invalid Intent: " + request.getCurrentIntent().getName());
        }
    }

    private LexResponse getCreateResponse(Map<String, String> slots) {
        KubernetesCluster cluster = getCluster(slots);

        // kops create cluster {name}
        // --master-count {masterNodes}
        // --node-count {workerNodes}
        // --zones {availabilityZones}
        // --state=s3://{s3Bucket}
        // --yes
        return LexResponse.getLexResponse("Do you want to create a Kubernetes cluster " +
                "named " + cluster.name + " with " +
                cluster.masterNodes + " master nodes, " +
                cluster.workerNodes + " worker nodes," +
                "in " + cluster.region + " region" +
                "using " + cluster.s3Bucket + " s3 bucket?", "Kubernetes cluster create");
    }

    private LexResponse getScaleResponse(Map<String, String> slots) {
        KubernetesCluster cluster = getCluster(slots);

        // ??
        return LexResponse.getLexResponse("Do you want to scale the Kubernetes cluster " +
                "named " + cluster.name + " with " +
                cluster.masterNodes + " worker nodes?", "Kubernetes cluster scale");
    }

    private LexResponse getDeleteResponse(Map<String, String> slots) {
        KubernetesCluster cluster = getCluster(slots);

        // kops delete cluster --name={name} --yes
        return LexResponse.getLexResponse("Do you want to delete the Kubernetes cluster named "
                + cluster.name + "?", "Kubernetes cluster delete");
    }

    private KubernetesCluster getCluster(Map<String, String> slots) {
        KubernetesCluster cluster = new KubernetesCluster();

        if (slots.get("master") == null) {
            cluster.setMasterNodes(3);
        } else {
            cluster.setMasterNodes(Integer.parseInt(slots.get("master")));
        }

        if (slots.get("worker") == null) {
            cluster.setWorkerNodes(3);
        } else {
            cluster.setWorkerNodes(Integer.parseInt(slots.get("worker")));
        }

        if (slots.get("region") == null) {
            cluster.setRegion(KubernetesCluster.DEFAULT_REGION);
        } else {
            cluster.setRegion(slots.get("region"));
        }
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        DescribeAvailabilityZonesRequest request = new DescribeAvailabilityZonesRequest();
        request.withZoneNames(cluster.getRegion());
        DescribeAvailabilityZonesResult result = ec2.describeAvailabilityZones(request);
        result.getAvailabilityZones().forEach((az) -> {
            cluster.addAvailabilityZone(az.getZoneName());
        });

        String bucketName;
        if (slots.get("s3") == null) {
            // get a default bucket name
            bucketName = "k8s-s3-" + UUID.randomUUID();
        } else {
            bucketName = slots.get("s3");
        }
        // create a s3 bucket using API
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
        if(!(s3Client.doesBucketExist(bucketName))) {
            // Note that CreateBucketRequest does not specify region. So bucket is
            // created in the region specified in the client.
            s3Client.createBucket(bucketName);
        }
        cluster.setS3Bucket("s3://" + bucketName);

        if (slots.get("name") == null) {
            // create a cluster name
            String name = "k8s-cluster-" + UUID.randomUUID();
            cluster.setName(name);
        } else {
            cluster.setName(slots.get("name"));
        }

        return cluster;
    }

    private LexResponse getHelpResponse() {
        return LexResponse.getLexResponse("You can create Kubernetes cluster", "Kubernetes Chatbot Help");
    }
}