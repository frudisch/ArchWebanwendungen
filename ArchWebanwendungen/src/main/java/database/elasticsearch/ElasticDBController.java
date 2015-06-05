package database.elasticsearch;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import database.DBController;
import database.Log;

public class ElasticDBController implements DBController{
	
	private TransportClient client;

	public ElasticDBController() {
		client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("192.168.2.121", 9300));
	}

	public void saveLog(Log log) {
		try {
			IndexResponse response = client.prepareIndex("log_test", "log")
			        .setSource(jsonBuilder()
			                    .startObject()
			                        .field("level", log.getLevel())
			                        .field("message", log.getMessage())
			                        .field("createDate", log.getCreateDate())
			                    .endObject()
			                  )
			        .execute()
			        .actionGet();
			
			System.out.println(response.getId());
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Log> query(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean shutdown() {
		try{
			client.close();
			return true;
		}catch (Exception e){
			return false;
		}
	}

}
