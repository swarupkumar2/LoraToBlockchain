package lorablockchain.services;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lorablockchain.entity.LoRaData;

@Service("BlockchainService")
public class BlockchainService implements IBlockchainMgmService {
	
// --------------------------------------- OAuth 2.0 Variables for BlockChain service START ---------------------------------
	
	private String clientId = System.getenv("BCclientid");
	private String clientSecret = System.getenv("BCclientsecret");
	private String tokenUrl = System.getenv("BCtokenurl");	
	private String chaincodeId = System.getenv("BCchaincodeid");
	private String version = System.getenv("BCversion");
	private String serviceUrl = System.getenv("BCserviceurl");
	
	private final ClientHttpRequestFactory clientHttpRequestFactory;
	private RestTemplate restTemplate;
		
// --------------------------------------- OAuth 2.0 Variables for BlockChain service END -----------------------------------
	
	@Autowired
	private Environment env;

	public BlockchainService() {
		
		clientHttpRequestFactory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
	}
	
	@PostConstruct                        
	public void init(){
		
		if(clientId == null) clientId = env.getProperty("BCclientid");
		if(clientSecret == null) clientSecret = env.getProperty("BCclientsecret");
		if(tokenUrl == null) tokenUrl = env.getProperty("BCtokenurl");
		if(chaincodeId == null) chaincodeId = env.getProperty("BCchaincodeid");
		if(version == null) version = env.getProperty("BCversion");
		if(serviceUrl == null) serviceUrl = env.getProperty("BCserviceurl");
		
		restTemplate = buildOAuth2RestTemplate(tokenUrl, clientId, clientSecret);
	}
	
	public String postToBlockchain(LoRaData loradata) {
			
		String time = Long.toString(new Date().getTime());
		int random = (int) (Math.random() * 100);
		String transId = time + random;
			
		String jsonbody = "{\"function\":\"create\",\"arguments\":[\""+transId+"\", \""+loradata.getApp_id()+
				"\", \""+loradata.getDev_id()+"\", \""+loradata.getHardware_serial()+"\", \""+loradata.getPort()+
				"\", \""+loradata.getCounter()+"\", \""+loradata.getPayload_raw()+"\", \""+loradata.getPayload_fields().getPayload()+
				"\", \""+loradata.getMetadata().getTime()+"\", \""+loradata.getDownlink_url()+"\"]}";
			
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String url = serviceUrl+"/chaincodes/"+chaincodeId+"/"+version+"/invoke";
		HttpEntity<String> entity = new HttpEntity<String>(jsonbody, headers);
			
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
			
		//System.out.println(response.getStatusCode().toString());
		
		return response.getStatusCode().toString();
	}
	
		
// ---------------------------------------------------------------------------------------------------------------------
// ---------------------------------------------- OAuth 2.0 Methods for BlockChain service------------------------------
// ---------------------------------------------------------------------------------------------------------------------
	    
	private RestTemplate buildOAuth2RestTemplate(String tokenUrl, String clientId, String clientSecret) {
		final OAuth2RestTemplate template = new OAuth2RestTemplate(fullAccessresourceDetailsClientOnly(tokenUrl, clientId, clientSecret), 
				new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest()));

		template.setRequestFactory(clientHttpRequestFactory);
		template.setAccessTokenProvider(clientAccessTokenProvider());

		return template;
	}
	    
	private AccessTokenProvider clientAccessTokenProvider() {
		final ClientCredentialsAccessTokenProvider accessTokenProvider = new ClientCredentialsAccessTokenProvider();

		accessTokenProvider.setRequestFactory(clientHttpRequestFactory);
		return accessTokenProvider;
	}

	private OAuth2ProtectedResourceDetails fullAccessresourceDetailsClientOnly(String tokenUrl, String clientId, String clientSecret) {
		final ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(tokenUrl);
		resource.setClientId(clientId);
		resource.setClientSecret(clientSecret);
		resource.setGrantType("client_credentials");
		return resource;
	}

}
