package database.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

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
		ArrayList<Log> rc = new ArrayList<Log>();
		// MatchAll on the whole cluster with all default options
		SearchResponse response = client.prepareSearch().execute().actionGet();
		for(SearchHit hit : response.getHits().getHits()){
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

}
