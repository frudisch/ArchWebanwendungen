package database.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;

import com.google.gson.Gson;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import database.DBController;
import database.Log;

public class ElasticDBController implements DBController{
	
	private Client client;

	public ElasticDBController() {
		Settings settings = ImmutableSettings.settingsBuilder()
	            .put("cluster.name", "log_test")
	            .build();
		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("192.168.2.121", 9300));
	}

	public void saveLog(Log log) {
		try {
			/*IndexResponse response = client.prepareIndex("log_test", "log")
			        .setSource(jsonBuilder()
			                    .startObject()
			                        .field("level", log.getLevel())
			                        .field("message", log.getMessage())
			                        .field("createDate", log.getCreateDate())
			                    .endObject()
			                  )
			        .execute()
			        .actionGet();*/
			IndexRequest indexRequest = new IndexRequest("twitter","tweet");
			indexRequest.source(new Gson().toJson(log));
			IndexResponse response = client.index(indexRequest).actionGet();
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		}
	}

	public List<Log> query(String query) {
		ArrayList<Log> rc = new ArrayList<Log>();
		client.admin().indices().refresh(new RefreshRequest("log_test")).actionGet();
		// MatchAll on the whole cluster with all default options
		SearchResponse response = client.prepareSearch().execute().actionGet();
		for(SearchHit hit : response.getHits()){
			rc.add(new Log("", "", new Date()));
		}
		return rc;
	}

	public boolean shutdown() {
		try{
			client.close();
			return true;
		}catch (Exception e){
			return false;
		}
	}

	public void clearContent() {
		client.delete(new DeleteRequest("log_test"));
	}

}
