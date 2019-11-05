package edu.wofford.wocoin;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.sql.*;

public class SQLControllerTest {

    @Test
    public final void testConstructor(){
        SQLController foo = new SQLController("testDB.sqlite3");
        assertEquals("jdbc:sqlite:testDB.sqlite3", foo.getPath());

        foo = new SQLController();
        assertEquals("jdbc:sqlite:wocoinDatabase.sqlite3", foo.getPath());
    }


    @Test
    public final void walletExists(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        assertTrue(foo.findWallet("srogers"));
    }

    @Test
    public final void walletNotExists(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        assertTrue(!foo.findWallet("tstark"));
    }

    @Test
    public final void addWallet(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        foo.removeWallet("test");
        assertEquals(SQLController.AddWalletResult.ADDED, foo.addWallet("test","8675309"));
    }

    @Test
    public final void addWalletDuplicate(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        foo.addWallet("test","8675309");
        assertEquals(SQLController.AddWalletResult.ALREADYEXISTS, foo.addWallet("test","8675309"));
    }

    @Test
    public final void replaceWallet(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        foo.addWallet("test","8675309");
        assertEquals(SQLController.ReplaceWalletResult.REPLACED, foo.replaceWallet("test","867530"));
        assertEquals("867530",foo.RetrievePublicKey("test"));
    }

    @Test
    public final void replaceNonExistentWallet(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        assertEquals(SQLController.ReplaceWalletResult.NOSUCHWALLET, foo.replaceWallet("tstark","86753099"));
    }

    @Test
    public final void removeWallet(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        foo.addWallet("bbanner","q8675309");
        assertEquals(SQLController.RemoveWalletResult.REMOVED,foo.removeWallet("bbanner"));
    }

    @Test
    public final void removeNonExistentWallet(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        foo.removeWallet("bbanner");
        assertEquals(SQLController.RemoveWalletResult.NOSUCHWALLET,foo.removeWallet("bbanner"));
    }

    @Test
    public final void getPublicKeyTest(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        foo.removeWallet("nfury");
        foo.addWallet("nfury","nf675309");
        assertEquals("nf675309",foo.RetrievePublicKey("nfury"));
    }

    @Test
    public final void publicKeyDoesNotExist(){
        SQLController foo = new SQLController("wocoinDatabase.sqlite3");
        foo.removeWallet("nfury");
        assertEquals("",foo.RetrievePublicKey("nfury"));
    }
}
