package org.sample.aws.chatbot.k8s.lex;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.sample.aws.lex.request.LexRequest;
import org.sample.aws.lex.response.LexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KubernetesBot implements RequestHandler<LexRequest, LexResponse> {

    private static final Logger log = LoggerFactory.getLogger(KubernetesBot.class);

    @Override
    public LexResponse handleRequest(LexRequest request, Context context) {
        log.info("onIntent requestId={} intent={}", context.getAwsRequestId(), request.getCurrentIntent().getName());

        if ("CreateIntent".equals(request.getCurrentIntent().getName())) {
            return getCreateResponse(request.getCurrentIntent().getSlots());
        } else if ("AMAZON.HelpIntent".equals(request.getCurrentIntent().getName())) {
            return getHelpResponse();
        } else {
            throw new RuntimeException("Invalid Intent: " + request.getCurrentIntent().getName());
        }
    }

    private LexResponse getCreateResponse(Map<String, String> slots) {
        KubernetesCluster cluster = getCluster(slots);

        return LexResponse.getLexResponse("Do you want to create a Kubernetes cluster " +
                "named " + cluster.name + " with " +
                cluster.masterNodes + " master nodes, " +
                cluster.workerNodes + " worker nodes," +
                "in " + cluster.region + " region" +
                "using " + cluster.s3Bucket + " s3 bucket?", "Kubernetes cluster create");
    }

    private KubernetesCluster getCluster(Map<String, String> slots) {
        KubernetesCluster cluster = new KubernetesCluster(
                Integer.parseInt(slots.get("master")),
                Integer.parseInt(slots.get("worker")));

        if (slots.get("region") == null)
            cluster.setRegion(KubernetesCluster.DEFAULT_REGION);

        if (slots.get("s3") == null) {
            // create a s3 bucket name
            String s3 = "";
            cluster.setS3Bucket(s3);
        }

        if (slots.get("name") == null) {
            // create a cluster name
            String name = "";
            cluster.setName(name);
        }

        return cluster;
    }

    private LexResponse getHelpResponse() {
        return LexResponse.getLexResponse("You can create Kubernetes cluster", "Kubernetes Chatbot Help");
    }
}