package database.elasticsearch;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.google.gson.Gson;

import static org.elasticsearch.common.xcontent.XContentFactory.*;
import database.DBController;
import database.Log;

public class ElasticDBController implements DBController {

	private Client client;

	public ElasticDBController() {
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", "log_test").build();
		client = new TransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(
						"192.168.2.121", 9300));
	}

	public void saveLog(Log log) {
		try {
			IndexRequest indexRequest = new IndexRequest("log_test", "log");
			indexRequest.source(jsonBuilder().startObject()
					.field("level", log.getLevel())
					.field("message", log.getMessage())
					.field("createDate", log.getCreateDate()).endObject());
			IndexResponse response = client.index(indexRequest).actionGet();
			/*
			 * IndexResponse response = client.prepareIndex("log_test", "log")
			 * .setSource(jsonBuilder() .startObject() .field("level",
			 * log.getLevel()) .field("message", log.getMessage())
			 * .field("createDate", log.getCreateDate()) .endObject() )
			 * .execute() .actionGet();
			 */
			/*
			 * IndexRequest indexRequest = new IndexRequest("log_test","log");
			 * indexRequest.source(new Gson().toJson(log)); IndexResponse
			 * response = client.index(indexRequest).actionGet();
			 */
		} catch (ElasticsearchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Log> query(String query) {
		ArrayList<Log> rc = new ArrayList<Log>();
		
		QueryBuilder qb = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchQuery(query.split(";")[0].split(":")[0], query.split(";")[0].split(":")[1]))
				.must(QueryBuilders.matchQuery(query.split(";")[1].split(":")[0], query.split(";")[1].split(":")[1]))
				.must(QueryBuilders.rangeQuery("createDate").to(query.split(";")[3].split("/")[1]).from(query.split(";")[2].split("/")[1]))
				;
		
		SearchResponse response = client.prepareSearch("log_test")
                .setTypes("log")
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(qb)   
                .execute()
                .actionGet();
        SearchHits hits=response.getHits(); 

        for (SearchHit hit : hits) {
        	String message = hit.field("message").getValue().toString();
        	String level = hit.field("level").getValue().toString();
        	Date date = null;
			try {
				date = new SimpleDateFormat().parse(hit.field("createDate").getValue().toString());
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			rc.add(new Log(message, level, date));
		}
		return rc;
		
		/*ArrayList<Log> rc = new ArrayList<Log>();
		client.admin().indices().refresh(new RefreshRequest("log_test"))
				.actionGet();
		// MatchAll on the whole cluster with all default options
		SearchResponse response = client.prepareSearch().execute().actionGet();
		for (SearchHit hit : response.getHits()) {
			rc.add(new Log("", "", new Date()));
		}
		return rc;*/
	}

	public boolean shutdown() {
		try {
			client.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void clearContent() {
		client.delete(new DeleteRequest("log_test"));
		client.delete(new DeleteRequest("log"));
	}

}
