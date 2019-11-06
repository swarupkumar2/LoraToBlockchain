package lorablockchain.services;

import lorablockchain.entity.LoRaData;

public interface IBlockchainMgmService {
	String postToBlockchain(LoRaData loradata);

}
