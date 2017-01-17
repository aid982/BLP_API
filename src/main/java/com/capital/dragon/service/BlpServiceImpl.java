package com.capital.dragon.service;

import com.bloomberglp.blpapi.Element;
import com.bloomberglp.blpapi.Event;
import com.bloomberglp.blpapi.Message;
import com.bloomberglp.blpapi.MessageIterator;
import com.bloomberglp.blpapi.Request;
import com.bloomberglp.blpapi.Service;
import com.bloomberglp.blpapi.Session;
import com.bloomberglp.blpapi.SessionOptions;
import com.capital.dragon.model.Security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@org.springframework.stereotype.Service("BlpService")
public class BlpServiceImpl implements BlpService {

    public static Security parseMessageToSecurity(String message) {
        Security security = new Security();
        String regex;
        Pattern pattern;
        Matcher matcher;

        regex = "(.*)ASK(.*)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            regex = "(\\d+)(.)(\\d+)";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(matcher.group());
            if (matcher.find()) {
                security.setAsk(matcher.group());
            }
        } else {
            return null;
        }

        regex = "(.*)BID(.*)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            regex = "(\\d+)(.)(\\d+)";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(matcher.group());
            if (matcher.find()) {
                security.setBid(matcher.group());
            }
        } else {
            return null;
        }
        regex = "(.*)LAST(.*)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            regex = "(\\d+)(.)(\\d+)";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(matcher.group());
            if (matcher.find()) {
                security.setLast(matcher.group());
            }
        } else {
            return null;
        }

        regex = "(.*)security = (.*)";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(message);
        if (matcher.find()) {
            regex = "\"(.*)\"";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(matcher.group());
            if (matcher.find()) {
                security.setName(matcher.group());
            }
        }
        return security;
    }

    @Override
    public List<Security> getSecurityListInfo(String data, List<String> securityList) throws Exception {
        List<Security> resultList = new ArrayList<>();
        String serverHost = "localhost";
        int serverPort = 8194;

        SessionOptions sessionOptions = new SessionOptions();
        sessionOptions.setServerHost(serverHost);
        sessionOptions.setServerPort(serverPort);

        System.out.println("Connecting to " + serverHost + ":" + serverPort);
        Session session = new Session(sessionOptions);
        if (!session.start()) {
            System.err.println("Failed to start session.");
            return resultList;
        }
        if (!session.openService("//blp/refdata"))

        {
            System.err.println("Failed to open //blp/refdata");
            return resultList;
        }

        Service refDataService = session.getService("//blp/refdata");
        Request request = refDataService.createRequest("HistoricalDataRequest");

        Element securities = request.getElement("securities");

        for (String sec : securityList) {
            securities.appendValue(sec);

        }

        Element fields = request.getElement("fields");
        fields.appendValue("PX_LAST");
        fields.appendValue("OPEN");
        fields.appendValue("BID");
        fields.appendValue("ASK");

        request.set("periodicityAdjustment", "ACTUAL");
        request.set("periodicitySelection", "DAILY");
        //request.set("startDate","20161118");
        request.set("startDate", data);
        request.set("endDate", data);
        request.set("maxDataPoints", 100);
        request.set("returnEids", true);

        System.out.println("Sending Request: " + request);
        session.sendRequest(request, null);


        while (true) {
            Event event = session.nextEvent();
            MessageIterator msgIter = event.messageIterator();
            while (msgIter.hasNext()) {
                Message msg = msgIter.next();
                String msgstr = msg.toString();
                if (msgstr != "") {
                    Security sec = parseMessageToSecurity(msgstr);
                    if (sec != null) {
                        resultList.add(sec);
                    }
                }
            }
            if (event.eventType() == Event.EventType.RESPONSE) {
                break;
            }

        }
        return resultList;
    }

    public static void run() throws Exception {

    }
}
