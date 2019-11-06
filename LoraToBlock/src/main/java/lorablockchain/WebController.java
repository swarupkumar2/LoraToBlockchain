package lorablockchain;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lorablockchain.entity.LoRaData;
import lorablockchain.entity.Payload;
import lorablockchain.services.IBlockchainMgmService;

@RestController
public class WebController {

	private final IBlockchainMgmService blockchainMgmService;
	
	public WebController(@Qualifier("BlockchainService") IBlockchainMgmService blockchainMgmService) {
		
		this.blockchainMgmService = blockchainMgmService;
	}

	@RequestMapping(value = "/")
	String home() {
		return "Welcome to LoRa to BlockChain service !!!";
	}
	
	@RequestMapping(value = "/toblockchain", method = RequestMethod.POST)
	@ResponseBody
    public String fromLoraDevice(@RequestBody LoRaData loradata) throws IOException {
		
		//System.out.println(loradata);
		String response = blockchainMgmService.postToBlockchain(loradata);
		
		System.out.println("Data posted to blockchain, status: " + response);
		return "Data posted to blockchain, status: " + response;
		
    }

}
