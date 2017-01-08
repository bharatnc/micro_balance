package com.bharat.nallan.balancer;
import org.asynchttpclient.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RequestRoundRobin {

    private ArrayList<String> hosts = new ArrayList();
    private ArrayList<String> unhealthyHosts = new ArrayList();
    private int node = 0;
    private int unhealthyNode = 0;
    private Request request;

    public RequestRoundRobin(ArrayList<String> hosts) {
        this.hosts = hosts;
    }

    //GET Method
    public Response get(String pathName) throws ExecutionException, InterruptedException, IOException {
        int randInt = this.generateRandom(101);
        if(randInt > 95 && this.getSizeUnhealthyNodes() > 0) {
            return this.getResponseFromUnhealthyNodes(pathName);
        } else {
            Response responseType = this.getResponse((String)this.hosts.get(this.node) + pathName);
            if(responseType == null) {
                this.nullResponseHandlerForHosts();
                return this.getResponse((String)this.hosts.get(this.node) + pathName);
            } else {
                this.node = (this.node + 1) % this.hosts.size();
                return responseType;
            }
        }
    }

    public Response getResponse(String fullDomain) throws IOException {
        DefaultAsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

        Response result;
        try {
            ListenableFuture e = asyncHttpClient.prepareGet(fullDomain).execute();
            Response r = (Response)e.get();
            int st = r.getStatusCode();
            result = r;
            asyncHttpClient.close();
        } catch (Exception var7) {
            result = null;
            asyncHttpClient.close();
        }

        return result;
    }

    public Response getResponseFromUnhealthyNodes(String pathName) throws InterruptedException, IOException {
        Response healthResponse = this.getResponse((String)this.getUnhealthyHosts().get(this.unhealthyNode) + pathName);
        if(healthResponse == null) {
            this.unhealthyNode = this.getUnhealthyNode();
            healthResponse = this.getResponse((String)this.hosts.get(this.node) + pathName);
        } else {
            this.hosts.add(this.getUnhealthyHosts().get(this.unhealthyNode));
            this.getUnhealthyHosts().remove(this.unhealthyNode);
            if(this.getUnhealthyHosts().size() >= 1) {
                this.unhealthyNode = (this.unhealthyNode + 1) % this.getUnhealthyHosts().size();
            }
        }

        return healthResponse;
    }

    //POST Method
    public Response post(String pathName, String postContent) throws ExecutionException, InterruptedException, IOException {
        int randInt = this.generateRandom(101);
        if(randInt > 95 && this.getUnhealthyHosts().size() > 0) {
            return this.postResponseFromUnhealthyNodes(pathName, postContent);
        } else {
            Response responseType = this.postResponse((String)this.hosts.get(this.node) + pathName, postContent);
            if(responseType == null) {
                this.nullResponseHandlerForHosts();
                return this.postResponse((String)this.hosts.get(this.node) + pathName, postContent);
            } else {
                this.node = (this.node + 1) % this.hosts.size();
                return responseType;
            }
        }
    }

    public Response postResponse(String fullDomain, String postContent) throws IOException {
        DefaultAsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        String jsonContent = postContent;
        Response result;
        try {
            ListenableFuture e = ((BoundRequestBuilder)((BoundRequestBuilder)((BoundRequestBuilder)asyncHttpClient.
                    preparePost(fullDomain).setHeader("Content-Type", "text/html")).
                    setHeader("Content-Length", "" + jsonContent.length())).
                    setBody(jsonContent)).execute();
            Response r = (Response)e.get();
            int st = r.getStatusCode();
            result = r;
            asyncHttpClient.close();
        } catch (Exception var9) {
            result = null;
            asyncHttpClient.close();
        }

        return result;
    }

    public Response postResponseFromUnhealthyNodes(String pathName, String postContent) throws InterruptedException, IOException {
        Response healthResponse = this.postResponse((String)this.getUnhealthyHosts().get(this.unhealthyNode) + pathName, postContent);
        if(healthResponse == null) {
            this.unhealthyNode = (this.unhealthyNode + 1) % this.getUnhealthyHosts().size();
            healthResponse = this.postResponse((String)this.hosts.get(this.node) + pathName, postContent);
        } else {
            this.hosts.add(this.getUnhealthyHosts().get(this.unhealthyNode));
            this.getUnhealthyHosts().remove(this.unhealthyNode);
            if(this.getUnhealthyHosts().size() >= 1) {
                this.unhealthyNode = (this.unhealthyNode + 1) % this.getUnhealthyHosts().size();
            }
        }

        return healthResponse;
    }

    public void nullResponseHandlerForHosts() {
        this.getUnhealthyHosts().add(this.hosts.get(this.node));
        this.hosts.remove(this.node);
        this.node = (this.node + 1) % this.hosts.size();
    }

    public ArrayList<String> getUnhealthyHosts() {
        return this.unhealthyHosts;
    }

    public int getSizeUnhealthyNodes() {

        return this.getUnhealthyHosts().size();
    }

    public int generateRandom(int num) {
        int randomNum = (int)Math.floor(Math.random() * (double)num);
        return randomNum;
    }

    public int getUnhealthyNode() {
        return (this.unhealthyNode + 1) % this.getUnhealthyHosts().size();
    }

    //Method to write to log file
    public void writeToLog(String fileName, String contentToWrite) {
        try {
            FileWriter ioe = new FileWriter(fileName, true);
            ioe.write(contentToWrite + "\n");
            ioe.close();
        } catch (IOException var4) {
            System.err.println("IOException: " + var4.getMessage());
        }
    }
}
