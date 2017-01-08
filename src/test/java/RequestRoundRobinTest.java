import com.bharat.nallan.microbalance.RequestRoundRobin;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

public class RequestRoundRobinTest {

    private ArrayList<String> sampleHealthyHosts = new ArrayList<String>() {{
        add("http://www.bharatnc.com");
    }};
    private ArrayList<String> sampleUnhealthyHosts = new ArrayList<String>() {{
        add("http://www.bharatnc.com");
    }};

    @Test
    public void testGet() throws IOException, ExecutionException, InterruptedException {
        Response r = createResponseObject();
        RequestRoundRobin rr = new RequestRoundRobin(sampleHealthyHosts);
        RequestRoundRobin rrMock = Mockito.spy(rr);
        Mockito.when(rrMock.generateRandom(101)).thenReturn(100);
        Mockito.when(rrMock.getResponse(sampleHealthyHosts.get(0))).thenReturn(r);
        Mockito.when(rrMock.getUnhealthyHosts()).thenReturn(sampleUnhealthyHosts);
        Mockito.when(rrMock.getSizeUnhealthyNodes()).thenReturn(1);
        System.out.print(rrMock.generateRandom(101));
        assertEquals(r, rrMock.get(""));
    }

    @Test
    public void testGetResponse() throws IOException, ExecutionException, InterruptedException {
        Response r = createResponseObject();
        RequestRoundRobin rr = new RequestRoundRobin(sampleHealthyHosts);
        RequestRoundRobin rrMock = Mockito.spy(rr);
        Mockito.when(rrMock.getResponse("http://www.bharatnc.com")).thenReturn(r);
        rrMock.getResponse("http://www.bharatnc.com");
        verify(rrMock).getResponse("http://www.bharatnc.com");
    }

    @Test
    public void  testGetResponseFromUnhealthyNodes() throws IOException, InterruptedException, ExecutionException {
        Response r = createResponseObject();
        RequestRoundRobin rr = new RequestRoundRobin(sampleHealthyHosts);
        RequestRoundRobin rrMock = Mockito.spy(rr);
        Mockito.when(rrMock.getResponse("http://www.bharatnc.com")).thenReturn(r);
        Mockito.when(rrMock.getUnhealthyHosts()).thenReturn(sampleUnhealthyHosts);
        rrMock.getResponseFromUnhealthyNodes("http://www.bharatnc.com");
        verify(rrMock).getResponseFromUnhealthyNodes("http://www.bharatnc.com");

    }
    //Create Response Object
    public Response createResponseObject() throws InterruptedException, ExecutionException {
        Response r;AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        Future<Response> f = asyncHttpClient.prepareGet("https://www.example.com").execute();
        r = f.get();
        return r;
    }
}
