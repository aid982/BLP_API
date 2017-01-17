package com.capital.dragon;

import com.capital.dragon.service.BlpServiceImpl;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by bluser on 11/23/2016.
 */
public class ParseTesting {
    @Test
    public void parseTest() throws  Exception{
        File file = new File("test.txt");
        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
       //String res = bufferedReader.re
        //while()
        //BlpServiceImpl.parseMessageToSecurity();

    }

}
