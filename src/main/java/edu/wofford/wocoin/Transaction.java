package edu.wofford.wocoin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Transaction {
    private static final Logger log = LoggerFactory.getLogger(Transaction.class);

    public static void run() throws Exception {
        Web3j web3j = Web3j.build(new HttpService());
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());
        Credentials credentials =
                WalletUtils.loadCredentials(
                        "adminpwd",
                        "C:\\Users\\cburd\\project-i-b-ok\\ethereum\\node0\\keystore\\UTC--2019-08-07T17-24-10.532680697Z--0fce4741f3f54fbffb97837b4ddaa8f769ba0f91.json");
        log.info("Credentials loaded");
        log.info("Sending Ether ..");
        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                "0xa615316333ba8622fd5bb60fe39758b3515f774d",
                BigDecimal.valueOf(11), Convert.Unit.ETHER).sendAsync()
                .get();
        log.info("Transaction complete : "
                + transferReceipt.getTransactionHash());
    }
}

/*      geth --datadir ./node0 init ./genesis.json
        geth --rpc --rpcaddr
        miner.start()

        eth.accounts
        web3.fromWei(eth.getBalance(<address_of_account>), "ether")
        eth.sendTransaction({from:”address”, to:”address”, value: web3.toWei(amount, "ether")})
        personal.unlockAccount(eth.accounts[0], "<password>")*/
